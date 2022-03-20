package com.homework.picsome.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.homework.picsome.R
import com.homework.picsome.data.model.ImageItem
import com.homework.picsome.databinding.ActivityMainBinding
import com.homework.picsome.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val model : MainViewModel by viewModels()
    @Inject lateinit var glideRequestManager: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model.registerNetworkListener(applicationContext)
        binding.tvDatetime.text = model.getDateAndTime()
        binding.rvImageList.layoutManager = LinearLayoutManager(this)

        model.imageOrStateData.observe(this) { imageOrStateData : Pair<Boolean, List<ImageItem>> ->
            if (imageOrStateData.second.isNotEmpty())
                refreshAdapterWithData(imageOrStateData.second)

            adapterDoOnClick(imageOrStateData.first)
        }
    }

    private fun refreshAdapterWithData(imageData: List<ImageItem>) {
        val state = binding.rvImageList.layoutManager?.onSaveInstanceState()
        binding.rvImageList.adapter =
            ImageListAdapter(
                glideRequestManager,
                imageData
            )
        binding.rvImageList.layoutManager?.onRestoreInstanceState(state)
    }

    private fun adapterDoOnClick(stateIsTrue: Boolean) {
        (binding.rvImageList.adapter as? ImageListAdapter)?.doOnClick {
            if (stateIsTrue) model.refresh()
            else showErrorMessage()
        }
    }

    private fun showErrorMessage() {
        runOnUiThread{
            val builder: AlertDialog.Builder = this.let {
                AlertDialog.Builder(it)
            }
            builder.setMessage(R.string.error_message)
                .setTitle(R.string.error_title)
            builder.apply {
                setPositiveButton(R.string.ok
                ) { dialog, _ ->
                    dialog.dismiss()
                }
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}
