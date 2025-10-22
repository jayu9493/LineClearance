package com.example.lineclearance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lineclearance.databinding.ActivityFeederBinding

class FeederActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeederBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeederBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.feederRecyclerview.layoutManager = LinearLayoutManager(this)

        val substationName = intent.getStringExtra("substationName") ?: ""
        val feeders = getFeedersForSubstation(substationName)
        val phoneNumber = getPhoneNumberForSubstation(substationName)

        val adapter = FeederAdapter(feeders) { feeder ->
            val message = "Line clearance permit request for ${feeder.name} in ${feeder.substationName}"
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phoneNumber") // Use the specific phone number
                putExtra("sms_body", message)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No SMS app found.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.feederRecyclerview.adapter = adapter
    }

    private fun getPhoneNumberForSubstation(substationName: String): String {
        return when (substationName) {
            "PATANVAV" -> "9909975777"
            "MOTIMAR" -> "9909975772"
            "LATH" -> "9081600388"
            "BHADER" -> "9081600377"
            "KALANA" -> "8160402913"
            "MURAKHAI" -> "9327119856"
            // Substations without provided numbers
            "BANTIYA" -> "1111111111" // Placeholder
            "UPLETA" -> "6666666666" // Placeholder
            "TANASAVA" -> "7777777777" // Placeholder
            else -> "" // Default number or empty
        }
    }

    private fun getFeedersForSubstation(substationName: String): List<Feeder> {
        return when (substationName) {
            "BANTIYA" -> listOf(Feeder("CHHATRAS", substationName), Feeder("NARAYAN", substationName))
            "BHADER" -> listOf(Feeder("RAMDEV", substationName), Feeder("VAGHESHVARI", substationName), Feeder("MAHADEV", substationName), Feeder("SANGANI", substationName), Feeder("BHADER", substationName), Feeder("NAGNATH", substationName), Feeder("KEDAR", substationName))
            "MOTIMAR" -> listOf(Feeder("VADODAR", substationName), Feeder("CHIKHALIA", substationName), Feeder("BHADAJALI", substationName), Feeder("RANDAL", substationName), Feeder("NAGALKHA", substationName), Feeder("MARUTI", substationName))
            "PATANVAV" -> listOf(Feeder("KALANA", substationName), Feeder("VELARIA", substationName), Feeder("CHICHOD", substationName), Feeder("NANI MAR", substationName), Feeder("TALANGAN", substationName), Feeder("PATANVAV", substationName), Feeder("CHUDVA RI", substationName), Feeder("OSAM", substationName), Feeder("MAJETHI", substationName), Feeder("PATANVAV SST", substationName))
            "LATH" -> listOf(Feeder("LATH", substationName), Feeder("BHADER", substationName), Feeder("KUNDHECH", substationName), Feeder("TRIVENI", substationName), Feeder("LATH AUX SST", substationName))
            "UPLETA" -> listOf(Feeder("HADFODI", substationName), Feeder("AAKASH", substationName))
            "TANASAVA" -> listOf(Feeder("NILAKHA", substationName), Feeder("DHARTI", substationName))
            "KALANA" -> listOf(Feeder("KALESHWA", substationName), Feeder("VAGADIYA", substationName), Feeder("PATI", substationName), Feeder("KARAR", substationName), Feeder("HINGLAJ", substationName), Feeder("KALANA SS SST", substationName), Feeder("SANGAM", substationName), Feeder("SAVAI", substationName))
            "MURAKHAI" -> listOf(Feeder("SURAJ", substationName), Feeder("URJA", substationName), Feeder("RUDRA", substationName))
            else -> emptyList()
        }
    }
}