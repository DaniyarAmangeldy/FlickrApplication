package kz.amangeldy.flickrapplication.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import com.miguelcatalan.materialsearchview.MaterialSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kz.amangeldy.flickrapplication.Mapper
import kz.amangeldy.flickrapplication.domain.ImagesUseCase
import kz.amangeldy.flickrapplication.domain.entity.PhotoModel
import kz.amangeldy.flickrapplication.presentation.binding.OnBottomScrollListener
import kz.amangeldy.flickrapplication.presentation.entity.FlickrImagePresentationModel
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val imagesUseCase: ImagesUseCase,
    private val mapper: Mapper<PhotoModel, FlickrImagePresentationModel>
): ViewModel() {

    private var page = 1
    private var searchPage = 1
    private var query: String? = null
    private val tempImagesCache = mutableListOf<FlickrImagePresentationModel>()

    val onBottomScrollListener = object: OnBottomScrollListener {
        override fun onScrolledToBottom() {
            if (query != null) {
                searchPage = searchPage.inc()
                requestImages(searchPage, query)
            } else {
                page = page.inc()
                requestImages(page)
            }
        }
    }

    val observable = PublishRelay.create<String?>()

    val onTextQueryListener = object: MaterialSearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean = true

        override fun onQueryTextChange(newText: String?): Boolean {
            observable.accept(newText)

            return true
        }
    }

    val imageListLiveData by lazy {
        MutableLiveData<MutableList<FlickrImagePresentationModel>>().apply {
            value = mutableListOf()
            requestImages(page)
        }
    }

    init {
        configureAutoComplete()
    }

    private fun requestImages(page: Int, query: String? = null) {
        imagesUseCase.invoke(page, query)
            .observeOn(AndroidSchedulers.mainThread())
            .map(mapper::invoke)
            .toList()
            .subscribe(::onImageLoadSuccess, ::onImageLoadError)
    }

    private fun onImageLoadSuccess(images: List<FlickrImagePresentationModel>) {
        updateImagesLiveData(images)
        if (query == null) {
            tempImagesCache.clear()
            tempImagesCache.addAll(imageListLiveData.value!!)
        }
    }

    private fun updateImagesLiveData(images: List<FlickrImagePresentationModel>) {
        imageListLiveData.value = imageListLiveData.value?.apply { addAll(images) }
    }

    private fun clearImagesLiveData() {
        imageListLiveData.value = mutableListOf()
    }

    private fun onImageLoadError(exception: Throwable) {
        throw exception
    }

    private fun configureAutoComplete() {
        observable
            .debounce(3000, TimeUnit.MILLISECONDS, Schedulers.io())
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNullOrBlank()) {
                    this@MainViewModel.query = null
                    clearImagesLiveData()
                    updateImagesLiveData(tempImagesCache)
                    return@subscribe
                }
                this@MainViewModel.query = it
                searchPage = 1
                requestImages(searchPage, it)
                clearImagesLiveData()
            }
    }
}