package com.example.lineclearance

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDashboardScreen(myPermits: List<LineClearancePermit>, userName: String, userRole: String) {
    val context = LocalContext.current

    val confirmed = myPermits.filter { it.status == "Confirmed" }
    val waiting = myPermits.filter { it.status == "Waiting" }
    val completed = myPermits.filter { it.status == "Completed" }

    var showReturnDialog by remember { mutableStateOf(false) }
    var selectedPermit by remember { mutableStateOf<LineClearancePermit?>(null) }

    if (showReturnDialog && selectedPermit != null) {
        ReturnLcDialog(permit = selectedPermit!!, onDismiss = { showReturnDialog = false }) {
            showReturnDialog = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Dashboard", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFD81B60)),
                actions = {
                    IconButton(onClick = {
                        val intent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(context, SubstationActivity::class.java)
                intent.putExtra("userName", userName)
                context.startActivity(intent)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Take New LC")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(8.dp)
        ) {
            item { UserInfoHeader(name = userName, role = userRole) }

            if (confirmed.isNotEmpty()) {
                item { CategoryHeader("LC Confirmed") }
                items(confirmed) { permit ->
                    PermitCard(permit = permit, onClick = {
                        selectedPermit = permit
                        showReturnDialog = true
                    })
                }
            }

            if (waiting.isNotEmpty()) {
                item { CategoryHeader("LC in Waiting") }
                items(waiting) { permit ->
                    PermitCard(permit = permit, onClick = {})
                }
            }

            if (completed.isNotEmpty()) {
                item { CategoryHeader("Completed LCs") }
                items(completed) { permit ->
                    PermitCard(permit = permit, onClick = {})
                }
            }
        }
    }
}

@Composable
fun UserInfoHeader(name: String, role: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Welcome, $name", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = role, color = Color.Gray)
        }
    }
}

@Composable
fun CategoryHeader(title: String) {
    Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
}

@Composable
fun PermitCard(permit: LineClearancePermit, onClick: () -> Unit) {
    var elapsedTime by remember { mutableStateOf("") }

    if (permit.status == "Confirmed") {
        LaunchedEffect(permit.timestamp) {
            while (true) {
                val diff = System.currentTimeMillis() - permit.timestamp
                val hours = diff / (1000 * 60 * 60)
                val minutes = (diff / (1000 * 60)) % 60
                elapsedTime = String.format("%02d:%02d", hours, minutes)
                delay(60000)
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 8.dp).clickable(enabled = permit.status == "Confirmed", onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = if (permit.status == "Completed") CardDefaults.cardColors(containerColor = Color.LightGray) else CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = permit.feederName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "by ${permit.requesterName ?: "N/A"}")
                }
                Column(horizontalAlignment = Alignment.End) {
                    val statusColor = when (permit.status) {
                        "Confirmed" -> Color(0xFF2E7D32)
                        "Waiting" -> Color.Blue
                        "Completed" -> Color.DarkGray
                        else -> Color.Gray
                    }
                    Text(text = permit.status, color = statusColor, fontWeight = FontWeight.Bold)
                    if (permit.status == "Confirmed") {
                        Text(text = "Time: $elapsedTime")
                    }
                }
            }
            Text(text = "Work: ${permit.workType ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ReturnLcDialog(permit: LineClearancePermit, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Return Line Clearance") },
        text = { Text("Are you sure you want to generate the return message for ${permit.feederName}?") },
        confirmButton = {
            Button(
                onClick = {
                    val phoneNumber = SubstationDataSource.getPhoneNumberForSubstation(permit.substationName)
                    val originalMessage = permit.confirmationSmsBody ?: ""
                    val returnMessage = "$originalMessage Please return this LC."

                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto:$phoneNumber")
                        putExtra("sms_body", returnMessage)
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "No SMS app found.", Toast.LENGTH_SHORT).show()
                    }
                    onConfirm()
                })
            {
                Text("Return LC")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PersonalDashboardPreview() {
    LineClearanceM3Theme {
       val fakePermits = listOf(
           LineClearancePermit(1, "NANI MARAD AG", "PATANVAV S/S", "Confirmed", "1234", requesterName = "Jay Patel"),
           LineClearancePermit(2, "RAMDEV JGY", "BHADER S/S", "Waiting", null, requesterName = "Jay Patel")
       )
       PersonalDashboardScreen(myPermits = fakePermits, userName = "Jay Patel", userRole = "JE")
    }
}
