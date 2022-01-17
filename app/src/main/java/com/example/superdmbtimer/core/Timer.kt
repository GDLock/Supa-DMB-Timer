package com.example.superdmbtimer.core

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

interface Timer {
    operator fun invoke(start: Int): Flow<Int>

    class Base : Timer {
        override operator fun invoke(start: Int) = flow {
            while (true) {
                emit(start - LocalDateTime.now().toSeconds() - 1)
                delay(1000)
            }
        }
    }
}