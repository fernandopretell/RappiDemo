package com.fernandopretell.rappidemo.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fernandopretell.componentes.carousel.models.BannerModel
import com.fernandopretell.componentes.carousel.models.CardModel
import com.fernandopretell.rappidemo.R
import com.fernandopretell.rappidemo.model.Pelicula
import com.fernandopretell.rappidemo.model.ResponseApi
import com.fernandopretell.rappidemo.source.local.PeliculasDatabase
import com.fernandopretell.rappidemo.util.Constants
import com.jledesma.dia2.viewmodel.PeliculaViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main_scrolling.*


class MainActivity : AppCompatActivity() {

    private var notaViewModel: PeliculaViewModel? = null

    var database:PeliculasDatabase? = null
    var list_nota:List<Pelicula>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        notaViewModel = ViewModelProviders.of(this).get(PeliculaViewModel::class.java)
        notaViewModel?.listarPeliculasRemoto()?.observe(this, Observer<ResponseApi> { data ->
            data?.let {

                val listCard = transformPeliculaToCard(data)

                val listPopular = arrayListOf<CardModel>()
                val listTopRated = arrayListOf<CardModel>()
                val listUpcoming = arrayListOf<CardModel>()

                listPopular.addAll(listCard)
                listPopular.sortByDescending { it.popularity }

                listTopRated.addAll(listCard)
                listTopRated.sortByDescending { it.vote_count }

                listUpcoming.addAll(listCard)
                listUpcoming.sortByDescending { it.release_date }

                val banner1 = BannerModel(data.poster_path,"Popular")
                val banner2 = BannerModel(data.poster_path,"Top Rated")
                val banner3 = BannerModel(data.poster_path,"Upcoming")

                carousel1.setItems(listPopular,banner1)
                carousel2.setItems(listTopRated,banner2)
                carousel3.setItems(listUpcoming,banner3)

                val url = Constants.URL_BASE+data.backdrop_path

                tvTitulo.text = data.name
                tvDescripcion.text = data.description


                val requestOptions = RequestOptions()
                    //.placeholder(placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

                Glide.with(this)
                    .load(url)
                    .apply(requestOptions)
                    .into(ivHeader)



            }
        })




    }

    fun transformPeliculaToCard(data:ResponseApi):ArrayList<CardModel>{
        val listCard = arrayListOf<CardModel>()
        for(pelicula in data.results){
            val card = CardModel(pelicula.id_remote,
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
   /* fun readJSONFromAsset(): String {
        var json: String? = null
        try {
            val  inputStream:InputStream = assets.open("document.json")
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null.toString()
        }
        return json
    }*/
}
