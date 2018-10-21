package kz.amangeldy.flickrapplication.domain.repository

import io.reactivex.Flowable
import kz.amangeldy.flickrapplication.data.entity.SearchQueryRoomModel

interface SearchQueryRepository {

    fun insertSearchQuery(text: String)

    fun getSearchQueries(): Flowable<List<SearchQueryRoomModel>>
}