package com.fernandopretell.rappidemo.repository

import android.annotation.SuppressLint
import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fernandopretell.rappidemo.source.remote.ResponseApi
import com.fernandopretell.rappidemo.source.local.ResponseDao
import com.fernandopretell.rappidemo.source.local.ResponseEntity
import com.fernandopretell.rappidemo.source.local.PeliculasDatabase
import com.fernandopretell.rappidemo.source.remote.HelperWs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeliculasRepository(application: Application) {

    private var responseDAo: ResponseDao? = null
    var list_peliculas: LiveData<ResponseEntity>? = null

    //Ws
    private var response: MutableLiveData<ResponseApi>? = null
    internal var webservice = HelperWs.getService()

    init {
        val database = PeliculasDatabase.getInstance(application)
        responseDAo = database?.responseDao()
        list_peliculas = responseDAo?.obtenerData()
    }

    fun listar_peliculas(): LiveData<ResponseEntity>?{
        return list_peliculas
    }

    fun insert(response: ResponseEntity){
        InsertNotaAsyncTask(responseDAo).execute(response)
    }

    fun deleteAll(){
        DeletePeliculasAsyncTask(responseDAo).execute()
    }

    @SuppressLint("StaticFieldLeak")
    inner class DeletePeliculasAsyncTask(private val responseDao: ResponseDao?) : AsyncTask<Void, Void, Void>(){

        override fun doInBackground(vararg aVoid: Void): Void? {
            responseDao?.deleteAll()
            return  null
        }

    }

    @SuppressLint("StaticFieldLeak")
    inner class InsertNotaAsyncTask(private val responseDao: ResponseDao?) : AsyncTask<ResponseEntity,Void,Void>(){

        override fun doInBackground(vararg response: ResponseEntity): Void? {
            responseDao?.insert(response[0])
            return  null
        }

    }

    //ws
    fun obtenerPeliculas(): LiveData<ResponseApi> {

        if (response == null) {
            response = MutableLiveData()
            listarPeliculasRemoto()
        }

        return response as MutableLiveData<ResponseApi>
    }

    private fun listarPeliculasRemoto() {

        webservice.obtenerDataRemota().enqueue(object : Callback<ResponseApi> {
            override fun onFailure(call: Call<ResponseApi>, t: Throwable) {
                Log.e("jledesma", t.message.toString())            }

            override fun onResponse(call: Call<ResponseApi>, response: Response<ResponseApi>) {
                when(response.code()){

                    200 -> {
                        deleteAll()
                        response.body()?.let { transformApiToEntity(it) }?.let { insert(it) }
                    }
                }
            }
        })
    }

    fun transformApiToEntity(response: ResponseApi):ResponseEntity{

        return ResponseEntity(response.id,response.page,response.revenue,response.name,response.description,response.backdrop_path,response.results,response.average_rating,response.poster_path)

    }
}