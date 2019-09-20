package com.fernandopretell.componentes.list_bienvenida

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fernandopretell.componentes.R
import com.fernandopretell.componentes.carousel.CardItemListener
import com.fernandopretell.componentes.carousel.Carousel
import com.fernandopretell.componentes.carousel.CarouselAdapter
import com.fernandopretell.componentes.carousel.models.BannerModel
import com.fernandopretell.componentes.carousel.models.CardModel
import com.fernandopretell.componentes.list_bienvenida.models.ItemBienvenida
import kotlinx.android.synthetic.main.carousel.view.*
import kotlinx.android.synthetic.main.lista_bienvenida.view.*

class Lista_bienvenida : RelativeLayout{

    private lateinit var bienvenidaAdapter: BienvenidaAdapter

    var bienvenidaListener: BienvenidaListener? = null

    var lm: LinearLayoutManager? = null

    constructor(context: Context) : super(context) {
        RelativeLayout.inflate(context, R.layout.lista_bienvenida, this)
        setupUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        RelativeLayout.inflate(context, R.layout.lista_bienvenida, this)
        setupAttrs(context, attrs)
        setupUI()
    }

    private fun setupAttrs(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.Lista_bienvenida, 0, 0)
        try {

        } finally {
            ta.recycle()
        }
    }

    private fun setupUI() {

        lm = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvListaBienvenida.layoutManager = lm
        rvListaBienvenida.isNestedScrollingEnabled = false

        bienvenidaAdapter = BienvenidaAdapter(bienvenidaItemListener)

        rvListaBienvenida.setHasFixedSize(true)
        rvListaBienvenida.adapter = bienvenidaAdapter

    }

    fun updateList(list: ArrayList<ItemBienvenida>) {
        if (!list.isEmpty()) {
            bienvenidaAdapter.updateData(list)
            rvListaBienvenida.visibility = View.VISIBLE
        }
    }

    private val bienvenidaItemListener = object : bienvenidaItemListener {

        override fun pressedItem(item: ItemBienvenida, position: Int) {
            bienvenidaListener?.pressedItem(item, position)
        }
    }

    interface BienvenidaListener {
        fun pressedItem(item: ItemBienvenida, position: Int)
    }
}