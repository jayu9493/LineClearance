package com.example.lineclearance

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(this)

        setContent {
            LineClearanceM3Theme {
                LoginScreen { mobileNumber, password ->
                    lifecycleScope.launch {
                        val user = withContext(Dispatchers.IO) {
                            db.userDao().login(mobileNumber.trim(), password.trim())
                        }
                        
                        if (user != null) {
                            Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity, PersonalDashboardActivity::class.java)
                            intent.putExtra("userName", user.name)
                            intent.putExtra("userRole", user.role)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@MainActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginClicked: (String, String) -> Unit) {
    var mobileNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Launcher for SMS permission request
    val smsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                Toast.makeText(context, "SMS permission is required for the app to function correctly.", Toast.LENGTH_LONG).show()
            }
        }
    )

    // Request permission when the screen is first displayed
    LaunchedEffect(key1 = true) {
        smsPermissionLauncher.launch(Manifest.permission.RECEIVE_SMS)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            label = { Text("Mobile Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onLoginClicked(mobileNumber, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}
