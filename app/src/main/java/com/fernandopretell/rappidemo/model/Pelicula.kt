package com.fernandopretell.rappidemo.model

data class Pelicula(val id: Int,
                    val original_title: String,
                    val vote_count: Int,
                    val popularity: Double,
                    val poster_path: String,
                    val backdrop_path: String,
                    val video: Boolean,
                    val adult: Boolean,
                    val vote_average: Double,
                    val overview: String,
                    val release_date: String)
