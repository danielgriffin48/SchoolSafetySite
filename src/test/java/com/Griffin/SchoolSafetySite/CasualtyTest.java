package com.Griffin.SchoolSafetySite;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CasualtyTest {

    private School testSchool;
    @BeforeAll
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCasualtyOutputString() {
    }
}