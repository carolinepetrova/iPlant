package com.project.iplant;

import com.project.iplant.scheduler_service.Scheduler;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SchedulerTest {
    @Test
    void testPayloadCreator() {
        Scheduler scheduler = new Scheduler();
        scheduler.setDate("2020-01-01");
        List<String> plantIDs = new ArrayList<>();
        plantIDs.add("test-id1");
        plantIDs.add("test-id2");
        scheduler.setPlantIDs(plantIDs);
        String test = "{\"date\":\"2020-01-01\",\"plantIDs\":[\"test-id1\",\"test-id2\"]}";
        assertEquals(test,scheduler.toJsonObject());
    }
}
