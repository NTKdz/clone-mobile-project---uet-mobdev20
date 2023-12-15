package com.sprint_sync_server.repositories

import com.sprint_sync_server.domains.Project
import org.springframework.data.mongodb.repository.MongoRepository

interface ProjectRepository : MongoRepository<Project, String>