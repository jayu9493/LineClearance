package com.example.lineclearance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val role: String, // DE, JE, LI, etc.
    val phoneNumber: String,
    val username: String, // A generated username for login
    val password: String // A default or user-set password
)