package com.sprint_sync_server.repositories

import com.sprint_sync_server.domains.Attachment
import org.springframework.data.mongodb.repository.MongoRepository

interface AttachmentRepository : MongoRepository<Attachment, String>