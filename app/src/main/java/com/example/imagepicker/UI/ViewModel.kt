package com.example.imagepicker.UI

import androidx.lifecycle.ViewModel
import com.example.imagepicker.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewModel(
    private val repository: Repository
) : ViewModel() {

    suspend fun getImageByName(name: String) = repository.getImageByName(name)

    fun getFavImages() = repository.getAllFavImages()

    fun deleteByLinkFromDb (link: String) = CoroutineScope(Dispatchers.IO).launch { repository.deleteByLinkFromDb(link)}
}