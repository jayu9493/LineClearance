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
                if (messageBody.contains("feeder LC no")) {
                    // Launch a coroutine to do the database work
                    CoroutineScope(Dispatchers.IO).launch {
                        val db = AppDatabase.getDatabase(context)
                        // This is a simplified parsing logic. It can be improved.
                        val feederName = messageBody.substringBefore("feeder LC no").substringAfter("kV ").trim()
                        val lcNumber = messageBody.substringAfter("LC no.").substringBefore("Time").trim()

                        val requestedPermit = db.lineClearancePermitDao().getRequestedPermitForFeeder(feederName)
                        if (requestedPermit != null) {
                            db.lineClearancePermitDao().updatePermitStatus(requestedPermit.id, "Live", lcNumber)
                        }
                    }
                    Toast.makeText(context, "LC Permit Received: $messageBody", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}