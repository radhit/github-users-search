package com.healthmate.api

import com.example.riskaadhita.api.DataResponse
import com.example.riskaadhita.data.Item
import retrofit2.Call
import retrofit2.http.*

interface BaseApi {
    @GET
    fun getUsers(@Url url: String): Call<DataResponse<List<Item>>>

}