package com.taskify.todomanager.ui.maincontent

import androidx.annotation.DrawableRes
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskify.todomanager.R

enum class BottomTab {
    HOME, ADD, CATEGORY
}

@Composable
fun BottomChipBar(
    selectedTab: BottomTab,
    onHomeClick: () -> Unit,
    onAddClick: () -> Unit,
    onCategoryClick: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Surface(
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(30.dp),
        color = primaryColor,
        modifier = Modifier
            .padding(
                start = 12.dp,
                end = 12.dp,
                bottom = 6.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BottomItem(
                selected = selectedTab == BottomTab.HOME,
                icon = R.drawable.home,
                label = "Home",
                onClick = onHomeClick
            )

            BottomItem(
                selected = selectedTab == BottomTab.ADD,
                icon = R.drawable.add,
                label = "Add",
                onClick = onAddClick
            )

            BottomItem(
                selected = selectedTab == BottomTab.CATEGORY,
                icon = R.drawable.categories,
                label = "Category",
                onClick = onCategoryClick
            )
        }
    }
}

@Composable
private fun BottomItem(
    selected: Boolean,
    @DrawableRes icon: Int,
    label: String,
    onClick: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Surface(
        onClick = onClick,
        shape = if (selected) RoundedCornerShape(25.dp) else RoundedCornerShape(0.dp),
        color = if (selected) Color.White else Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                modifier = Modifier.size(22.dp),
                contentDescription = label,
                tint = if (selected) primaryColor else Color.White
            )

            if (selected) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    color = primaryColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
