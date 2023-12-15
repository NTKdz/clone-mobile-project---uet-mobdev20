package com.sprint_sync_server.services

import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.BatchResponse
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.MulticastMessage
import com.sprint_sync_server.domains.Notice
import org.springframework.stereotype.Service

@Service
class NotificationService(private val firebaseMessaging: FirebaseMessaging) {
	fun sendNotification(notice: Notice): BatchResponse? {
		val multicastMessage = MulticastMessage.builder()
			.setAndroidConfig(AndroidConfig.builder().setPriority(AndroidConfig.Priority.HIGH).build())
			.addAllTokens(notice.tokens)
			.putAllData(notice.body)
			.build()

		var batchResponse: BatchResponse? = null
		try {
			batchResponse = firebaseMessaging.sendEachForMulticast(multicastMessage)
		}
		catch (e: FirebaseMessagingException) {
			println("Error sending message: ${e.message}")
		}

		return batchResponse
	}
}