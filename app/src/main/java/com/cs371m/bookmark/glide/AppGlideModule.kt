package com.cs371m.bookmark.glide

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.cs371m.bookmark.MainActivity2
import com.cs371m.bookmark.R


@GlideModule
class AppGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // You can change this to make Glide more verbose
        builder.setLogLevel(Log.ERROR)
    }
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}

// Calling glideapp.with with the most specific Activity/Fragment
// context allows it to track lifecycles for your fetch
// https://stackoverflow.com/questions/31964737/glide-image-loading-with-application-context
object Glide {
    private val width = Resources.getSystem().displayMetrics.widthPixels
    private val height = Resources.getSystem().displayMetrics.heightPixels
    private var glideOptions = RequestOptions ()
        // Options like CenterCrop are possible, but I like this one best
        // Evidently you need fitCenter or dontTransform.  If you use centerCrop, your
        // list disappears.  I think that was an old bug.
        .fitCenter()
        // Rounded corners are so lovely.
        .transform(RoundedCorners (5))

    private fun fromHtml(source: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(source).toString()
        }
    }
    // Please ignore, this is for our testing
    private fun assetFetch(urlString: String, imageView: ImageView) {
        GlideApp.with(imageView.context)
            .load(urlString)
            .apply(glideOptions)
            .override(width, height)
            .into(imageView)
        if (urlString.endsWith("bigcat0.jpg")) {
            imageView.tag = "bigcat0.jpg"
        } else if (urlString.endsWith("bigcat1.jpg")) {
            imageView.tag = "bigcat1.jpg"
        } else if (urlString.endsWith("bigcat2.jpg")) {
            imageView.tag = "bigcat2.jpg"
        } else if (urlString.endsWith("bigdog0.jpg")) {
            imageView.tag = "bigdog0.jpg"
        }
    }

    fun glideFetch(urlString: String, imageView: ImageView, heightSize: Int) {
        if (MainActivity2.globalDebug) {
           assetFetch(urlString, imageView)
        } else {
            GlideApp.with(imageView.context)
                .load(fromHtml(urlString))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Log.d("glide", "width: ${resource?.minimumWidth}")
                        if (resource?.minimumWidth == 1) {
                            imageView.setImageResource(R.drawable.no_picture)
                            return true
                        }

                        return false
                    }
                })
                .apply(glideOptions)
                .error(R.drawable.no_picture)
                .override(width * heightSize / height, heightSize)
                //.override(widthsize, widthsize * height / height)
                .into(imageView)
        }
    }
}
