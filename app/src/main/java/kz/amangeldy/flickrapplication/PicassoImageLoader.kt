package kz.amangeldy.flickrapplication

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.squareup.picasso.Picasso

class PicassoImageLoader: ImageLoader {

    override fun loadImage(url: String, view: ImageView, @DrawableRes placeholder: Int?) {
        val loader = Picasso.get().load(url)
        placeholder?.let {
            loader.placeholder(it)
        }
        loader.into(view)
    }
}