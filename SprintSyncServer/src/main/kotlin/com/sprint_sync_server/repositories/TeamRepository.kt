package com.sprint_sync_server.repositories

import com.sprint_sync_server.domains.Team
import org.springframework.data.mongodb.repository.MongoRepository

interface TeamRepository : MongoRepository<Team, String>