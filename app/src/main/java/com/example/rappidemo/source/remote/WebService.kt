package com.example.rappidemo.source.remote

import com.example.rappidemo.model.Pelicula
import com.example.rappidemo.util.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.ArrayList

/**
 * Created by fernandopretell.
 */
interface WebService {

    @Headers("Content-Type: application/json","Authorization: Bearer ${Constants.API_KEY}")
    @GET("list")
    fun listarPeliculas(@Query("sort_by") sort_by: String? ): Call<ArrayList<Pelicula>>
}