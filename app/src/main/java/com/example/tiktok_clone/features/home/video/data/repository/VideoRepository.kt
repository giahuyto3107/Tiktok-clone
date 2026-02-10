package com.example.tiktok_clone.features.home.video.data.repository

import android.content.Context
import android.net.Uri
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.abedelazizshe.lightcompressorlibrary.config.AppSpecificStorageConfiguration
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

class VideoRepository(private val context: Context) {
    private val storage = FirebaseStorage.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun uploadVideo(
        videoUri: Uri,
        description: String
    ): Flow<Result<Boolean>> = callbackFlow {
        try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val compressedFileName = "temp_video_${System.currentTimeMillis()}.mp4"

            VideoCompressor.start(
                context = context,
                uris = listOf(videoUri),
                isStreamable = false,
                appSpecificStorageConfiguration = AppSpecificStorageConfiguration(
                    subFolderName = "compressed_videos",

                ),
                configureWith = Configuration(
                    quality = VideoQuality.MEDIUM,
                    isMinBitrateCheckEnabled = false,
                    videoBitrateInMbps = 2,
                    videoNames = listOf(compressedFileName)
                ),
                listener = object : CompressionListener {
                    override fun onProgress(index: Int, percent: Float) {

                    }
                    override fun onStart(index: Int) {

                    }
                    override fun onSuccess(index: Int, size: Long, path: String?) {
                        path?.let {
                            val compressedFile = File(it)
                            uploadToFirebase(
                                compressedFile = compressedFile,
                                description = description,
                                currentUser = currentUser,
                                scope = this@callbackFlow
                            )
                        } ?: run {
                            trySend(Result.failure(Exception("Compression succeeded but path is null")))
                        }
                    }
                    override fun onFailure(index: Int, failureMessage: String) {
                        trySend(Result.failure(Exception(failureMessage)))
                    }
                    override fun onCancelled(index: Int) {
                        trySend(Result.failure(Exception("Compression cancelled")))
                    }
                },
            )
        } catch (e: Exception) {
            trySend(Result.failure(e))
        }
        awaitClose { }
    }


    private fun uploadToFirebase(
        compressedFile: File,
        description: String,
        currentUser: FirebaseUser,
        scope: ProducerScope<Result<Boolean>> // Add scope parameter
    ) {
        val videoId = db.collection("videos").document().id
        val storageRef = storage.reference.child("videos/${currentUser.uid}/$videoId.mp4")

        storageRef.putFile(Uri.fromFile(compressedFile))
            .addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                // Optional: send upload progress
            }
            .addOnSuccessListener {
                // Get download URL
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Save video metadata to Firestore
                    val videoData = hashMapOf(
                        "id" to videoId,
                        "userId" to currentUser.uid,
                        "username" to (currentUser.displayName ?: "Unknown"),
                        "videoUrl" to downloadUri.toString(),
                        "description" to description,
                        "timestamp" to System.currentTimeMillis(),
                        "likes" to 0,
                        "comments" to 0
                    )

                    db.collection("videos")
                        .document(videoId)
                        .set(videoData)
                        .addOnSuccessListener {
                            // Clean up compressed file
                            compressedFile.delete()
                            scope.trySend(Result.success(true))
                        }
                        .addOnFailureListener { e ->
                            compressedFile.delete()
                            scope.trySend(Result.failure(e))
                        }
                }.addOnFailureListener { e ->
                    compressedFile.delete()
                    scope.trySend(Result.failure(e))
                }
            }
            .addOnFailureListener { e ->
                compressedFile.delete()
                scope.trySend(Result.failure(e))
            }
    }
}

