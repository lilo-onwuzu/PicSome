package com.homework.picsome.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.homework.picsome.databinding.ActivityMainBinding
import com.homework.picsome.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val model : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model.registerNetworkListener(
            applicationContext,
            onAvailable = { showList() },
            onLost = { showErrorMessage() }
        )
        binding.tvDatetime.text = model.getDateAndTime()
        binding.rvImageList.layoutManager = LinearLayoutManager(this)

        model.imageData.observe(this) { imageData ->
            if (imageData.isNotEmpty()) {
                val state = binding.rvImageList.layoutManager?.onSaveInstanceState()
                binding.rvImageList.adapter =
                    ImageListAdapter(
                        applicationContext,
                        imageData,
                        onItemClick = { model.refresh() })
                binding.rvImageList.layoutManager?.onRestoreInstanceState(state)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        model.imageData.value?.let {
            if (it.isEmpty()) model.refresh()
        }
    }

    private fun showList() {
        runOnUiThread {
            binding.tvErrorMessage.visibility = View.GONE
            binding.llImageList.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage() {
        runOnUiThread{
            binding.llImageList.visibility = View.GONE
            binding.tvErrorMessage.visibility = View.VISIBLE
        }
    }
}
