package com.sprint_sync_server.repositories

import com.sprint_sync_server.domains.Comment
import org.springframework.data.mongodb.repository.MongoRepository

interface CommentRepository : MongoRepository<Comment, String>