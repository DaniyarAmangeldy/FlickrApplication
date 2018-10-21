package kz.amangeldy.flickrapplication.di

import androidx.databinding.DataBindingComponent
import androidx.room.Room
import io.reactivex.schedulers.Schedulers
import kz.amangeldy.flickrapplication.BuildConfig
import kz.amangeldy.flickrapplication.data.source.FlickrApi
import kz.amangeldy.flickrapplication.data.source.FlickrDatabase
import kz.amangeldy.flickrapplication.presentation.ExceptionMessageFactory
import kz.amangeldy.flickrapplication.presentation.binding.MainBindingAdapter
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val applicationModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(FlickrApi::class.java) as FlickrApi
    }

    single {
        Room.databaseBuilder(androidContext(), FlickrDatabase::class.java, "flickr-database")
            .build()
    }

    factory {
        object: DataBindingComponent {
            override fun getMainBindingAdapter(): MainBindingAdapter {
                return MainBindingAdapter(imageLoader = get())
            }
        } as DataBindingComponent
    }

    factory {
        ExceptionMessageFactory(androidApplication().resources)
    }
}