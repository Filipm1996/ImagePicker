package com.example.imagepicker.data.RoomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
entities = [ImageDB::class],
    version = 2
)
abstract class MyImageDatabase : RoomDatabase() {
    abstract fun myImageDao() : MyImageDao
}