package com.guicedee.activitymaster.tests;

import com.guicedee.activitymaster.fsdm.rest.arrangement.ArrangementRestService;

import com.guicedee.activitymaster.fsdm.client.services.IArrangementsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.UUID;

import static org.mockito.Mockito.*;

class ArrangementTypeUpdateTest {
    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    void resolvesTheTypeAndKeepsItsRelationshipClassification() throws Exception {
        var endpoint = new ArrangementRestService();
        var service = mock(IArrangementsService.class);
        var field = ArrangementRestService.class.getDeclaredField("arrangementsService");
        field.setAccessible(true);
        field.set(endpoint, service);
        var session = mock(Mutiny.StatelessSession.class);
        var system = mock(ISystems.class);
        var arrangement = mock(IArrangement.class);
        var type = mock(IArrangementType.class);
        var token = UUID.randomUUID();
        when(service.findArrangementType(session, "PackingSession", system, token))
                .thenReturn(Uni.createFrom().item(type));
        when(arrangement.addOrUpdateArrangementType(session, "NoClassification", type, null,
                "Evening session", system, token)).thenReturn(Uni.createFrom().nullItem());

        var method = ArrangementRestService.class.getDeclaredMethod("updateArrangementTypeValue",
                Mutiny.StatelessSession.class, IArrangement.class, String.class, String.class, ISystems.class, UUID[].class);
        method.setAccessible(true);
        ((Uni<?>) method.invoke(endpoint, session, arrangement, "PackingSession", "Evening session", system,
                new UUID[]{token})).await().atMost(Duration.ofSeconds(5));

        verify(arrangement).addOrUpdateArrangementType(session, "NoClassification", type, null,
                "Evening session", system, token);
        verifyNoMoreInteractions(arrangement);
    }
}
