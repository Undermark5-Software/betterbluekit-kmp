package com.betterblue.kit.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

@Serializable
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Instant,
) {
    val isValid: Boolean
        // 5-minute buffer matching the Swift implementation
        get() = Clock.System.now() < expiresAt - 300.seconds
}
