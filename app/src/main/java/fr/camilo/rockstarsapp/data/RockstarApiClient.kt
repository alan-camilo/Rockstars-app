package fr.camilo.rockstarsapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RockstarApiClient {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.myjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RockstarApi = retrofit.create(RockstarApi::class.java)
}