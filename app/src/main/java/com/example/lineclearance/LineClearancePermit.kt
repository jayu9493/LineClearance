package com.example.lineclearance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "line_clearance_permits")
data class LineClearancePermit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val feederName: String,
    val substationName: String,
    var status: String, // e.g., "Requested", "Confirmed", "Rejected", "Waiting"
    var lcNumber: String? = null,
    var timestamp: Long = System.currentTimeMillis(),
    var requesterName: String? = null,
    var workType: String? = null,
    var approxTime: String? = null,
    var confirmationSmsBody: String? = null,
    var latitude: Double? = null, // New field for location
    var longitude: Double? = null // New field for location
)