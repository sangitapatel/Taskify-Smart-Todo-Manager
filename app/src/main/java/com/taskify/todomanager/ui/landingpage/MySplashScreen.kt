package com.taskify.todomanager.ui.landingpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.taskify.todomanager.R
import com.taskify.todomanager.ui.utils.theme.MyTasksTheme
import kotlinx.coroutines.delay

@Composable
fun MySplashScreen(
    modifier: Modifier = Modifier,
    navigate: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.primary

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
    }
    LaunchedEffect(key1 = Unit) {
        delay(2000)
        navigate()
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.mytasks_icon),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MySplashScreenPreview() {
    MyTasksTheme {
        MySplashScreen(
            navigate = {}
        )
    }
}