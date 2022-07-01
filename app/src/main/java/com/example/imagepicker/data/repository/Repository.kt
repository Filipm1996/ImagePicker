package com.example.imagepicker.data.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.imagepicker.common.Resource
import com.example.imagepicker.data.Retrofit.Pexels.RetrofitInstanceForPexels
import com.example.imagepicker.data.Retrofit.Pixabay.Hit
import com.example.imagepicker.data.Retrofit.Pixabay.RetrofitInstanceForPixabay
import com.example.imagepicker.data.RoomDatabase.ImageDB
import com.example.imagepicker.data.RoomDatabase.MyImageDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Repository(
    context : Context
) {

    private var db = Room.databaseBuilder(context, MyImageDatabase::class.java, "myImageDb").fallbackToDestructiveMigration().build()

    fun getImageByNameFromPixabay(name : String) : Flow<Resource<List<Hit>>> = flow{

        try{
            emit(Resource.Loading())
            println("here 2")
        val response = RetrofitInstanceForPixabay.api.getPhotoBySearchName(name).body()
            emit(Resource.Success(response!!.hits))
    }catch(exception: Exception){
            emit(Resource.Error(exception.message ?: "error"))
        }

    }

    fun getImageByNameFromPexels(name: String) : Flow<Resource<List<String>>> = flow {
        try {
            emit(Resource.Loading())
            println("here")
            val response = RetrofitInstanceForPexels.api.getListOfPhotosFromPexelsApi(name).body()?.getListOfPhotos()
            emit(Resource.Success(response!!))
        }catch (exception : java.lang.Exception){
            emit(Resource.Error(exception.message ?: "error"))
        }
    }

    suspend fun insertLinkToDb (link:String) = db.myImageDao().insertNewImageId(ImageDB(link))

    fun deleteByLinkFromDb (link: String) = db.myImageDao().deleteByLink(link)

    fun getAllFavImages () = db.myImageDao().getAllCurrencies()
}