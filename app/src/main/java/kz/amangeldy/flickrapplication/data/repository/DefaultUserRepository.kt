package kz.amangeldy.flickrapplication.data.repository

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kz.amangeldy.flickrapplication.data.entity.ProfileDataModel
import kz.amangeldy.flickrapplication.data.source.FlickrApi
import kz.amangeldy.flickrapplication.domain.repository.UserRepository

class DefaultUserRepository(
    private val apiSource: FlickrApi
): UserRepository {

    override fun getUserById(id: String): Single<ProfileDataModel> {
        return apiSource.getUserById(id)
    }
}