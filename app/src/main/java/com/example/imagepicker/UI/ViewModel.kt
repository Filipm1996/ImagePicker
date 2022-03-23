package com.example.imagepicker.UI

import androidx.lifecycle.ViewModel
import com.example.imagepicker.data.repository.Repository

class ViewModel(
    private val repository: Repository
) : ViewModel() {

    suspend fun getImageByName(name: String) = repository.getImageByName(name)

    fun getFavImages() = repository.getAllFavImages()

    fun deleteByLinkFromDb (link: String) = repository.deleteByLinkFromDb(link)
}