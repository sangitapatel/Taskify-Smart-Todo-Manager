package com.taskify.todomanager.ui.home.parts.topbar

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskify.todomanager.R
import com.taskify.todomanager.ui.utils.theme.MyTasksTheme
import com.taskify.todomanager.ui.utils.theme.black
import com.taskify.todomanager.ui.utils.theme.primary
import com.taskify.todomanager.ui.utils.theme.secondary
import com.taskify.todomanager.ui.utils.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onSearchClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(
            bottomStart = 20.dp,
            bottomEnd = 20.dp
        ),
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 4.dp
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "My Tasks",
                    color = Color.White
                )
            },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        painterResource(R.drawable.search),
                        "Search",
                        tint = white,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    MyTasksTheme {
        AppTopBar({})
    }
}