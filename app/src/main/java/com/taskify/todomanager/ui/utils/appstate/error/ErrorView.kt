package com.taskify.todomanager.ui.utils.appstate.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.taskify.todomanager.ui.utils.theme.MyTasksTheme

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    errorMessage: String?,
    onRetryClick: (() -> Unit)? = null
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )

        Spacer(Modifier.height(16.dp))


        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(6.dp))


        Text(
            text = errorMessage ?: "Unknown error",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )


        if (onRetryClick != null) {
            Spacer(Modifier.height(18.dp))

            Button(
                onClick = onRetryClick
            ) {
                Text("Retry")
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun ErrorView_Preview() {
    MyTasksTheme {
        ErrorView(
            errorMessage = "org.jetbrains.kotlin.gradle.tasks.CompilationErrorException: Compilation error."
        )
    }
}