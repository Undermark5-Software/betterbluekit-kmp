package com.betterblue.kit.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Brand(val displayName: String) {
    @SerialName("hyundai") hyundai("Hyundai"),
    @SerialName("kia") kia("Kia"),
    @SerialName("fake") fake("Fake (Testing)");

    companion object {
        fun availableBrands(username: String = "", password: String = ""): List<Brand> =
            if (isTestAccount(username, password)) entries else listOf(hyundai, kia)

        fun hyundaiBaseUrl(region: Region): String = when (region) {
            Region.usa -> "https://api.telematics.hyundaiusa.com"
            Region.canada -> "https://mybluelink.ca"
            Region.europe -> "https://prd.eu-ccapi.hyundai.com:8080"
            Region.australia -> "https://au-apigw.ccs.hyundai.com.au:8080"
            Region.china -> "https://prd.cn-ccapi.hyundai.com"
            Region.india -> "https://prd.in-ccapi.hyundai.connected-car.io:8080"
        }

        fun kiaBaseUrl(region: Region): String = when (region) {
            Region.usa -> "https://api.owners.kia.com"
            Region.canada -> "https://kiaconnect.ca"
            Region.europe -> "https://prd.eu-ccapi.kia.com:8080"
            Region.australia -> "https://au-apigw.ccs.kia.com.au:8082"
            Region.china -> "https://prd.cn-ccapi.kia.com"
            Region.india -> "https://prd.in-ccapi.kia.connected-car.io:8080"
        }
    }
}

fun isTestAccount(username: String, password: String): Boolean =
    username.lowercase() == "testaccount@betterblue.com" && password == "betterblue"

@Serializable
enum class FuelType {
    @SerialName("gas") gas,
    @SerialName("electric") electric,
    @SerialName("phev") phev;

    val hasElectricCapability: Boolean get() = this != gas

    companion object {
        fun fromNumber(number: Int): FuelType = when (number) {
            0 -> gas
            1 -> phev
            2 -> electric
            else -> gas
        }
    }
}

@Serializable
enum class Region(val rawValue: String, val displayName: String) {
    @SerialName("US") usa("US", "USA"),
    @SerialName("CA") canada("CA", "Canada"),
    @SerialName("EU") europe("EU", "Europe"),
    @SerialName("AU") australia("AU", "Australia"),
    @SerialName("CN") china("CN", "China"),
    @SerialName("IN") india("IN", "India");

    fun apiBaseUrl(brand: Brand): String = when (brand) {
        Brand.hyundai -> Brand.hyundaiBaseUrl(this)
        Brand.kia -> Brand.kiaBaseUrl(this)
        Brand.fake -> "https://fake.api.testing.com"
    }

    companion object {
        fun fromRawValue(rawValue: String): Region? = entries.find { it.rawValue == rawValue }
    }
}

@Serializable
sealed class VehicleMarketOptions {
    abstract val ccs2Supported: Boolean

    @Serializable
    @SerialName("hyundaiEurope")
    data class HyundaiEurope(override val ccs2Supported: Boolean) : VehicleMarketOptions()

    @Serializable
    @SerialName("generic")
    data object Generic : VehicleMarketOptions() {
        override val ccs2Supported: Boolean = false
    }
}
