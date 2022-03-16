package com.homework.picsome.data.db

import androidx.room.*
import com.homework.picsome.data.model.ImageItem

@Dao
interface ImageDao {
    @Query("SELECT * FROM image_table")
    suspend fun getAll(): List<ImageItem>

    @Query("SELECT * FROM image_table LIMIT :batchSize")
    suspend fun getCurrentBatch(batchSize: Int): List<ImageItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(images: List<ImageItem>)

    @Delete
    suspend fun deleteDisplayed(displayed: List<ImageItem>)

    @Query("DELETE FROM image_table")
    suspend fun clear()
}