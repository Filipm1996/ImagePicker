package com.example.imagepicker.data.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.imagepicker.data.Retrofit.Pexels.RetrofitInstanceForPexels
import com.example.imagepicker.data.Retrofit.Pixabay.Hit
import com.example.imagepicker.data.Retrofit.Pixabay.RetrofitInstanceForPixabay
import com.example.imagepicker.data.RoomDatabase.ImageDB
import com.example.imagepicker.data.RoomDatabase.MyImageDatabase


class Repository(
    context : Context
) {

    private var db = Room.databaseBuilder(context, MyImageDatabase::class.java, "myImageDb").fallbackToDestructiveMigration().build()

    suspend fun getImageByNameFromPixabay(name : String) : List<Hit>{
        val response = RetrofitInstanceForPixabay.api.getPhotoBySearchName(name).body()
        Log.e("Response from Pixabay", response?.get_hits().toString())
        return response!!.get_hits()
    }

    suspend fun getImageByNameFromPexels(name: String) : List<String> {
        val response = RetrofitInstanceForPexels.api.getListOfPhotosFromPexelsApi(name).body()
        Log.e("Response from Pexels", response?.photos.toString())
        return response!!.getListOfPhotos()
    }

    suspend fun insertLinkToDb (link:String) = db.myImageDao().insertNewImageId(ImageDB(link))

    fun deleteByLinkFromDb (link: String) = db.myImageDao().deleteByLink(link)

    fun getAllFavImages () = db.myImageDao().getAllCurrencies()
}