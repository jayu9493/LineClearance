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

    @Query("SELECT * FROM line_clearance_permits WHERE feederName = :feederName AND status = 'Requested' LIMIT 1")
    fun getRequestedPermitForFeeder(feederName: String): LineClearancePermit?

    @Query("UPDATE line_clearance_permits SET status = :status, lcNumber = :lcNumber WHERE id = :id")
    fun updatePermitStatus(id: Int, status: String, lcNumber: String?)
}