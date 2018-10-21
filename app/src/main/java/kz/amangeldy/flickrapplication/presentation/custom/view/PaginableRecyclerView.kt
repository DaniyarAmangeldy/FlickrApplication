package kz.amangeldy.flickrapplication.presentation.custom.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kz.amangeldy.flickrapplication.presentation.LoadableAdapter

class PaginableRecyclerView(
    context: Context,
    attributeSet: AttributeSet?,
    attrs: Int
): RecyclerView(context, attributeSet, attrs), LoadableAdapter {

    var onBottomScrollListener: OnBottomScrollListener? = null
    private var isLoading = false

    private val onScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemCount = layoutManager?.childCount ?: -1
            val totalItemCount = layoutManager?.itemCount ?: -1
            val pastVisibleItems = getPastVisibleItems()
            if (visibleItemCount + pastVisibleItems >= totalItemCount && !isLoading) {
                isLoading = true
                (recyclerView.adapter as LoadableAdapter).addProgressBar()
                onBottomScrollListener?.onScrolledToBottom()
            }
        }
    }

    constructor(context: Context): this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet, 0)

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        addOnScrollListener(onScrollListener)
    }

    override fun addProgressBar() {
        (adapter as? LoadableAdapter)?.addProgressBar()
    }

    override fun removeProgressBar() {
        (adapter as? LoadableAdapter)?.removeProgressBar()
    }

    fun <T> submitList(list: List<T>) {
        isLoading = false
        (adapter  as? ListAdapter<T, *>)?.submitList(list)
    }

    private fun getPastVisibleItems(): Int {
        return when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                (layoutManager as StaggeredGridLayoutManager)
                    .findFirstVisibleItemPositions(intArrayOf(1, 2))
                    .firstOrNull() ?: -1
            }
            is GridLayoutManager ->
                (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            is LinearLayoutManager ->
                (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            else -> -1
        }
    }

    interface OnBottomScrollListener {

        fun onScrolledToBottom()
    }
}