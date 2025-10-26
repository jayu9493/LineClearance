package com.example.lineclearance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lineclearance.databinding.ActivityFeederBinding

class FeederActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeederBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeederBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val substationName = intent.getStringExtra("substationName") ?: ""
        val userName = intent.getStringExtra("userName") ?: ""

        binding.feederRecyclerview.layoutManager = LinearLayoutManager(this)
        val feeders = FeederDataSource.getAllFeedersForSubstation(substationName) // Updated
        val phoneNumber = SubstationDataSource.getPhoneNumberForSubstation(substationName) // Updated

        val adapter = FeederAdapter(feeders) { feeder ->
            val intent = Intent(this, PermitDetailsActivity::class.java).apply {
                putExtra("feederName", feeder.name)
                putExtra("substationName", substationName)
                putExtra("userName", userName)
                putExtra("phoneNumber", phoneNumber)
            }
            startActivity(intent)
        }
        binding.feederRecyclerview.adapter = adapter
    }
}