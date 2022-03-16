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

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository: ImageRepository
): ViewModel() {

    private val _imageData = MutableLiveData<List<ImageItem>>()
    val imageData : LiveData<List<ImageItem>>
        get() = _imageData

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                refresh()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val current = repository.getCurrentBatch()
                _imageData.postValue(current)
                repository.removeDisplayedFromDb(current)
            }
        }
    }

    fun getDateAndTime()
            = SimpleDateFormat("MM-dd-yy hh:mm a", Locale.getDefault()).format(Date()) ?: ""
}