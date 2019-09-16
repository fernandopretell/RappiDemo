package com.fernandopretell.rappidemo.repository

import android.annotation.SuppressLint
import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fernandopretell.rappidemo.model.ResponseApi
import com.fernandopretell.rappidemo.source.local.PeliculaDao
import com.fernandopretell.rappidemo.source.local.ResponseEntity
import com.fernandopretell.rappidemo.source.local.PeliculasDatabase
import com.fernandopretell.rappidemo.source.remote.HelperWs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeliculasRepository(application: Application) {

    private var peliculaDAo: PeliculaDao? = null
    var list_peliculas: LiveData<List<ResponseEntity>>? = null

    //Ws
    private var pelicula: MutableLiveData<ResponseApi>? = null
    internal var webservice = HelperWs.getService()

    init {
        val database = PeliculasDatabase.getInstance(application)
        peliculaDAo = database?.notaDao()
        list_peliculas = peliculaDAo?.listarPeliculas()
    }

    fun listar_peliculas(): LiveData<List<ResponseEntity>>?{
        return list_peliculas
    }

    fun insert(response: ResponseEntity){
        InsertNotaAsyncTask(peliculaDAo).execute(response)
    }

    fun delete(response: ResponseEntity){
        DeletePeliculaAsyncTask(peliculaDAo).execute(response)
    }

    @SuppressLint("StaticFieldLeak")
    inner class DeletePeliculaAsyncTask(private val peliculaDao: PeliculaDao?) : AsyncTask<ResponseEntity,Void,Void>(){

        override fun doInBackground(vararg response: ResponseEntity): Void? {
            peliculaDao?.delete(response[0])
            return  null
        }

    }

    @SuppressLint("StaticFieldLeak")
    inner class InsertNotaAsyncTask(private val peliculaDao: PeliculaDao?) : AsyncTask<ResponseEntity,Void,Void>(){

        override fun doInBackground(vararg response: ResponseEntity): Void? {
            peliculaDao?.insert(response[0])
            return  null
        }

    }

    //ws
    fun obtenerPeliculas(): LiveData<ResponseApi> {

        if (pelicula == null) {
            pelicula = MutableLiveData()
            listarProductos()
        }

        return pelicula as MutableLiveData<ResponseApi>
    }

    private fun listarProductos() {

        webservice.listarPeliculas().enqueue(object : Callback<ResponseApi> {
            override fun onFailure(call: Call<ResponseApi>, t: Throwable) {
                Log.e("jledesma", t.message.toString())            }

            override fun onResponse(call: Call<ResponseApi>, response: Response<ResponseApi>) {
                pelicula?.value = response.body()
                //Log.e("jledesma", response.body()?)
            }
        })
    }
}