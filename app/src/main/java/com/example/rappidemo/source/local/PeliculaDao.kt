package com.example.rappidemo.source.local

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by jledesma on 8/23/19.
 */
@Dao
interface PeliculaDao {

    @Insert
    fun insert(pelicula: PeliculaEntity)

    @Update
    fun update(pelicula: PeliculaEntity)

    @Delete
    fun delete(pelicula: PeliculaEntity)

    @Query("DELETE FROM TABLA_PELICULAS")
    fun deleteAll()

    @Query("SELECT * FROM TABLA_PELICULAS order by id desc")
    fun listarPeliculas(): LiveData<List<PeliculaEntity>>
}