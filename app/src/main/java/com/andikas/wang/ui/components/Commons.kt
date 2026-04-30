package com.andikas.wang.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WangRing(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.tertiary,
    thickness: Dp = 32.dp
) {
    Canvas(modifier = modifier) {
        val strokeWidthPx = thickness.toPx()
        val radius = (size.minDimension - strokeWidthPx) / 2

        drawCircle(
            color = color,
            radius = radius,
            style = Stroke(width = strokeWidthPx)
        )
    }
}