package com.example.lineclearance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lineclearance.databinding.ActivityPermitDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PermitDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermitDetailsBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermitDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getDatabase(this)

        // Get data from previous screen
        val feederName = intent.getStringExtra("feederName") ?: ""
        val substationName = intent.getStringExtra("substationName") ?: ""
        val userName = intent.getStringExtra("userName") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""

        // Populate Spinners
        setupWorkTypeSpinner()
        setupTimeSpinner()

        // Set requester name
        binding.requesterNameInput.setText(userName)

        binding.generateSmsButton.setOnClickListener {
            val workType = binding.workTypeSpinner.selectedItem.toString()
            val time = binding.timeSpinner.selectedItem.toString()
            val requester = binding.requesterNameInput.text.toString()

            // Build the dynamic message
            val message = "Please give LC of $feederName feeder for the work of $workType for approx $time from $requester"

            // Save to database
            lifecycleScope.launch(Dispatchers.IO) {
                val permit = LineClearancePermit(
                    feederName = feederName,
                    substationName = substationName,
                    status = "Requested"
                )
                db.lineClearancePermitDao().insert(permit)
            }

            // Create and launch SMS Intent
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phoneNumber")
                putExtra("sms_body", message)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No SMS app found.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupWorkTypeSpinner() {
        val workTypes = listOf("Jumper work", "HT conductor broken", "TC replacement", "Jumper work at DP for safety", "Maintenance work", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, workTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.workTypeSpinner.adapter = adapter
    }

    private fun setupTimeSpinner() {
        val times = (1..16).map { (it * 0.5).toString() + " Hours" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.timeSpinner.adapter = adapter
    }
}