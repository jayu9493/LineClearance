package com.example.lineclearance

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (sms in messages) {
                val messageBody = sms.messageBody
                if (messageBody.contains("feeder LC no", ignoreCase = true)) {
                    findAndProcessPermit(context, messageBody)
                }
            }
        }
    }

    private fun findAndProcessPermit(context: Context, messageBody: String) {
        val allFeederNames = FeederDataSource.getAllFeederNames()
        var foundFeederName: String? = null

        for (feederName in allFeederNames) {
            if (messageBody.contains(feederName, ignoreCase = true)) {
                foundFeederName = feederName
                break
            }
        }

        if (foundFeederName != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(context)
                val lcNumber = messageBody.substringAfter("LC no.").substringBefore("Time").trim()

                val requestedPermit = db.lineClearancePermitDao().getRequestedPermitForFeeder(foundFeederName)

                if (requestedPermit != null) {
                    db.lineClearancePermitDao().updatePermitStatus(requestedPermit.id, "Confirmed", lcNumber)
                    // We can now show a more specific toast on the main thread if needed
                } else {
                    // Optional: Handle case where confirmation is received but no matching permit is found
                }
            }
            Toast.makeText(context, "LC Permit Confirmed for $foundFeederName", Toast.LENGTH_LONG).show()
        } else {
            // Optional: Handle case where SMS is an LC message but feeder name is not recognized
        }
    }
}