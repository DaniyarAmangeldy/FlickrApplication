package kz.amangeldy.flickrapplication.presentation.entity

import kz.amangeldy.flickrapplication.presentation.MainListItem

data class ImagePresentationModel(
    val id: Long,
    val imageUrl: String,
    val title: String,
    val owner: Owner
): MainListItem

data class Owner(
    val id: String,
    val fullName: String?,
    val userName: String?,
    val avatarUrl: String
)