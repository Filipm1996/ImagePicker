package com.example.imagepicker.data.Retrofit.Pixabay

data class PixabayImage(
    val total: Int,
    val totalHits: Int,
    val hits: List<Hit>
) {
    fun get_hits (): List<Hit> {
        return hits
    }
}