package com.example.powercrew.ui.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.powercrew.R


import com.example.powercrew.databinding.CityCardBinding
import com.example.powercrew.domain.models.CityItem
import com.google.android.material.animation.AnimationUtils

class CitiesListAdapter:RecyclerView.Adapter<CitiesListAdapter.CitiesViewHolder>() {
    lateinit var onCityClick:((CityItem)->Unit)

    private val diffUtil = object: DiffUtil.ItemCallback<CityItem>() {
        override fun areItemsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {
            return (oldItem.cityNameEn==newItem.cityNameEn)
        }

        override fun areContentsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {
            return oldItem == newItem
        }
    }
     val diff = AsyncListDiffer(this,diffUtil)

   inner class CitiesViewHolder( val binding: CityCardBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        return CitiesViewHolder(CityCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.binding.cityNameTv.text = diff.currentList[position].cityNameEn
        holder.binding.root.setBackgroundResource(R.drawable.city_card_background)
        holder.itemView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(holder.itemView.context,R.anim.city_card_anim))
        holder.itemView.setOnClickListener {
            onCityClick.invoke(diff.currentList[position])
        }
    }
}