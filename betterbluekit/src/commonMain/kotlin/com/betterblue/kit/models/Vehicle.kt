@file:UseSerializers(UuidSerializer::class)

package com.betterblue.kit.models

import com.betterblue.kit.serialization.UuidSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlin.uuid.Uuid

@Serializable
data class Vehicle(
    val vin: String,
    val regId: String,
    val model: String,
    val accountId: Uuid,
    val fuelType: FuelType,
    val generation: Int,
    val odometer: Distance,
    val vehicleKey: String? = null,
    val marketOptions: VehicleMarketOptions = VehicleMarketOptions.Generic,
)
