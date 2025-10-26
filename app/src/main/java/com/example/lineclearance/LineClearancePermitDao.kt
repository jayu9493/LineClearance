package com.example.lineclearance

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LineClearancePermitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(permit: LineClearancePermit)

    @Query("SELECT * FROM line_clearance_permits ORDER BY timestamp DESC")
    fun getAllPermits(): LiveData<List<LineClearancePermit>>

    @Query("SELECT * FROM line_clearance_permits WHERE feederName = :feederName AND status = 'Waiting' LIMIT 1")
    fun getRequestedPermitForFeeder(feederName: String): LineClearancePermit?

    // Generic function to update status and other details
    @Query("UPDATE line_clearance_permits SET status = :status, lcNumber = :lcNumber, confirmationSmsBody = :smsBody WHERE id = :id")
    fun updatePermitStatus(id: Int, status: String, lcNumber: String?, smsBody: String?)

    // Specific function to find a permit to mark as returned
    @Query("SELECT * FROM line_clearance_permits WHERE lcNumber = :lcNumber AND status = 'Confirmed' LIMIT 1")
    fun getConfirmedPermitByLcNumber(lcNumber: String): LineClearancePermit?

    // Specific function to mark a permit as completed
    @Query("UPDATE line_clearance_permits SET status = 'Completed' WHERE id = :id")
    fun markAsCompleted(id: Int)
}