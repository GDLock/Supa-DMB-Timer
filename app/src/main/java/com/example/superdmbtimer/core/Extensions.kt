package com.example.superdmbtimer.core

import java.time.LocalDateTime
import java.time.OffsetDateTime

fun LocalDateTime.toSeconds() = this.toEpochSecond(OffsetDateTime.now().offset).toInt()