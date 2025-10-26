package com.example.lineclearance

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun LineClearanceM3Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFD81B60), // pgvcl_primary
            secondary = Color(0xFFF8BBD0), // pgvcl_accent
            tertiary = Color(0xFFA00037) // pgvcl_primary_dark
        ),
        content = content
    )
}
