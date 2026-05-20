package com.betterblue.kit.models

import com.betterblue.kit.logging.BBLogCategory
import com.betterblue.kit.logging.BBLogger

class APIError(
    message: String,
    val code: Int? = null,
    val apiName: String? = null,
    val errorType: ErrorType = ErrorType.general,
    val userInfo: Map<String, String>? = null,
) : Exception(message) {

    enum class ErrorType(val displayLabel: String) {
        general("Error"),
        invalidVehicleSession("Session Expired"),
        invalidCredentials("Invalid Credentials"),
        serverError("Server Error"),
        invalidPin("Invalid PIN"),
        concurrentRequest("Request In Progress"),
        failedRetryLogin("Reauthentication Failed"),
        requiresMFA("Verification Required"),
        kiaInvalidRequest("Request Rejected"),
        regionNotSupported("Region Not Supported"),
    }

    companion object {
        fun logError(
            message: String,
            code: Int? = null,
            apiName: String? = null,
            errorType: ErrorType = ErrorType.general,
            userInfo: Map<String, String>? = null,
        ): APIError {
            val error = APIError(message, code, apiName, errorType, userInfo)
            var logMessage = "${apiName ?: "Unknown"}: $message"
            if (code != null) logMessage += " | Status Code: $code"
            if (errorType != ErrorType.general) logMessage += " | Error Type: ${errorType.name}"
            if (userInfo != null) logMessage += " | User Info: $userInfo"
            BBLogger.error(BBLogCategory.api, logMessage)
            return error
        }

        fun requiresMFA(
            xid: String,
            otpKey: String? = null,
            hasEmail: Boolean = false,
            hasPhone: Boolean = false,
            email: String? = null,
            phone: String? = null,
            rmTokenExpired: Boolean = false,
            apiName: String? = null,
        ): APIError {
            val info = mutableMapOf("xid" to xid)
            if (otpKey != null) info["otpKey"] = otpKey
            info["hasEmail"] = if (hasEmail) "true" else "false"
            info["hasPhone"] = if (hasPhone) "true" else "false"
            if (email != null) info["email"] = email
            if (phone != null) info["phone"] = phone
            if (rmTokenExpired) info["rmTokenExpired"] = "true"
            val message = if (rmTokenExpired)
                "Session expired - verification required"
            else
                "Multi-Factor Authentication Required"
            return logError(message, apiName = apiName, errorType = ErrorType.requiresMFA, userInfo = info)
        }

        fun invalidVehicleSession(
            message: String = "Invalid vehicle for current session",
            apiName: String? = null,
        ): APIError = logError(message, code = 1005, apiName = apiName, errorType = ErrorType.invalidVehicleSession)

        fun invalidCredentials(
            message: String = "Invalid username or password",
            apiName: String? = null,
        ): APIError = logError(message, code = 401, apiName = apiName, errorType = ErrorType.invalidCredentials)

        fun serverError(
            message: String = "Server temporarily unavailable",
            apiName: String? = null,
        ): APIError = logError(message, code = 502, apiName = apiName, errorType = ErrorType.serverError)

        fun invalidPin(message: String, apiName: String? = null): APIError =
            logError(message, apiName = apiName, errorType = ErrorType.invalidPin)

        fun concurrentRequest(
            message: String = "Another request is already in progress. Please wait and try again.",
            apiName: String? = null,
        ): APIError = logError(message, code = 502, apiName = apiName, errorType = ErrorType.concurrentRequest)

        fun failedRetryLogin(
            message: String = "Failed to reauthenticate",
            apiName: String? = null,
        ): APIError = logError(message, code = 502, apiName = apiName, errorType = ErrorType.failedRetryLogin)

        fun kiaInvalidRequest(
            message: String = "Invalid request",
            apiName: String? = null,
        ): APIError = logError(message, code = 502, apiName = apiName, errorType = ErrorType.kiaInvalidRequest)

        fun regionNotSupported(
            message: String = "This region is not yet supported",
            apiName: String? = null,
        ): APIError = logError(message, apiName = apiName, errorType = ErrorType.regionNotSupported)
    }
}
