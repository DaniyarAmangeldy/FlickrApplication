package kz.amangeldy.flickrapplication.presentation.mapper

import kz.amangeldy.flickrapplication.utils.Mapper
import kz.amangeldy.flickrapplication.data.entity.OwnerDataModel
import kz.amangeldy.flickrapplication.data.entity.PhotoDataModel
import kz.amangeldy.flickrapplication.domain.entity.PhotoModel
import kz.amangeldy.flickrapplication.presentation.entity.ImagePresentationModel
import kz.amangeldy.flickrapplication.presentation.entity.Owner

private const val IMAGE_URL_FMT = "https://farm%s.staticflickr.com/%s/%s_%s.jpg"
private const val OWNER_AVATAR_URL_FMT = "http://farm%s.staticflickr.com/%s/buddyicons/%s.jpg"

class ImagesDomainToPresentationMapper: Mapper<PhotoModel, ImagePresentationModel> {

    override fun invoke(from: PhotoModel): ImagePresentationModel {
        with(from) {
            val owner = Owner(
                from.owner.id,
                from.owner.realname?._content,
                from.owner.username?._content,
                from.owner.mapOwnerAvatarUrl()
            )

            return ImagePresentationModel(
                photoDataModel.id.toLongOrNull() ?: 0L,
                photoDataModel.mapImageUrl(),
                photoDataModel.title,
                owner
            )
        }
    }

    private fun PhotoDataModel.mapImageUrl(): String =
        IMAGE_URL_FMT.format(farm, server, id, secret)

    private fun OwnerDataModel.mapOwnerAvatarUrl(): String =
        OWNER_AVATAR_URL_FMT.format(iconfarm, iconserver, id)
}