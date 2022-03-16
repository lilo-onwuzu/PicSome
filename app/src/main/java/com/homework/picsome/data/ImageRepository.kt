package com.homework.picsome.data

import com.homework.picsome.data.model.ImageItem

interface ImageRepository {
    suspend fun getCurrentBatch(): List<ImageItem>
    suspend fun removeDisplayedFromDb(displayed: List<ImageItem>)
}