package com.example.lineclearance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32) // dashboard_green
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            UserProfileCard()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Live Permits",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(permits) { permit ->
                    PermitCard(permit)
                }
            }
        }
    }
}

@Composable
fun UserProfileCard() {
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = permit.feederName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = permit.substationName)
            }
            Column(horizontalAlignment = Alignment.End) {
                val statusColor = when (permit.status) {
                    "Live" -> Color(0xFF2E7D32) // dashboard_green
                    "Requested" -> Color.Blue
                    else -> Color.Gray
                }
                Text(text = permit.status, color = statusColor, fontWeight = FontWeight.Bold)
                Text(text = "LC #${permit.lcNumber ?: "N/A"}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val samplePermit = LineClearancePermit(id=1, feederName = "Nani marad AG", substationName = "Patanvav", status = "Live", lcNumber = "1234")
    LineClearanceM3Theme {
        DashboardScreen(listOf(samplePermit))
    }
}

@Composable
fun LineClearanceM3Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFD81B60), // pgvcl_primary
            secondary = Color(0xFFF8BBD0), // pgvcl_accent
            tertiary = Color(0xFFA00037) // pgvcl_primary_dark
        ),
        content = content
    )
}