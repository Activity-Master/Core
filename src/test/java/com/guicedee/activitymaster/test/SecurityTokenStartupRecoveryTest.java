package com.guicedee.activitymaster.test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.TypeLiteral;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.systems.SecurityTokenSystem;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.scopes.CallScoper;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityTokenStartupRecoveryTest {
    private final Mutiny.StatelessSession session = mock(Mutiny.StatelessSession.class);
    private final IEnterprise<?, ?> enterprise = mock(IEnterprise.class);
    private final ISystems<?, ?> system = mock(ISystems.class);
    private final ISystemsService<?> systems = mock(ISystemsService.class);
    private final SecurityTokenSystem startup = spy(new SecurityTokenSystem());
    private MockedStatic<IGuiceContext> context;

    @BeforeEach
    void setup() {
        // Prevent the globally registered Mutiny interceptor from bootstrapping a database runtime.
        context = mockStatic(IGuiceContext.class);
        context.when(() -> IGuiceContext.get(CallScoper.class)).thenReturn(mock(CallScoper.class));
        Guice.createInjector(new AbstractModule() {
            @Override protected void configure() {
                bind(new TypeLiteral<ISystemsService<?>>() {}).toInstance(systems);
                bind(new TypeLiteral<IEnterpriseService<?>>() {}).toInstance(mock(IEnterpriseService.class));
                bind(new TypeLiteral<IClassificationService<?>>() {}).toInstance(mock(IClassificationService.class));
                bind(new TypeLiteral<ISecurityTokenService<?>>() {}).toInstance(mock(ISecurityTokenService.class));
                bind(Mutiny.SessionFactory.class).toInstance(mock(Mutiny.SessionFactory.class));
            }
        }).injectMembers(startup);
        when(enterprise.getName()).thenReturn("StartupRecoveryFixture");
        when(system.getName()).thenReturn(ISecurityTokenService.SecurityTokenSystemName);
        doReturn(Uni.createFrom().item(system)).when(systems).findSystem(session, enterprise, ISecurityTokenService.SecurityTokenSystemName);
        doReturn(Uni.createFrom().item(UUID.randomUUID())).when(systems).getSecurityIdentityToken(session, system);
        doReturn(Uni.createFrom().item(system)).when(startup).registerSystem(session, enterprise);
    }

    @AfterEach void closeContext() {
        context.close();
    }

    private void start() {
        startup.postStartup(session, enterprise).await().atMost(Duration.ofSeconds(2));
    }

    @Test void healthyStartupDoesNotRepair() {
        start();
        verify(startup, never()).registerSystem(any(), any());
        verify(systems).getSecurityIdentityToken(session, system);
    }

    @Test void missingSystemIsRegisteredAndIdentityVerified() {
        doReturn(Uni.createFrom().failure(new NoResultException("missing system")))
                .when(systems).findSystem(session, enterprise, ISecurityTokenService.SecurityTokenSystemName);
        start();
        var order = inOrder(startup, systems);
        order.verify(startup).registerSystem(session, enterprise);
        order.verify(systems).getSecurityIdentityToken(session, system);
    }

    @Test void missingIdentityIsRepairedAndReadBack() {
        doReturn(Uni.createFrom().failure(new NoResultException("missing identity")), Uni.createFrom().item(UUID.randomUUID()))
                .when(systems).getSecurityIdentityToken(session, system);
        start();
        verify(startup).registerSystem(session, enterprise);
        verify(systems, times(2)).getSecurityIdentityToken(session, system);
    }

    @Test void nullSystemIsRecovered() {
        doReturn(Uni.createFrom().nullItem()).when(systems).findSystem(session, enterprise, ISecurityTokenService.SecurityTokenSystemName);
        start();
        verify(startup).registerSystem(session, enterprise);
    }

    @Test void nullIdentityIsRecovered() {
        doReturn(Uni.createFrom().nullItem(), Uni.createFrom().item(UUID.randomUUID()))
                .when(systems).getSecurityIdentityToken(session, system);
        start();
        verify(startup).registerSystem(session, enterprise);
    }

    @Test void databaseFailureDoesNotTriggerRegistration() {
        var failure = new IllegalStateException("database unavailable");
        doReturn(Uni.createFrom().failure(failure)).when(systems).findSystem(session, enterprise, ISecurityTokenService.SecurityTokenSystemName);
        assertSame(failure, assertThrows(IllegalStateException.class, this::start));
        verify(startup, never()).registerSystem(any(), any());
    }

    @Test void ambiguousIdentityDoesNotTriggerRegistration() {
        var failure = new NonUniqueResultException("duplicate identity");
        doReturn(Uni.createFrom().failure(failure)).when(systems).getSecurityIdentityToken(session, system);
        assertSame(failure, assertThrows(NonUniqueResultException.class, this::start));
        verify(startup, never()).registerSystem(any(), any());
    }

    @Test void repairFailurePropagates() {
        var failure = new IllegalStateException("registration failed");
        doReturn(Uni.createFrom().failure(new NoResultException())).when(systems).getSecurityIdentityToken(session, system);
        doReturn(Uni.createFrom().failure(failure)).when(startup).registerSystem(session, enterprise);
        assertSame(failure, assertThrows(IllegalStateException.class, this::start));
        verify(startup).registerSystem(session, enterprise);
        verify(systems).getSecurityIdentityToken(session, system);
    }

    @Test void stillMissingIdentityFailsAfterOneRepair() {
        doReturn(Uni.createFrom().failure(new NoResultException())).when(systems).getSecurityIdentityToken(session, system);
        assertThrows(NoResultException.class, this::start);
        verify(startup).registerSystem(session, enterprise);
        verify(systems, times(2)).getSecurityIdentityToken(session, system);
    }

    @Test void stillNullIdentityFailsAfterOneRepair() {
        doReturn(Uni.createFrom().nullItem()).when(systems).getSecurityIdentityToken(session, system);
        assertThrows(NoResultException.class, this::start);
        verify(startup).registerSystem(session, enterprise);
        verify(systems, times(2)).getSecurityIdentityToken(session, system);
    }
}
