package com.example.lineclearance

object FeederDataSource {

    fun getAllFeederNames(): List<String> {
        return listOf(
            "OSAM JGY", "CHICHOD JGY", "PATANVAV AG", "CHUDAVA AG", "TALNGANA AG",
            "MAJETHI AG", "NANI MARAD AG", "KALANA AG", "VELARIYA AG", "ONEX SOLAR",
            "RANDAL JGY", "CHIKHALIYA AG", "BHADA JALIYA AG", "VADODAR AG", "UDAKIYA AG",
            "CHHADVA VADAR AG", "BHOLA AG", "SUKHNATH AG", "MARUTI AG", "NAGALKHADA AG",
            "KUNDHECH JGY", "BHADAR AG", "LATHA AG", "TRIVENI AG", "SANGAM AG",
            "RAMDEV JGY", "KEDAR AG", "NAGNATH AG", "SANGANI AG", "VAGHESHWARI AG",
            "AMBALIYA AG", "MAHADEV AG",
            "KALESHWAR JGY", "HINGALAJ AG", "KARAAR AG", "VAGADIYA AG", "PATI AG",
            "SAVAJ AG", "MEGNETIK SOLAR",
            "URJA JGY", "SURAJ JGY", "RUDR AG",
            "NARAYAN AG", "CHHATRASA AG",
            "NILAKHA JGY", "DHARATI AG", "AKASH AG",
            "HADFODI AG"
        )
    }

    fun getAllFeedersForSubstation(substationName: String): List<Feeder> {
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