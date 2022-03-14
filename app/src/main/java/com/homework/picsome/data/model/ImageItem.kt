package com.homework.picsome.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class ImageItem (
    @PrimaryKey val id : String = "",
    val author : String?,
    val width: Int?,
    val height: Int?,
    val url: String?,
    val download_url: String?
)