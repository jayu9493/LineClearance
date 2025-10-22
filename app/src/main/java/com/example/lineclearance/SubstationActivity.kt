package com.example.lineclearance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lineclearance.databinding.ActivitySubstationBinding

class SubstationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubstationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubstationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.substationRecyclerview.layoutManager = LinearLayoutManager(this)

        val substations = listOf(
            Substation("BANTIYA"),
            Substation("BHADER"),
            Substation("MOTIMAR"),
            Substation("PATANVAV"),
            Substation("LATH"),
            Substation("UPLETA"),
            Substation("TANASAVA"),
            Substation("KALANA"),
            Substation("MURAKHADA")
        )

        val adapter = SubstationAdapter(substations) { substation ->
            val intent = Intent(this, FeederActivity::class.java)
            intent.putExtra("substationName", substation.name)
            startActivity(intent)
        }
        binding.substationRecyclerview.adapter = adapter
    }
}