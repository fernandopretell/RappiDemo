package com.fernandopretell.rappidemo.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fernandopretell.componentes.Constants_comp
import com.fernandopretell.componentes.carousel.models.BannerModel
import com.fernandopretell.componentes.carousel.models.CardModel
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.InputStream
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fernandopretell.rappidemo.R
import com.fernandopretell.rappidemo.model.Pelicula
import com.fernandopretell.rappidemo.source.local.PeliculaEntity
import com.fernandopretell.rappidemo.source.local.PeliculasDatabase
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.jledesma.dia2.viewmodel.PeliculaViewModel
import kotlinx.android.synthetic.main.content_main_scrolling.*


class MainActivity : AppCompatActivity() {

    private var notaViewModel: PeliculaViewModel? = null

    var database:PeliculasDatabase? = null
    var list_nota:List<Pelicula>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        notaViewModel = ViewModelProviders.of(this).get(PeliculaViewModel::class.java)
        notaViewModel?.listarPeliculas()?.observe(this, Observer<List<PeliculaEntity>> { notas ->
            //Toast.makeText(MainActivity.this,"Ok",Toast.LENGTH_SHORT).show();
            notas?.let {

            }
        })

        /*val jsonObject = JSONObject(readJSONFromAsset())

        val result = jsonObject.getJSONArray("results")

        val typeToken = object : TypeToken<ArrayList<CardModel>>() {}
        val list = Gson().fromJson<ArrayList<CardModel>>(result.toString(), typeToken.type)

        val listPopular = arrayListOf<CardModel>()
        val listTopRated = arrayListOf<CardModel>()
        val listUpcoming = arrayListOf<CardModel>()

        listPopular.addAll(list)
        listPopular.sortByDescending { it.popularity }

        listTopRated.addAll(list)
        listTopRated.sortByDescending { it.vote_count }

        listUpcoming.addAll(list)
        listUpcoming.sortByDescending { it.release_date }

        val banner1 = BannerModel(jsonObject.getString("poster_path"),"Popular")
        val banner2 = BannerModel(jsonObject.getString("poster_path"),"Top Rated")
        val banner3 = BannerModel(jsonObject.getString("poster_path"),"Upcoming")

        carousel1.setItems(listPopular,banner1)
        carousel2.setItems(listTopRated,banner2)
        carousel3.setItems(listUpcoming,banner3)

        val url = Constants_comp.URL_BASE+jsonObject.getString("backdrop_path")

        tvTitulo.text = jsonObject.getString("name")
        tvDescripcion.text = jsonObject.getString("description")


        val requestOptions = RequestOptions()
            //.placeholder(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .into(ivHeader)
*/


    }


    fun readJSONFromAsset(): String {
        var json: String? = null
        try {
            val  inputStream:InputStream = assets.open("document.json")
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null.toString()
        }
        return json
    }
}
