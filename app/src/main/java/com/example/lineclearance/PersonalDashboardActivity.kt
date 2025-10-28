package com.example.lineclearance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider

class PersonalDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userName = intent.getStringExtra("userName") ?: "Unknown User"
        val userRole = intent.getStringExtra("userRole") ?: "Unknown Role"

        val viewModelFactory = PersonalDashboardViewModel.Factory(application, userName)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(PersonalDashboardViewModel::class.java)

        setContent {
            LineClearanceM3Theme {
                val myPermits by viewModel.myPermits.observeAsState(initial = emptyList())
                PersonalDashboardScreen(
                    myPermits = myPermits,
                    userName = userName,
                    userRole = userRole
                )
            }
        }
    }
}