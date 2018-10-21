package kz.amangeldy.flickrapplication.domain

import io.reactivex.Flowable
import kz.amangeldy.flickrapplication.data.entity.SearchQueryRoomModel
import kz.amangeldy.flickrapplication.domain.repository.SearchQueryRepository


class GetSearchQueriesUseCase(
    private val searchQueryRepository: SearchQueryRepository
) {

    operator fun invoke(): Flowable<List<SearchQueryRoomModel>> {
        return searchQueryRepository.getSearchQueries()
    }
}