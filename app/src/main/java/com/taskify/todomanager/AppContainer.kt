package com.taskify.todomanager

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.taskify.todomanager.ui.utils.theme.MyTasksTheme
import com.taskify.todomanager.ui.landingpage.MySplashScreen
import com.taskify.todomanager.ui.maincontent.MainContent
import com.taskify.todomanager.ui.nav.HomeScreenRoute
import com.taskify.todomanager.ui.nav.SplashScreenRoute

@Composable
fun AppContainer() {
    val navController = rememberNavController()

    MyTasksTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(
                    navController = navController,
                    startDestination = SplashScreenRoute
                ) {

                    composable<SplashScreenRoute> {
                        MySplashScreen(
                            navigate = {
                                navController.navigate(HomeScreenRoute) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    composable<HomeScreenRoute> {
                        MainContent(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
