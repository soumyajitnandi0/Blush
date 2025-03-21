package com.example.blush

data class UserPreferences(
    val interestedIn: List<String>, // Genders interested in
    val ageRange: IntRange,
    val maxDistance: Int, // in kilometers
    val lookingFor: List<String> // "casual", "serious", "friends", etc.
)
