package com.homework.picsome.data.db

import androidx.room.*
import com.homework.picsome.data.model.PageKey

@Dao
interface PageKeyDao {
    @Query("SELECT * FROM page_table")
    suspend fun getAll() : List<PageKey>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun useKey(key : PageKey)

    @Query("DELETE FROM page_table")
    suspend fun clear()
}