package kz.amangeldy.flickrapplication.domain.entity

import kz.amangeldy.flickrapplication.data.entity.OwnerDataModel
import kz.amangeldy.flickrapplication.data.entity.PhotoDataModel

data class PhotoModel(
    val photoDataModel: PhotoDataModel,
    val owner: OwnerDataModel
)