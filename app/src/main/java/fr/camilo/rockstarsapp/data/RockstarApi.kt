package fr.camilo.rockstarsapp.data

import fr.camilo.rockstarsapp.model.Rockstar
import retrofit2.Call
import retrofit2.http.GET


interface RockstarApi {

    @GET("bins/19791b")
    fun listRockstars(): Call<ArrayList<Rockstar>>
}