package com.example.powercrew.ui.engineers

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.powercrew.R
import com.example.powercrew.R.color


import com.example.powercrew.databinding.CityCardBinding
import com.example.powercrew.databinding.EngineerCardBinding
import com.example.powercrew.domain.models.CityItem
import com.example.powercrew.domain.models.Engineer
import com.google.android.material.animation.AnimationUtils

class EngineersListAdapter:RecyclerView.Adapter<EngineersListAdapter.EngineersViewHolder>() {
    lateinit var onCallClick:((String)->Unit)

    private val diffUtil = object: DiffUtil.ItemCallback<Engineer>() {
        override fun areItemsTheSame(oldItem: Engineer, newItem: Engineer): Boolean {
            return (oldItem.fullName==newItem.fullName)
        }

        override fun areContentsTheSame(oldItem: Engineer, newItem: Engineer): Boolean {
            return oldItem == newItem
        }
    }
     val diff = AsyncListDiffer(this,diffUtil)

   inner class EngineersViewHolder( val binding: EngineerCardBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngineersViewHolder {
        return EngineersViewHolder(EngineerCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: EngineersViewHolder, position: Int) {
        holder.binding.nameTv.text = diff.currentList[position].fullName
        holder.binding.cityTv.text = diff.currentList[position].cityItem!!.cityNameEn
        if (diff.currentList[position].state!!){
            holder.binding.stateTv.text = "online"
        } else{
            holder.binding.stateTv.text = "offline"
            holder.binding.stateTv.setTextColor(Color.RED)
        }
        holder.binding.root.setBackgroundResource(R.drawable.city_card_background)
        holder.binding.callIconIv.setOnClickListener {
            onCallClick.invoke(diff.currentList[position].phone)
        }
    }
}