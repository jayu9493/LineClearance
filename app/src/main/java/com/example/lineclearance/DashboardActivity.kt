package com.example.lineclearance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class DashboardActivity : ComponentActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(this)

        val permits = db.lineClearancePermitDao().getAllPermits()

        setContent {
            val permitList by permits.observeAsState(initial = emptyList())
            LineClearanceM3Theme {
                DashboardScreen(permitList)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(permits: List<LineClearancePermit>) {
    val confirmed = permits.filter { it.status == "Confirmed" }
    val waiting = permits.filter { it.status == "Waiting" }
    val rejected = permits.filter { it.status == "Rejected" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2E7D32))
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            item { UserProfileCard() }

            if (confirmed.isNotEmpty()) {
                item { CategoryHeader("LC Confirmed") }
                items(confirmed) { permit ->
                    PermitCard(permit)
                }
            }

            if (waiting.isNotEmpty()) {
                item { CategoryHeader("LC in Waiting") }
                items(waiting) { permit ->
                    PermitCard(permit)
                }
            }

            if (rejected.isNotEmpty()) {
                item { CategoryHeader("LC Rejected") }
                items(rejected) { permit ->
                    PermitCard(permit)
                }
            }
        }
    }
}

@Composable
fun CategoryHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
    )
}

@Composable
fun UserProfileCard() { // ... (This is unchanged) ...
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "James Smith", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Line Clearance Requester")
        }
    }
}

@Composable
fun PermitCard(permit: LineClearancePermit) {
    var elapsedTime by remember { mutableStateOf("") }

    if (permit.status == "Confirmed") {
        LaunchedEffect(permit.timestamp) {
            while (true) {
                val diff = System.currentTimeMillis() - permit.timestamp
                val hours = diff / (1000 * 60 * 60)
                val minutes = (diff / (1000 * 60)) % 60
                elapsedTime = String.format("%02d:%02d", hours, minutes)
                delay(60000) // Update every minute
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() { // ... (This is unchanged) ...
    val samplePermit = LineClearancePermit(id=1, feederName = "Nani marad AG", substationName = "Patanvav", status = "Confirmed", lcNumber = "1234", requesterName = "user", workType = "Jumper work")
    LineClearanceM3Theme {
        DashboardScreen(listOf(samplePermit))
    }
}

@Composable
fun LineClearanceM3Theme(content: @Composable () -> Unit) { // ... (This is unchanged) ...
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFD81B60), 
            secondary = Color(0xFFF8BBD0), 
            tertiary = Color(0xFFA00037) 
        ),
        content = content
    )
}

