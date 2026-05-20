package com.betterblue.kit.models

import kotlinx.serialization.Serializable

sealed class VehicleCommand {
    data object Lock : VehicleCommand()
    data object Unlock : VehicleCommand()
    data class StartClimate(val options: ClimateOptions) : VehicleCommand()
    data object StopClimate : VehicleCommand()
    data object StartCharge : VehicleCommand()
    data object StopCharge : VehicleCommand()
    data class SetTargetSOC(val acLevel: Int, val dcLevel: Int) : VehicleCommand()
}

@Serializable
data class ClimateOptions(
    val climate: Boolean = true,
    val temperature: Temperature = Temperature.create(units = 1, value = "72"),
    val defrost: Boolean = false,
    val duration: Int = 10,
    val frontLeftSeat: Int = 0,
    val frontRightSeat: Int = 0,
    val rearLeftSeat: Int = 0,
    val rearRightSeat: Int = 0,
    val steeringWheel: Int = 0,
    val frontLeftVentilation: Boolean = false,
    val frontRightVentilation: Boolean = false,
    val rearLeftVentilation: Boolean = false,
    val rearRightVentilation: Boolean = false,
    val rearDefrost: Boolean = false,
) {
    val heatValue: Int
        get() = when {
            !rearDefrost && steeringWheel == 0 -> 0
            rearDefrost && steeringWheel != 0 -> 4
            rearDefrost -> 2
            steeringWheel != 0 -> 3
            else -> 0
        }

    fun getSeatHeaterVentInfo(): Map<String, Int> = mapOf(
        "drvSeatHeatState" to convertSeatSetting(frontLeftSeat, frontLeftVentilation),
        "astSeatHeatState" to convertSeatSetting(frontRightSeat, frontRightVentilation),
        "rlSeatHeatState" to convertSeatSetting(rearLeftSeat, rearLeftVentilation),
        "rrSeatHeatState" to convertSeatSetting(rearRightSeat, rearRightVentilation),
    )
}

fun convertSeatSetting(value: Int, cooling: Boolean): Int {
    if (value == 0) return 0
    return if (cooling) value + 2 else value + 5
}
