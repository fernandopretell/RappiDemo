package com.example.rappidemo.repository

import android.annotation.SuppressLint
import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.rappidemo.source.local.PeliculaDao
import com.example.rappidemo.source.local.PeliculaEntity
import com.example.rappidemo.source.local.PeliculasDatabase

class PeliculasRepository(application: Application) {

    private var notaDAo: PeliculaDao? = null
    var list_notas: LiveData<List<PeliculaEntity>>? = null

    init {
        val database = PeliculasDatabase.getInstance(application)
        notaDAo = database?.notaDao()
        list_notas = notaDAo?.listarPeliculas()
    }

    fun listar_notas(): LiveData<List<PeliculaEntity>>?{
        return list_notas
    }

    fun insert(pelicula: PeliculaEntity){
        InsertNotaAsyncTask(notaDAo).execute(pelicula)
    }

    fun delete(pelicula: PeliculaEntity){
        DeleteNotaAsyncTask(notaDAo).execute(pelicula)
    }

    @SuppressLint("StaticFieldLeak")
    inner class DeleteNotaAsyncTask(private val notaDao: PeliculaDao?) : AsyncTask<PeliculaEntity,Void,Void>(){

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
}