package me.nooneboss.data

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(val login: String, val password: String) : Principal {
}