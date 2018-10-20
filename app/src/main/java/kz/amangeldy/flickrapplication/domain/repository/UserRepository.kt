package kz.amangeldy.flickrapplication.domain.repository

import io.reactivex.Single
import kz.amangeldy.flickrapplication.data.entity.ProfileDataModel

interface UserRepository {

    fun getUserById(id: String): Single<ProfileDataModel>
}