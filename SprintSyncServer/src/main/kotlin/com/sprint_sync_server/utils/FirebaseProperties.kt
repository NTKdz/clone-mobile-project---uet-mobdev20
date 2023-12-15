package com.sprint_sync_server.utils

import lombok.Data
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
data class FirebaseProperties(
    @Value("\${firebase.google.credentials}")
    val googleCredentials: String
)