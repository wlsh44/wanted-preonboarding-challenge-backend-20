package com.example.wantedmarket.common;


import com.example.wantedmarket.common.db.DatabaseCleanerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@RecordApplicationEvents
@SpringBootTest
@ExtendWith(DatabaseCleanerExtension.class)
public abstract class IntegrationTest {

    @Autowired
    protected ApplicationEvents events;
}
