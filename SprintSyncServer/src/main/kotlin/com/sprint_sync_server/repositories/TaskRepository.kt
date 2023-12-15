package com.sprint_sync_server.repositories

import com.sprint_sync_server.domains.Task
import org.springframework.data.mongodb.repository.MongoRepository

interface TaskRepository : MongoRepository<Task, String>