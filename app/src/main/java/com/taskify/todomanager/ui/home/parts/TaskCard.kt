package com.taskify.todomanager.ui.home.parts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskify.todomanager.R
import com.taskify.todomanager.data.entity.TaskEntity
import com.taskify.todomanager.ui.utils.theme.MyTasksTheme
import com.taskify.todomanager.ui.utils.util.formatDate
import com.taskify.todomanager.ui.utils.util.trimText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    dataTask: TaskEntity,
    categoryName: String?,
    onSelect: () -> Unit,
    onLongPress: () -> Unit,
    isSelected: Boolean = false,
    onPinClick: () -> Unit,
    onDoneClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true   // ⭐ NEW
) {

    val formatter = DateTimeFormatter.ofPattern("dd - MM - yyyy HH:mm")
    val now = LocalDateTime.now()

    val taskDateTime = try {
        LocalDateTime.parse("${dataTask.date} ${dataTask.time}", formatter)
    } catch (_: Exception) {
        null
    }

    val isLate = taskDateTime?.isBefore(now) == true

    val priorityColor = when (dataTask.priority) {
        1 -> Color(0xFFE53935) // High - red
        2 -> Color(0xFFFF9800) // Medium - orange
        else -> Color(0xFF43A047) // Low - green
    }

    val priorityText = when (dataTask.priority) {
        1 -> "High"
        2 -> "Medium"
        else -> "Low"
    }
    val priorityBg = when (dataTask.priority) {
        1 -> Color(0xFFFFEBEE)
        2 -> Color(0xFFFFF3E0)
        else -> Color(0xFFE8F5E9)
    }
    Column {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { onSelect() },
                    onLongClick = { onLongPress() }
                )
                .background(
                    if (isSelected)
                        MaterialTheme.colorScheme.primary.copy(0.08f)
                    else Color.White
                )
                .padding(horizontal = 10.dp, vertical = 6.dp), // ⭐ less padding
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(Modifier.size(22.dp)) {
                CustomToggleButton(
                    isChecked = dataTask.isDone,
                    onCheckedChange = onDoneClick
                )
            }

            Spacer(Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = dataTask.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = trimText()
                )

                Spacer(Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    categoryName?.let {
                        Text(it, fontSize = 11.sp)
                        Spacer(Modifier.width(4.dp))
                    }

                    Box(
                        modifier = Modifier
                            .background(priorityBg, RoundedCornerShape(10.dp))
                            .padding(horizontal = 6.dp, vertical = 1.0.dp)
                    ) {
                        Text(
                            text = priorityText,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = priorityColor
                        )
                    }
                }

                Spacer(Modifier.height(1.dp))

                Text(
                    text = "${formatDate(dataTask.date)}, ${dataTask.time}",
                    fontSize = 10.sp,
                    color = Color.Gray   // ⭐ changed
                )
            }

            IconButton(onClick = onPinClick) {
                Image(
                    painter = painterResource(
                        if (dataTask.isPinned)
                            R.drawable.enabled_pin
                        else
                            R.drawable.disabled_pin
                    ),
                    contentDescription = "Pin",
                    modifier = Modifier.size(20.dp)
                )
            }
        }


        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 44.dp),
                thickness = 0.5.dp,
                color = Color.LightGray
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun TaskCardPreview() {

    MyTasksTheme {

        val dummy = TaskEntity(
            id = 1,
            name = "Buy Groceries",
            desc = "",
            date = "08 - 02 - 2026",
            time = "15:30",
            priority = 1,
            isDone = false,
            isPinned = true,
            categoryId = 1
        )

        Column {

            TaskCard(
                dataTask = dummy.copy(priority = 2),
                categoryName = "Personal",
                onSelect = {},
                onLongPress = {},
                onDoneClick = {},
                onPinClick = {},
                isSelected = false
            )

            TaskCard(
                dataTask = dummy.copy(priority = 3, isDone = false),
                categoryName = "Work",
                onSelect = {},
                onLongPress = {},
                onDoneClick = {},
                onPinClick = {},
                isSelected = true
            )
            TaskCard(
                dataTask = dummy.copy(isDone = true),
                categoryName = "Work",
                onSelect = {},
                onLongPress = {},
                onDoneClick = {},
                onPinClick = {},
                isSelected = true
            )
        }
    }
}
