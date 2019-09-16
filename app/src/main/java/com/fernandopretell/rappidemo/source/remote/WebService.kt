package com.fernandopretell.rappidemo.source.remote

import com.fernandopretell.rappidemo.model.Pelicula
import com.fernandopretell.rappidemo.model.ResponseApi
import com.fernandopretell.rappidemo.util.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.ArrayList

/**
 * Created by fernandopretell.
 */
interface WebService {

    @Headers("Content-Type: application/json","Authorization: Bearer ${Constants.API_KEY}")
    @GET("list/1?page=1&api_key=<<api_key>>")
    fun listarPeliculas(): Call<ResponseApi>
}