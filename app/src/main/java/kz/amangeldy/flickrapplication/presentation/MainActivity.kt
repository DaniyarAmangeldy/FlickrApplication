package kz.amangeldy.flickrapplication.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import kz.amangeldy.flickrapplication.R
import kz.amangeldy.flickrapplication.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.content.ComponentName
import android.app.SearchManager
import android.content.Context
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import kotlinx.android.synthetic.main.activity_main.*
import kz.amangeldy.flickrapplication.presentation.image.detail.ImageFullDialogFragment
import kz.amangeldy.flickrapplication.utils.Event
import kz.amangeldy.flickrapplication.utils.hideKeyboard
import kz.amangeldy.flickrapplication.utils.observe
import kz.amangeldy.flickrapplication.utils.showSnackbar

private const val IMAGE_FULL_FRAGMENT_TAG = "image_full_fragment_tag"

class MainActivity : AppCompatActivity(), SearchView.OnSuggestionListener {

    private val mainViewModel: MainViewModel by viewModel()
    private val component: DataBindingComponent by inject()
    private val exceptionMessageFactory: ExceptionMessageFactory by inject()

    private lateinit var searchView: SearchView
    private lateinit var rootView: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main, component)
        binding.setLifecycleOwner(this)
        binding.viewModel = mainViewModel
        rootView = activity_main_root
        configureSearchView()
        observeViewModel()
    }

    override fun onSuggestionSelect(position: Int): Boolean {
        return true
    }

    override fun onSuggestionClick(position: Int): Boolean {
        searchView.setQuery(mainViewModel.searchQueriesLiveData.value?.get(position), true)
        hideKeyboard()

        return true
    }

    private fun configureSearchView() {
        searchView = activity_main_search_view.apply {
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val cn = ComponentName(this@MainActivity, MainActivity::class.java)
            setSearchableInfo(searchManager.getSearchableInfo(cn))
            setOnSuggestionListener(this@MainActivity)
        }
    }

    private fun observeViewModel() {
        mainViewModel.onImageClickEvent.observe(this, ::onClickImageItem)
        mainViewModel.errorLiveData.observe(this, ::onError)
        mainViewModel.hideKeyboardEvent.observe(this, ::handleHideKeyboard)
    }

    private fun onError(event: Event<Throwable>) {
        val exception = event.getContentIfNotHandled() ?: return
        val message = exceptionMessageFactory.createMessage(exception as Exception)
        message?.let { rootView.showSnackbar(it) } ?: throw exception
    }

    private fun handleHideKeyboard(event: Event<Unit>) {
        event.getContentIfNotHandled() ?: return
        hideKeyboard()
        searchView.dismissSuggestions()
    }

    private fun onClickImageItem(event: Event<String>) {
        val imageUrl = event.getContentIfNotHandled() ?: return
        ImageFullDialogFragment
            .createInstance(imageUrl)
            .show(supportFragmentManager, IMAGE_FULL_FRAGMENT_TAG)
    }

    private fun SearchView.dismissSuggestions() {
        clearFocus()
    }
}