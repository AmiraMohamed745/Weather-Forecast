package com.example.weatherforecast.alerts.view

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.weatherforecast.R
import com.example.weatherforecast.navigation.NavigationRouter
import com.example.weatherforecast.navigation.viewmodel.NavigationViewModel
import com.example.weatherforecast.ui.theme.composables.ButtonSecondary
import com.example.weatherforecast.ui.theme.composables.Dimensions
import com.example.weatherforecast.ui.theme.composables.TextMediumBlack
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
//import com.example.weatherforecast.alerts.view.NotificationAlertWorker.Companion.NOTIFICATION_WORK
import com.example.weatherforecast.model.Alert
import com.example.weatherforecast.utils.location.LocationHelper
import com.example.weatherforecast.utils.location.LocationViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit.MILLISECONDS

/*
To Do:
- Extract string as resources
- Use the paddings in Dimensions file
 */

@Composable
fun SettingAlertScreen(
    navigationViewModel: NavigationViewModel,
    locationViewModel: LocationViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                top = Dimensions.paddingExtraLarge,
                bottom = Dimensions.paddingMediumExtraLarge,
                start = Dimensions.paddingModerate,
                end = Dimensions.paddingModerate
            )
    ) {
        navigationViewModel.setCurrentScreen(NavigationRouter.SettingAlertScreen)
        AlertSelections(locationViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertSelections(locationViewModel: LocationViewModel) {

    val context = LocalContext.current

    val locationHelper = LocationHelper(context)
    //val currentLocation by locationViewModel.currentLocation.observeAsState()
    val currentLocation by locationViewModel.currentLocation.collectAsStateWithLifecycle()
    //val currentAddress = locationHelper.getAddress(currentLocation.value)
    val currentAddress = locationHelper.getAddress(currentLocation)

    var alertTitle by rememberSaveable { mutableStateOf("") }
    var currentSelectedDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedTime by rememberSaveable { mutableStateOf<Pair<Int, Int>?>(null) }
    var alertLocation by rememberSaveable { mutableStateOf("") }
    var alertType by rememberSaveable { mutableStateOf<String?>(null) }

    var alert = Alert(
        id = 0,
        title = alertTitle,
        location = if (alertLocation == "Current location") {
            currentAddress
        } else {
            ""
        },
        latitude = if (alertLocation == "Current location") {
            //currentLocation.value?.latitude.toString()
            currentLocation?.latitude.toString()
        } else {
            ""
        },
        longitude = if (alertLocation == "Current location") {
            //currentLocation.value?.longitude.toString()
            currentLocation?.longitude.toString()
        } else {
            ""
        },
        date = currentSelectedDate,
        time = selectedTime,
        type = alertType ?: ""
    )

    val inputData = workDataOf(
        "ALERT_TITLE" to alertTitle,
        "ALERT_LOCATION" to alertLocation, // I am not sure I need this as I can get it from the API
        //"LOCATION_LATITUDE" to currentLocation.value?.latitude,
        "LOCATION_LATITUDE" to currentLocation?.latitude,
        //"LOCATION_LONGITUDE" to currentLocation.value?.longitude,
        "LOCATION_LONGITUDE" to currentLocation?.longitude,
    )

    val initialDelay = calculateInitialDelay(currentSelectedDate, selectedTime)

    AlertTitle(
        title = alertTitle,
        onTitleChanged = { receivedTitle -> alertTitle = receivedTitle }
    )

    Spacer(Modifier.height(Dimensions.paddingMedium))

    AlertLocation(
        favoritePlaces = listOf("Favorite 1", "Favorite 2"),
        onCustomLocationSelected = {},
        onLocationSelected = { selectedLocation -> alertLocation = selectedLocation }
    )

    Spacer(Modifier.height(Dimensions.paddingMedium))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            AlertDate(
                selectedDate = currentSelectedDate,
                onDateSelected = { chosenDate -> currentSelectedDate = chosenDate },
                modifier = Modifier
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            AlertTime(
                selectedTime = selectedTime,
                onTimeSelected = { timeSelected -> selectedTime = timeSelected }
            )
        }
    }

    Spacer(Modifier.height(Dimensions.paddingMedium))

    AlertType { option ->
        alertType = option
        if (option == "Alarm") {
            //
        } else if (option == "Notification") {
            //
        }
    }

    Spacer(Modifier.height(Dimensions.paddingLarge))

    ConfirmationButton {
        if (alertType == "Notification") {
            // schedule notification
            // insert alert to data base
            Log.d("SettingAlertScreen", "Calculated delay = $initialDelay ms") // gets printed
            //scheduleNotification(context = context, delay = initialDelay, data = inputData)
        } else if (alertType == "Alarm") {
            //
        }
        Log.d("SettingAlertScreen", "Confirmation button clicked!") // gets printed
    }
}

@Composable
fun AlertTitle(
    title: String,
    onTitleChanged: (String) -> Unit
) {

    //var alertTitle by rememberSaveable { mutableStateOf("") }

    TextMediumBlack("Title")
    Spacer(Modifier.height(Dimensions.paddingSmall))
    TextField(
        // value = alertTitle,
        // onValueChange = { alertTitle = it },
        value = title,
        onValueChange = onTitleChanged,
        /*label = {
            Text(
                text = stringResource(R.string.alert_title),
                fontSize = 12.sp
            )
        },*/
        placeholder = {
            Text(
                text = stringResource(R.string.e_g_alert_for_alex_trip),
                fontSize = 12.sp
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            /*focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.LightGray*/
            focusedContainerColor = MaterialTheme.colorScheme.tertiary,
            unfocusedContainerColor = MaterialTheme.colorScheme.tertiary

        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertLocation(
    favoritePlaces: List<String>,
    onCustomLocationSelected: () -> Unit,
    onLocationSelected: (String) -> Unit
) {

    val options = listOf("Current location") + favoritePlaces + listOf("Custom location...")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOption by rememberSaveable { mutableStateOf("") }

    TextMediumBlack("Location")
    Spacer(Modifier.height(Dimensions.paddingSmall))
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            value = selectedOption,
            readOnly = true,
            onValueChange = {/* READ-ONLY */ },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            /*trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = "Dropdown icon"
                    )
                }
            }*/
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier
                .menuAnchor(
                    ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option
                        )
                    },
                    onClick = {
                        selectedOption = option
                        expanded = false
                        if (option == "Custom location") {
                            onCustomLocationSelected()
                        } else {
                            onLocationSelected(option)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDate(
    selectedDate: Long?,
    onDateSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val futureSelectableDays = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startOfDay = calendar.timeInMillis
            return utcTimeMillis >= startOfDay
        }

        override fun isSelectableYear(year: Int): Boolean {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            return year >= currentYear
        }
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = futureSelectableDays
    )
    val formattedDate = selectedDate?.let { convertMillisToDate(it) } ?: ""

    TextMediumBlack("Date")
    Spacer(Modifier.height(Dimensions.paddingSmall))
    OutlinedTextField(
        value = formattedDate,
        onValueChange = {},
        label = { Text("Alert Date") },
        placeholder = { Text("MM/DD/YYYY") },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            }
        },
        modifier = modifier.fillMaxWidth()
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
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
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertTime(
    selectedTime: Pair<Int, Int>?,
    onTimeSelected: (selectedTime: Pair<Int, Int>?) -> Unit,

    ) {
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = selectedTime?.first ?: 12,
        initialMinute = selectedTime?.second ?: 0,
        is24Hour = false,
    )
    val formattedTime = selectedTime?.let {
        String.format(Locale.getDefault(), "%02d:%02d", it.first, it.second)
    } ?: ""

    TextMediumBlack("Time")
    Spacer(Modifier.height(Dimensions.paddingSmall))
    OutlinedTextField(
        value = formattedTime,
        onValueChange = {},
        label = { Text("Alert Time") },
        placeholder = { Text("HH:MM") },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showTimePicker = true }) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Select time"
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text(text = "Select Time") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeSelected(Pair(timePickerState.hour, timePickerState.minute))
                        showTimePicker = false
                    }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@Composable
fun AlertType(onOptionSelected: (String) -> Unit) {

    val alertTypes = listOf("Alarm", "Notification")
    val (selectedOption, setSelectedOption) = rememberSaveable { mutableStateOf<String?>(null) }

    TextMediumBlack("Type")
    Spacer(Modifier.height(Dimensions.paddingSmall))
    Row {
        alertTypes.forEach { type ->
            Row(
                Modifier
                    .weight(1f)
                    .height(56.dp)
                    .selectable(
                        selected = (type == selectedOption),
                        onClick = { null },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingVerySmall)
            ) {
                RadioButton(
                    selected = (type == selectedOption),
                    onClick = {
                        setSelectedOption(type)
                        onOptionSelected(type)
                    }
                )
                TextMediumBlack(type)
            }
        }
    }
}

@Composable
fun ConfirmationButton(
    onConfirmation: () -> Unit
) {
    ButtonSecondary(
        height = 50,
        horizontalPadding = 32,
        text = "Confirm"
    ) { onConfirmation() }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

/*private fun scheduleNotification(context: Context, delay: Long, data: Data) {
    val notificationWork = OneTimeWorkRequest
        .Builder(NotificationAlertWorker::class.java)
        .setInitialDelay(delay, MILLISECONDS)
        .setInputData(data)
        .build()

    val instanceWorkManager = WorkManager.getInstance(context)

    instanceWorkManager
        .beginUniqueWork(
            NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE,
            notificationWork
        )
        .enqueue()
}*/

private fun calculateInitialDelay(dateMillis: Long?, time: Pair<Int, Int>?): Long {

    if (dateMillis == null || time == null) return 0L

    val calendar = Calendar.getInstance().apply {
        timeInMillis = dateMillis // need better understanding for this
        set(Calendar.HOUR_OF_DAY, time.first)
        set(Calendar.MINUTE, time.second)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val targetTime = calendar.timeInMillis
    val currentTime = System.currentTimeMillis()
    val delay = targetTime - currentTime

    return if (delay > 0) delay else 0L
}