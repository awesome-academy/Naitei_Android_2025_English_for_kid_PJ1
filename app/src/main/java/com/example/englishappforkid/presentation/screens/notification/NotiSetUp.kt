package com.example.englishappforkid.presentation.screens.notification

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.englishappforkid.R
import com.example.englishappforkid.ui.theme.Pink80
import com.example.englishappforkid.ui.theme.boxBackground
import com.example.englishappforkid.ui.theme.englishAppForKidTheme
import com.example.englishappforkid.utils.openGoogleCalendar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun notiSetup(navController: NavHostController) {
    val context = LocalContext.current
    val defaultFrequency = stringResource(R.string.once_everyday)
    var selectedFrequency by remember { mutableStateOf(defaultFrequency) }
    var fromDate by remember { mutableStateOf("") }
    var toDate by remember { mutableStateOf("") }

    val dateFormatter =
        remember {
            SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        }

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(dateFormatter.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        ).show()
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Pink80,
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .size(28.dp)
                        .clickable {
                            navController.popBackStack()
                        },
            )

            Text(
                text = stringResource(R.string.notification_setup),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Date Picker Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F7F7)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(0.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = stringResource(R.string.select_date), fontSize = 36.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.enter_date), fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = fromDate,
                    onValueChange = {},
                    placeholder = { Text(stringResource(R.string.placeholder_date)) },
                    label = { Text(stringResource(R.string.label_from)) },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_calendar),
                            contentDescription = null,
                        )
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker { fromDate = it } },
                    shape = RoundedCornerShape(8.dp),
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = toDate,
                    onValueChange = {},
                    placeholder = { Text(stringResource(R.string.placeholder_date)) },
                    label = { Text(stringResource(R.string.label_to)) },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_calendar),
                            contentDescription = null,
                        )
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker { toDate = it } },
                    shape = RoundedCornerShape(8.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.clickable { navController.popBackStack() },
                    )
                    Text(
                        text = stringResource(R.string.ok),
                        color = Color(0xFF00BCD4),
                        fontSize = 16.sp,
                        modifier = Modifier.clickable { /* xác nhận ngày */ },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Frequency Dropdown
        frequencyDropdown(
            selectedOption = selectedFrequency,
            onOptionSelected = { selectedFrequency = it },
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (fromDate.isNotEmpty() && toDate.isNotEmpty()) {
                    val sharedPref = context.getSharedPreferences("noti_prefer", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("fromDate", fromDate)
                        putString("toDate", toDate)
                        putString("frequency", selectedFrequency)
                        apply()
                    }
                    openGoogleCalendar(
                        context,
                        title = context.getString(R.string.english_practice),
                        description = "Daily reminder for English learning",
                        startDate = fromDate,
                        endDate = toDate,
                        frequency = selectedFrequency,
                    )
                    createNotificationUtil(context)
                    scheduleNotification(context)
                    navController.popBackStack()
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE082)),
            shape = RoundedCornerShape(24.dp),
        ) {
            Text(
                text = stringResource(R.string.complete),
                fontWeight = FontWeight.SemiBold,
                fontSize = 36.sp,
                color = Color.Gray,
            )
        }
    }
}

@Composable
fun frequencyDropdown(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    val options =
        listOf(
            stringResource(R.string.once_everyday),
            stringResource(R.string.twice_a_day),
            stringResource(R.string.weekly),
        )
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Gray),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = boxBackground),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_repeat),
                    contentDescription = null,
                    tint = Color.Black,
                )
                Text(
                    text = selectedOption,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Pink80,
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = null,
                    tint = Color.Black,
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewNotiScreen() {
    englishAppForKidTheme {
        val navController = rememberNavController()
        notiSetup(navController = navController)
    }
}
