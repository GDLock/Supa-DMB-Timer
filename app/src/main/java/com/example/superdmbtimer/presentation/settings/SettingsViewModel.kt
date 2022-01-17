package com.example.superdmbtimer.presentation.settings

import androidx.lifecycle.viewModelScope
import com.example.superdmbtimer.core.BaseViewModel
import com.example.superdmbtimer.data.DataStorePref
import com.example.superdmbtimer.domain.model.Theme
import com.example.superdmbtimer.navigation.Destinations
import com.example.superdmbtimer.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val themePref: DataStorePref<Theme>
) : BaseViewModel<Theme, SettingsAction>(Theme.LIKE_SYSTEM, navigationManager) {

    override val state = themePref.value.stateInit()

    override fun action(action: SettingsAction) {
        viewModelScope.launch {
            when (action) {
                SettingsAction.Back -> back()
                SettingsAction.Edit -> navigate(Destinations.person.route)
                is SettingsAction.SetTheme -> themePref.setValue(action.item)
            }
        }
    }
}
