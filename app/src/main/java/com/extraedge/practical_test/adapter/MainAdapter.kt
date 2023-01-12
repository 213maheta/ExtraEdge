package com.extraedge.practical_test.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.extraedge.practical_test.databinding.ItemRocketBinding
import com.extraedge.practical_test.iinterface.GenericClickListener
import com.extraedge.practical_test.room.RocketModel

class MainAdapter(val listener:GenericClickListener): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    val rocketlist = arrayListOf<RocketModel>()

    class ViewHolder(val binding: ItemRocketBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRocketBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rocketModel = rocketlist.get(position)
        holder.binding.rocket = rocketModel
        Glide.with(holder.binding.flickerImage).load(rocketModel.flicker_image?.imagelist?.get(0)).into(holder.binding.flickerImage)
        holder.binding.root.setOnClickListener {
            listener.onClick(rocketModel.id)
        }
    }

    override fun getItemCount(): Int {
        return rocketlist.size
    }
}