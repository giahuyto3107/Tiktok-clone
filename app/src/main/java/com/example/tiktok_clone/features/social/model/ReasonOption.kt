package com.example.tiktok_clone.features.social.model

data class ReasonOption(
    val reasonId: Int,
    val reasonOption: String,
)
enum class ReasonType {
    REPORT,
    NOT_INTERESTED,
    SPEED,
    CAST
}
//
//data class ReasonOption(
//    val id: Int,
//    val type: ReasonType,
//    val title: String
//)
