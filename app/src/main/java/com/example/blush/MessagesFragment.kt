package com.example.blush.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blush.Match
import com.example.blush.Message
import com.example.blush.User
import com.example.blush.UserPreferences
import com.example.blush.databinding.FragmentMessagesBinding
import com.example.blush.ui.matches.MatchWithProfile
import java.util.Date

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private lateinit var conversationsAdapter: ConversationsAdapter

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
            preferences = UserPreferences(
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
            preferences = UserPreferences(
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
            preferences = UserPreferences(
                interestedIn = listOf("Male"),
                ageRange = 25..35,
                maxDistance = 25,
                lookingFor = listOf("serious")
            )
        )
    )

    // Demo matches for demonstration
    private val demoMatches = listOf(
        Match(
            id = "match1",
            users = Pair("current_user", "user1"),
            createdAt = Date(System.currentTimeMillis() - 86400000), // 1 day ago
            lastActivity = Date(System.currentTimeMillis() - 3600000) // 1 hour ago
        ),
        Match(
            id = "match2",
            users = Pair("current_user", "user2"),
            createdAt = Date(System.currentTimeMillis() - 172800000), // 2 days ago
            lastActivity = Date(System.currentTimeMillis() - 7200000) // 2 hours ago
        ),
        Match(
            id = "match3",
            users = Pair("current_user", "user3"),
            createdAt = Date(System.currentTimeMillis() - 3600000), // 1 hour ago
            lastActivity = Date(System.currentTimeMillis() - 1800000) // 30 min ago
        )
    )

    // Demo messages for demonstration
    private val demoMessages = listOf(
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadConversations()
    }

    private fun setupRecyclerView() {
        conversationsAdapter = ConversationsAdapter { matchId ->
            // Navigate to chat with this match
            val action = MessagesFragmentDirections.actionMessagesFragmentToChatFragment(matchId)
            findNavController().navigate(action)
        }

        binding.conversationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = conversationsAdapter
        }
    }

    private fun loadConversations() {
        // In a real app, fetch conversations from repository
        val conversationsData = demoMatches.map { match ->
            val otherUserId = if (match.users.first == "current_user") match.users.second else match.users.first
            val otherUser = demoUsers.find { it.id == otherUserId }
            val lastMessage = demoMessages.filter { it.matchId == match.id }.maxByOrNull { it.timestamp }

            ConversationData(
                match = match,
                otherUser = otherUser!!,
                lastMessage = lastMessage
            )
        }.sortedByDescending { it.lastMessage?.timestamp ?: it.match.lastActivity }

        if (conversationsData.isEmpty()) {
            binding.noMessagesText.visibility = View.VISIBLE
            binding.conversationsRecyclerView.visibility = View.GONE
        } else {
            binding.noMessagesText.visibility = View.GONE
            binding.conversationsRecyclerView.visibility = View.VISIBLE
            conversationsAdapter.submitList(conversationsData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class ConversationData(
    val match: Match,
    val otherUser: User,
    val lastMessage: Message?
)