package com.homework.picsome.viewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
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

    fun registerNetworkListener(context: Context, onAvailable: () -> Unit, onLost: () -> Unit ) {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                onAvailable()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                onLost()
            }
        }

        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        if (!isNetworkAvailable(connectivityManager)) onLost()
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    private fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        )
        return capabilities != null
                    && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    fun getDateAndTime()
            = SimpleDateFormat("MM-dd-yy hh:mm a", Locale.getDefault()).format(Date()) ?: ""
}