package me.nooneboss.data.logs

import kotlinx.serialization.Serializable

@Serializable
data class LogRequest(val logs: List<ActionLog>)