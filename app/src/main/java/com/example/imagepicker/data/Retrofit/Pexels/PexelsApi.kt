package com.example.imagepicker.data.Retrofit.Pexels

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PexelsApi {

    @Headers(
        "Authorization: 563492ad6f91700001000001cba1a67ab86043409eac67b616c0c556"
    )
    @GET("search")
    suspend fun getListOfPhotosFromPexelsApi(@Query("query") search_tag :String): Response<PexelsResponse>
}