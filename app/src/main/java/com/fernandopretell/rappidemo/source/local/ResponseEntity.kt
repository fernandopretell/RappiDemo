package com.fernandopretell.rappidemo.source.local

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Created by fernandopretell.
 */
@Entity(tableName = "tabla_peliculas")
data class ResponseEntity(

        @PrimaryKey(autoGenerate = true)
        @NonNull
        var id: Int,

        @ColumnInfo(name = "id_remote")
        var id_remote: Int,

        @ColumnInfo(name = "original_title")
        var original_title: String,

        @ColumnInfo(name = "vote_count")
        var vote_count: Int,

        @ColumnInfo(name = "popularity")
        var popularity: Double,

        @ColumnInfo(name = "poster_path")
        var poster_path: String,

        @ColumnInfo(name = "backdrop_path")
        var backdrop_path: String,

        @ColumnInfo(name = "video")
        var video: Boolean,

        @ColumnInfo(name = "adult")
        var adult: Boolean,

        @ColumnInfo(name = "vote_average")
        var vote_average: Double,

        @ColumnInfo(name = "overview")
        var overview: String,

        @ColumnInfo(name = "release_date")
        var release_date: String


){
    @Ignore
    constructor(id_remote: Int,
                original_title: String,
                vote_count: Int,
                popularity: Double,
                poster_path: String,
                backdrop_path: String,
                video: Boolean,
                adult: Boolean,
                vote_average: Double,
                overview: String,
                release_date: String):this(0,id_remote,
        original_title,
        vote_count,
        popularity,
        poster_path,
        backdrop_path,
        video,
        adult,
        vote_average,
        overview,
        release_date)
}

