package com.fernandopretell.componentes.carousel.models


data class CardModel(
    val id_remote: Int,
    val original_title: String,
    val vote_count: Int,
    val popularity: Double,
    val poster_path: String,
    val backdrop_path: String,
    val video: Boolean,
    val adult: Boolean,
    val vote_average: Double,
    val overview: String,
    val release_date: String
) : Comparable<CardModel> {

    var index: Int = 0

    override fun compareTo(other: CardModel): Int {
        return 0
    }

}