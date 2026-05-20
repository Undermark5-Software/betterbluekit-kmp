@file:UseSerializers(UuidSerializer::class)

package com.betterblue.kit.models

import com.betterblue.kit.serialization.UuidSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlin.uuid.Uuid

@Serializable
enum class HTTPRequestType(val displayName: String) {
    @SerialName("login") login("Login"),
    @SerialName("fetchVehicles") fetchVehicles("Fetch Vehicles"),
    @SerialName("fetchVehicleStatus") fetchVehicleStatus("Fetch Status"),
    @SerialName("sendCommand") sendCommand("Send Command"),
    @SerialName("sendMFA") sendMFA("Send MFA"),
    @SerialName("verifyMFA") verifyMFA("Verify MFA"),
    @SerialName("fetchEVTripDetails") fetchEVTripDetails("Fetch Trip Details"),
}

typealias HTTPLogSink = (HTTPLog) -> Unit

@Serializable
data class HTTPLog(
    val timestamp: Instant,
    val accountId: Uuid,
    val requestType: HTTPRequestType,
    val method: String,
    val url: String,
    val requestHeaders: Map<String, String>,
    val requestBody: String?,
    val responseStatus: Int?,
    val responseHeaders: Map<String, String>,
    val responseBody: String?,
    val error: String?,
    val apiError: String? = null,
    val duration: Double,
    val stackTrace: String? = null,
    val vin: String? = null,
) {
    val statusText: String
        get() = responseStatus?.let {
            if (apiError != null) "$it (API Error)" else "$it"
        } ?: if (error != null) "Error" else "Pending"

    val isSuccess: Boolean
        get() = responseStatus?.let { it in 200..299 && error == null && apiError == null } ?: false

    val formattedDuration: String get() = "%.2fs".format(duration)

    val preciseTimestamp: String
        get() {
            val local = timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
            val ms = local.nanosecond / 1_000_000
            return "${local.hour.pad2()}:${local.minute.pad2()}:${local.second.pad2()}.${ms.pad3()}"
        }

    private fun Int.pad2(): String = "$this".padStart(2, '0')
    private fun Int.pad3(): String = "$this".padStart(3, '0')
}
