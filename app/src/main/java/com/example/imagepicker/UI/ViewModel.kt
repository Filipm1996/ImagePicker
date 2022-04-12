package com.example.imagepicker.UI

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.imagepicker.data.Retrofit.Pixabay.Hit
import com.example.imagepicker.data.repository.Repository
import com.example.imagepicker.getBitmapFromUrl
import com.example.imagepicker.savePhotoToExternalStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


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

    fun setAddPhotoDialog (link : String , actContext : Context) {
        val builder = AlertDialog.Builder(actContext )
        builder.setMessage("Do you want to add photo?")
        builder.setCancelable(true)
        builder.setPositiveButton("yes"){dialog,_ ->
            CoroutineScope(Dispatchers.IO).launch {
                repository.insertLinkToDb(link)
            }
            Toast.makeText(actContext ,"Added photo", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No"){dialog,_->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }




}