//package com.example.tiktok_clone.features.home.helper
//
//import android.content.ContentUris
//import android.content.Context
//import android.provider.MediaStore
//
//fun getLastGalleryImageUri(context: Context): android.net.Uri? {
//    val projection = arrayOf(
//        MediaStore.Images.Media._ID,
//        MediaStore.Images.Media.DATE_TAKEN
//    )
//
//    // Query images, sorted by Date Taken (Descending)
//    val cursor = context.contentResolver.query(
//        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//        projection,
//        null,
//        null,
//        "${MediaStore.Images.Media.DATE_TAKEN} DESC"
//    )
//
//    return cursor?.use {
//        if (it.moveToFirst()) {
//            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//            val id = it.getLong(idColumn)
//            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//        } else {
//            null
//        }
//    }
//}