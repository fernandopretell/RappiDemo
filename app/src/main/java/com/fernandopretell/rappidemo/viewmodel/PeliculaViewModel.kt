package com.jledesma.dia2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fernandopretell.rappidemo.model.ResponseApi
import com.fernandopretell.rappidemo.repository.PeliculasRepository
import com.fernandopretell.rappidemo.source.local.ResponseEntity

/**
 * Created by fernandopretell.
 */
class PeliculaViewModel(application: Application): AndroidViewModel(application) {

    private var peliculaRepository: PeliculasRepository? = null
    private var list_peliculas: LiveData<List<ResponseEntity>>? = null


    init {

        peliculaRepository = PeliculasRepository(application)
        list_peliculas = peliculaRepository?.listar_peliculas()
    }

    fun insert(response: ResponseEntity) {

        peliculaRepository?.insert(response)
    }

    fun listarPeliculas(): LiveData<List<ResponseEntity>>? {

        return list_peliculas
    }

    //ws
    fun listarPeliculasRemoto(): LiveData<ResponseApi> {
        return peliculaRepository?.obtenerPeliculas()!!
    }
}