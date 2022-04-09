package com.example.imagepicker.data.Retrofit.Pexels

data class PexelsResponse(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int
){
    fun getListOfPhotos (): List<String> {
        val mutableList = mutableListOf<String>()
        for (i in photos){
            mutableList.add(i.src.medium)
        }
        return mutableList
    }
}