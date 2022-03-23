package com.example.imagepicker.data.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.imagepicker.data.Retrofit.Hit
import com.example.imagepicker.data.Retrofit.RetrofitInstance
import com.example.imagepicker.data.RoomDatabase.ImageDB
import com.example.imagepicker.data.RoomDatabase.MyImageDatabase


class Repository(
    context : Context
) {

    private var db = Room.databaseBuilder(context, MyImageDatabase::class.java, "myImageDb").fallbackToDestructiveMigration().build()
    suspend fun getImageByName(name : String) : List<Hit>{
        val response = RetrofitInstance.api.getPhotoBySearchName(name).body()
        Log.e("Response", response?.get_hits().toString())
        return response!!.get_hits()
    }

    suspend fun insertLinkToDb (link:String) = db.myImageDao().insertNewImageId(ImageDB(link))

    fun deleteByLinkFromDb (link: String) = db.myImageDao().deleteByLink(link)

    fun getAllFavImages () = db.myImageDao().getAllCurrencies()
}