package com.example.lineclearance

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PermitDetailsActivity : ComponentActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(this)

        val feederName = intent.getStringExtra("feederName") ?: ""
        val substationName = intent.getStringExtra("substationName") ?: ""
        val userName = intent.getStringExtra("userName") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""

        setContent {
            LineClearanceM3Theme {
                PermitDetailsScreen(
                    feederName = feederName,
                    substationName = substationName,
                    userName = userName,
                    phoneNumber = phoneNumber,
                    onGenerateSms = { permit, message ->
                        saveAndSend(permit, message, phoneNumber)
                    }
                )
            }
        }
    }

    private fun saveAndSend(permit: LineClearancePermit, message: String, phoneNumber: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.lineClearancePermitDao().insert(permit)
        }
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber")
            putExtra("sms_body", message)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No SMS app found.", Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermitDetailsScreen(
    feederName: String,
    substationName: String,
    userName: String,
    phoneNumber: String,
    onGenerateSms: (LineClearancePermit, String) -> Unit
) {
    var workType by remember { mutableStateOf("Jumper work") }
    val workTypes = listOf("Jumper work", "HT conductor broken", "TC replacement", "Jumper work at DP for safety", "Maintenance work", "Other")
    var time by remember { mutableStateOf("0.5 Hours") }
    val times = (1..16).map { (it * 0.5).toString() + " Hours" }
    var requesterName by remember { mutableStateOf(userName) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                showConfirmDialog = true
            } else {
                Toast.makeText(context, "Location permission is required to log permit.", Toast.LENGTH_LONG).show()
            }
        }
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Permit Details") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Dropdown(label = "Type of Work", options = workTypes, selectedOption = workType, onOptionSelected = { workType = it })
            Dropdown(label = "Approximate Time", options = times, selectedOption = time, onOptionSelected = { time = it })
            OutlinedTextField(value = requesterName, onValueChange = { requesterName = it }, label = { Text("Requester Name") }, modifier = Modifier.fillMaxWidth())

            Button(onClick = {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                        showConfirmDialog = true
                    }
                    else -> {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Generate SMS")
            }
        }

        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Confirm Permit Request") },
                text = { Text("This will log a 'Waiting' permit with your current location and open your SMS app. Proceed?") },
                confirmButton = {
                    Button(
                        onClick = {
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                if (location != null) {
                                    val message = "Please give LC of $feederName feeder for the work of $workType for approx $time from $requesterName"
                                    val permit = LineClearancePermit(
                                        feederName = feederName,
                                        substationName = substationName,
                                        status = "Waiting",
                                        requesterName = requesterName,
                                        workType = workType,
                                        approxTime = time,
                                        timestamp = System.currentTimeMillis(),
                                        latitude = location.latitude,
                                        longitude = location.longitude
                                    )
                                    onGenerateSms(permit, message)
                                } else {
                                    Toast.makeText(context, "Could not get location. Please ensure location is enabled.", Toast.LENGTH_LONG).show()
                                }
                            }
                            showConfirmDialog = false
                        })
                    {
                        Text("Proceed")
                    }
                },
                dismissButton = {
                    Button(onClick = { showConfirmDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth()) {
            options.forEach {
                DropdownMenuItem(text = { Text(it) }, onClick = {
                    onOptionSelected(it)
                    expanded = false
                })
            }
        }
    }
}
