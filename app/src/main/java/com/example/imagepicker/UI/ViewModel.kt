package com.example.imagepicker.UI

import androidx.lifecycle.ViewModel
import com.example.imagepicker.data.Retrofit.Pixabay.Hit
import com.example.imagepicker.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class ViewModel(
    private val repository: Repository
) : ViewModel() {

    suspend fun getImageByNameFromPixabay(name: String) =  deleteRepeatedItems(repository.getImageByNameFromPixabay(name))

    suspend fun getImageByNameFromPexels(name : String) =   repository.getImageByNameFromPexels(name)

    fun getFavImages() = repository.getAllFavImages()

    fun deleteByLinkFromDb (link: String) = CoroutineScope(Dispatchers.IO).launch { repository.deleteByLinkFromDb(link)}

    private fun deleteRepeatedItems(listOfPhotos: List<Hit>) : List<String> {
        val listOfNewPhotos = mutableListOf<String>()
        for (i in listOfPhotos){
            if (!listOfNewPhotos.contains(i.webformatURL)){
                listOfNewPhotos.add(i.webformatURL)
            }
        }
        return listOfNewPhotos
    }

}