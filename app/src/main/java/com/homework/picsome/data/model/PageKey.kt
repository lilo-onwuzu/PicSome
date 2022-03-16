package com.homework.picsome.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_table")
data class PageKey (
    @PrimaryKey val id: Int = 1,
)