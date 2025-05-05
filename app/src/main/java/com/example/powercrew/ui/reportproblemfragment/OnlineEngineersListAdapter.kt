package com.example.powercrew.ui.reportproblemfragment

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.powercrew.R


import com.example.powercrew.databinding.SelectEngineerCardBinding

import com.example.powercrew.domain.models.Engineer


class OnlineEngineersListAdapter : RecyclerView.Adapter<OnlineEngineersListAdapter.EngineersViewHolder>() {

    lateinit var onCardClick: ((String,String) -> Unit)

    private val diffUtil = object : DiffUtil.ItemCallback<Engineer>() {
        override fun areItemsTheSame(oldItem: Engineer, newItem: Engineer): Boolean {
            return oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(oldItem: Engineer, newItem: Engineer): Boolean {
            return oldItem == newItem
        }
    }

    val diff = AsyncListDiffer(this, diffUtil)

    private  var selectedPosition = RecyclerView.NO_POSITION
    inner class EngineersViewHolder(val binding: SelectEngineerCardBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngineersViewHolder {
        return EngineersViewHolder(
            SelectEngineerCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = diff.currentList.size

    override fun onBindViewHolder(holder: EngineersViewHolder, position: Int) {
        val engineer = diff.currentList[position]

        holder.binding.nameTv.text = engineer.fullName
        holder.binding.cityTv.text = engineer.cityItem!!.cityNameEn
        holder.binding.stateTv.text = if (engineer.state) "online" else "offline"
        holder.binding.stateTv.setTextColor(if (engineer.state) Color.GREEN else Color.RED)

        holder.binding.checkbox.isChecked = holder.bindingAdapterPosition  == selectedPosition

        holder.binding.root.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION && selectedPosition != currentPosition) {
                val previousPosition = selectedPosition
                selectedPosition = currentPosition
                val id = diff.currentList[currentPosition].userId
                val token = diff.currentList[currentPosition].token
                onCardClick.invoke(id, token!!)

                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
            }
        }
    }
    fun clearListSelector(){
         selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()

    }
}

