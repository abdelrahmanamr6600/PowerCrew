package com.example.powercrew.ui.pendingproblems

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.powercrew.R
import com.example.powercrew.databinding.ProblemCardBinding
import com.example.powercrew.domain.models.Problem

class ProblemsAdapter:RecyclerView.Adapter<ProblemsAdapter.ProblemViewHolder>() {
    lateinit var onProblemCardClick:((Problem)->Unit)

    private val diffUtil = object: DiffUtil.ItemCallback<Problem>() {
        override fun areItemsTheSame(oldItem: Problem, newItem: Problem): Boolean {
            return (oldItem.title==newItem.title)
        }

        override fun areContentsTheSame(oldItem: Problem, newItem: Problem): Boolean {
            return oldItem == newItem
        }
    }
    val diff = AsyncListDiffer(this,diffUtil)

    inner class ProblemViewHolder( val binding: ProblemCardBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemViewHolder {
        return ProblemViewHolder(ProblemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: ProblemViewHolder, position: Int) {
        holder.binding.titleTv.text = diff.currentList[position].title
        holder.binding.dateTv.text = diff.currentList[position].createdAt
        holder.binding.addressTv.text = diff.currentList[position].address
        holder.binding.root.setBackgroundResource(R.drawable.city_card_background)
        holder.binding.root.setOnClickListener {
            onProblemCardClick.invoke(diff.currentList[position])

        }
    }
}