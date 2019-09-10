package com.example.rappidemo.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rappidemo.model.Pelicula

/**
 * Created by jledesma on 8/23/19.
 */
@Dao
interface PeliculaDao {

    @Insert
    fun insert(pelicula: Pelicula)

    @Update
    fun update(pelicula: Pelicula)

    @Delete
    fun delete(pelicula: Pelicula)

    @Query("DELETE FROM tabla_notas")
    fun deleteAll()

    @Query("SELECT * FROM tabla_notas order by id desc")
    fun listarNotas(): LiveData<List<Pelicula>>
}