package com.example.lineclearance

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Telephony
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SmsReceiver : BroadcastReceiver() {

    private val lcNumberRegex: Pattern = Pattern.compile("LC\\s*(?:no|number|num)?[.:]?\\s*(\\d+)", Pattern.CASE_INSENSITIVE)

    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION != intent.action) return

        val pendingResult: PendingResult = goAsync()
        val coroutineScope = CoroutineScope(Dispatchers.IO)

        coroutineScope.launch {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (sms in messages) {
                val messageBody = sms.messageBody
                
                if (messageBody.contains("Returned", ignoreCase = true)) {
                    processReturnConfirmation(context, messageBody)
                } else if (messageBody.contains("LC", ignoreCase = true)) {
                    processNewLcConfirmation(context, messageBody)
                }
            }
            pendingResult.finish()
        }
    }

    private fun processNewLcConfirmation(context: Context, messageBody: String) {
        val lcNumberMatcher = lcNumberRegex.matcher(messageBody)
        if (!lcNumberMatcher.find()) return
        
        val lcNumber = lcNumberMatcher.group(1) ?: ""
        var foundFeederName: String? = null

        for (feederName in FeederDataSource.getAllFeederNames()) {
            if (messageBody.contains(feederName, ignoreCase = true)) {
                foundFeederName = feederName
                break
            }
        }

        if (foundFeederName != null) {
            val db = AppDatabase.getDatabase(context)
            val requestedPermit = db.lineClearancePermitDao().getRequestedPermitForFeeder(foundFeederName)

            if (requestedPermit != null) {
                db.lineClearancePermitDao().updatePermitStatus(requestedPermit.id, "Confirmed", lcNumber, messageBody)
            } else {
                launchToast(context, "Confirmed LC for $foundFeederName, but no 'Waiting' permit was found.")
            }
        } else {
            launchToast(context, "LC message received, but feeder name not recognized.")
        }
    }

    private fun processReturnConfirmation(context: Context, messageBody: String) {
        val lcNumberMatcher = lcNumberRegex.matcher(messageBody)
        if (!lcNumberMatcher.find()) return

        val lcNumber = lcNumberMatcher.group(1) ?: ""

        if (lcNumber.isNotEmpty()) {
            val db = AppDatabase.getDatabase(context)
            val confirmedPermit = db.lineClearancePermitDao().getConfirmedPermitByLcNumber(lcNumber)

            if (confirmedPermit != null) {
                db.lineClearancePermitDao().markAsCompleted(confirmedPermit.id)
            }
        }
    }

    private fun launchToast(context: Context, message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}