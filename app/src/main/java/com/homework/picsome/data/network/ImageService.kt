package com.homework.picsome.data.network

import com.homework.picsome.data.model.ImageItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {
    @GET("v2/list")
    fun fetchImages(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 100
    ) : Call<List<ImageItem>>
}