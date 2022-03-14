package com.homework.picsome.data

import com.homework.picsome.data.db.ImageDao
import com.homework.picsome.data.model.ImageItem
import com.homework.picsome.data.network.ImageService
import javax.inject.Inject


class DefaultImageRepository @Inject constructor(
    private val imageDao: ImageDao,
    private val imageService: ImageService
) : ImageRepository {

    override suspend fun getImages(): List<ImageItem> {
        imageDao.insert(fetchImagesFromService())
        return imageDao.getAll()
    }

    private fun fetchImagesFromService(): List<ImageItem> {
        imageService.fetchImages().execute().body() ?.let {
            return it
        }
        return listOf()
    }
}