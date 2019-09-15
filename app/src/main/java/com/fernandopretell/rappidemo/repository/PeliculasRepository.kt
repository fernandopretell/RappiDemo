package com.fernandopretell.rappidemo.repository

import android.annotation.SuppressLint
import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fernandopretell.rappidemo.model.Pelicula
import com.fernandopretell.rappidemo.source.local.PeliculaDao
import com.fernandopretell.rappidemo.source.local.PeliculaEntity
import com.fernandopretell.rappidemo.source.local.PeliculasDatabase
import com.fernandopretell.rappidemo.source.remote.HelperWs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PeliculasRepository(application: Application) {

    private var peliculaDAo: PeliculaDao? = null
    var list_peliculas: LiveData<List<PeliculaEntity>>? = null

    //Ws
    private var productos: MutableLiveData<List<Pelicula>>? = null
    internal var webservice = HelperWs.getService()

    init {
        val database = PeliculasDatabase.getInstance(application)
        peliculaDAo = database?.notaDao()
        list_peliculas = peliculaDAo?.listarPeliculas()
    }

    fun listar_peliculas(): LiveData<List<PeliculaEntity>>?{
        return list_peliculas
    }

    fun insert(pelicula: PeliculaEntity){
        InsertNotaAsyncTask(peliculaDAo).execute(pelicula)
    }

    fun delete(pelicula: PeliculaEntity){
        DeletePeliculaAsyncTask(peliculaDAo).execute(pelicula)
    }

    @SuppressLint("StaticFieldLeak")
    inner class DeletePeliculaAsyncTask(private val notaDao: PeliculaDao?) : AsyncTask<PeliculaEntity,Void,Void>(){

        override fun doInBackground(vararg pelicula: PeliculaEntity): Void? {
            notaDao?.delete(pelicula[0])
            return  null
        }

    }

    @SuppressLint("StaticFieldLeak")
    inner class InsertNotaAsyncTask(private val notaDao: PeliculaDao?) : AsyncTask<PeliculaEntity,Void,Void>(){

        override fun doInBackground(vararg pelicula: PeliculaEntity): Void? {
            notaDao?.insert(pelicula[0])
            return  null
        }

    }

    //ws
    fun obtenerPeliculas(): LiveData<List<Pelicula>> {

        if (productos == null) {
            productos = MutableLiveData()
            listarProductos()
        }

        return productos as MutableLiveData<List<Pelicula>>
    }

    private fun listarProductos() {

        webservice.listarPeliculas().enqueue(object : Callback<ArrayList<Pelicula>> {
            override fun onResponse(call: Call<ArrayList<Pelicula>>, response: Response<ArrayList<Pelicula>>) {
                productos?.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<Pelicula>>, t: Throwable) {

                Log.e("TAG", t.message.toString())
            }
        })
    }
}