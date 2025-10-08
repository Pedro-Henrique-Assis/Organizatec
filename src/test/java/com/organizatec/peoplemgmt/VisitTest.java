package com.organizatec.peoplemgmt;

import com.organizatec.peoplemgmt.domain.Visit;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class VisitTest {

    @Test
    void testSetEntryAndExitTime() {
        Visit visit = new Visit();
        LocalDateTime now = LocalDateTime.now();
        visit.setEntryTime(now);
        visit.setExitTime(now.plusHours(1));

        assertNotNull(visit.getEntryTime());
        assertNotNull(visit.getExitTime());
        assertTrue(visit.getExitTime().isAfter(visit.getEntryTime()));
    }

    @Test
    void testBadgeCodeFormat() {
        Visit visit = new Visit();
        visit.setBadgeCode("V-ABC123");
        assertTrue(visit.getBadgeCode().startsWith("V-"));
    }
}