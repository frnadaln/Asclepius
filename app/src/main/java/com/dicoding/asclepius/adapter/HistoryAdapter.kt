package com.dicoding.asclepius.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.HistoryEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val onDeleteClicked: (HistoryEntity) -> Unit
) : ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(history: HistoryEntity) {
            val imageUri = Uri.parse(history.image)
            Log.d("HistoryAdapter", "Image URI: $imageUri")

            Glide.with(binding.imageView.context)
                .load(imageUri)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(binding.imageView)

            binding.resultText.text = "Result: ${history.result}"
            binding.confidenceScoreText.text = "Confidence Score: ${history.confidenceScore}%"

            binding.deleteButton.setOnClickListener {
                onDeleteClicked(history)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<HistoryEntity>() {
        override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}
