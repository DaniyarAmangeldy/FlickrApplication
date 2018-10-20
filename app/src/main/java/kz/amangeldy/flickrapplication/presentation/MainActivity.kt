package kz.amangeldy.flickrapplication.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_main.*
import kz.amangeldy.flickrapplication.R
import kz.amangeldy.flickrapplication.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private val component: DataBindingComponent by inject()

    private lateinit var searchView: MaterialSearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main, component)
        binding.setLifecycleOwner(this)
        binding.viewModel = mainViewModel
        setSupportActionBar(activity_main_toolbar)
        searchView = activity_main_toolbar_search_view
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.main_menu, menu)
        val item = menu?.findItem(R.id.action_search)
        searchView.setMenuItem(item)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (searchView.isSearchOpen) {
            searchView.closeSearch()
        } else {
            super.onBackPressed()
        }
    }
}