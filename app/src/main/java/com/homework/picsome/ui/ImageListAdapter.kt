package com.homework.picsome.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.homework.picsome.data.model.ImageItem
import com.homework.picsome.databinding.CellImageViewBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ImageListAdapter(
    private val context: Context,
    private val imageData: List<ImageItem>,
    private val onItemClick : () -> Unit
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    private lateinit var binding: CellImageViewBinding
    private val requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
    private val glide : RequestManager
        get() = Glide.with(context).apply { requestOptions }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CellImageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindImageDetails(imageData[position])
    }

    override fun getItemCount(): Int {
        return imageData.size
    }

    inner class ViewHolder(
        private val binding: CellImageViewBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            preloadImages()
        }

        private fun preloadImages() {
            for (image in imageData) {
                glide.load(image.download_url).preload()
            }
        }

        fun bindImageDetails(image : ImageItem) {
            try {
                glide
                    .load(image.download_url)
                    .centerCrop()
                    .into(binding.ivImage)
            } catch (e: Exception) {
                System.out.println("Error downloading image")
            }
            val date = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            binding.tvImageAuthor.text = image.author
            binding.tvImageDetail.text = String.format("%s | %s pixels by %s pixels | %s", image.id, image.width, image.height, date)
            binding.ivImage.setOnClickListener { onItemClick() }
        }
    }
}