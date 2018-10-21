package kz.amangeldy.flickrapplication.presentation.image.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_image_full.view.*
import kz.amangeldy.flickrapplication.R
import kz.amangeldy.flickrapplication.utils.ImageLoader
import org.koin.android.ext.android.inject

private const val IMAGE_URL_KEY = "image_url_key"

class ImageFullDialogFragment: DialogFragment(), View.OnClickListener {

    private val imageLoader: ImageLoader by inject()

    companion object {
        fun createInstance(imageUrl: String): DialogFragment {
            return ImageFullDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_URL_KEY, imageUrl)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_AppCompat_Light)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_full, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.fragment_image_full_back.setOnClickListener(this)
        val imageUrl = arguments?.getString(IMAGE_URL_KEY)!!
        imageLoader.loadImage(imageUrl, view.fragment_image_full_image)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogSlideAnimation)
    }

    override fun onClick(view: View?) {
        dismiss()
    }
}