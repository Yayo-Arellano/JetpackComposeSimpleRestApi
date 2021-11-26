package com.nopalsoft.simple.rest.datasource

import com.nopalsoft.simple.rest.model.ApiResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET


interface RestDataSource {

    @GET("?inc=name,location,picture")
    suspend fun getAllResults() : ApiResponse

}
