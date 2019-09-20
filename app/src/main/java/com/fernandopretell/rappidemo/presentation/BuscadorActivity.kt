package com.fernandopretell.rappidemo.presentation

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fernandopretell.componentes.carousel.Card_pelicula
import com.fernandopretell.componentes.carousel.Carousel
import com.fernandopretell.componentes.carousel.models.BannerModel
import com.fernandopretell.componentes.carousel.models.CardModel
import com.fernandopretell.rappidemo.R
import com.fernandopretell.rappidemo.base.BaseActivity
import com.fernandopretell.rappidemo.model.CardModelParcelable
import com.fernandopretell.rappidemo.model.Pelicula
import com.fernandopretell.rappidemo.model.ResponseFinal
import com.fernandopretell.rappidemo.source.local.ResponseEntity
import com.fernandopretell.rappidemo.util.ConnectivityReceiver
import com.fernandopretell.rappidemo.util.Constants
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jledesma.dia2.viewmodel.PeliculaViewModel
import kotlinx.android.synthetic.main.activity_buscador.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main_scrolling.*
import java.util.ArrayList
import kotlin.math.round

class BuscadorActivity : BaseActivity()  {

    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscador)

        val list_card =intent?.getStringExtra("pelicula_list")

        val gson = Gson()
        val itemType = object : TypeToken<List<Pelicula>>() {}.type
        val itemList = gson.fromJson<List<Pelicula>>(list_card, itemType)

        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        searchBuscarPelicula.setActivated(true)
        searchBuscarPelicula.setQueryHint("Buscar película")
        searchBuscarPelicula.onActionViewExpanded()
        searchBuscarPelicula.setIconified(false)
        searchBuscarPelicula.setEnabled(false)

        actualizarUI(itemList)

        searchBuscarPelicula.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                val peliculaList = arrayListOf<CardModel>()
                for (pelicula in itemList) {
                    if (pelicula.original_title.toLowerCase().contains(newText)) {
                        peliculaList.add(transformPeliculaTocardModel(pelicula))
                    }
                }

                carouselBuscador.setItemsWithOutBanner(peliculaList)
                carouselBuscador.setHeightWrap()
                searchBuscarPelicula.setEnabled(true)
                return false
            }
        })

    }

    private fun actualizarUI(response: List<Pelicula>) {

        val listBuscador = arrayListOf<CardModel>()

        for (pelicula in response) {
            listBuscador.add(transformPeliculaTocardModel(pelicula))
        }

        carouselBuscador.setItemsWithOutBanner(listBuscador)

        carouselBuscador.carouselListener = object : Carousel.CarouselListener {

            override fun pressedItem(item: CardModel, position: Int) {

                val card = transformToParcelable(item)
                val intent = Intent(this@BuscadorActivity,VistaDetalleActivity::class.java)
                intent.putExtra("pelicula",card)
                startActivity(intent)
            }
        }
    }

    fun transformResponseFinalToListCard(data: ResponseFinal):ArrayList<CardModel>{
        val listCard = arrayListOf<CardModel>()
        for(pelicula in data.results){
            val card = CardModel(pelicula.id,
                pelicula.original_title,
                pelicula.vote_count,
                pelicula.popularity,
                pelicula.poster_path,
                pelicula.backdrop_path,
                pelicula.video,
                pelicula.adult,
                pelicula.vote_average,
                pelicula.overview,
                pelicula.release_date)
            listCard.add(card)
        }

        return listCard
    }

    override fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackBar = Snackbar.make(containerBuscador, "", Snackbar.LENGTH_LONG)


            val layout = snackBar?.getView() as Snackbar.SnackbarLayout
            layout.setBackgroundColor(ContextCompat.getColor(layout.context, android.R.color.transparent))
            layout.setPadding(0, 0, 0, 0)

            val snackView = LayoutInflater.from(this@BuscadorActivity).inflate(R.layout.snack_bar, null) as View

            layout.addView(snackView, 0)
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()

        } else {
            snackBar?.dismiss()
        }
    }

    private fun transformPeliculaTocardModel(item: Pelicula): CardModel {

        return CardModel(item.id,item.original_title,item.vote_count,item.popularity,item.poster_path,item.backdrop_path,item.video,item.adult,item.vote_average,item.overview,item.release_date)
    }

    private fun transformToParcelable(item: CardModel): CardModelParcelable {

        return CardModelParcelable(item.id_remote,item.original_title,item.vote_count,item.popularity,item.poster_path,item.backdrop_path,item.video,item.adult,item.vote_average,item.overview,item.release_date)
    }

    fun transformResponseEntityToFinal(data:ResponseEntity): ResponseFinal {
        data.let {
            val res = ResponseFinal(data.id,data.page,data.revenue,data.name,data.description,data.backdrop_path,data.results,data.average_rating,data.poster_path)
            return res
        }
    }

}
