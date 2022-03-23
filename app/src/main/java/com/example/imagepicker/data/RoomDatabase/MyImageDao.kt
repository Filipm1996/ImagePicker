package com.example.imagepicker.data.RoomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MyImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewImageId(image : ImageDB)

    @Query("DELETE FROM IdTable WHERE imageLink= :link")
    fun deleteByLink (link:String)

    @Query("SELECT * FROM IdTable")
    fun getAllCurrencies(): LiveData<List<ImageDB>>
}