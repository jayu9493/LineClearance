package com.example.lineclearance

object SubstationDataSource {
    fun getPhoneNumberForSubstation(substationName: String): String {
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
            else -> "" // Return empty if not found
        }
    }
}