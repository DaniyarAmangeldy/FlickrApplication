package kz.amangeldy.flickrapplication.data.entity

data class PhotosRootDataModel(
    val photos: PhotosDataModel?,
    val stat: String
)

data class PhotosDataModel(
    val page: Int,
    val pages: Int,
    val photo: List<PhotoDataModel>
)

data class PhotoDataModel(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: String,
    val title: String
)

data class ProfileDataModel(
    val person: OwnerDataModel
)

data class OwnerDataModel(
    val id: String,
    val iconserver: Int,
    val iconfarm: Int,
    val realname: CommonContentDataModel?,
    val username: CommonContentDataModel?
)

data class CommonContentDataModel(
    val _content: String
)