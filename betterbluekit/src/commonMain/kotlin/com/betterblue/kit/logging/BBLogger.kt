package com.betterblue.kit.logging

enum class BBLogLevel(val emoji: String) {
    debug("🔍"),
    info("ℹ️"),
    warning("⚠️"),
    error("❌"),
}

enum class BBLogCategory(val displayName: String) {
    api("API"),
    auth("Auth"),
    mfa("MFA"),
    liveActivity("LiveActivity"),
    intent("Intent"),
    background("Background"),
    push("Push"),
    app("App"),
    vehicle("Vehicle"),
    fakeAPI("FakeAPI"),
}

fun interface BBLogSink {
    fun log(level: BBLogLevel, category: BBLogCategory, message: String)
}

object BBPrintLogSink : BBLogSink {
    override fun log(level: BBLogLevel, category: BBLogCategory, message: String) {
        println("${level.emoji} [${category.displayName}] $message")
    }
}

object BBLogger {
    var sink: BBLogSink = BBPrintLogSink

    fun debug(category: BBLogCategory, message: String) =
        sink.log(BBLogLevel.debug, category, message)

    fun info(category: BBLogCategory, message: String) =
        sink.log(BBLogLevel.info, category, message)

    fun warning(category: BBLogCategory, message: String) =
        sink.log(BBLogLevel.warning, category, message)

    fun error(category: BBLogCategory, message: String) =
        sink.log(BBLogLevel.error, category, message)
}
