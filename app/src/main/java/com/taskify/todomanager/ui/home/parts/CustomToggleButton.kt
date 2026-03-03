package com.taskify.todomanager.ui.home.parts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.taskify.todomanager.ui.utils.theme.MyTasksTheme

@Composable
fun CustomToggleButton(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val borderColor = Color(0xFF879093)
    val backgroundColor = if (isChecked) MaterialTheme.colorScheme.primary else Color.Transparent
    val iconColor = if (isChecked) Color.White else Color.Transparent

    Box(
        modifier = Modifier
            .size(20.dp)
            .clickable { onCheckedChange(!isChecked) }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val borderWidth = 1.dp.toPx()
            val corner = 5.dp.toPx()

            drawRoundRect(
                color = backgroundColor,
                cornerRadius = CornerRadius(corner)
            )

            drawRoundRect(
                color = borderColor,
                cornerRadius = CornerRadius(corner),
                style = Stroke(width = borderWidth)
            )
        }

        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Check Icon",
            tint = iconColor,
            modifier = Modifier
                .align(Alignment.Center)
                .size(18.dp)
        )
    }
}





@Composable
@Preview
private fun ToggleButtonPreview() {
    MyTasksTheme {
        var isChecked by remember {
            mutableStateOf(false)
        }
        CustomToggleButton(
            isChecked = isChecked
        ) {
            isChecked = it
        }
    }
}