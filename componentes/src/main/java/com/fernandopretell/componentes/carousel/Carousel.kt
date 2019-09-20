package com.fernandopretell.componentes.carousel

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import biz.belcorp.mobile.components.core.decorations.CustomHorizontalDecoration
import biz.belcorp.mobile.components.core.helpers.StylesHelper
import com.fernandopretell.componentes.R
import com.fernandopretell.componentes.carousel.models.CardModel
import com.fernandopretell.componentes.carousel.models.BannerModel
import kotlinx.android.synthetic.main.carousel.view.*

class Carousel: RelativeLayout {

    private lateinit var carouselAdapter: CarouselAdapter

    var carouselListener: CarouselListener? = null

    var lm: LinearLayoutManager? = null

    var gr: GridLayoutManager? = null

    var hasBanner: Boolean = false

    var bannerPlaceHolder: Drawable? = null

    var bannerImage: String? = null

    var processData: Boolean = true

    var listItems = arrayListOf<Comparable<*>>()

    var textLabel: String? = context.resources.getString(R.string.carousel_label)
    var textLabelSize: Float = context.resources.getDimension(R.dimen.carousel_default_size_text_label)
    var heightRecycler: Float = context.resources.getDimension(R.dimen.carousel_default_height_recyclerview)
    var layoutRecycler: String? = context.resources.getString(R.string.carousel_default_layout_recyclerview)

    constructor(context: Context) : super(context) {
        inflate(context, R.layout.carousel, this)
        setupUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflate(context, R.layout.carousel, this)
        setupAttrs(context, attrs)
        setupUI()
    }

    private fun setupAttrs(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.Carousel, 0, 0)
        try {
            textLabel = ta.getString(R.styleable.Carousel_carousel_textLabel)
            textLabelSize = ta.getDimension(R.styleable.Carousel_carousel_size_textLabel, textLabelSize)
            heightRecycler = ta.getDimension(R.styleable.Carousel_carousel_height_recycler, heightRecycler)
            layoutRecycler = ta.getString(R.styleable.Carousel_carousel_layout_recycler)
        } finally {
            ta.recycle()
        }
    }

    private fun setupUI() {

        if(layoutRecycler.equals("linear")){
            lm = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            itemsRecycler.layoutManager = lm
            itemsRecycler.isNestedScrollingEnabled = false
        }else if(layoutRecycler.equals("grid")){
            gr = GridLayoutManager(context,3)
            itemsRecycler.layoutManager = gr
        }

        itemsRecycler.layoutParams = RelativeLayout.LayoutParams(itemsRecycler.layoutParams.width,heightRecycler.toInt())


        carouselAdapter = CarouselAdapter(
            carouselItemListener,
            context
        )

        itemsRecycler.setHasFixedSize(true)
        itemsRecycler.adapter = carouselAdapter

    }

    fun setItems(list: ArrayList<CardModel>,banner:BannerModel){
        updateList(list,banner)
    }

    fun setItemsWithOutBanner(list: ArrayList<CardModel>){
        updateListWithOutBanner(list)
    }

    fun updateBannerPlaceHolder(newBannerPH: Drawable) {
        hasBanner = true
        bannerPlaceHolder = newBannerPH
    }

    fun updateImagePlaceHolder(newItemPH: Drawable) {
        carouselAdapter.itemPlaceholder = newItemPH
    }

    fun updateBannerImage(newBannerImg: String) {
        hasBanner = true
        bannerImage = newBannerImg
    }

    fun disableProcessData() {
        carouselAdapter.useAlternativeLayout = true
        processData = false
    }

    fun setHeight(height: Float) {
        itemsRecycler.layoutParams = RelativeLayout.LayoutParams(itemsRecycler.layoutParams.width, StylesHelper(context).dpToPx(height))
        itemsRecycler.requestLayout()
    }

    fun setHeightWrap() {
        itemsRecycler.layoutParams = RelativeLayout.LayoutParams(itemsRecycler.layoutParams.width, RelativeLayout.LayoutParams.MATCH_PARENT)
        itemsRecycler.requestLayout()
    }

    fun resetScroll() {
        itemsRecycler.adapter?.let {
            val items = (it as CarouselAdapter).itemCount
            if (items > 0) {
                val fullWidth = items * context.resources.getDimensionPixelSize(R.dimen.card_default_container_width)
                itemsRecycler.smoothScrollBy(-fullWidth, 0)
            }
        }
    }

    fun updateList(list: ArrayList<CardModel>, banner: BannerModel) {
        if (!list.isEmpty()) {
            listItems.clear()
            listItems.addAll(list)

                carouselAdapter.updateData(processList(listItems,banner))

            itemsRecycler.visibility = View.VISIBLE

        }
    }

    fun updateListWithOutBanner(list: ArrayList<CardModel>) {
        if (!list.isEmpty()) {
            listItems.clear()
            listItems.addAll(list)

            carouselAdapter.updateData(listItems)

            itemsRecycler.visibility = View.VISIBLE

        }
    }

    private fun processList(fullList: ArrayList<Comparable<*>>, banner: BannerModel) : ArrayList<Comparable<*>> {
        val finalList = arrayListOf<Comparable<*>>()
        var startIndex = 0

        finalList.add(banner)

        var finalIndex = fullList.size-1

        for (i in startIndex..finalIndex) {
            finalList.add(fullList[i])
        }

        listItems = finalList

        return finalList
    }

    private val carouselItemListener = object : CardItemListener {

        override fun pressedItem(item: CardModel, position: Int) {
            carouselListener?.pressedItem(item, position)
        }
    }

    interface CarouselListener {
        fun pressedItem(item: CardModel, position: Int)
    }
}