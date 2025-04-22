package com.rexvit.retext

data class DeletedNotification(
    val packageName: String,
    val appName: String,
    val title: String,
    val text: String,
    val timestamp: Long
)