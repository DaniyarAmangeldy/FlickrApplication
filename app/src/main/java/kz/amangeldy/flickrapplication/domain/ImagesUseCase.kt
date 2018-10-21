package kz.amangeldy.flickrapplication.domain

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import kz.amangeldy.flickrapplication.data.entity.PhotoDataModel
import kz.amangeldy.flickrapplication.data.entity.ProfileDataModel
import kz.amangeldy.flickrapplication.domain.entity.PhotoModel
import kz.amangeldy.flickrapplication.domain.repository.ImagesRepository
import kz.amangeldy.flickrapplication.domain.repository.SearchQueryRepository
import kz.amangeldy.flickrapplication.domain.repository.UserRepository

class ImagesUseCase(
    private val imagesRepository: ImagesRepository,
    private val userRepository: UserRepository,
    private val searchQueryRepository: SearchQueryRepository
) {

    operator fun invoke(page: Int, query: String?): Observable<PhotoModel> {
        return if (query == null) {
            imagesRepository.getImages(page)
        } else {
            imagesRepository.searchImages(page, query)
                .flatMap {
                Completable.fromAction {
                    searchQueryRepository.insertSearchQuery(query)
                }.andThen(Single.just(it))
            }
        }
            .flatMapObservable { Observable.fromArray(it.photos?.photo ?: arrayListOf()) }
            .flatMapIterable { photo -> photo }
            .flatMap {
                Observable.combineLatest(
                    Observable.just(it),
                    getUserInfo(it.owner),
                    BiFunction<PhotoDataModel, ProfileDataModel, PhotoModel> { photoDataModel,
                                                                               profileDataModel ->
                        val person = profileDataModel.person
                        PhotoModel(photoDataModel, person)
                    }
                )
            }
    }

    private fun getUserInfo(userId: String): Observable<ProfileDataModel> {
        return userRepository.getUserById(userId).toObservable()
    }
}