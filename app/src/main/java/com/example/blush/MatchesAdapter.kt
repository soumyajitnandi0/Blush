package com.example.blush.ui.matches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blush.databinding.ItemMatchBinding
import java.text.SimpleDateFormat
import java.util.Locale

class MatchesAdapter(private val onMatchClick: (String) -> Unit) :
    ListAdapter<MatchWithProfile, MatchesAdapter.MatchViewHolder>(MatchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val binding = ItemMatchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MatchViewHolder(private val binding: ItemMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMatchClick(getItem(position).match.id)
                }
            }
        }

        fun bind(matchWithProfile: MatchWithProfile) {
            val (match, user) = matchWithProfile

            binding.matchName.text = user.name
            binding.matchAge.text = "${user.age}"

            // Format time as "X time ago"
            val timeFormatter = SimpleDateFormat("MMM dd", Locale.getDefault())
            binding.matchDate.text = timeFormatter.format(match.createdAt)

            // In a real app, load image with Glide or Coil
            // binding.matchImage.load(user.photos.firstOrNull())
        }
    }

    class MatchDiffCallback : DiffUtil.ItemCallback<MatchWithProfile>() {
        override fun areItemsTheSame(oldItem: MatchWithProfile, newItem: MatchWithProfile): Boolean {
            return oldItem.match.id == newItem.match.id
        }

        override fun areContentsTheSame(oldItem: MatchWithProfile, newItem: MatchWithProfile): Boolean {
            return oldItem == newItem
        }
    }
}