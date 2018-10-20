package kz.amangeldy.flickrapplication.di

import androidx.databinding.DataBindingComponent
import io.reactivex.schedulers.Schedulers
import kz.amangeldy.flickrapplication.BuildConfig
import kz.amangeldy.flickrapplication.ImageLoader
import kz.amangeldy.flickrapplication.Mapper
import kz.amangeldy.flickrapplication.PicassoImageLoader
import kz.amangeldy.flickrapplication.data.repository.DefaultImagesRepository
import kz.amangeldy.flickrapplication.data.repository.DefaultUserRepository
import kz.amangeldy.flickrapplication.data.source.FlickrApi
import kz.amangeldy.flickrapplication.domain.ImagesUseCase
import kz.amangeldy.flickrapplication.domain.entity.PhotoModel
import kz.amangeldy.flickrapplication.domain.repository.ImagesRepository
import kz.amangeldy.flickrapplication.domain.repository.UserRepository
import kz.amangeldy.flickrapplication.presentation.MainViewModel
import kz.amangeldy.flickrapplication.presentation.binding.MainBindingAdapter
import kz.amangeldy.flickrapplication.presentation.entity.FlickrImagePresentationModel
import kz.amangeldy.flickrapplication.presentation.mapper.ImagesDomainToPresentationMapper
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val mainModule: Module = module {

    viewModel {
        MainViewModel(
            imagesUseCase = get(),
            mapper = get()
        )
    }

    factory {
        ImagesUseCase(
            imagesRepository = get(),
            userRepository = get()
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

    factory {
        ImagesDomainToPresentationMapper() as Mapper<PhotoModel, FlickrImagePresentationModel>
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(FlickrApi::class.java) as FlickrApi
    }

    single {
        PicassoImageLoader() as ImageLoader
    }

    factory {
        object: DataBindingComponent {
            override fun getMainBindingAdapter(): MainBindingAdapter {
                return MainBindingAdapter(imageLoader = get())
            }
        } as DataBindingComponent
    }
}