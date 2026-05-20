package com.betterblue.kit.models

import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    val vin: String,
    val regId: String,
    val model: String,
    val accountId: String,
    val fuelType: FuelType,
    val generation: Int,
    val odometer: Distance,
    val vehicleKey: String? = null,
    val marketOptions: VehicleMarketOptions = VehicleMarketOptions.Generic,
)
