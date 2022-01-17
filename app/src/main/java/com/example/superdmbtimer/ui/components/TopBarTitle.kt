package com.example.superdmbtimer.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BoxScope.TopBarTitle(title: String) {

    Text(
        modifier = Modifier.align(Alignment.Center),
        text = title,
        style = MaterialTheme.typography.h6
    )
}