package com.example.imagepicker.data.Retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ImageApi {
    @GET("/api/?key=25974127-a47eb74233b2aa2aa204586ea")
    suspend fun getPhotoBySearchName(@Query("q") search_name : String) : Response<PixabayImage>

}