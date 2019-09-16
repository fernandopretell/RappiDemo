package com.fernandopretell.rappidemo.source.remote

import com.fernandopretell.rappidemo.model.Pelicula

data class ResponseApi(val id: Int,
                       val page: Int,
                       //val iso_3166_1: String,
                       //val total_results: Int,
                       //val object_ids: List<VideoPelicula>,
                       val revenue: String,
                       val name: String,
                       val description: String,
                       val backdrop_path: String,
                       val results: List<Pelicula>,
                       val average_rating: Double,
                       val poster_path: String)