package kz.amangeldy.flickrapplication.domain.repository

import io.reactivex.Single
import kz.amangeldy.flickrapplication.data.entity.PhotosRootDataModel


interface ImagesRepository {

    fun getImages(page: Int): Single<PhotosRootDataModel>

    fun searchImages(page: Int, searchQuery: String?): Single<PhotosRootDataModel>
}