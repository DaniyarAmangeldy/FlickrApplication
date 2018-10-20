package kz.amangeldy.flickrapplication.data.repository

import io.reactivex.Single
import kz.amangeldy.flickrapplication.data.entity.PhotosRootDataModel
import kz.amangeldy.flickrapplication.data.source.FlickrApi
import kz.amangeldy.flickrapplication.domain.repository.ImagesRepository

private const val QUERY_TEXT = "text"
private const val QUERY_PAGE = "page"

class DefaultImagesRepository(
    private val apiSource: FlickrApi
) : ImagesRepository {

    override fun getImages(page: Int): Single<PhotosRootDataModel> {

        return apiSource.getImageList(page)
    }

    override fun searchImages(page: Int, searchQuery: String?): Single<PhotosRootDataModel> {
        val queryMap = mutableMapOf<String, @JvmSuppressWildcards Any>(QUERY_PAGE to page)
        searchQuery?.let { queryMap.put(QUERY_TEXT, it ) }

        return apiSource.searchImages(queryMap)
    }
}