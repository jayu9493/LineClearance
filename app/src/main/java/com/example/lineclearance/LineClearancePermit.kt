package com.example.lineclearance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "line_clearance_permits")
data class LineClearancePermit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val feederName: String,
    val substationName: String,
    var status: String, // e.g., "Requested", "Live", "Completed"
    var lcNumber: String? = null,
    var timestamp: Long = System.currentTimeMillis()
)