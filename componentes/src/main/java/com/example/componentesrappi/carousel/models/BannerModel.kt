package com.example.componentesrappi.carousel.models

data class BannerModel(val url: String, val textLabel: String) : Comparable<BannerModel> {

    override fun compareTo(other: BannerModel): Int {
        return 0
    }

}
