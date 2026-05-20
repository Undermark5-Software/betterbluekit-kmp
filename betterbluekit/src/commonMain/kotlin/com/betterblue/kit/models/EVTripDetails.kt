package com.betterblue.kit.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class EVTripDetail(
    val distance: Double,
    val odometer: Double,
    val accessoriesEnergy: Int,
    val totalEnergyUsed: Int,
    val regenEnergy: Int,
    val climateEnergy: Int,
    val drivetrainEnergy: Int,
    val batteryCareEnergy: Int,
    val startDate: Instant,
    val durationSeconds: Int,
    val avgSpeed: Double,
    val maxSpeed: Double,
) {
    val id: String get() = "${startDate.epochSeconds}-$odometer"

    val efficiency: Double
        get() = if (totalEnergyUsed > 0) distance / (totalEnergyUsed / 1000.0) else 0.0

    val formattedDuration: String
        get() {
            val hours = durationSeconds / 3600
            val minutes = (durationSeconds % 3600) / 60
            return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
        }
}

@Serializable
data class EVTripDetailsResponse(val trips: List<EVTripDetail>)
