package com.example.lineclearance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map

class PersonalDashboardViewModel(application: Application, userName: String) : AndroidViewModel(application) {

    private val db: AppDatabase = AppDatabase.getDatabase(application)

    val myPermits: LiveData<List<LineClearancePermit>> = db.lineClearancePermitDao().getAllPermits().map {
        it.filter { permit -> permit.requesterName == userName }
    }

    // Factory to create the ViewModel with parameters
    class Factory(private val application: Application, private val userName: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PersonalDashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PersonalDashboardViewModel(application, userName) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}