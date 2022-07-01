package com.example.imagepicker.UI

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagepicker.common.Resource
import com.example.imagepicker.data.Retrofit.Pixabay.Hit
import com.example.imagepicker.data.repository.Repository
import com.example.imagepicker.getBitmapFromUrl
import com.example.imagepicker.savePhotoToExternalStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*


class ViewModel(
    private val repository: Repository
) : ViewModel() {
    private val mutableListOfLinks = mutableListOf<String>()
    private val listOfLinksLiveData = MutableLiveData<MutableList<String>>()
    private val loading = MutableLiveData<Boolean>()
    val errorCollector = MutableLiveData<String> ()
    fun getImageByNameFromPixabay(name: String) =  deleteRepeatedItems(name)

    fun getFavImages() = repository.getAllFavImages()

    fun deleteByLinkFromDb (link: String) = CoroutineScope(Dispatchers.IO).launch { repository.deleteByLinkFromDb(link)}

    private fun deleteRepeatedItems(name: String){
        repository.getImageByNameFromPixabay(name).onEach{result ->
            when(result){
                is Resource.Success ->{
                    for (i in result.data!!){
                        if (!mutableListOfLinks.contains(i.webformatURL)){
                            mutableListOfLinks.add(i.webformatURL)
                        }
                    }
                    listOfLinksLiveData.postValue(mutableListOfLinks)
                    loading.postValue(false)
                }
                is Resource.Loading -> {
                    loading.postValue(true)
                }
                is Resource.Error -> {
                    loading.postValue(false)
                    errorCollector.postValue(result.message ?: "Error")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getImageByNameFromPexels(name : String){
        repository.getImageByNameFromPexels(name).onEach {result ->
            when(result){
                is Resource.Success ->{
                    result.data!!.forEach{
                        mutableListOfLinks.add(it)
                    }
                    listOfLinksLiveData.postValue(mutableListOfLinks)
                    loading.postValue(false)
                }
                is Resource.Loading -> {
                    loading.postValue(true)
                }
                is Resource.Error -> {
                    loading.postValue(false)
                    errorCollector.postValue(result.message ?: "Error")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getLinks() : LiveData<MutableList<String>> {
        return listOfLinksLiveData
    }

    fun getErrors()  : MutableLiveData<String>{
        return errorCollector
    }

    fun getLoadingLiveData() : MutableLiveData<Boolean>{
        return loading
    }

    fun clearErrorCollector (){
        errorCollector.postValue(null)
    }

    fun insertLinkToDb (link : String) = viewModelScope.launch {
        repository.insertLinkToDb(link)
    }
}