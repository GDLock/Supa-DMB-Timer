package com.example.superdmbtimer.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.superdmbtimer.ui.theme.padding

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = MaterialTheme.padding.general,
    elevation: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.padding(paddingValues),
        elevation = elevation,
        content = content
    )
}