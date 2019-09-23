package com.fernandopretell.rappidemo.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
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
import com.fernandopretell.rappidemo.base.BaseActivity
import com.fernandopretell.rappidemo.model.CardModelParcelable
import com.fernandopretell.rappidemo.model.ResponseFinal
import com.fernandopretell.rappidemo.source.local.ResponseEntity
import com.fernandopretell.rappidemo.viewmodel.PeliculaViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main_scrolling.*
import java.io.File
import kotlin.math.round


class MainActivity() : BaseActivity(){

    private var notaViewModel: PeliculaViewModel? = null
    private var snackBar: Snackbar? = null
    private var id: Int? = null
    private var conneted: Boolean? = null
    private var cat: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pbMain.visibility = View.VISIBLE
        ivIconBuscador.visibility = View.GONE
        icMoney.visibility = View.GONE

        id = intent.getIntExtra("id",1)


        notaViewModel = ViewModelProviders.of(this).get(PeliculaViewModel::class.java)

        conneted = isNetworkVConnected(this)

        if(conneted as Boolean){
            notaViewModel!!.getPeliculasRemoto(id?:1)
            showNetworkMessage(conneted as Boolean)
        }

        Handler().postDelayed({

            notaViewModel!!.listarPeliculasLocal()?.observe(this, Observer<ResponseEntity> { response ->
                val dataEntity = response
                dataEntity?.let { transformResponseEntityToFinal(it) }?.let { actualizarUI(it) }
            })

        }, 3000)
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

        response.backdrop_path.let {

            val appDirectoryName = "RappiDemo"
            val imageRoot = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ), appDirectoryName
            )

            val uriFile = Uri.fromFile(File(imageRoot,it.substring(1)))

            val recaudacion = round((response.revenue.toDouble()/1000000)).toInt()

            tvTitulo.text = response.name
            tvDescripcion.text = response.description
            tvRevenue.text = "$recaudacion Mlls"

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_image_default)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

            Glide.with(this)
                .load(uriFile)
                .apply(requestOptions)
                .into(ivHeader)

            pbMain.visibility = View.GONE
            contaninerMain.visibility = View.VISIBLE
            ivIconBuscador.visibility = View.VISIBLE
            icMoney.visibility = View.VISIBLE
        }

        ivIconBuscador.setOnClickListener {
            val intent = Intent(this@MainActivity,BuscadorActivity::class.java)
            intent.putExtra("pelicula_list", Gson().toJson(response.results))
            startActivity(intent)
        }

        carousel1.carouselListener = object : Carousel.CarouselListener {

            override fun pressedItem(item: CardModel, position: Int) {

                val card = transformToParcelableCardModel(item)
                val intent = Intent(this@MainActivity,VistaDetalleActivity::class.java)
                intent.putExtra("pelicula",card)
                startActivity(intent)
            }
        }

        carousel2.carouselListener = object : Carousel.CarouselListener {

            override fun pressedItem(item: CardModel, position: Int) {
                val card = transformToParcelableCardModel(item)
                val intent = Intent(this@MainActivity,VistaDetalleActivity::class.java)
                intent.putExtra("pelicula",card)
                startActivity(intent)
            }
        }

        carousel3.carouselListener = object : Carousel.CarouselListener {

            override fun pressedItem(item: CardModel, position: Int) {
                val card = transformToParcelableCardModel(item)
                val intent = Intent(this@MainActivity,VistaDetalleActivity::class.java)
                intent.putExtra("pelicula",card)
                startActivity(intent)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        conneted?.let { showNetworkMessage(it) }
    }

    private fun transformToParcelableCardModel(item: CardModel): CardModelParcelable {

        return CardModelParcelable(item.id_remote,item.original_title,item.vote_count,item.popularity,item.poster_path,item.backdrop_path,item.video,item.adult,item.vote_average,item.overview,item.release_date)
    }

    fun transformResponseEntityToFinal(data:ResponseEntity):ResponseFinal{
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

    override fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackBar = make(nsContainer, "", Snackbar.LENGTH_LONG)

            val layout = snackBar?.getView() as Snackbar.SnackbarLayout
            layout.setBackgroundColor(ContextCompat.getColor(layout.context, android.R.color.transparent))
            layout.setPadding(0, 0, 0, 0)

            val snackView = LayoutInflater.from(this@MainActivity).inflate(R.layout.snack_bar, null) as View

            layout.addView(snackView, 0)
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()

        } else {
            snackBar?.dismiss()
            notaViewModel!!.getPeliculasRemoto(id?:1)
        }
    }
}
