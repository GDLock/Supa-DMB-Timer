package com.example.superdmbtimer.core

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.superdmbtimer.ui.theme.SystemBarOnScrimDarkColor
import com.example.superdmbtimer.ui.theme.SystemBarOnScrimLightColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetLayout(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    scrimColor: Color = Color.Black.copy(0.6f),
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val isVisible = sheetState.targetValue != ModalBottomSheetValue.Hidden

    SystemBarsScrim(isVisible)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        modifier = modifier,
        sheetContent = sheetContent,
        sheetShape = sheetShape,
        sheetElevation = sheetElevation,
        sheetBackgroundColor = sheetBackgroundColor,
        sheetContentColor = sheetContentColor,
        scrimColor = scrimColor,
        content = content
    )

    BackHandler(isVisible) {
        scope.launch { sheetState.hide() }
    }
}

@Composable
fun SystemBarsScrim(visible: Boolean) {

    val isLight = MaterialTheme.colors.isLight

    val darkIcons = if (visible) false else isLight

    val backgroundColor = MaterialTheme.colors.background

    val barsColor by animateColorAsState(
        targetValue = if (visible) {
            if (isLight) SystemBarOnScrimLightColor
            else SystemBarOnScrimDarkColor
        } else backgroundColor,
        animationSpec = TweenSpec()
    )
    val systemUiController = rememberSystemUiController()

    DisposableEffect(barsColor) {
        systemUiController.setSystemBarsColor(
            barsColor,
            darkIcons
        )
        onDispose { }
    }
}



@ExperimentalMaterialApi
@Composable
fun BottomSheetLayout(
    modifier: Modifier = Modifier,
    sheetContent: @Composable () -> Unit = {},
    screenState: ScreenState = rememberScreenState(),
    topBar: @Composable () -> Unit = { },
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    floatingActionButton: @Composable () -> Unit = { },
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    isFloatingActionButtonDocked: Boolean = false,
    sheetGesturesEnabled: Boolean = true,
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = 0.dp,
    sheetBackgroundColor: Color = Color.Transparent,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    sheetPeekHeight: Dp = 0.dp,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = contentColorFor(backgroundColor),
    onSheetDismissBefore: () -> Unit = {},
    onSheetDismissAfter: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = screenState.sheetState)
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetContent = { sheetContent() },
        modifier = modifier,
        scaffoldState = scaffoldState,
        snackbarHost = snackbarHost,
        sheetGesturesEnabled = sheetGesturesEnabled,
        sheetShape = sheetShape,
        sheetElevation = sheetElevation,
        sheetBackgroundColor = Color.Transparent,
        sheetContentColor = sheetContentColor,
        sheetPeekHeight = sheetPeekHeight,
    ) {
        Box(Modifier.fillMaxSize()) {
            content()
            Scrim(
                color = Color.Black,
                onDismiss = {
                    scope.launch {
                        onSheetDismissBefore()
                        screenState.collapse()
                        onSheetDismissAfter()
                    }
                },
                visible = screenState.scrimEnabled
            )
        }
    }

    BackHandler(screenState.scrimEnabled) {
        scope.launch {
            screenState.collapse()
        }
    }
}

@Composable
private fun Scrim(
    color: Color,
    onDismiss: () -> Unit,
    visible: Boolean,
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 0.6f else 0f,
        animationSpec = TweenSpec()
    )
    val dismissModifier = if (visible) {
        Modifier
            .pointerInput(onDismiss) { detectTapGestures { onDismiss() } }
            .semantics(mergeDescendants = true) {
                contentDescription = "Close sheet"
                onClick { onDismiss(); true }
            }
    } else {
        Modifier
    }

    val isLight = MaterialTheme.colors.isLight

    val darkIcons = if (visible) false else isLight

    val backgroundColor = MaterialTheme.colors.background

    val barsColor by animateColorAsState(
        targetValue = if (visible) {
            if (isLight) SystemBarOnScrimLightColor
            else SystemBarOnScrimDarkColor
        } else backgroundColor,
        animationSpec = TweenSpec()
    )
    val systemUiController = rememberSystemUiController()

        DisposableEffect(barsColor) {
            systemUiController.setSystemBarsColor(
                barsColor,
                darkIcons = darkIcons
            )
            onDispose {  }
        }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(dismissModifier)
            .background(color.copy(alpha))
    )
}

@ExperimentalMaterialApi
@Stable
class ScreenState(
    val sheetState: BottomSheetState,
) {
    val scrimEnabled: Boolean
        get() = sheetState.isExpanded && !(sheetState.isAnimationRunning && sheetState.targetValue == BottomSheetValue.Collapsed) || sheetState.isAnimationRunning && sheetState.targetValue == BottomSheetValue.Expanded

    suspend fun expand() = sheetState.expand()

    suspend fun collapse() = sheetState.collapse()
}


@ExperimentalMaterialApi
@Composable
fun rememberScreenState(
    sheetState: BottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
): ScreenState = remember(sheetState) { ScreenState(sheetState) }