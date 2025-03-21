package com.example.blush

import java.util.Date
import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val matchId: String,
    val senderId: String,
    val content: String,
    val timestamp: Date = Date(),
    val isRead: Boolean = false
)
