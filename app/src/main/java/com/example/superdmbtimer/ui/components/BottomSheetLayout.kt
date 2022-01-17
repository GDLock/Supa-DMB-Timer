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