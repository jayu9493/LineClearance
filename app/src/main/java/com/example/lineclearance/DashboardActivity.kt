package com.example.lineclearance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lineclearance.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getDatabase(this)

        val adapter = DashboardAdapter()
        binding.dashboardRecyclerview.adapter = adapter
        binding.dashboardRecyclerview.layoutManager = LinearLayoutManager(this)

        db.lineClearancePermitDao().getAllPermits().observe(this, Observer {
            it?.let { adapter.submitList(it) }
        })
    }
}