package kz.amangeldy.flickrapplication.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_card_image_item.view.*
import kz.amangeldy.flickrapplication.utils.ImageLoader
import kz.amangeldy.flickrapplication.R
import kz.amangeldy.flickrapplication.presentation.entity.FlickrImagePresentationModel
import kz.amangeldy.flickrapplication.presentation.entity.Owner
import kz.amangeldy.flickrapplication.utils.setGone
import kz.amangeldy.flickrapplication.utils.setVisible

class ImageViewHolder(
    private val view: View,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    @DrawableRes
    private val avatarPlaceholderRes = R.drawable.flick_avatar_placeholder

    private val ownerName: TextView = view.layout_card_image_item_name
    private val ownerUsername: TextView = view.layout_card_image_item_username
    private val ownerImage: ImageView = view.layout_card_image_item_avatar
    private val image: ImageView = view.layout_card_image_item_image
    private val description: TextView = view.layout_card_image_item_description
    private var item: FlickrImagePresentationModel? = null
    private var onImageClickListener: OnImageClickListener? = null

    override fun onClick(view: View?) {
        item?.let { onImageClickListener?.onImageClick(it) }
    }

    fun bind(item: FlickrImagePresentationModel, onImageClickListener: OnImageClickListener?) {
        view.setOnClickListener(this)
        this.onImageClickListener = onImageClickListener
        this.item = item
        setOwnerInfo(item.owner)
        image.setImageDrawable(null)
        imageLoader.loadImage(item.imageUrl, image)
        description.text = item.title
    }

    fun onRecycled() {
        view.setOnClickListener(null)
        onImageClickListener = null
    }

    private fun setOwnerInfo(item: Owner) {
        with(item) {
            if (fullName.isNullOrBlank()) {
                ownerName.setGone()
            } else {
                ownerName.setVisible()
                ownerName.text = fullName
            }
            if (userName.isNullOrBlank()) {
                ownerUsername.setGone()
            } else {
                ownerUsername.setVisible()
                ownerUsername.text = userName
            }
            if (avatarUrl.isNotBlank()) {
                imageLoader.loadImage(avatarUrl, ownerImage, placeholder = avatarPlaceholderRes)
            }
        }
    }

    interface OnImageClickListener {
        fun onImageClick(image: FlickrImagePresentationModel)
    }
}