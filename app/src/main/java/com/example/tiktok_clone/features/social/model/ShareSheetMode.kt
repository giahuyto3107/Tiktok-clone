package com.example.tiktok_clone.features.social.model

sealed class ShareSheetMode{
    object Default: ShareSheetMode()
    object Report: ShareSheetMode()
    object NotInterested: ShareSheetMode()
    object Speed: ShareSheetMode()
}