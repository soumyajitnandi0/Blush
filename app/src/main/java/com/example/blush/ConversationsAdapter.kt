package com.example.blush.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blush.R
import com.example.blush.databinding.ItemConversationBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ConversationsAdapter(private val onConversationClick: (String) -> Unit) :
    ListAdapter<ConversationData, ConversationsAdapter.ConversationViewHolder>(ConversationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val binding = ItemConversationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ConversationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ConversationViewHolder(private val binding: ItemConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onConversationClick(getItem(position).match.id)
                }
            }
        }

        fun bind(conversationData: ConversationData) {
            val (match, user, lastMessage) = conversationData

            binding.conversationName.text = user.name

            // Set last message content or default text if no messages
            binding.conversationLastMessage.text = lastMessage?.content ?: "Say hi to start a conversation!"

            // Format time as "X time ago" or date
            val messageDate = lastMessage?.timestamp ?: match.lastActivity
            binding.conversationTime.text = formatMessageTime(messageDate)

            // Set unread indicator
            val isUnread = lastMessage != null && !lastMessage.isRead && lastMessage.senderId != "current_user"
            val textColor = if (isUnread)
                ContextCompat.getColor(binding.root.context, android.R.color.black)
            else
                ContextCompat.getColor(binding.root.context, android.R.color.darker_gray)

            binding.conversationLastMessage.setTextColor(textColor)

            if (isUnread) {
                binding.conversationUnreadIndicator.visibility = ViewGroup.VISIBLE
            } else {
                binding.conversationUnreadIndicator.visibility = ViewGroup.GONE
            }

            // In a real app, load image with Glide or Coil
            // binding.conversationImage.load(user.photos.firstOrNull())
        }

        private fun formatMessageTime(date: Date): String {
            val now = Calendar.getInstance()
            val messageTime = Calendar.getInstance().apply { time = date }

            return when {
                // Today
                now.get(Calendar.DATE) == messageTime.get(Calendar.DATE) &&
                        now.get(Calendar.MONTH) == messageTime.get(Calendar.MONTH) &&
                        now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR) -> {
                    SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
                }
                // Yesterday
                now.get(Calendar.DATE) - messageTime.get(Calendar.DATE) == 1 &&
                        now.get(Calendar.MONTH) == messageTime.get(Calendar.MONTH) &&
                        now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR) -> {
                    "Yesterday"
                }
                // Within a week
                now.get(Calendar.DATE) - messageTime.get(Calendar.DATE) < 7 &&
                        now.get(Calendar.MONTH) == messageTime.get(Calendar.MONTH) &&
                        now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR) -> {
                    SimpleDateFormat("EEE", Locale.getDefault()).format(date)
                }
                // Older
                else -> {
                    SimpleDateFormat("MMM d", Locale.getDefault()).format(date)
                }
            }
        }
    }

    class ConversationDiffCallback : DiffUtil.ItemCallback<ConversationData>() {
        override fun areItemsTheSame(oldItem: ConversationData, newItem: ConversationData): Boolean {
            return oldItem.match.id == newItem.match.id
        }

        override fun areContentsTheSame(oldItem: ConversationData, newItem: ConversationData): Boolean {
            return oldItem == newItem
        }
    }
}