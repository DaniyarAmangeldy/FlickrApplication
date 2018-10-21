package kz.amangeldy.flickrapplication.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImageLoader {

    fun loadImage(url: String, view: ImageView, @DrawableRes placeholder: Int? = null)
}