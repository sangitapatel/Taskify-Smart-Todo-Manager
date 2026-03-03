package com.taskify.todomanager.ui.utils.parts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskify.todomanager.R
import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.ui.categories.CategoryIcons
import com.taskify.todomanager.ui.utils.theme.MyTasksTheme
import com.taskify.todomanager.ui.utils.theme.primary
import com.taskify.todomanager.ui.utils.theme.secondary
import com.taskify.todomanager.ui.utils.theme.white
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskForm(
    taskNameValue: String,
    onNameValueChange: (String) -> Unit,
    taskDescValue: String,
    onDescValueChange: (String) -> Unit,
    taskDateValue: String,
    onDateValueChange: (String) -> Unit,
    taskTimeValue: String,
    onTimeValueChange: (String) -> Unit,
    taskPriorityValue: Int,
    onPriorityValueChange: (Int) -> Unit,
    taskCategoryValue: String,
    onCategoryValueChange: (String) -> Unit,
    categoryData: List<CategoryEntity>,
) {
    // Priority text
    val priorityText = when (taskPriorityValue) {
        1 -> "High"
        3 -> "Low"
        else -> "Medium"
    }

    val focusManager = LocalFocusManager.current

    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val selectedCategory = categoryData.find { it.id.toString() == taskCategoryValue }
    var selectedCategoryName by remember { mutableStateOf("Choose category") }

    val (initialHour, initialMinute) = remember(taskTimeValue) {
        if (taskTimeValue.isNotEmpty()) {
            val parts = taskTimeValue.split(":")
            parts[0].toInt() to parts[1].toInt()
        } else {
            val now = LocalDateTime.now().plusHours(1)
            now.hour to now.minute
        }
    }
    var selectedHour by remember(taskTimeValue) { mutableIntStateOf(initialHour) }
    var selectedMinute by remember(taskTimeValue) { mutableIntStateOf(initialMinute) }

    LaunchedEffect(taskCategoryValue, categoryData) {
        selectedCategoryName = selectedCategory?.name ?: "Choose category"
    }

    LaunchedEffect(Unit) {
        if (taskDateValue.isEmpty()) {
            val today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd - MM - yyyy"))
            onDateValueChange(today)
        }
        if (taskTimeValue.isEmpty()) {
            val now = LocalTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm"))
            onTimeValueChange(now)
        }
    }

    var nameError by remember { mutableStateOf(false) }
    var descError by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        OutlinedTextField(
            value = taskNameValue,
            onValueChange = {
                onNameValueChange(it)
                nameError = it.isEmpty()
            },
            label = { Text("Task Name*") },
            isError = nameError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = primary,
                disabledBorderColor = secondary,
                focusedTrailingIconColor = primary,
                cursorColor = primary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                unfocusedBorderColor = secondary,
                focusedBorderColor = primary
            ),
        )
        if (nameError) {
            Text(
                text = "Task name is required",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }
        val showDescription = false
        if (showDescription) {
            OutlinedTextField(
                value = taskDescValue,
                onValueChange = {
                    onDescValueChange(it)
                    descError = it.isEmpty()
                },
                label = { Text("Description*") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Filled.AssignmentTurnedIn, contentDescription = null) },
                isError = descError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = primary,
                    disabledBorderColor = secondary,
                    focusedTrailingIconColor = primary,
                    cursorColor = primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    unfocusedBorderColor = secondary,
                    focusedBorderColor = primary
                )
            )
            if (descError) {
                Text(
                    text = "Description is required",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
        OutlinedTextField(
            value = taskDateValue,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("Date*") },

            trailingIcon = { Icon(painterResource(R.drawable.calendar), modifier = Modifier.size(18.dp), contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    focusManager.clearFocus()
                    showDatePicker = true
                },
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = primary,
                disabledBorderColor = secondary,
                focusedTrailingIconColor = primary,
                cursorColor = primary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                unfocusedBorderColor = secondary,
                focusedBorderColor = primary
            )
        )

        // Date Picker Dialog
        if (showDatePicker) {
            val dateFormatter = DateTimeFormatter.ofPattern("dd - MM - yyyy")
            val initialDate = remember(taskDateValue) {
                if (taskDateValue.isNotEmpty()) {
                    LocalDate.parse(taskDateValue, dateFormatter)
                } else {
                    LocalDate.now()
                }
            }
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = initialDate.atStartOfDay(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selected = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDateValueChange(selected.format(dateFormatter))
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        selectedDayContainerColor = primary,
                        selectedDayContentColor = white,
                        todayContentColor = primary,
                        todayDateBorderColor = primary,
                        dayContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        // Time Field
        OutlinedTextField(
            value = "%02d:%02d".format(selectedHour, selectedMinute),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("Time*") },
            trailingIcon = { Icon(painterResource(R.drawable.time),  modifier = Modifier.size(18.dp),contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    focusManager.clearFocus()
                    showTimePicker = true
                },
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = primary,
                disabledBorderColor = secondary,
                focusedTrailingIconColor = primary,
                cursorColor = primary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                unfocusedBorderColor = secondary,
                focusedBorderColor = primary
            )
        )

        // Time Picker Dialog
        if (showTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = selectedHour,
                initialMinute = selectedMinute,
                is24Hour = true
            )
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        selectedHour = timePickerState.hour
                        selectedMinute = timePickerState.minute
                        onTimeValueChange("%02d:%02d".format(selectedHour, selectedMinute))
                        showTimePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) {
                        Text("Cancel")
                    }
                },
                text = {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            selectorColor = primary,
                            timeSelectorSelectedContainerColor = primary,
                            timeSelectorSelectedContentColor = white,
                            timeSelectorUnselectedContainerColor = secondary,
                            timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                            clockDialSelectedContentColor = white,
                            clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            )
        }

        // Priority Field (Read-only display)
        OutlinedTextField(
            value = priorityText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Priority") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = primary,
                disabledBorderColor = secondary,
                focusedTrailingIconColor = primary,
                cursorColor = primary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                unfocusedBorderColor = secondary,
                focusedBorderColor = primary
            )
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            PriorityRadio("High", 1, taskPriorityValue, onPriorityValueChange)
            PriorityRadio("Medium", 2, taskPriorityValue, onPriorityValueChange)
            PriorityRadio("Low", 3, taskPriorityValue, onPriorityValueChange)
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedCategoryName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = primary,
                    disabledBorderColor = secondary,
                    focusedTrailingIconColor = primary,
                    cursorColor = primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    unfocusedBorderColor = secondary,
                    focusedBorderColor = primary
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categoryData.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat.name, fontSize = 16.sp) },
                        onClick = {
                            selectedCategoryName = cat.name
                            onCategoryValueChange(cat.id.toString())
                            expanded = false
                        },
                        modifier = Modifier.height(56.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PriorityRadio(
    text: String,
    value: Int,
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onSelect(value) }
    ) {
        RadioButton(
            selected = selected == value,
            onClick = { onSelect(value) }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text)
    }
}
