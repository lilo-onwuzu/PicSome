package com.homework.picsome.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        binding.tvDatetime.text = model.getDateAndTime()
        binding.rvImageList.layoutManager = LinearLayoutManager(this)

        model.imageData.observe(this) { imageData ->
            val state = binding.rvImageList.layoutManager?.onSaveInstanceState()
            binding.rvImageList.adapter =
                ImageListAdapter(imageData, onItemClick = { model.refresh() })
            binding.rvImageList.layoutManager?.onRestoreInstanceState(state)
        }
    }
}
