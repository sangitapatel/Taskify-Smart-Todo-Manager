package com.taskify.todomanager.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskify.todomanager.R
import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.ui.utils.theme.primary
import com.taskify.todomanager.ui.utils.theme.secondary

@Composable
fun CategoryItem(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    data: CategoryEntity,
    isSelected: Boolean = false
) {
    val icon = CategoryIcons.iconList.getOrElse(data.iconRes) {
        Icons.Default.List
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ICON
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        if (isSelected) primary else secondary,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
//                    painter = painterResource(iconRes),
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = data.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onEditClick) {
                Icon(painterResource(R.drawable.edit), null,modifier = Modifier.size(20.dp))
            }

            IconButton(onClick = onDeleteClick) {
                Icon(painterResource(R.drawable.delete), null, modifier = Modifier.size(20.dp))
            }
        }

        Divider(
            thickness = 0.7.dp,
            color = Color.LightGray,
            modifier = Modifier.padding(start = 60.dp)
        )
    }
}

