package com.example.blush

import java.util.Date
import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val age: Int,
    val gender: String,
    val location: String,
    val bio: String,
    val interests: List<String>,
    val photos: List<String>, // URLs to photos
    val preferences: UserPreferences,
    val createdAt: Date = Date()
)
