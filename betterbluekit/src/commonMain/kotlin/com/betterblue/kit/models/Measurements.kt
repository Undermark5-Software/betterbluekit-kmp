package com.betterblue.kit.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToLong

@Serializable
data class Distance(val length: Double, val units: Units) {
    @Serializable
    enum class Units(val displayName: String, val abbreviation: String) {
        @SerialName("miles") miles("Miles", "mi"),
        @SerialName("kilometers") kilometers("Kilometers", "km");

        fun convert(length: Double, to: Units): Double = when {
            this == to -> length
            this == miles && to == kilometers -> length * 1.609344
            this == kilometers && to == miles -> length / 1.609344
            else -> length
        }

        fun format(length: Double, to: Units): String {
            val converted = convert(length, to)
            return "${converted.roundToLong()} ${to.abbreviation}"
        }

        companion object {
            fun fromInt(value: Int): Units = if (value == 1) kilometers else miles
        }
    }
}

@Serializable
data class Temperature(val value: Double, val units: Units) {
    @Serializable
    enum class Units(val displayName: String, val symbol: String) {
        @SerialName("celsius") celsius("Celsius", "°C"),
        @SerialName("fahrenheit") fahrenheit("Fahrenheit", "°F");

        val hvacRange: ClosedFloatingPointRange<Double>
            get() = when (this) {
                fahrenheit -> 62.0..82.0
                celsius -> 16.0..28.0
            }

        fun integer(): Int = if (this == fahrenheit) 1 else 0

        fun convert(temperature: Double, to: Units): Double = when {
            this == to -> temperature
            this == celsius && to == fahrenheit -> (temperature * 9.0 / 5.0) + 32.0
            this == fahrenheit && to == celsius -> (temperature - 32.0) * 5.0 / 9.0
            else -> temperature
        }

        fun format(temperature: Double, to: Units): String {
            val converted = convert(temperature, to)
            return "${converted.roundToLong()}${to.symbol}"
        }

        companion object {
            fun fromInt(value: Int?): Units = if (value == 1) fahrenheit else celsius
        }
    }

    companion object {
        const val MINIMUM = 62.0
        const val MAXIMUM = 82.0

        fun create(units: Int?, value: String?): Temperature {
            val u = Units.fromInt(units)
            val v = when {
                value != null && value.toDoubleOrNull() != null -> value.toDouble()
                value == "HI" -> Units.fahrenheit.convert(MAXIMUM, u)
                else -> Units.fahrenheit.convert(MINIMUM, u)
            }
            return Temperature(v, u)
        }
    }
}
