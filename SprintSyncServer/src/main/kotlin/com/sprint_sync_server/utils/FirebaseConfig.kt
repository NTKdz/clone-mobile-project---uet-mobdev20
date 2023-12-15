package com.sprint_sync_server.utils

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
@RequiredArgsConstructor
class FirebaseConfig(private val firebaseProperties: FirebaseProperties) {
	@Bean
	fun firebaseMessaging(): FirebaseMessaging {
		val googleCredentials = GoogleCredentials.fromStream(FileInputStream(firebaseProperties.googleCredentials))
		val firebaseOptions = FirebaseOptions.builder().setCredentials(googleCredentials).build()
		val app = FirebaseApp.initializeApp(firebaseOptions, "sprint-sync-server")
		return FirebaseMessaging.getInstance(app)
	}
}