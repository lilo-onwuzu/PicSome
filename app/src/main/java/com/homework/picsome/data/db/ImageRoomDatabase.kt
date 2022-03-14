package com.homework.picsome.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.homework.picsome.data.model.ImageItem

@Database(
    entities = [ImageItem::class],
    version = 1,
    exportSchema = false
)
abstract class ImageRoomDatabase : RoomDatabase() {

    abstract fun imageDao() : ImageDao

    companion object {
        @Volatile
        private var INSTANCE : ImageRoomDatabase? = null

        fun getDatabase(context: Context) : ImageRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageRoomDatabase::class.java,
                    "image_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}