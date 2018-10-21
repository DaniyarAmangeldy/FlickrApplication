package kz.amangeldy.flickrapplication.di

import kz.amangeldy.flickrapplication.utils.ImageLoader
import kz.amangeldy.flickrapplication.utils.Mapper
import kz.amangeldy.flickrapplication.utils.PicassoImageLoader
import kz.amangeldy.flickrapplication.data.repository.DefaultImagesRepository
import kz.amangeldy.flickrapplication.data.repository.DefaultSearchQueryRepository
import kz.amangeldy.flickrapplication.data.repository.DefaultUserRepository
import kz.amangeldy.flickrapplication.data.source.FlickrDatabase
import kz.amangeldy.flickrapplication.domain.GetSearchQueriesUseCase
import kz.amangeldy.flickrapplication.domain.ImagesUseCase
import kz.amangeldy.flickrapplication.domain.entity.PhotoModel
import kz.amangeldy.flickrapplication.domain.repository.ImagesRepository
import kz.amangeldy.flickrapplication.domain.repository.SearchQueryRepository
import kz.amangeldy.flickrapplication.domain.repository.UserRepository
import kz.amangeldy.flickrapplication.presentation.MainViewModel
import kz.amangeldy.flickrapplication.presentation.entity.FlickrImagePresentationModel
import kz.amangeldy.flickrapplication.presentation.mapper.ImagesDomainToPresentationMapper
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val mainModule: Module = module {

    viewModel {
        MainViewModel(
            imagesUseCase = get(),
            mapper = get(),
            getSearchQueriesUseCase = get()
        )
    }

    factory {
        ImagesUseCase(
            imagesRepository = get(),
            userRepository = get(),
            searchQueryRepository = get()
        )
    }

    factory {
        GetSearchQueriesUseCase(
            searchQueryRepository = get()
        )
    }

    single {
        DefaultImagesRepository(
            apiSource = get()
        ) as ImagesRepository
    }

    single {
        DefaultUserRepository(
            apiSource = get()
        ) as UserRepository
    }

    single {
        DefaultSearchQueryRepository(
            cacheSource = get<FlickrDatabase>().searchQueryDao()
        ) as SearchQueryRepository
    }

    factory {
        ImagesDomainToPresentationMapper() as Mapper<PhotoModel, FlickrImagePresentationModel>
    }

    single {
        PicassoImageLoader() as ImageLoader
    }
}