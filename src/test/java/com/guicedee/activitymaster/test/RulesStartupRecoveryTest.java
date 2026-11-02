package com.guicedee.activitymaster.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.TypeLiteral;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IRulesService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.systems.RulesSystem;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.scopes.CallScoper;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RulesStartupRecoveryTest {
    private final Mutiny.StatelessSession session = mock(Mutiny.StatelessSession.class);
    private final IEnterprise<?, ?> enterprise = mock(IEnterprise.class);
    private final ISystems<?, ?> system = mock(ISystems.class);
    private final ISystemsService<?> systems = mock(ISystemsService.class);
    private final RulesSystem startup = spy(new RulesSystem());
    private MockedStatic<IGuiceContext> context;

    @BeforeEach void setup() {
        context = mockStatic(IGuiceContext.class);
        context.when(() -> IGuiceContext.get(CallScoper.class)).thenReturn(mock(CallScoper.class));
        Guice.createInjector(new AbstractModule() {
            @Override protected void configure() {
                bind(new TypeLiteral<ISystemsService<?>>() {}).toInstance(systems);
                bind(new TypeLiteral<IEnterpriseService<?>>() {}).toInstance(mock(IEnterpriseService.class));
                bind(new TypeLiteral<IClassificationService<?>>() {}).toInstance(mock(IClassificationService.class));
                bind(Mutiny.SessionFactory.class).toInstance(mock(Mutiny.SessionFactory.class));
            }
        }).injectMembers(startup);
        when(enterprise.getName()).thenReturn("RulesRecoveryFixture");
        when(system.getName()).thenReturn(IRulesService.RulesSystemName);
        doReturn(Uni.createFrom().item(system)).when(systems).findSystem(session, enterprise, IRulesService.RulesSystemName);
        doReturn(Uni.createFrom().item(UUID.randomUUID())).when(systems).getSecurityIdentityToken(session, system);
        doReturn(Uni.createFrom().item(system)).when(startup).registerSystem(session, enterprise);
    }

    @AfterEach void closeContext() { context.close(); }
    private void start() { startup.postStartup(session, enterprise).await().atMost(Duration.ofSeconds(2)); }

    @Test void existingSystemDoesNotRegister() {
        start();
        verify(startup, never()).registerSystem(any(), any());
        verify(systems).getSecurityIdentityToken(session, system);
    }

    @Test void missingSystemIsRegisteredReadBackAndVerified() {
        doReturn(Uni.createFrom().failure(new NoResultException()), Uni.createFrom().item(system))
                .when(systems).findSystem(session, enterprise, IRulesService.RulesSystemName);
        start();
        var order = inOrder(startup, systems);
        order.verify(startup).registerSystem(session, enterprise);
        order.verify(systems).findSystem(session, enterprise, IRulesService.RulesSystemName);
        order.verify(systems).getSecurityIdentityToken(session, system);
    }

    @Test void databaseFailureDoesNotRegister() {
        var failure = new IllegalStateException("database unavailable");
        doReturn(Uni.createFrom().failure(failure)).when(systems).findSystem(session, enterprise, IRulesService.RulesSystemName);
        assertSame(failure, assertThrows(IllegalStateException.class, this::start));
        verify(startup, never()).registerSystem(any(), any());
    }

    @Test void failedRepairOrMissingIdentityStillFails() {
        doReturn(Uni.createFrom().failure(new NoResultException())).when(systems).findSystem(session, enterprise, IRulesService.RulesSystemName);
        var failure = new IllegalStateException("registration failed");
        doReturn(Uni.createFrom().failure(failure)).when(startup).registerSystem(session, enterprise);
        assertSame(failure, assertThrows(IllegalStateException.class, this::start));
        verify(systems, never()).getSecurityIdentityToken(any(), any());
    }

    @Test void missingIdentityDoesNotCreateAnotherSystem() {
        doReturn(Uni.createFrom().failure(new NoResultException("missing identity"))).when(systems).getSecurityIdentityToken(session, system);
        assertThrows(NoResultException.class, this::start);
        verify(startup, never()).registerSystem(any(), any());
    }
}
