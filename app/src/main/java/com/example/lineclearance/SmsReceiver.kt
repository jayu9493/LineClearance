package com.example.lineclearance

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SmsReceiver : BroadcastReceiver() {

    // This regex looks for "LC", then optionally "no", "number", or "num",
    // then optional punctuation, then captures the digits that follow.
    private val lcNumberRegex: Pattern = Pattern.compile("LC\\s*(?:no|number|num)?[.:]?\\s*(\\d+)", Pattern.CASE_INSENSITIVE)

    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (sms in messages) {
                val messageBody = sms.messageBody

                if (messageBody.contains("Returned", ignoreCase = true)) {
                    processReturnConfirmation(context, messageBody)
                } else if (messageBody.contains("LC", ignoreCase = true)) {
                    processNewLcConfirmation(context, messageBody)
                }
            }
        }
    }

    private fun processNewLcConfirmation(context: Context, messageBody: String) {
        val lcNumberMatcher = lcNumberRegex.matcher(messageBody)
        if (!lcNumberMatcher.find()) return // Exit if no LC number pattern is found
        
        val lcNumber = lcNumberMatcher.group(1) ?: ""
        var foundFeederName: String? = null

        for (feederName in FeederDataSource.getAllFeederNames()) {
            if (messageBody.contains(feederName, ignoreCase = true)) {
                foundFeederName = feederName
                break
            }
        }

        if (foundFeederName != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(context)
                val requestedPermit = db.lineClearancePermitDao().getRequestedPermitForFeeder(foundFeederName)

                if (requestedPermit != null) {
                    db.lineClearancePermitDao().updatePermitStatus(requestedPermit.id, "Confirmed", lcNumber, messageBody)
                }
            }
            Toast.makeText(context, "LC Permit Confirmed for $foundFeederName", Toast.LENGTH_LONG).show()
        }
    }

    private fun processReturnConfirmation(context: Context, messageBody: String) {
        val lcNumberMatcher = lcNumberRegex.matcher(messageBody)
        if (!lcNumberMatcher.find()) return // Exit if no LC number pattern is found

        val lcNumber = lcNumberMatcher.group(1) ?: ""

        if (lcNumber.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(context)
                val confirmedPermit = db.lineClearancePermitDao().getConfirmedPermitByLcNumber(lcNumber)

                if (confirmedPermit != null) {
                    db.lineClearancePermitDao().markAsCompleted(confirmedPermit.id)
                }
            }
            Toast.makeText(context, "LC #$lcNumber has been returned.", Toast.LENGTH_LONG).show()
        }
    }
}