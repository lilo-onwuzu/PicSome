package com.homework.picsome.data

import com.homework.picsome.data.model.ImageItem

interface ImageRepository {
    suspend fun getImages() : List<ImageItem>
}