package kz.amangeldy.flickrapplication.data.source

import io.reactivex.Single
import kz.amangeldy.flickrapplication.BuildConfig
import kz.amangeldy.flickrapplication.data.entity.ProfileDataModel
import kz.amangeldy.flickrapplication.data.entity.PhotosRootDataModel
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

private const val FORMAT_JSON = "json"
private const val NO_JSON_CALLBACK_VALUE = 1
private const val IMAGES_PER_PAGE = 20
private const val GET_RECENT_PHOTOS_METHOD = "flickr.photos.getRecent"
private const val SEARCH_PHOTOS_METHOD = "flickr.photos.search"
private const val GET_USER_BY_ID_METHOD = "flickr.people.getInfo"

interface FlickrApi {

    @GET("/services/rest")
    fun getImageList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = IMAGES_PER_PAGE,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("method") method: String = GET_RECENT_PHOTOS_METHOD,
        @Query("nojsoncallback") noJsonCallback: Int = NO_JSON_CALLBACK_VALUE,
        @Query("format") format: String = FORMAT_JSON
    ): Single<PhotosRootDataModel>

    @GET("/services/rest")
    fun searchImages(
        @Query("page") page: Int,
        @Query("text") text: String,
        @Query("per_page") perPage: Int = IMAGES_PER_PAGE,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("method") method: String = SEARCH_PHOTOS_METHOD,
        @Query("nojsoncallback") noJsonCallback: Int = NO_JSON_CALLBACK_VALUE,
        @Query("format") format: String = FORMAT_JSON
    ): Single<PhotosRootDataModel>

    @GET("/services/rest")
    fun getUserById(@Query("user_id") userId: String,
                    @Query("api_key") apiKey: String = BuildConfig.API_KEY,
                    @Query("method") method: String = GET_USER_BY_ID_METHOD,
                    @Query("nojsoncallback") noJsonCallback: Int = NO_JSON_CALLBACK_VALUE,
                    @Query("format") format: String = FORMAT_JSON): Single<ProfileDataModel>
}