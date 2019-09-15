package com.fernandopretell.componentes.carousel

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fernandopretell.componentes.R
import com.fernandopretell.componentes.carousel.models.BannerModel
import com.fernandopretell.componentes.carousel.models.CardModel
import kotlinx.android.synthetic.main.card_pelicula.view.*
import kotlinx.android.synthetic.main.carousel_banner_item.view.*
import kotlinx.android.synthetic.main.carousel_card_item.view.*

class CarouselAdapter(
    private val listener: CardItemListener,val ctx: Context
): RecyclerView.Adapter<CarouselAdapter.BaseViewHolder<*>>() {

    companion object {
        const val TYPE_BANNER = 0
        const val TYPE_MOVIE = 1
    }

    var itemPlaceholder: Drawable? = null

    var useAlternativeLayout: Boolean = false

    private val data: ArrayList<Comparable<*>> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val context = parent.context
        return when (viewType) {
            TYPE_BANNER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.carousel_banner_item, parent, false)
                BannerViewHolder(view,data)
            }
            TYPE_MOVIE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.carousel_card_item, parent, false)
                CardMovieViewHolder(view,data)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = data[position]
        when (holder) {
            is BannerViewHolder -> holder.bind(position, element as BannerModel, listener)
            is CardMovieViewHolder -> holder.bind(position, element as CardModel, listener)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val comparable = data[position]
        return when (comparable) {
            is BannerModel -> TYPE_BANNER
            is CardModel -> TYPE_MOVIE
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: List<Comparable<*>>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun resetData() {
        data.clear()
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(position: Int, item: T, itemListener: CardItemListener)
    }

    class BannerViewHolder(val view: View, val data: List<Comparable<*>>) :
        BaseViewHolder<BannerModel>(view) {

        private val banner = view.carouselBanner as Banner

        override fun bind(position: Int, item: BannerModel, itemListener: CardItemListener) {
                banner.updateImageAndText(item)
        }


    }

    inner class CardMovieViewHolder(
        val view: View, val data: List<Comparable<*>>
    ) :
        BaseViewHolder<CardModel>(view) {

        private val card = view.carouselCard as Card_pelicula

        override fun bind(position: Int, item: CardModel, itemListener: CardItemListener) {

            card.updateImage(item.poster_path)

            // Listener
            card.cardClickListener = object : Card_pelicula.CardClickListener {
                override fun onClick(view: View) {
                    itemListener.pressedItem(item,position)
                }
            }
        }


    }
}

interface CardItemListener {

    fun pressedItem(item: CardModel, position: Int)

    fun onBindMulti(imagenCard: ImageView, position: Int)
}