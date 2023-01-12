package com.extraedge.practical_test.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.extraedge.practical_test.databinding.ItemImageBinding

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    val imagelist = mutableListOf<String>()

    class ViewHolder(val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context))
        binding.root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.image).load(imagelist.get(position)).into(holder.binding.image)
    }

    override fun getItemCount(): Int {
        return imagelist.size
    }
}