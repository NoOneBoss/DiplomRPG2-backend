package me.nooneboss.data.chat


import kotlinx.serialization.Serializable

@Serializable
data class ChatLog(val user_uuid: String, val timestamp: String, val message: String, val chat_type: String)