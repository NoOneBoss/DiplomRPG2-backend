package me.nooneboss.data.logs

import kotlinx.serialization.Serializable

@Serializable
data class ActionLog(val timestamp: String, val uuid: String, val action: String, val context: String)