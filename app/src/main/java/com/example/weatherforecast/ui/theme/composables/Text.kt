package com.example.weatherforecast.ui.theme.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TextSmallBlack(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun TextSmallWhite(text: String) {
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun TextMediumBlack(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun TextMediumWhite(text: String) {
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun TextLarge(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun TextExtraLarge(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge
    )
}