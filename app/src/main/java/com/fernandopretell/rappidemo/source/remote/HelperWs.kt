package com.fernandopretell.rappidemo.source.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by fernandopretell.
 */
class HelperWs {

    companion object {

        fun  getConfigurationServiceData(): Retrofit {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .connectTimeout(180, TimeUnit.SECONDS)
                    .build()

            return Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/4/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()


        }

        fun getServiceData(): WebServiceData {
            return getConfigurationServiceData().create(WebServiceData::class.java)
        }

        fun  getConfigurationServiceGetImages(): Retrofit {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor)
                .readTimeout(180, TimeUnit.SECONDS)
                .connectTimeout(180, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://image.tmdb.org/t/p/w500/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()


        }

        fun getServiceImages(): WebServiceImages {
            return getConfigurationServiceGetImages().create(WebServiceImages::class.java)
        }
    }


}