package kz.amangeldy.flickrapplication.data.repository

import io.reactivex.Flowable
import kz.amangeldy.flickrapplication.data.entity.SearchQueryRoomModel
import kz.amangeldy.flickrapplication.data.source.SearchQueryDAO
import kz.amangeldy.flickrapplication.domain.repository.SearchQueryRepository

class DefaultSearchQueryRepository(
    private val cacheSource: SearchQueryDAO
): SearchQueryRepository {

    override fun insertSearchQuery(text: String) {
        return cacheSource.insertSearchQuery(SearchQueryRoomModel(query = text))

    }

    override fun getSearchQueries(): Flowable<List<SearchQueryRoomModel>> {
        return cacheSource.getAllSearchQueries()
    }
}