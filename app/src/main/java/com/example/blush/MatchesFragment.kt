package com.example.blush.ui.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blush.Match
import com.example.blush.R
import com.example.blush.User
import com.example.blush.databinding.FragmentMatchesBinding
import java.util.Date

class MatchesFragment : Fragment() {

    private var _binding: FragmentMatchesBinding? = null
    private val binding get() = _binding!!

    private lateinit var matchesAdapter: MatchesAdapter

    // Demo matches for demonstration
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadMatches()
    }

    private fun setupRecyclerView() {
        matchesAdapter = MatchesAdapter { matchId ->
            // Navigate to chat with this match
            val action = MatchesFragmentDirections.actionMatchesFragmentToChatFragment(matchId)
            findNavController().navigate(action)
        }

        binding.matchesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = matchesAdapter
        }
    }

    private fun loadMatches() {
        // In a real app, fetch matches from repository
        val matchesWithProfiles = demoMatches.map { match ->
            val otherUserId = if (match.users.first == "current_user") match.users.second else match.users.first
            val otherUser = demoUsers.find { it.id == otherUserId }
            MatchWithProfile(match, otherUser!!)
        }

        if (matchesWithProfiles.isEmpty()) {
            binding.noMatchesText.visibility = View.VISIBLE
            binding.matchesRecyclerView.visibility = View.GONE
        } else {
            binding.noMatchesText.visibility = View.GONE
            binding.matchesRecyclerView.visibility = View.VISIBLE
            matchesAdapter.submitList(matchesWithProfiles)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class MatchWithProfile(
    val match: Match,
    val otherUser: User
)