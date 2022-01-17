package com.example.superdmbtimer.presentation.settings

import android.provider.Settings
import com.example.superdmbtimer.domain.model.Theme

sealed class SettingsAction {
    object Edit: SettingsAction()
    data class SetTheme(val item: Theme): SettingsAction()
    object Back: SettingsAction()
}