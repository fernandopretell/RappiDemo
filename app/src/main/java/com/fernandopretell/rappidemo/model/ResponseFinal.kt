package com.fernandopretell.rappidemo.model

class ResponseFinal(val id: Int,
                    val page: Int,
                    val revenue: String,
                    val name: String,
                    val description: String,
                    val backdrop_path: String,
                    val results: List<Pelicula>,
                    val average_rating: Double,
                    val poster_path: String)