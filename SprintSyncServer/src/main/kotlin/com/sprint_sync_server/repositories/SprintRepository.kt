package com.sprint_sync_server.repositories

import com.sprint_sync_server.domains.Sprint
import org.springframework.data.mongodb.repository.MongoRepository

interface SprintRepository : MongoRepository<Sprint, String>