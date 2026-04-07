package com.example.tiktok_clone.features.auth.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage 
import com.example.tiktok_clone.features.profile.viewmodel.ProfileViewModel
import com.example.tiktok_clone.features.social.ui.components.Avatar
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.features.social.ui.SocialUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthenticatedProfile(
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel = koinViewModel(),
    socialViewModel: SocialViewModel = koinViewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    // Observe profile via StateFlow — auto-updates when updateProfile() completes
    val currentUser by profileViewModel.profileData.collectAsState()
    val displayName = currentUser?.displayName ?: "TikTok User"
    val email = currentUser?.email ?: "No email"
    val photoUrl = currentUser?.avtPhotoUrl

    // Dialog state
    var showEditDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    var editPhotoUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    LaunchedEffect(currentUser?.id) {
        if (currentUser?.id != null) {
            socialViewModel.loadFollowing(currentUser!!.id)
            socialViewModel.loadFollowers(currentUser!!.id)
        }
    }

    val socialUiState by socialViewModel.uiState.collectAsState()
    val followingCount = (socialUiState as? SocialUiState.Success)?.data?.following?.size ?: 0
    val followersCount = (socialUiState as? SocialUiState.Success)?.data?.followers?.size ?: 0


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Ảnh đại diện
        Avatar(
            avatarUrl = if (photoUrl == "null") null else photoUrl,
            avatarSize = 100,
        )

        // 2. Tên và Email
        Text(
            text = "@${displayName.replace(" ", "_").lowercase()}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 12.dp),
            color = Color.Black
        )
        Text(text = email, fontSize = 14.sp, color = Color.Gray)

        // 3. Chỉ số (Following, Followers, Likes)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ProfileStat(followingCount.toString(), "Following")
            ProfileStat(followersCount.toString(), "Followers")
            ProfileStat("10.5M", "Likes")
        }

        // 4. Nút chức năng
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            Button(
                onClick = { 
                    editName = displayName
                    editPhotoUrl = if (photoUrl == "null") "" else photoUrl ?: ""
                    selectedImageUri = null
                    showEditDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9E9E9)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Update Profile", color = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.weight(1f)
            ) {
                Text("Log out", color = Color.White)
            }
        }
    }

    // 5. Edit Profile Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { if (!isSaving) showEditDialog = false },
            title = { Text("Update Profile", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Avatar Preview
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Avatar",
                            modifier = Modifier.size(80.dp).clip(CircleShape).border(1.dp, Color.Gray, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else if (editPhotoUrl.isNotBlank()) {
                        AsyncImage(
                            model = editPhotoUrl,
                            contentDescription = "Current Avatar",
                            modifier = Modifier.size(80.dp).clip(CircleShape).border(1.dp, Color.Gray, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No Image")
                        }
                    }

                    Button(
                        onClick = { 
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            ) 
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Select Photo from Gallery")
                    }

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Display Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                val coroutineScope = rememberCoroutineScope()
                Button(
                    onClick = {
                        isSaving = true
                        if (selectedImageUri != null) {
                            // Run suspend function in coroutine to upload to backend first
                            coroutineScope.launch {
                                val uploadedUrl = profileViewModel.uploadAvatarLocal(selectedImageUri!!)
                                if (uploadedUrl == null) {
                                    android.widget.Toast.makeText(context, "Upload image failed. Using previous image.", android.widget.Toast.LENGTH_SHORT).show()
                                }
                                val finalUrl = uploadedUrl ?: editPhotoUrl
                                profileViewModel.updateProfile(editName, finalUrl) { success ->
                                    isSaving = false
                                    if (success) {
                                        android.widget.Toast.makeText(context, "Profile updated successfully!", android.widget.Toast.LENGTH_SHORT).show()
                                        showEditDialog = false
                                    } else {
                                        android.widget.Toast.makeText(context, "Failed to update profile", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            // Just update name
                            profileViewModel.updateProfile(editName, editPhotoUrl) { success ->
                                isSaving = false
                                if (success) {
                                    android.widget.Toast.makeText(context, "Profile updated successfully!", android.widget.Toast.LENGTH_SHORT).show()
                                    showEditDialog = false
                                } else {
                                    android.widget.Toast.makeText(context, "Failed to update profile", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    enabled = !isSaving
                ) {
                    Text(if (isSaving) "Saving..." else "Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }, enabled = !isSaving) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun ProfileStat(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 15.dp)
    ) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
    }
}