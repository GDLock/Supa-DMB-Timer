package com.example.superdmbtimer.presentation.person

import com.example.superdmbtimer.domain.model.PersonDate

data class PersonViewState(
    val name: String = "",
    val date: PersonDate = PersonDate(),
    val backEnabled: Boolean = false
) {
    val buttonEnabled = name.isNotBlank() && date.run { start != -1 && end != -1 }
}