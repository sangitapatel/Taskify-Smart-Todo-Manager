package com.taskify.todomanager.ui.home.parts.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskify.todomanager.R
import com.taskify.todomanager.ui.utils.theme.MyTasksTheme
import com.taskify.todomanager.ui.utils.theme.black
import com.taskify.todomanager.ui.utils.theme.gray
import com.taskify.todomanager.ui.utils.theme.poppins
import com.taskify.todomanager.ui.utils.theme.primary
import com.taskify.todomanager.ui.utils.theme.secondary
import com.taskify.todomanager.ui.utils.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    navigateBack: () -> Unit,
    onValueChange: (String) -> Unit,
    value: String
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.search), null, tint = white, modifier = Modifier.size(20.dp))
                CustomSearchField(
                    value = value, onValueChange = onValueChange
                )

            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = primary
        ),
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back),
                    tint = white
                )
            }
        }
    )
}

@Composable
fun CustomSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = poppins,
                fontSize = 16.sp,
                color = white,
                fontWeight = FontWeight.Medium
            ),
            cursorBrush = SolidColor(secondary),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "Search...",
                            fontSize = 16.sp,
                            color = secondary,
                            fontWeight = FontWeight.Medium
                        )

                    }
                    innerTextField()
                }

            }
        )

    }
}


@Preview
@Composable
fun SearchBarPreview() {
    MyTasksTheme {
        var searchText by remember{ mutableStateOf("") }

        SearchTopBar(
            {},
            value = searchText,
            onValueChange = {searchText = it}
        )
    }
}