package com.fernandopretell.rappidemo.source.local

import androidx.annotation.NonNull
import androidx.room.*
import com.fernandopretell.rappidemo.model.Pelicula

/**
 * Created by fernandopretell.
 */
@Entity(tableName = "tabla_response")
data class ResponseEntity(

    /*@PrimaryKey(autoGenerate = true)
        @NonNull
        var _id: Int,*/
    @PrimaryKey
    @ColumnInfo(name = "id")
        var id: Int,

    @ColumnInfo(name = "page")
        var page: Int,

    @ColumnInfo(name = "revenue")
        var revenue: String,

    @ColumnInfo(name = "name")
        var name: String,

    @ColumnInfo(name = "description")
        var description: String,

    @ColumnInfo(name = "backdrop_path")
        var backdrop_path: String,

    @TypeConverters(ResultConverter::class)
        @ColumnInfo(name = "results")
        var results: List<Pelicula> = ArrayList(),

    @ColumnInfo(name = "average_rating")
        var average_rating: Double,

    @ColumnInfo(name = "poster_path")
        var poster_path: String,

    @ColumnInfo(name = "poster_path_local")
        var poster_path_local: String,

    @ColumnInfo(name = "backdrop_path_local")
        var backdrop_path_local: String


){
   /* @Ignore
    constructor(id_remote: Int,
                page: Int,
                revenue: String,
                name: String,
                description: String,
                backdrop_path: String,
                results: List<Pelicula>,
                average_rating: Double,
                poster_path: String):this(0,id_remote,
        page,
        revenue,
        name,
        description,
        backdrop_path,
        results,
        average_rating,
        poster_path)*/
}

