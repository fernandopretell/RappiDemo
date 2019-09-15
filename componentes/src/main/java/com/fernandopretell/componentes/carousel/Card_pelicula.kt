package com.fernandopretell.componentes.carousel

import android.content.Context
import android.graphics.drawable.Drawable
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
import kotlinx.android.synthetic.main.card_pelicula.view.*

class Card_pelicula:RelativeLayout {

    var cardClickListener: CardClickListener? = null

    var cardRadius: Float = context.resources.getDimension(R.dimen.banner_default_radius)

    var textLabel: String? = context.resources.getString(R.string.banner_label)

    var placeholder: Drawable? = null

    constructor(context: Context) : super(context) {
        inflate(context, R.layout.card_pelicula, this)
        setupUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflate(context, R.layout.card_pelicula, this)
        setupAttrs(context, attrs)
        setupUI()
    }

    private fun setupAttrs(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.Card_pelicula, 0, 0)
        try {
            placeholder = ta.getDrawable(R.styleable.Card_pelicula_card_placeholder)
            cardRadius = ta.getDimension(R.styleable.Card_pelicula_card_card_corner,
                context.resources.getDimension(R.dimen.banner_default_radius))
        } finally {
            ta.recycle()
        }
    }
    private fun setupUI() {
        cardContainer.radius = cardRadius

        if (placeholder != null) {
            ivBackdrop_path.setImageDrawable(placeholder)
        }

        setupListener()
    }

    private fun setupListener() {
        cardContainer.setOnClickListener {
            cardClickListener?.onClick(it)
        }
    }

    fun updatePlaceHolder(newPH: Drawable) {
        placeholder = newPH
    }

    fun updateCornerRadius(radius: Float) {
        cardContainer.radius = cardRadius
    }

    fun updateImage(imageURL: String){

        val url = Constants_comp.URL_BASE+imageURL

        val requestOptions = RequestOptions()
            //.placeholder(placeholder)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        Glide.with(context)
            .load(url)
            .apply(requestOptions)
            .into(ivBackdrop_path)
    }

    interface CardClickListener {
        fun onClick(view: View)
    }

}