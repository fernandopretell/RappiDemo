package com.jledesma.dia2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fernandopretell.rappidemo.model.Pelicula
import com.fernandopretell.rappidemo.repository.PeliculasRepository
import com.fernandopretell.rappidemo.source.local.PeliculaEntity

/**
 * Created by fernandopretell.
 */
class PeliculaViewModel(application: Application): AndroidViewModel(application) {

    private var notaRepository: PeliculasRepository? = null
    private var list_notas: LiveData<List<PeliculaEntity>>? = null


    init {

        notaRepository = PeliculasRepository(application)
        list_notas = notaRepository?.listar_peliculas()
    }

    fun insert(pelicula: PeliculaEntity) {

        notaRepository?.insert(pelicula)
    }

    fun listarPeliculas(): LiveData<List<PeliculaEntity>>? {

        return list_notas
    }

    //ws
    fun obtenerProductos(): LiveData<List<Pelicula>> {
        return notaRepository?.obtenerPeliculas()!!
    }
}