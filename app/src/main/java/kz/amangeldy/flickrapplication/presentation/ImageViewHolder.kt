package kz.amangeldy.flickrapplication.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_card_image_item.view.*
import kz.amangeldy.flickrapplication.ImageLoader
import kz.amangeldy.flickrapplication.R
import kz.amangeldy.flickrapplication.presentation.entity.FlickrImagePresentationModel
import kz.amangeldy.flickrapplication.presentation.entity.Owner
import kz.amangeldy.flickrapplication.setGone
import kz.amangeldy.flickrapplication.setVisible

class ImageViewHolder(
    view: View,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(view) {

    @DrawableRes
    private val avatarPlaceholderRes = R.drawable.flick_avatar_placeholder

    private val ownerName: TextView = view.layout_card_image_item_name
    private val ownerUsername: TextView = view.layout_card_image_item_username
    private val ownerImage: ImageView = view.layout_card_image_item_avatar
    private val image: ImageView = view.layout_card_image_item_image
    private val description: TextView = view.layout_card_image_item_description

    fun bind(item: FlickrImagePresentationModel) {
        setOwnerInfo(item.owner)
        imageLoader.loadImage(item.imageUrl, image)
        description.text = item.title
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
}