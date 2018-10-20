package kz.amangeldy.flickrapplication.presentation.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kz.amangeldy.flickrapplication.ImageLoader
import kz.amangeldy.flickrapplication.presentation.ImagesAdapter
import kz.amangeldy.flickrapplication.presentation.PaginableRecyclerView
import kz.amangeldy.flickrapplication.presentation.entity.FlickrImagePresentationModel

class MainBindingAdapter(private val imageLoader: ImageLoader) {

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter(value = ["imageList", "onScrolledToBottom"], requireAll = true)
    fun bindImageList(recyclerView: PaginableRecyclerView,
                      images: List<FlickrImagePresentationModel>?,
                      onBottomScrollListener: OnBottomScrollListener) {
        if (images == null)  return

        if (recyclerView.adapter == null) {
            val imagesAdapter = ImagesAdapter(imageLoader)
            val layoutManager = StaggeredGridLayoutManager(2, VERTICAL)

            recyclerView.layoutManager = layoutManager
            recyclerView.itemAnimator = null
            recyclerView.adapter = imagesAdapter
            recyclerView.onBottomScrollListener = onBottomScrollListener
        } else {
            recyclerView.removeProgressBar()
            recyclerView.submitList(images)
        }
        recyclerView.submitList(images)
    }

    @BindingAdapter("onQueryTextSubmit")
    fun bindQueryTextSubmitListener(searchView: MaterialSearchView, listener: MaterialSearchView.OnQueryTextListener) {
        searchView.setOnQueryTextListener(listener)
    }
}

interface OnBottomScrollListener {

    fun onScrolledToBottom()
}