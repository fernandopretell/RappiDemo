package com.fernandopretell.rappidemo.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.fernandopretell.rappidemo.source.remote.ResponseApi
import com.fernandopretell.rappidemo.util.ConnectivityReceiver
import com.fernandopretell.rappidemo.util.Constants
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.jledesma.dia2.viewmodel.PeliculaViewModel
import kotlinx.android.synthetic.main.activity_buscador.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main_scrolling.*
import java.io.File
import kotlin.math.round


class MainActivity() : BaseActivity(){

    /*override val ctx: Context = this
    override val layoutSnack: Int = R.layout.snack_bar
    override val containerSnack: View = nsContainer*/

    private var notaViewModel: PeliculaViewModel? = null
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notaViewModel = ViewModelProviders.of(this).get(PeliculaViewModel::class.java)

        val conneted = isNetworkVConnected(this)

        if(conneted){
             notaViewModel!!.getPeliculasRemoto()
                 showNetworkMessage(conneted)
        }

        notaViewModel!!.listarPeliculasLocal()?.observe(this, Observer<ResponseEntity> { response ->
            val dataEntity = response
            dataEntity?.let { transformResponseEntityToFinal(it) }?.let { actualizarUI(it) }
        })

        verifyStoragePermissions(this)
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

        val appDirectoryName = "RappiDemo"
        val imageRoot = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), appDirectoryName
        )

        val uriFile = Uri.fromFile(File(imageRoot,response.backdrop_path.substring(1)))

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

    private fun transformToParcelableCardModel(item: CardModel): CardModelParcelable {

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

    private fun verifyStoragePermissions(activity: Activity): Boolean {
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val REQUEST_EXTERNAL_STORAGE = 1
        val permission =
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    applicationContext,
                    "Gracias por darnos los permisos.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //runtime_permissions();
            }
        }
    }

    override fun showNetworkMessage(isConnected: Boolean) {
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
}
