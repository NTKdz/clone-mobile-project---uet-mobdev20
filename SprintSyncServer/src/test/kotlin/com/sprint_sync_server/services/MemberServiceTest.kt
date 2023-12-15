package com.sprint_sync_server.services

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class MemberServiceTest {
    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun getAllMembersTest() {
        mvc.get("/members").andExpect {
            status { isOk() }
            content { contentType("application/json") }
            // check that the response contains at least one member
            jsonPath("$[0]") { exists() }
        }
    }
}