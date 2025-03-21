package com.example.blush.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blush.Message
import com.example.blush.User
import com.example.blush.databinding.FragmentChatBinding
import java.util.Date

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var messagesAdapter: MessagesAdapter
    private val args: ChatFragmentArgs by navArgs()

    // Demo data (in a real app this would come from a repository)
    private val currentUserId = "current_user"

    // Demo messages for demonstration
    private val demoMessages = mutableListOf(
        Message(
            id = "msg1",
            matchId = "match1",
            senderId = "user1",
            content = "Hey there! How's your day going?",
            timestamp = Date(System.currentTimeMillis() - 3600000), // 1 hour ago
            isRead = true
        ),
        Message(
            id = "msg2",
            matchId = "match1",
            senderId = "current_user",
            content = "Hi Sarah! It's going well, thanks for asking. How about yours?",
            timestamp = Date(System.currentTimeMillis() - 3500000), // 58 min ago
            isRead = true
        ),
        Message(
            id = "msg3",
            matchId = "match1",
            senderId = "user1",
            content = "Pretty good! Just finished a hike in the mountains.",
            timestamp = Date(System.currentTimeMillis() - 3400000), // 56 min ago
            isRead = true
        ),
        Message(
            id = "msg4",
            matchId = "match2",
            senderId = "user2",
            content = "Hello! I noticed we both like photography. What kind of pictures do you take?",
            timestamp = Date(System.currentTimeMillis() - 7200000), // 2 hours ago
            isRead = true
        ),
        Message(
            id = "msg5",
            matchId = "match3",
            senderId = "user3",
            content = "Hi there! Have you read any good books lately?",
            timestamp = Date(System.currentTimeMillis() - 1800000), // 30 min ago
            isRead = false
        )
    )

    // Demo users for demonstration
    private val demoUsers = listOf(
        User(
            id = "user1",
            name = "Sarah",
            age = 27,
            gender = "Female",
            location = "San Francisco, CA",
            bio = "Hiking enthusiast and coffee lover. Software engineer by day, painter by night.",
            interests = listOf("Hiking", "Art", "Coffee", "Coding"),
            photos = listOf("https://example.com/photo1.jpg"),
            preferences = com.example.blush.UserPreferences(
                interestedIn = listOf("Male"),
                ageRange = 25..35,
                maxDistance = 25,
                lookingFor = listOf("serious")
            )
        ),
        User(
            id = "user2",
            name = "Mike",
            age = 30,
            gender = "Male",
            location = "Los Angeles, CA",
            bio = "Fitness trainer who loves to travel. Always planning my next adventure!",
            interests = listOf("Fitness", "Travel", "Food", "Photography"),
            photos = listOf("https://example.com/photo2.jpg"),
            preferences = com.example.blush.UserPreferences(
                interestedIn = listOf("Female"),
                ageRange = 25..35,
                maxDistance = 50,
                lookingFor = listOf("casual", "serious")
            )
        ),
        User(
            id = "user3",
            name = "Jessica",
            age = 26,
            gender = "Female",
            location = "New York, NY",
            bio = "Book lover and aspiring author. Looking for someone to share quiet evenings and deep conversations.",
            interests = listOf("Books", "Writing", "Museums", "Jazz"),
            photos = listOf("https://example.com/photo3.jpg"),
            preferences = com.example.blush.UserPreferences(
                interestedIn = listOf("Male"),
                ageRange = 25..35,
                maxDistance = 25,
                lookingFor = listOf("serious")
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the match ID from navigation arguments
        val matchId = args.matchId

        // Find the other user in this conversation
        val matchingMessages = demoMessages.filter { it.matchId == matchId }
        val otherUserId = if (matchingMessages.isNotEmpty()) {
            val firstMessage = matchingMessages.first()
            if (firstMessage.senderId == currentUserId) {
                // If first message is from current user, find a message from the other user
                matchingMessages.find { it.senderId != currentUserId }?.senderId ?: "user1"
            } else {
                firstMessage.senderId
            }
        } else {
            // Fallback to a default user
            "user1"
        }

        val otherUser = demoUsers.find { it.id == otherUserId } ?: demoUsers.first()

        // Set up toolbar with user info
        binding.chatUserName.text = otherUser.name
        // In a real app, load user image
        // binding.chatUserImage.load(otherUser.photos.firstOrNull())

        // Set up back button
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Set up info button
        binding.infoButton.setOnClickListener {
            // Navigate to user profile or show user info
            Toast.makeText(requireContext(), "View ${otherUser.name}'s profile", Toast.LENGTH_SHORT).show()
        }

        setupRecyclerView(matchId)
        setupMessageInput(matchId)

        // Mark messages as read
        markMessagesAsRead(matchId)
    }

    private fun setupRecyclerView(matchId: String) {
        messagesAdapter = MessagesAdapter(currentUserId)

        binding.messagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true // Display messages from bottom to top
            }
            adapter = messagesAdapter
        }

        // Load messages for this match
        val messages = demoMessages.filter { it.matchId == matchId }
            .sortedBy { it.timestamp }

        messagesAdapter.submitList(messages)

        // Scroll to the bottom
        if (messages.isNotEmpty()) {
            binding.messagesRecyclerView.post {
                binding.messagesRecyclerView.smoothScrollToPosition(messages.size - 1)
            }
        }
    }

    private fun setupMessageInput(matchId: String) {
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageInput.text.toString().trim()

            if (messageText.isNotEmpty()) {
                // Create and send a new message
                val newMessage = Message(
                    matchId = matchId,
                    senderId = currentUserId,
                    content = messageText,
                    timestamp = Date(),
                    isRead = true
                )

                // Add message to the demo list
                demoMessages.add(newMessage)

                // Update the UI
                val updatedMessages = demoMessages
                    .filter { it.matchId == matchId }
                    .sortedBy { it.timestamp }

                messagesAdapter.submitList(updatedMessages)

                // Scroll to the bottom
                binding.messagesRecyclerView.post {
                    binding.messagesRecyclerView.smoothScrollToPosition(updatedMessages.size - 1)
                }

                // Clear the input field
                binding.messageInput.text.clear()
            }
        }
    }

    private fun markMessagesAsRead(matchId: String) {
        // In a real app, update messages in the repository
        demoMessages.forEach { message ->
            if (message.matchId == matchId && message.senderId != currentUserId && !message.isRead) {
                // Update message
                val index = demoMessages.indexOf(message)
                if (index != -1) {
                    demoMessages[index] = message.copy(isRead = true)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}