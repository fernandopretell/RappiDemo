package com.example.componentesrappi.carousel

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import biz.belcorp.mobile.components.core.helpers.StylesHelper
import com.example.componentesrappi.R
import com.example.componentesrappi.carousel.models.CardModel
import com.example.componentesrappi.carousel.models.BannerModel
import kotlinx.android.synthetic.main.carousel.view.*

class Carousel: RelativeLayout {

    private lateinit var carouselAdapter: CarouselAdapter

    var carouselListener: CarouselListener? = null

    var lm: LinearLayoutManager? = null

    var hasBanner: Boolean = false

    var bannerPlaceHolder: Drawable? = null

    var bannerImage: String? = null

    var carouselItems: Int = context.resources.getInteger(R.integer.carousel_default_items)

    var carouselDecoration: Float = context.resources.getDimension(R.dimen.carousel_default_decoration)

    var processData: Boolean = true

    var showMoreCard: Boolean = false

    var listItems = arrayListOf<Comparable<*>>()

    var visibleList = arrayListOf<CardModel>()

    var textLabel: String? = context.resources.getString(R.string.carousel_label)
    var textLabelSize: Float = context.resources.getDimension(R.dimen.carousel_default_size_text_label)
    var heightRecycler: Float = context.resources.getDimension(R.dimen.carousel_default_height_recyclerview)

    constructor(context: Context) : super(context) {
        RelativeLayout.inflate(context, R.layout.carousel, this)
        setupUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        RelativeLayout.inflate(context, R.layout.carousel, this)
        setupAttrs(context, attrs)
        setupUI()
    }

    private fun setupAttrs(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.Carousel, 0, 0)
        try {
            textLabel = ta.getString(R.styleable.Carousel_carousel_textLabel)
            textLabelSize = ta.getDimension(R.styleable.Carousel_carousel_size_textLabel, textLabelSize)
            heightRecycler = ta.getDimension(R.styleable.Carousel_carousel_height_recycler, heightRecycler)
        } finally {
            ta.recycle()
        }
    }

    private fun setupUI() {
        lm = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        carouselAdapter = CarouselAdapter(
            carouselItemListener,
            context
        )
        itemsRecycler.layoutManager = lm
        //itemsRecycler.addItemDecoration(CustomHorizontalDecoration(carouselDecoration.toInt()))
        itemsRecycler.setHasFixedSize(true)
        itemsRecycler.adapter = carouselAdapter
        //itemsRecycler.addOnScrollListener(CarouselScrollListener())
        itemsRecycler.isNestedScrollingEnabled = false
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

    fun showLoading() {
        itemsLoading.visibility = View.VISIBLE
        itemsRecycler.visibility = View.GONE
    }

    fun isLoading() : Boolean {
        return itemsLoading.visibility == View.VISIBLE
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

    fun updateList(list: ArrayList<CardModel>) {
        if (!list.isEmpty()) {

            if (processData) {
                listItems = arrayListOf()

                if (hasBanner) {
                    listItems.add(bannerImage!!)
                }

                listItems.addAll(list)



                carouselAdapter.updateData(processList(listItems))
            } else {
                listItems.addAll(list)
                carouselAdapter.updateData(list)
            }

            itemsRecycler.visibility = View.VISIBLE
            itemsLoading.visibility = View.GONE
        }
    }

    fun showMoreCard() {
        showMoreCard = true
    }


    private fun processList(fullList: ArrayList<Comparable<*>>) : ArrayList<Comparable<*>> {
        val finalList = arrayListOf<Comparable<*>>()
        var startIndex = 0

        if (fullList[0] is String) {
            finalList.add(fullList[0])
            startIndex = 1
        }

        var finalIndex = startIndex + carouselItems

        if (finalIndex > fullList.size)
            finalIndex = fullList.size

        finalIndex--

        for (i in startIndex..finalIndex) {
            finalList.add(fullList[i])
        }

        if (fullList[fullList.size - 1] is BannerModel) {
            finalList.add(fullList[fullList.size - 1])
        }

        listItems = finalList

        return finalList
    }

    private val carouselItemListener = object : CardItemListener {

        override fun pressedItem(item: CardModel, position: Int) {
            carouselListener?.pressedItem(item, position)
        }

        override fun onBindMulti(multiItem: ImageView, position: Int) {
            carouselListener?.bindMulti(multiItem, position)
        }
    }

    fun setIsCustomView(isCustomView: Boolean) {
        carouselAdapter.setIsCustomView(isCustomView)
    }

    interface CarouselListener {


        fun pressedItem(item: CardModel, position: Int)

        fun bindMulti(multiItem: ImageView, position: Int)
    }
}