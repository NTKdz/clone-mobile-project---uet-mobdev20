package com.sprint_sync_server.repositories

import com.sprint_sync_server.domains.Member
import org.springframework.data.mongodb.repository.MongoRepository

interface MemberRepository : MongoRepository<Member, String>