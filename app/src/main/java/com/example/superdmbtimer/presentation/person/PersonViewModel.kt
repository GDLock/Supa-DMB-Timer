package com.example.superdmbtimer.presentation.person

import androidx.lifecycle.viewModelScope
import com.example.superdmbtimer.core.BaseViewModel
import com.example.superdmbtimer.domain.model.PersonDate
import com.example.superdmbtimer.navigation.Destinations
import com.example.superdmbtimer.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val personUseCases: PersonUseCases,
) : BaseViewModel<PersonViewState, PersonAction>(PersonViewState(), navigationManager) {

    private var initialName = ""

    private val name = MutableStateFlow("")
    private val date = MutableStateFlow(PersonDate())

    private val _uiEffect = Channel<UIEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    override val state = combine(name, date) { name, date ->
        PersonViewState(name, date, initialName.isNotEmpty())
    }.stateInit()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            initialName = personUseCases.getNameUseCase()
            name.value = initialName
            date.value = personUseCases.getDatesUseCase()
        }
    }

    fun effect(effect: UIEffect) {
        viewModelScope.launch {
            when (effect) {
                UIEffect.OpenSheet -> _uiEffect.send(effect)
            }
        }
    }

    override fun action(action: PersonAction) {

        when (action) {
            PersonAction.Navigate -> {
                state.value.run {
                    viewModelScope.launch {
                        personUseCases.setPersonUseCase(name, date)
                    }
                }
                if (initialName.isNotBlank()) back()
                else navigate(Destinations.home.route) {
                    popUpTo(Destinations.person.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
            is PersonAction.SetDate -> date.value = action.date
            is PersonAction.SetName -> name.value = action.input
            PersonAction.Back -> back()
        }
    }
}