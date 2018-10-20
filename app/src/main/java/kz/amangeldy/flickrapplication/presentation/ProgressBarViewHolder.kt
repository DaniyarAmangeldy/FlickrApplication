package kz.amangeldy.flickrapplication.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager



class ProgressBarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    init {
        val layoutParams = itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
    }
}