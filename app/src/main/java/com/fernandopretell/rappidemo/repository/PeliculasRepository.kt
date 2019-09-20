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
import com.fernandopretell.rappidemo.util.DownloadFileAsyncTask
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class PeliculasRepository(application: Application) {

    private var responseDAo: ResponseDao? = null
    var list_peliculas: LiveData<ResponseEntity>? = null

    var sizeList: Int? =null
    var array = arrayListOf<String>()

    //Ws
    private var response: MutableLiveData<ResponseApi>? = null
    internal var webserviceData = HelperWs.getServiceData()
    internal var webserviceImages = HelperWs.getServiceImages()

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

        webserviceData.obtenerDataRemota(6).enqueue(object : Callback<ResponseApi> {
            override fun onFailure(call: Call<ResponseApi>, t: Throwable) {
                Log.e("TAG", t.message.toString())
            }

            override fun onResponse(call: Call<ResponseApi>, response: Response<ResponseApi>) {
                when(response.code()){

                    200 -> {
                        deleteAll()
                        response.body()?.let {
                            insert(transformApiToEntity(it))
                            listarUrlImagenes(it)
                        }
                    }
                }
            }
        })
    }

    private fun listarUrlImagenes(response: ResponseApi) {

        val list_images = arrayListOf<String>()
        list_images.add(response.backdrop_path.substring(1))
        list_images.add(response.poster_path.substring(1))

        for (ls in response.results){
            list_images.add(ls.backdrop_path.substring(1))
            list_images.add(ls.poster_path.substring(1))
        }

        array = ArrayList()

        for (path in list_images) {
            array.add(path)
        }
        sizeList = array.size

        descargarImagenes()
    }

    private fun descargarImagenes(){

        if (sizeList != 0) {
            webserviceImages.obtenerImagen(array.get(array.size - sizeList!!)).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("TAG", t.message.toString())
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    when(response.code()){

                        200 -> {
                            val downloadFileAsyncTask = DownloadFileAsyncTask(array.get(array.size - sizeList!!))
                            downloadFileAsyncTask.execute(response.body()?.byteStream())
                            sizeList = sizeList!!.dec()
                            descargarImagenes()
                        }
                    }
                }
            })
        }
    }

    fun transformApiToEntity(response: ResponseApi):ResponseEntity{

        return ResponseEntity(response.id,response.page,response.revenue,response.name,response.description,response.backdrop_path,response.results,response.average_rating,response.poster_path,"","")
    }
}