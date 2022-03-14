package com.homework.picsome.viewModel

import androidx.lifecycle.*
import com.homework.picsome.data.ImageRepository
import com.homework.picsome.data.model.ImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.roundToInt

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository: ImageRepository
): ViewModel() {

    private val _imageData = MutableLiveData<List<ImageItem>>()
    val imageData : LiveData<List<ImageItem>>
        get() = _imageData

    init {
        getImages()
    }

    fun getImages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _imageData.postValue(getRandomImages(repository.getImages()))
            }
        }
    }

    private fun getRandomImages(fullList: List<ImageItem>): List<ImageItem> {
        val newList = mutableListOf<ImageItem>()
        for (i in 0..2) {
            val index = (floor(Math.random()*fullList.size)).roundToInt()
            newList.add(fullList[index])
        }
        return newList
    }

    fun getDateAndTime()
            = SimpleDateFormat("MM-dd-yy hh:mm a", Locale.getDefault()).format(Date()) ?: ""
}