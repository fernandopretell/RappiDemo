package com.example.rappidemo.source.remote

import android.provider.Settings.System.getConfiguration
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

        fun  getConfiguration(): Retrofit {


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

        fun getService(): WebService {
            return getConfiguration().create(WebService::class.java)
        }
    }


}