package com.fernandopretell.rappidemo.presentation

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fernandopretell.componentes.carousel.Carousel
import com.fernandopretell.componentes.carousel.models.BannerModel
import com.fernandopretell.componentes.carousel.models.CardModel
import com.fernandopretell.rappidemo.R
import com.fernandopretell.rappidemo.model.CardModelParcelable
import com.fernandopretell.rappidemo.model.ResponseFinal
import com.fernandopretell.rappidemo.source.local.ResponseEntity
import com.fernandopretell.rappidemo.source.remote.ResponseApi
import com.fernandopretell.rappidemo.util.ConnectivityReceiver
import com.fernandopretell.rappidemo.util.Constants
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.jledesma.dia2.viewmodel.PeliculaViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main_scrolling.*
import kotlin.math.round


class MainActivity : AppCompatActivity(),ConnectivityReceiver.ConnectivityReceiverListener {

    private var notaViewModel: PeliculaViewModel? = null
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notaViewModel = ViewModelProviders.of(this).get(PeliculaViewModel::class.java)


        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        if(isNetworkVConnected(this)){
             notaViewModel!!.listarPeliculasRemoto().observe(this, Observer<ResponseApi> { response ->
                val dataApi = response
                 dataApi?.let { transformResponseApiToFinal(it) }?.let { actualizarUI(it) }
            })
        }

        notaViewModel!!.listarPeliculas()?.observe(this, Observer<ResponseEntity> { response ->
            val dataEntity = response
            dataEntity?.let { transformResponseEntityToFinal(it) }?.let { actualizarUI(it) }
        })
    }

    private fun actualizarUI(response: ResponseFinal) {

        val listPopular = arrayListOf<CardModel>()
        val listTopRated = arrayListOf<CardModel>()
        val listUpcoming = arrayListOf<CardModel>()

        listPopular.addAll(transformResponseFinalToListCard(response))
        listPopular.sortByDescending { it.popularity }

        listTopRated.addAll(transformResponseFinalToListCard(response))
        listTopRated.sortByDescending { it.vote_count }

        listUpcoming.addAll(transformResponseFinalToListCard(response))
        listUpcoming.sortByDescending { it.release_date }

        val banner1 = BannerModel(response.poster_path,"Popular")
        val banner2 = BannerModel(response.poster_path,"Top Rated")
        val banner3 = BannerModel(response.poster_path,"Upcoming")

        carousel1.setItems(listPopular,banner1)
        carousel2.setItems(listTopRated,banner2)
        carousel3.setItems(listUpcoming,banner3)

        val url = Constants.URL_BASE+response.backdrop_path

        val recaudacion = round((response.revenue.toDouble()/1000000)).toInt()

        tvTitulo.text = response.name
        tvDescripcion.text = response.description
        tvRevenue.text = "$recaudacion Mlls"

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_image_default)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .into(ivHeader)

        carousel1.carouselListener = object : Carousel.CarouselListener {

            override fun pressedItem(item: CardModel, position: Int) {

                val card = transformToParcelable(item)
                val intent = Intent(this@MainActivity,VistaDetalleActivity::class.java)
                intent.putExtra("pelicula",card)
                startActivity(intent)
            }
        }

        carousel2.carouselListener = object : Carousel.CarouselListener {

            override fun pressedItem(item: CardModel, position: Int) {
                val card = transformToParcelable(item)
                val intent = Intent(this@MainActivity,VistaDetalleActivity::class.java)
                intent.putExtra("pelicula",card)
                startActivity(intent)
            }
        }

        carousel3.carouselListener = object : Carousel.CarouselListener {

            override fun pressedItem(item: CardModel, position: Int) {
                val card = transformToParcelable(item)
                val intent = Intent(this@MainActivity,VistaDetalleActivity::class.java)
                intent.putExtra("pelicula",card)
                startActivity(intent)
            }
        }
    }

    private fun transformToParcelable(item: CardModel): CardModelParcelable {

        return CardModelParcelable(item.id_remote,item.original_title,item.vote_count,item.popularity,item.poster_path,item.backdrop_path,item.video,item.adult,item.vote_average,item.overview,item.release_date)
    }

    fun transformResponseEntityToFinal(data:ResponseEntity):ResponseFinal{
        data.let {
            val res = ResponseFinal(data.id,data.page,data.revenue,data.name,data.description,data.backdrop_path,data.results,data.average_rating,data.poster_path)
            return res
        }
    }

    fun transformResponseApiToFinal(data: ResponseApi):ResponseFinal{
        data.let {
            val res = ResponseFinal(data.id,data.page,data.revenue,data.name,data.description,data.backdrop_path,data.results,data.average_rating,data.poster_path)
            return res
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

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }
    private fun showNetworkMessage(isConnected: Boolean) {

        if (!isConnected) {
            snackBar = Snackbar.make(nsContainer, "", Snackbar.LENGTH_LONG)


            val layout = snackBar?.getView() as Snackbar.SnackbarLayout
            layout.setBackgroundColor(ContextCompat.getColor(layout.context, android.R.color.transparent))
            layout.setPadding(0, 0, 0, 0)

            val snackView = LayoutInflater.from(this@MainActivity).inflate(R.layout.snack_bar, null) as View

            layout.addView(snackView, 0)
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()

        } else {
            snackBar?.dismiss()
        }
    }

    fun isNetworkVConnected(context: Context):Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting

    }
}
