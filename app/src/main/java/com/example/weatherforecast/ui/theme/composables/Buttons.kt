package com.example.weatherforecast.ui.theme.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ButtonPrimary(
    text: String,
    onClick: () -> Unit
) {
    Button(onClick = { onClick() }) {
        TextMediumBlack(text)
    }
}

@Composable
fun ButtonSecondary(
    text: String,
    height: Int,
    horizontalPadding: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        ),
        shape = RoundedCornerShape(Dimensions.paddingMediumLarge),
        modifier = Modifier
            .fillMaxSize()
            .height(height.dp)
            .padding(horizontal = horizontalPadding.dp)
    ) {
        TextMediumWhite(text)
    }
}