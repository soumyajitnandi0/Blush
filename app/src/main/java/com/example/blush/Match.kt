package com.example.blush

import java.util.Date
import java.util.UUID

data class Match(
    val id: String = UUID.randomUUID().toString(),
    val users: Pair<String, String>, // User IDs
    val createdAt: Date = Date(),
    val lastActivity: Date = Date()
)
