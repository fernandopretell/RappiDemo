package com.fernandopretell.rappidemo.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

/**
 * Created by jledesma on 8/23/19.
 */
@Dao
interface ResponseDao {

    @Insert(onConflict = REPLACE)
    fun insert(response: ResponseEntity)

    @Query("DELETE FROM TABLA_RESPONSE")
    fun deleteAll()

    @Query("SELECT * FROM TABLA_RESPONSE order by id desc")
    fun obtenerData(): LiveData<ResponseEntity>
}