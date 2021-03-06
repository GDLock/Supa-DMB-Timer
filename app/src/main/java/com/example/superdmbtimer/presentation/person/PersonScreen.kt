package com.example.superdmbtimer.presentation.person

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.superdmbtimer.R
import com.example.superdmbtimer.domain.model.PersonDate
import com.example.superdmbtimer.presentation.person.component.DatePickerView
import com.example.superdmbtimer.ui.components.BackButton
import com.example.superdmbtimer.ui.components.BottomSheetLayout
import com.example.superdmbtimer.ui.theme.padding
import kotlinx.coroutines.flow.Flow

@Composable
fun PersonScreen(viewModel: PersonViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsState()

    PersonLayout(
        name = state.name,
        date = state.date,
        buttonEnabled = state.buttonEnabled,
        backEnabled = state.backEnabled,
        isLoaded = state.isLoaded,
        uiEffect = viewModel.uiEffect,
        openSheet = { viewModel.effect(UIEffect.OpenSheet) },
        action = viewModel::action
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonLayout(
    name: String,
    date: PersonDate,
    buttonEnabled: Boolean,
    uiEffect: Flow<UIEffect>,
    backEnabled: Boolean,
    action: (PersonAction) -> Unit,
    openSheet: () -> Unit,
    isLoaded: Boolean
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val focusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        uiEffect.collect {
            when (it) {
                UIEffect.OpenSheet -> {
                    focusManager.clearFocus()
                    sheetState.show()
                }
            }
        }
    }

    BottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            if (isLoaded)
            DatePickerView(
                start = date.start,
                end = date.end,
                isRange = true,
                onDateRangeChange = { start, end ->
                    action(PersonAction.SetDate(PersonDate(start, end)))
                }
            )
            else Text("")
        }
    ) {
        val dateText = remember(date) { date.getDate() }
        Box {
            PersonScaffold(
                name = name,
                onNameChange = { action(PersonAction.SetName(it)) },
                date = dateText,
                openSheet = openSheet,
                backEnabled = backEnabled,
                onBackClick = { action(PersonAction.Back) }
            )

            if (buttonEnabled)
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(MaterialTheme.padding.general),
                    text = { Text(stringResource(R.string.person_screen_fab)) },
                    onClick = { action(PersonAction.Navigate) }
                )
        }
    }
}

@Composable
fun PersonScaffold(
    name: String,
    onNameChange: (String) -> Unit,
    date: String,
    openSheet: () -> Unit,
    backEnabled: Boolean,
    onBackClick: () -> Unit
) {
    if (backEnabled)
        BackButton(
            modifier = Modifier.padding(MaterialTheme.padding.topBar),
            onClick = onBackClick
        )

    PersonContent(
        name = name,
        onNameChange = onNameChange,
        date = date,
        openSheet = openSheet
    )
}

@Composable
fun PersonContent(
    name: String,
    onNameChange: (String) -> Unit,
    openSheet: () -> Unit,
    date: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
            label = { Text(stringResource(R.string.person_screen_label)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = openSheet) {
            Text(date.ifEmpty { stringResource(R.string.person_screen_button)})
        }
    }
}

