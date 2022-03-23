package com.example.imagepicker.data.RoomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "IdTable")
data class ImageDB (
    val imageLink :String,
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null
        )