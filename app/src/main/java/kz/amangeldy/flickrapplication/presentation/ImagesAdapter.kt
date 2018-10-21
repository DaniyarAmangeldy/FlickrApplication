package kz.amangeldy.flickrapplication.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.amangeldy.flickrapplication.utils.ImageLoader
import kz.amangeldy.flickrapplication.R
import kz.amangeldy.flickrapplication.presentation.entity.ImagePresentationModel
import kz.amangeldy.flickrapplication.utils.removeIfInstance
import java.lang.IllegalStateException

class ImagesAdapter(
    private val imageLoader: ImageLoader,
    private val onImageClickListener: ImageViewHolder.OnImageClickListener?
) : ListAdapter<MainListItem, RecyclerView.ViewHolder>(ImageDiffCallback()), LoadableAdapter {

    private var list = mutableListOf<MainListItem>()

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ImagePresentationModel -> R.layout.layout_card_image_item
            is ProgressBarListItem -> R.layout.layout_progress_bar_item
            else -> throw IllegalStateException("Cannot create view type with element: ${list[position]}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)

        return when (viewType) {
            R.layout.layout_card_image_item -> ImageViewHolder(view, imageLoader)
            R.layout.layout_progress_bar_item -> ProgressBarViewHolder(view)
            else -> throw IllegalStateException("Cannot create View holder with viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            holder.bind(getItem(position) as ImagePresentationModel, onImageClickListener)
        }
    }

    override fun submitList(list: MutableList<MainListItem>?) {
        list?.let {
            this.list.clear()
            this.list.addAll(it)
        }
        super.submitList(this.list)
    }

    override fun addProgressBar() {
        list.add(ProgressBarListItem())
        notifyItemInserted(list.count() - 1)
    }

    override fun removeProgressBar() {
        list.removeIfInstance { it is ProgressBarListItem }
        notifyItemRemoved(list.count())
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        (holder as? ImageViewHolder)?.onRecycled()
        super.onViewRecycled(holder)
    }
}

class ImageDiffCallback : DiffUtil.ItemCallback<MainListItem>() {
    override fun areContentsTheSame(
        oldItem: MainListItem,
        newItem: MainListItem
    ): Boolean {
        return oldItem is ImagePresentationModel &&
                newItem is ImagePresentationModel &&
                (oldItem.id == newItem.id ||
                 oldItem.title == newItem.title ||
                 oldItem.imageUrl == newItem.imageUrl ||
                 oldItem.owner == newItem.owner
                 )
    }

    override fun areItemsTheSame(
        oldItem: MainListItem,
        newItem: MainListItem
    ): Boolean {
        return oldItem == newItem
    }
}

interface LoadableAdapter {

    fun addProgressBar()

    fun removeProgressBar()
}

interface MainListItem

class ProgressBarListItem : MainListItem