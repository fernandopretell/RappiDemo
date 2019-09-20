package com.fernandopretell.componentes.carousel

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.SyncStateContract
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fernandopretell.componentes.Constants_comp
import com.fernandopretell.componentes.R
import com.fernandopretell.componentes.carousel.models.BannerModel
import kotlinx.android.synthetic.main.banner.view.*
import java.io.File


class Banner : RelativeLayout {

    var bannerClickListener: BannerClickListener? = null

    var cardRadius: Float = context.resources.getDimension(R.dimen.banner_default_radius)

    var textLabel: String? = context.resources.getString(R.string.banner_label)

    var placeholder: Drawable? = null

    constructor(context: Context) : super(context) {
        inflate(context, R.layout.banner, this)
        setupUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflate(context, R.layout.banner, this)
        setupAttrs(context, attrs)
        setupUI()
    }

    private fun setupAttrs(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.Banner, 0, 0)
        try {
            textLabel = ta.getString(R.styleable.Banner_banner_text_label)
            placeholder = ta.getDrawable(R.styleable.Banner_banner_placeholder)
            cardRadius = ta.getDimension(R.styleable.Banner_banner_card_corner,
                context.resources.getDimension(R.dimen.banner_default_radius))
        } finally {
            ta.recycle()
        }
    }

    private fun setupUI() {
        bannerContainer.radius = cardRadius

        if (placeholder != null) {
            bannerImage.setImageDrawable(placeholder)
        }

        setupListener()
    }

    private fun setupListener() {
        bannerContainer.setOnClickListener {
            bannerClickListener?.onClick(it)
        }
    }

    fun updatePlaceHolder(newPH: Drawable) {
        placeholder = newPH
    }

    fun updateCornerRadius(radius: Float) {
        bannerContainer.radius = cardRadius
    }

    fun updateImageAndTextLocal(item: BannerModel) {

        //val url = Constants_comp.URL_BASE+item.url

        val appDirectoryName = "RappiDemo"
        val imageRoot = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), appDirectoryName
        )

        val uriFile = Uri.fromFile(File(imageRoot,item.url.substring(1)))

        val requestOptions = RequestOptions()
            //.placeholder(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        Glide.with(context)
            .load(uriFile)
            .apply(requestOptions)
            .into(bannerImage)
        tvTituloBanner.text = item.textLabel
    }

    fun updateImageAndText(item: BannerModel) {

        val url = Constants_comp.URL_BASE+item.url
        val requestOptions = RequestOptions()
            //.placeholder(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        Glide.with(context)
            .load(url)
            .apply(requestOptions)
            .into(bannerImage)
        tvTituloBanner.text = item.textLabel
    }

    interface BannerClickListener {
        fun onClick(view: View)
    }
}
