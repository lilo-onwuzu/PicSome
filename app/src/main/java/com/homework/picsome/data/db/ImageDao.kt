package com.homework.picsome.data.db

import androidx.room.*
import com.homework.picsome.data.model.ImageItem

@Dao
interface ImageDao {
    @Query ("SELECT * FROM image_table")
    suspend fun getAll(): List<ImageItem>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(images: List<ImageItem>)

    @Query ("DELETE FROM image_table")
    suspend fun deleteAll()
}