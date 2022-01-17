package com.example.superdmbtimer.presentation.person

import com.example.superdmbtimer.domain.model.PersonDate

sealed class PersonAction {
    data class SetName(val input: String): PersonAction()
    data class SetDate(val date: PersonDate): PersonAction()
    object Back: PersonAction()
    object Navigate: PersonAction()
}