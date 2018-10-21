package kz.amangeldy.flickrapplication.presentation.binding

import android.database.MatrixCursor
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kz.amangeldy.flickrapplication.presentation.ImageViewHolder
import kz.amangeldy.flickrapplication.utils.ImageLoader
import kz.amangeldy.flickrapplication.presentation.ImagesAdapter
import kz.amangeldy.flickrapplication.presentation.custom.view.PaginableRecyclerView
import kz.amangeldy.flickrapplication.presentation.entity.ImagePresentationModel

class MainBindingAdapter(private val imageLoader: ImageLoader) {

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter(value = ["imageList", "onScrolledToBottom", "onImageClickListener"], requireAll = true)
    fun bindImageList(recyclerView: PaginableRecyclerView,
                      images: List<ImagePresentationModel>?,
                      onBottomScrollListener: PaginableRecyclerView.OnBottomScrollListener,
                      onImageClickListener: ImageViewHolder.OnImageClickListener?
    ) {
        if (images == null)  return

        if (recyclerView.adapter == null) {
            val imagesAdapter = ImagesAdapter(imageLoader, onImageClickListener)
            val layoutManager = StaggeredGridLayoutManager(2, VERTICAL)

            recyclerView.layoutManager = layoutManager
            recyclerView.itemAnimator = null
            recyclerView.adapter = imagesAdapter
            recyclerView.onBottomScrollListener = onBottomScrollListener
            recyclerView.submitList(images)
        } else {
            recyclerView.removeProgressBar()
            recyclerView.submitList(images)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    @BindingAdapter("onQueryTextSubmit")
    fun bindQueryTextSubmitListener(searchView: SearchView, listener: SearchView.OnQueryTextListener) {
        searchView.setOnQueryTextListener(listener)
    }

    @BindingAdapter("suggestions")
    fun bindSuggestionList(searchView: SearchView, suggestions: List<String>) {
        val columns = arrayOf("_id", "name")
        val cursor = MatrixCursor(columns)
        suggestions.forEachIndexed { index, item -> cursor.addRow(arrayOf(index, item)) }
        val cursorAdapter = SimpleCursorAdapter(
            searchView.context,
            android.R.layout.simple_list_item_1,
            cursor,
            columns,
            intArrayOf(-1, android.R.id.text1),
            FLAG_REGISTER_CONTENT_OBSERVER
        )
        searchView.suggestionsAdapter = (cursorAdapter)
    }

    @BindingAdapter("onRefresh")
    fun bindOnRefreshListener(layout: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
        layout.setOnRefreshListener(listener)
    }
}