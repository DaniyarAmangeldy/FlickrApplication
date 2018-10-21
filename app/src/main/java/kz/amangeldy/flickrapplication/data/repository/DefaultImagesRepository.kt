package kz.amangeldy.flickrapplication.data.repository

import io.reactivex.Single
import kz.amangeldy.flickrapplication.data.entity.PhotosRootDataModel
import kz.amangeldy.flickrapplication.data.source.FlickrApi
import kz.amangeldy.flickrapplication.domain.repository.ImagesRepository

class DefaultImagesRepository(
    private val apiSource: FlickrApi
) : ImagesRepository {

    override fun getImages(page: Int): Single<PhotosRootDataModel> {
        return apiSource.getImageList(page)
    }

    override fun searchImages(page: Int, searchQuery: String): Single<PhotosRootDataModel> {
        return apiSource.searchImages(page, searchQuery)
    }
}