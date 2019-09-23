package com.fernandopretell.rappidemo.source.remote

import com.fernandopretell.rappidemo.util.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by fernandopretell.
 */
interface WebServiceData {

    @Headers("Content-Type: application/json","Authorization: Bearer ${Constants.API_KEY}")
    @GET("list/{list_id}?page=1&api_key=<<api_key>>")
    fun obtenerDataRemota(@Path("list_id") list_id: Int?): Call<ResponseApi>
}