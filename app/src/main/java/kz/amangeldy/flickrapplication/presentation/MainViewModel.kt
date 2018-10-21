package kz.amangeldy.flickrapplication.presentation

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kz.amangeldy.flickrapplication.utils.Mapper
import kz.amangeldy.flickrapplication.data.entity.SearchQueryRoomModel
import kz.amangeldy.flickrapplication.domain.GetSearchQueriesUseCase
import kz.amangeldy.flickrapplication.domain.ImagesUseCase
import kz.amangeldy.flickrapplication.domain.entity.PhotoModel
import kz.amangeldy.flickrapplication.presentation.custom.view.PaginableRecyclerView
import kz.amangeldy.flickrapplication.presentation.entity.FlickrImagePresentationModel
import kz.amangeldy.flickrapplication.utils.Event
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val imagesUseCase: ImagesUseCase,
    private val getSearchQueriesUseCase: GetSearchQueriesUseCase,
    private val mapper: Mapper<PhotoModel, FlickrImagePresentationModel>
) : ViewModel() {

    val onImageClickEvent = MutableLiveData<Event<String>>()
    val errorLiveData = MutableLiveData<Event<Throwable>>()
    val isRefreshingLiveData = MutableLiveData<Boolean>()
    val hideKeyboardEvent = MutableLiveData<Event<Unit>>()

    val onBottomScrollListener = object : PaginableRecyclerView.OnBottomScrollListener {
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

    val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        setIsRefreshing(false)
        page = 1
        searchPage = 1
        requestImages(page, query)
    }

    val onImageClickListener = object: ImageViewHolder.OnImageClickListener {
        override fun onImageClick(image: FlickrImagePresentationModel) {
            onImageClickEvent.value = Event(image.imageUrl)
        }
    }

    val onTextQueryListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
            hideKeyboard()
            searchPublishSubject?.onComplete()
            configureAutoComplete()

            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            searchPublishSubject?.onNext(newText ?: "")

            return true
        }
    }

    private fun hideKeyboard() {
        hideKeyboardEvent.value = Event(Unit)
    }

    val imageListLiveData by lazy {
        MutableLiveData<MutableList<FlickrImagePresentationModel>>().apply {
            value = mutableListOf()
            setIsRefreshing(true)
            requestImages(page)
        }
    }

    val searchQueriesLiveData by lazy {
        MutableLiveData<List<String>>().apply {
            value = mutableListOf()
            observeSearchQueries()
        }
    }

    private val tempImagesCache = mutableListOf<FlickrImagePresentationModel>()
    private val compositeDisposable = CompositeDisposable()
    private var page = 1
    private var searchPage = 1
    private var query: String? = null
    private var searchPublishSubject: PublishSubject<String?>? = null

    init {
        configureAutoComplete()
        observeSearchQueries()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    private fun requestImages(page: Int, query: String? = null) {
        val disposable = imagesUseCase.invoke(page, query)
            .observeOn(AndroidSchedulers.mainThread())
            .map(mapper::invoke)
            .distinct()
            .toList()
            .subscribe(::onImageLoadSuccess, ::onError)
        compositeDisposable.add(disposable)
    }

    private fun observeSearchQueries() {
        val disposable = getSearchQueriesUseCase.invoke()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onSearchQueryUpdated, ::onError)
        compositeDisposable.add(disposable)
    }

    private fun onImageLoadSuccess(images: List<FlickrImagePresentationModel>) {
        setIsRefreshing(false)
        updateImagesLiveData(images)
        if (query.isNullOrBlank()) {
            tempImagesCache.clear()
            tempImagesCache.addAll(imageListLiveData.value!!)
        }
    }

    private fun setIsRefreshing(isRefreshing: Boolean) {
        isRefreshingLiveData.value = isRefreshing
    }

    private fun onSearchQueryUpdated(queries: List<SearchQueryRoomModel>) {
        searchQueriesLiveData.value = queries.map { it.query }
    }

    private fun updateImagesLiveData(images: List<FlickrImagePresentationModel>) {
        imageListLiveData.value = imageListLiveData.value?.apply { addAll(images) }
    }

    private fun clearImagesLiveData() {
        imageListLiveData.value = mutableListOf()
    }

    private fun onError(exception: Throwable) {
        errorLiveData.value = Event(exception)
    }

    private fun configureAutoComplete() {
        searchPublishSubject = PublishSubject.create<String?>().also { subject ->
            subject
                .debounce(3000, TimeUnit.MILLISECONDS, Schedulers.io())
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { onSearchImages(it) }
        }
    }

    private fun onSearchImages(it: String?) {
        if (it.isNullOrBlank()) {
            this@MainViewModel.query = null
            clearImagesLiveData()
            updateImagesLiveData(tempImagesCache)
            return
        }
        this@MainViewModel.query = it
        searchPage = 1
        clearImagesLiveData()
        setIsRefreshing(true)
        requestImages(searchPage, it)
    }
}