package com.fernandopretell.rappidemo.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

/**
 * Created by jledesma on 8/23/19.
 */
@Dao
interface PeliculaDao {

    @Insert(onConflict = REPLACE)
    fun insert(response: ResponseEntity)

    /*@Update
    fun update(pelicula: ResponseEntity)

    @Delete
    fun delete(pelicula: ResponseEntity)

    @Query("DELETE FROM TABLA_PELICULAS")
    fun deleteAll()*/

    @Query("SELECT * FROM TABLA_PELICULAS order by id desc")
    fun listarPeliculas(): LiveData<List<ResponseEntity>>
}