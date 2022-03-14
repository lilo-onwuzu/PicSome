package com.homework.picsome.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homework.picsome.data.model.ImageItem
import com.homework.picsome.databinding.CellImageViewBinding
import java.text.SimpleDateFormat
import java.util.*

class ImageListAdapter(
    private val imageData: List<ImageItem>,
    private val onItemClick : () -> Unit
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    private lateinit var binding: CellImageViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CellImageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageData[position])
    }

    override fun getItemCount(): Int {
        return imageData.size
    }

    inner class ViewHolder(
        private val binding: CellImageViewBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(image : ImageItem) {
            Glide.with(binding.root.context)
                .load(image.download_url)
                .centerCrop()
                .into(binding.ivImage)
            val date = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            binding.tvImageAuthor.text = image.author
            binding.tvImageDetail.text = String.format("%s | %s pixels by %s pixels | %s", image.id, image.width, image.height, date)
            binding.ivImage.setOnClickListener { onItemClick() }
        }
    }
}