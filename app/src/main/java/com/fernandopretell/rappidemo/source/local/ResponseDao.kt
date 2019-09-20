package com.fernandopretell.rappidemo.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

/**
 * Created by fernandopretell.
 */
@Dao
interface ResponseDao {

    @Insert(onConflict = REPLACE)
    fun insert(response: ResponseEntity)

    @Query("DELETE FROM TABLA_RESPONSE")
    fun deleteAll()

    @Query("SELECT * FROM TABLA_RESPONSE LIMIT 1")
    fun obtenerData(): LiveData<ResponseEntity>
}