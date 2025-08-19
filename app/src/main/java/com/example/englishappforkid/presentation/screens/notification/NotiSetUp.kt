package com.example.englishappforkid.presentation.screens.notification

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.englishappforkid.R
import com.example.englishappforkid.ui.theme.Cowbell
import com.example.englishappforkid.ui.theme.Pink80
import com.example.englishappforkid.ui.theme.boxBackground
import com.example.englishappforkid.ui.theme.englishAppForKidTheme

@Composable
fun notiSetup(navController: NavHostController) {
    var selectedFrequency by remember { mutableStateOf("Once everyday") }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Top Bar
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .size(28.dp)
                        .clickable { navController.popBackStack() },
                tint = Pink80,
            )

            Text(
                text = stringResource(R.string.notification_setup),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Date Picker Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = boxBackground),
            elevation = CardDefaults.cardElevation(4.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.select_date),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 36.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.enter_date),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Calendar",
                        tint = Color.Black,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("mm/dd/yyyy") },
                    label = { Text("From") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(36.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("mm/dd/yyyy") },
                    label = { Text("To") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = Color.Gray,
                        modifier = Modifier.clickable { /* TODO */ },
                    )
                    Spacer(modifier = Modifier.width(36.dp))
                    Text(
                        text = stringResource(R.string.ok),
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { /* TODO */ },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Frequency Dropdown
        frequencyDropdown(
            selectedOption = selectedFrequency,
            onOptionSelected = { selectedFrequency = it },
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Complete Button
        Button(
            onClick = { /* TODO */ },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Cowbell),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(R.string.complete),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color.Black,
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
            stringResource(R.string.custom),
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
