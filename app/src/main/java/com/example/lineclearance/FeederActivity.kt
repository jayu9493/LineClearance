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
        val feeders = getFeedersForSubstation(substationName)
        val phoneNumber = getPhoneNumberForSubstation(substationName)

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

    private fun getPhoneNumberForSubstation(substationName: String): String { 
        return when (substationName) {
            "PATANAVAV S/S" -> "+919909975777"
            "MOTI MARAD S/S" -> "+919909975772"
            "LATH S/S" -> "+919081600388"
            "BHADER S/S" -> "+919081600377"
            "KALANA S/S" -> "+918160402913"
            "MURAKHADA S/S" -> "+919327119856"
            "BANTIYA S/S" -> "+919328620179"
            "TANSAVA S/S" -> "+919909975775"
            "UPALETA S/S" -> "+919909975774"
            else -> ""
        }
    }

    private fun getFeedersForSubstation(substationName: String): List<Feeder> {
        return when (substationName) {
            "PATANAVAV S/S" -> listOf(
                Feeder("OSAM JGY", substationName),
                Feeder("CHICHOD JGY", substationName),
                Feeder("PATANVAV AG", substationName),
                Feeder("CHUDAVA AG", substationName),
                Feeder("TALNGANA AG", substationName),
                Feeder("MAJETHI AG", substationName),
                Feeder("NANI MARAD AG", substationName),
                Feeder("KALANA AG", substationName),
                Feeder("VELARIYA AG", substationName),
                Feeder("ONEX SOLAR", substationName)
            )
            "MOTI MARAD S/S" -> listOf(
                Feeder("RANDAL JGY", substationName),
                Feeder("CHIKHALIYA AG", substationName),
                Feeder("BHADA JALIYA AG", substationName),
                Feeder("VADODAR AG", substationName),
                Feeder("UDAKIYA AG", substationName),
                Feeder("CHHADVA VADAR AG", substationName),
                Feeder("BHOLA AG", substationName),
                Feeder("SUKHNATH AG", substationName),
                Feeder("MARUTI AG", substationName),
                Feeder("NAGALKHADA AG", substationName)
            )
            "LATH S/S" -> listOf(
                Feeder("KUNDHECH JGY", substationName),
                Feeder("BHADAR AG", substationName),
                Feeder("LATHA AG", substationName),
                Feeder("TRIVENI AG", substationName),
                Feeder("SANGAM AG", substationName)
            )
            "BHADER S/S" -> listOf(
                Feeder("RAMDEV JGY", substationName),
                Feeder("KEDAR AG", substationName),
                Feeder("NAGNATH AG", substationName),
                Feeder("SANGANI AG", substationName),
                Feeder("VAGHESHWARI AG", substationName),
                Feeder("AMBALIYA AG", substationName),
                Feeder("MAHADEV AG", substationName)
            )
            "KALANA S/S" -> listOf(
                Feeder("KALESHWAR JGY", substationName),
                Feeder("HINGALAJ AG", substationName),
                Feeder("KARAAR AG", substationName),
                Feeder("VAGADIYA AG", substationName),
                Feeder("PATI AG", substationName),
                Feeder("SAVAJ AG", substationName),
                Feeder("MEGNETIK SOLAR", substationName)
            )
            "MURAKHADA S/S" -> listOf(
                Feeder("URJA JGY", substationName),
                Feeder("SURAJ JGY", substationName),
                Feeder("RUDR AG", substationName)
            )
            "BANTIYA S/S" -> listOf(
                Feeder("NARAYAN AG", substationName),
                Feeder("CHHATRASA AG", substationName)
            )
            "TANSAVA S/S" -> listOf(
                Feeder("NILAKHA JGY", substationName),
                Feeder("DHARATI AG", substationName),
                Feeder("AKASH AG", substationName)
            )
            "UPALETA S/S" -> listOf(
                Feeder("HADFODI AG", substationName)
            )
            else -> emptyList()
        }
    }
}