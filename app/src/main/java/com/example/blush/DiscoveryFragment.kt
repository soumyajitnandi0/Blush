package com.example.blush.ui.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blush.User
import com.example.blush.UserPreferences
import com.example.blush.databinding.FragmentDiscoveryBinding
//import com.example.blush.data.model.User

class DiscoveryFragment : Fragment() {

    private var _binding: FragmentDiscoveryBinding? = null
    private val binding get() = _binding!!

    // Sample users for demonstration
    private val demoUsers = listOf(
        User(
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
        )
    )

    private var currentUserIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up swipe actions
        binding.likeButton.setOnClickListener {
            handleLike()
        }

        binding.dislikeButton.setOnClickListener {
            handleDislike()
        }

        // Load first profile
        displayCurrentProfile()
    }

    private fun displayCurrentProfile() {
        if (currentUserIndex < demoUsers.size) {
            val user = demoUsers[currentUserIndex]
            binding.profileName.text = "${user.name}, ${user.age}"
            binding.profileLocation.text = user.location
            binding.profileBio.text = user.bio

            // Clear previous chips and add new ones
            binding.profileInterests.removeAllViews()
            // In a real app, add interest chips here

            // Load profile image (in a real app, use a library like Glide or Coil)
            // binding.profileImage.load(user.photos.firstOrNull())
        } else {
            // No more profiles
            binding.noMoreProfilesText.visibility = View.VISIBLE
            binding.profileCard.visibility = View.GONE
            binding.actionButtons.visibility = View.GONE
        }
    }

    private fun handleLike() {
        // In a real app, send like to backend
        // If match, show match notification

        // Go to next profile
        currentUserIndex++
        displayCurrentProfile()
    }

    private fun handleDislike() {
        // Skip to next profile
        currentUserIndex++
        displayCurrentProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}