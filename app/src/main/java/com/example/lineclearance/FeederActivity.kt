package com.example.lineclearance

import android.content.Intent
import android.net.Uri
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

        binding.feederRecyclerview.layoutManager = LinearLayoutManager(this)

        val substationName = intent.getStringExtra("substationName") ?: ""

        val feeders = getFeedersForSubstation(substationName)

        val adapter = FeederAdapter(feeders) { feeder ->
            val message = "Line clearance permit request for ${feeder.name} in ${feeder.substationName}"
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:9227985451") // This ensures only SMS apps respond
                putExtra("sms_body", message)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        binding.feederRecyclerview.adapter = adapter
    }

    private fun getFeedersForSubstation(substationName: String): List<Feeder> {
        return when (substationName) {
            "BANTIYA" -> listOf(Feeder("CHHATRASA", substationName), Feeder("NARAYAN", substationName))
            "BHADER" -> listOf(Feeder("RAMDEV", substationName), Feeder("VAGHESHVARI", substationName), Feeder("MAHADEV", substationName), Feeder("SANGANI", substationName), Feeder("BHADER", substationName), Feeder("NAGNATH", substationName), Feeder("KEDAR", substationName))
            "MOTI MARAD" -> listOf(Feeder("VADODAR", substationName), Feeder("CHIKHALIA", substationName), Feeder("BHADAJALIYA", substationName), Feeder("RANDAL", substationName), Feeder("NAGALKHADA", substationName), Feeder("MARUTI", substationName))
            "PATANVAV" -> listOf(Feeder("KALANA", substationName), Feeder("VELARIA", substationName), Feeder("CHICHOD", substationName), Feeder("NANI MARAD", substationName), Feeder("TALANGANA", substationName), Feeder("PATANVAV", substationName), Feeder("CHUDVA ROAD", substationName), Feeder("OSAM", substationName), Feeder("MAJETHI", substationName), Feeder("PATANVAV SST AUX", substationName))
            "LATH" -> listOf(Feeder("LATH", substationName), Feeder("BHADAR", substationName), Feeder("KUNDHECH", substationName), Feeder("TRIVENI", substationName), Feeder("LATH AUX SST", substationName),Feeder("SANGAM", substationName))
            "UPLETA" -> listOf(Feeder("HADFODI", substationName), Feeder("AAKASH", substationName))
            "TANASAVA" -> listOf(Feeder("NILAKHA", substationName), Feeder("DHARTI", substationName))
            "KALANA" -> listOf(Feeder("KALESHWAR", substationName), Feeder("VAGADIYA", substationName), Feeder("PATI", substationName), Feeder("KARAR", substationName), Feeder("HINGLAJ", substationName), Feeder("KALANA SS SST", substationName), Feeder("SANGAM", substationName), Feeder("SAVAI", substationName))
            "MURAKHAI" -> listOf(Feeder("SURAJ", substationName), Feeder("URJA", substationName), Feeder("RUDRA", substationName))
            else -> emptyList()
        }
    }
}