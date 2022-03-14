package com.homework.picsome.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.homework.picsome.BuildConfig
import com.homework.picsome.data.DefaultImageRepository
import com.homework.picsome.data.ImageRepository
import com.homework.picsome.data.db.ImageDao
import com.homework.picsome.data.db.ImageRoomDatabase
import com.homework.picsome.data.network.ImageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://picsum.photos/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Singleton
    @Provides
    fun providesRetrofit() : Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) httpClient.addInterceptor(interceptor)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(httpClient.build())
            .build()
    }

    @Singleton
    @Provides
    fun providesImageService(
        retrofit: Retrofit
    ): ImageService = retrofit.create(ImageService::class.java)

    @Singleton
    @Provides
    fun providesImageDatabase(
        @ApplicationContext context : Context
    ) : ImageRoomDatabase = ImageRoomDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun providesImageDao(
        imageRoomDatabase: ImageRoomDatabase
    ) : ImageDao = imageRoomDatabase.imageDao()

    @Provides
    fun providesImageRepository(
        imageDao: ImageDao,
        imageService: ImageService
    ): ImageRepository = DefaultImageRepository(imageDao, imageService)
}