package com.homework.picsome.data

import com.homework.picsome.data.db.ImageDao
import com.homework.picsome.data.db.PageKeyDao
import com.homework.picsome.data.model.ImageItem
import com.homework.picsome.data.model.PageKey
import com.homework.picsome.data.network.ImageService
import java.lang.Exception
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.roundToInt

const val PAGE_LIMIT = 993
const val SET_SIZE = 3

class DefaultImageRepository @Inject constructor(
    private val imageDao: ImageDao,
    private val pageKeyDao: PageKeyDao,
    private val imageService: ImageService
) : ImageRepository {

    override suspend fun getNextSet(): List<ImageItem> {
        val nextSet = fetchNextSet()
        val currentSet = imageDao.getCurrentSet(SET_SIZE)
        if (nextSet.isNotEmpty()) {
            imageDao.deleteDisplayed(currentSet)
            imageDao.insert(nextSet)
        }
        return imageDao.getCurrentSet(SET_SIZE)
    }

    private suspend fun fetchNextSet(): List<ImageItem> {
        val pageList = getUsedPageKeys()
        val list = mutableListOf<ImageItem>()
        if (pageList.size >= PAGE_LIMIT) return emptyList()
        for (i in 1..SET_SIZE) {
            var page = generateRandomKey()
            while (pageList.contains(page))
                page = generateRandomKey()
            pageKeyDao.useKey(PageKey(page))
            list.addAll(fetchImagesFromService(page))
        }
        return list
    }

    private fun fetchImagesFromService(page: Int): List<ImageItem> {
        try {
            imageService.fetchImages(page).execute().body()?.let {
                return it
            }
        } catch (e: Exception) {
            System.out.println("Error making network request " + e.localizedMessage)
        }
        return listOf()
    }

    private suspend fun getUsedPageKeys() : List<Int>
            = pageKeyDao.getAll().map { it.id }

    private fun generateRandomKey() : Int
            = (floor(Math.random()* PAGE_LIMIT)).roundToInt()
}