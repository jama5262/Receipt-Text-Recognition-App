package com.jama.receipttextrecognitionapp.services

import com.jama.receipttextrecognitionapp.model.Giphy
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val GIPHY_API_KEY = "dc6zaTOxFJmzC"
const val BASE_URL = "http://api.giphy.com/v1/gifs/"

interface JsonPlaceHolderAPI {

    @GET("random")
    fun getGif(
        @Query("tag") tag: String
    ): Call<Giphy>

    companion object {
        operator fun invoke(): JsonPlaceHolderAPI {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", GIPHY_API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JsonPlaceHolderAPI::class.java)
        }
    }

}