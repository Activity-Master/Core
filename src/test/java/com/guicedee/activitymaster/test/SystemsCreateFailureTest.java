package com.guicedee.activitymaster.test;

import com.guicedee.activitymaster.fsdm.SystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SystemsCreateFailureTest {
    @Test void databaseFailureIsNotTreatedAsMissingSystem() {
        var service = spy(new SystemsService());
        var session = mock(Mutiny.StatelessSession.class);
        var enterprise = mock(IEnterprise.class);
        var failure = new IllegalStateException("database unavailable");
        doReturn(Uni.createFrom().failure(failure)).when(service).findSystem(session, enterprise, "Rules System");
        assertSame(failure, assertThrows(IllegalStateException.class, () -> service
                .create(session, enterprise, "Rules System", "Rules")
                .await().atMost(Duration.ofSeconds(2))));
    }
}
