package com.example.superdmbtimer.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.superdmbtimer.domain.model.Theme
import com.example.superdmbtimer.domain.model.Theme.*
import com.example.superdmbtimer.ui.components.BackButton
import com.example.superdmbtimer.ui.components.CustomCard
import com.example.superdmbtimer.ui.components.TopBarTitle
import com.example.superdmbtimer.ui.theme.dimensions
import com.example.superdmbtimer.ui.theme.padding

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsState()

    SettingsScaffold(
        theme = state,
        onBackClick = {viewModel.action(SettingsAction.Back)},
        onEditClick = {viewModel.action(SettingsAction.Edit)},
        onSetTheme = {viewModel.action(SettingsAction.SetTheme(it))}
    )
}

@Composable
fun SettingsScaffold(
    theme: Theme,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onSetTheme: (Theme) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)) {

            BackButton { onBackClick() }

            TopBarTitle("Настройки")
        }

        Spacer(Modifier.height(MaterialTheme.dimensions.spaceMedium))

        CustomCard {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEditClick() }
                    .padding(MaterialTheme.padding.general),
                text = "Редактировать")
        }

        CustomCard {
            Column {
                Theme.values().forEach {
                    ThemeSelector(
                        theme = it,
                        current = theme,
                        onClick = { onSetTheme(it) }
                    )
                    if (it != Theme.values().last())
                        Divider()
                }
            }
        }
    }
}

@Composable
fun ThemeSelector(
    theme: Theme,
    current: Theme,
    onClick: () -> Unit
) {
    val title = remember {
        when (theme) {
            LIKE_SYSTEM -> "Как в системе"
            LIGHT -> "Светлая тема"
            DARK -> "Темная тема"
        }
    }
    Row(modifier = Modifier
        .height(IntrinsicSize.Max)
        .clickable { onClick() }
        .padding(MaterialTheme.padding.general),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title
        )
        if (theme == current)
            Icon(imageVector = Icons.Outlined.Done, contentDescription = null)
    }
}