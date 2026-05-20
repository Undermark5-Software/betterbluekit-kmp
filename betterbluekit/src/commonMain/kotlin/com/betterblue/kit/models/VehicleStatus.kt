package com.betterblue.kit.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Serializable
data class VehicleStatus(
    val vin: String,
    val lastUpdated: Instant,
    val syncDate: Instant? = null,
    val gasRange: FuelRange? = null,
    val evStatus: EVStatus? = null,
    val location: Location,
    val lockStatus: LockStatus,
    val climateStatus: ClimateStatus,
    val odometer: Distance? = null,
    val battery12V: Int? = null,
    val doorOpen: DoorStatus? = null,
    val trunkOpen: Boolean? = null,
    val hoodOpen: Boolean? = null,
    val tirePressureWarning: TirePressureWarning? = null,
    val engineOn: Boolean? = null,
    val accessoryOn: Boolean? = null,
    val remoteIgnition: Boolean? = null,
    val transmissionCondition: Boolean? = null,
    val sleepMode: Boolean? = null,
    val washerFluidLow: Boolean? = null,
) {
    @Serializable
    data class FuelRange(val range: Distance, val percentage: Double)

    @Serializable
    enum class PlugType(val value: Int) {
        @SerialName("0") unplugged(0),
        @SerialName("2") acCharger(2),
        @SerialName("1") dcCharger(1);

        companion object {
            fun fromBatteryPlugin(value: Int): PlugType = when (value) {
                0 -> unplugged
                2 -> acCharger
                else -> dcCharger
            }
        }
    }

    @Serializable
    data class EVStatus(
        val charging: Boolean,
        val chargeSpeed: Double,
        val evRange: FuelRange,
        val plugType: PlugType = PlugType.unplugged,
        val chargeTimeSeconds: Long = 0L,
        val targetSocAC: Double? = null,
        val targetSocDC: Double? = null,
    ) {
        val pluggedIn: Boolean get() = plugType != PlugType.unplugged
        val chargeTime: Duration get() = chargeTimeSeconds.seconds
        val currentTargetSOC: Double?
            get() = when (plugType) {
                PlugType.acCharger -> targetSocAC
                PlugType.dcCharger -> targetSocDC
                PlugType.unplugged -> null
            }
    }

    @Serializable
    data class Location(val latitude: Double, val longitude: Double) {
        val debug: String get() = "$latitude°, $longitude°"
    }

    @Serializable
    enum class LockStatus {
        @SerialName("locked") locked,
        @SerialName("unlocked") unlocked,
        @SerialName("unknown") unknown;

        fun toggled(): LockStatus = when (this) {
            locked -> unlocked
            unlocked -> locked
            unknown -> unknown
        }

        companion object {
            fun fromBoolean(locked: Boolean?): LockStatus = when (locked) {
                true -> LockStatus.locked
                false -> LockStatus.unlocked
                null -> LockStatus.unknown
            }
        }
    }

    @Serializable
    data class ClimateStatus(
        val defrostOn: Boolean,
        val airControlOn: Boolean,
        val steeringWheelHeatingOn: Boolean,
        val temperature: Temperature,
    )

    @Serializable
    data class DoorStatus(
        val frontLeft: Boolean,
        val frontRight: Boolean,
        val backLeft: Boolean,
        val backRight: Boolean,
    ) {
        val anyOpen: Boolean get() = frontLeft || frontRight || backLeft || backRight

        val openDoorsDescription: String
            get() = buildList {
                if (frontLeft) add("FL")
                if (frontRight) add("FR")
                if (backLeft) add("BL")
                if (backRight) add("BR")
            }.takeIf { it.isNotEmpty() }?.joinToString(", ") ?: "None"
    }

    @Serializable
    data class TirePressureWarning(
        val frontLeft: Boolean,
        val frontRight: Boolean,
        val rearLeft: Boolean,
        val rearRight: Boolean,
        val all: Boolean,
    ) {
        val hasWarning: Boolean get() = all || frontLeft || frontRight || rearLeft || rearRight

        val warningDescription: String
            get() {
                if (all) return "All tires"
                return buildList {
                    if (frontLeft) add("FL")
                    if (frontRight) add("FR")
                    if (rearLeft) add("RL")
                    if (rearRight) add("RR")
                }.takeIf { it.isNotEmpty() }?.joinToString(", ") ?: "OK"
            }
    }
}
