package com.extraedge.practical_test.retrofit

import com.extraedge.practical_test.model.RocketResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("rockets")
    suspend fun getRocketList(): Response<List<RocketResponse>>

    @GET("rockets/{id}")
    suspend fun getRecketDetail(@Path("id") id:String):Response<RocketResponse>
}