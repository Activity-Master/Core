package com.guicedee.activitymaster.test;

import com.guicedee.activitymaster.fsdm.InvolvedPartyService;
import com.guicedee.activitymaster.fsdm.SecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

/** Executes real service/security pipelines with controlled persistence failures; no runtime bootstrap. */
@SuppressWarnings({"unchecked", "rawtypes"})
class PartySecurityFailureTest {
    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    private static final List<String> FOLDERS = List.of("administrators", "systems", "applications", "plugins");
    private MockedStatic<IGuiceContext> context;

    @BeforeEach void isolateRuntime() {
        context = mockStatic(IGuiceContext.class);
        context.when(() -> IGuiceContext.get(com.guicedee.client.scopes.CallScoper.class))
                .thenThrow(new IllegalStateException("No call scope in this unit fixture"));
    }
    @AfterEach void closeRuntime() { context.close(); }

    @Test void statelessRestrictedGrantsAreSubscribedSequentiallyWithExactPermissions() {
        InvolvedParty party = spy(new InvolvedParty());
        Map<String, ISecurityToken<?, ?>> folders = folders(); ISecurityToken scope = mock(ISecurityToken.class);
        List<String> written = new ArrayList<>();
        doAnswer(call -> Uni.createFrom().deferred(() -> {
            ISecurityToken token = call.getArgument(4);
            String name = token == scope ? "scope" : FOLDERS.stream().filter(k -> folders.get(k) == token).findFirst().orElseThrow();
            written.add(name);
            assertEquals(!name.equals("scope"), call.getArgument(5));
            assertEquals(!name.equals("scope"), call.getArgument(6));
            assertEquals(name.equals("administrators"), call.getArgument(7));
            assertEquals(true, call.getArgument(8));
            return Uni.createFrom().item(1L);
        })).when(party).createSecurityGrant(any(), any(), any(), any(), any(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any(UUID[].class));
        var result = party.createScopeRestrictedSecurity(mock(Mutiny.StatelessSession.class), mock(ISystems.class),
                mock(IEnterprise.class), mock(IActiveFlag.class), folders, scope, UUID.randomUUID());
        assertTrue(written.isEmpty(), "Constructing a pipeline must not perform writes");
        assertEquals(5L, result.await().atMost(TIMEOUT));
        assertEquals(List.of("administrators", "systems", "applications", "plugins", "scope"), written);
    }

    @Test void statelessMissingFoldersFailBeforeAnyGrantAndGrantFailureStopsLaterWrites() {
        InvolvedParty party = spy(new InvolvedParty()); AtomicInteger writes = new AtomicInteger();
        RuntimeException failure = new IllegalStateException("fixture grant failure");
        doAnswer(call -> Uni.createFrom().deferred(() -> writes.incrementAndGet() == 2
                ? Uni.createFrom().failure(failure) : Uni.createFrom().item(1L)))
                .when(party).createSecurityGrant(any(), any(), any(), any(), any(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any(UUID[].class));
        var session = mock(Mutiny.StatelessSession.class); var system = mock(ISystems.class);
        var enterprise = mock(IEnterprise.class); var flag = mock(IActiveFlag.class);
        for (String missing : FOLDERS) {
            var incomplete = folders(); incomplete.remove(missing);
            assertThrows(IllegalStateException.class, () -> party.createScopeRestrictedSecurity(session, system,
                    enterprise, flag, incomplete, null, UUID.randomUUID()).await().atMost(TIMEOUT));
        }
        assertEquals(0, writes.get());
        assertSame(failure, assertThrows(IllegalStateException.class, () -> party.createScopeRestrictedSecurity(session,
                system, enterprise, flag, folders(), null, UUID.randomUUID()).await().atMost(TIMEOUT)));
        assertEquals(2, writes.get());
    }

    @Test void liveRestrictedSecurityDoesNotSwallowMissingCanonicalFolders() {
        var session = mock(Mutiny.StatelessSession.class); var system = mock(ISystems.class);
        var security = mock(SecurityTokenService.class); var failure = new NoResultException("missing fixture folder");
        doReturn(Uni.createFrom().failure(failure)).when(security).getAdministratorsFolder(eq(session), eq(system), any(UUID[].class));
        {
            context.when(() -> IGuiceContext.get(SecurityTokenService.class)).thenReturn(security);
            assertSame(failure, assertThrows(NoResultException.class, () -> new InvolvedParty()
                    .createScopeRestrictedSecurity(session, system, null, UUID.randomUUID()).await().atMost(TIMEOUT)));
            verify(security, never()).getSystemsFolder(any(Mutiny.StatelessSession.class), any(), any(UUID[].class));
        }
    }

    @Test void liveGrantLookupFailureIsNotReinterpretedAsPermissionToInsert() {
        var session = mock(Mutiny.StatelessSession.class); var system = mock(ISystems.class);
        var security = mock(SecurityTokenService.class);
        var token = mock(com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken.class);
        var record = mock(InvolvedPartySecurityToken.class);
        var query = mock(com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartySecurityTokenQueryBuilder.class, RETURNS_SELF);
        var failure = new IllegalStateException("fixture lookup unavailable");
        doReturn(Uni.createFrom().item(token)).when(security).getAdministratorsFolder(eq(session), eq(system), any(UUID[].class));
        when(record.builder(session)).thenReturn(query);
        when(query.get()).thenReturn(Uni.createFrom().failure(failure));
        context.when(() -> IGuiceContext.get(SecurityTokenService.class)).thenReturn(security);
        context.when(() -> IGuiceContext.get(InvolvedPartySecurityToken.class)).thenReturn(record);
        assertSame(failure, assertThrows(IllegalStateException.class, () -> new InvolvedParty()
                .createScopeRestrictedSecurity(session, system, null, UUID.randomUUID()).await().atMost(TIMEOUT)));
        verify(session, never()).insert(any(Object.class));
        when(query.get()).thenReturn(Uni.createFrom().nullItem());
        assertThrows(IllegalStateException.class, () -> new InvolvedParty()
                .createScopeRestrictedSecurity(session, system, null, UUID.randomUUID()).await().atMost(TIMEOUT));
        verify(session, never()).insert(any(Object.class));
    }

    @Test void statelessPartyAndBothSubtypeFailuresPropagate() {
        for (boolean organic : List.of(true, false)) for (boolean failParent : List.of(true, false))
            assertServiceFailure(organic, failParent);
    }

    private static Map<String, ISecurityToken<?, ?>> folders() {
        Map<String, ISecurityToken<?, ?>> result = new HashMap<>();
        FOLDERS.forEach(name -> result.put(name, mock(ISecurityToken.class))); return result;
    }

    private void assertServiceFailure(boolean organic, boolean failParent) {
        var batch = mock(Mutiny.StatelessSession.class);
        var system = mock(ISystems.class); var enterprise = mock(IEnterprise.class); var flag = mock(IActiveFlag.class);
        var active = mock(IActiveFlagService.class); var tokens = mock(ISecurityTokenService.class);
        var scope = mock(ISecurityToken.class); var identification = mock(IInvolvedPartyIdentificationType.class);
        var service = spy(new InvolvedPartyService()); UUID id = UUID.randomUUID(); UUID[] identity = {UUID.randomUUID()};
        var failure = new IllegalStateException("fixture security failure");
        when(system.getEnterprise()).thenReturn(enterprise);
        when(system.getId()).thenReturn(UUID.randomUUID());
        when(batch.insert(any(Object.class))).thenReturn(Uni.createFrom().voidItem());
        doReturn(Uni.createFrom().item(flag)).when(active).getActiveFlag(eq(batch), eq(enterprise), any(UUID[].class));
        doReturn(Uni.createFrom().item(folders())).when(tokens).resolveDefaultGroupFolderTokens(eq(batch), eq(system), any(UUID[].class));
        doReturn(Uni.createFrom().item(identification)).when(service).findInvolvedPartyIdentificationType(eq(batch), anyString(), eq(system), any(UUID[].class));
        MockedConstruction.MockInitializer<InvolvedParty> initializeParty = (party, ignored) -> {
            when(party.getId()).thenReturn(id);
            doReturn(failParent ? Uni.createFrom().failure(failure) : Uni.createFrom().item(5L))
                    .when(party).createScopeRestrictedSecurity(eq(batch), eq(system), eq(enterprise), eq(flag), anyMap(), eq(scope), any(UUID[].class));
            doReturn(Uni.createFrom().nullItem()).when(party).addOrReuseInvolvedPartyIdentificationType(
                    eq(batch), anyString(), eq(identification), anyString(), eq(system), any(UUID[].class));
        };
        try (var parties = mockConstruction(InvolvedParty.class, initializeParty);
             var people = mockConstruction(InvolvedPartyOrganic.class, (record, ignored) -> {
                 doReturn(Uni.createFrom().failure(failure)).when(record).createScopeRestrictedSecurity(eq(batch), eq(system), eq(enterprise), eq(flag), anyMap(), eq(scope), any(UUID[].class));
             });
             var organizations = mockConstruction(InvolvedPartyNonOrganic.class, (record, ignored) -> {
                 doReturn(Uni.createFrom().failure(failure)).when(record).createScopeRestrictedSecurity(eq(batch), eq(system), eq(enterprise), eq(flag), anyMap(), eq(scope), any(UUID[].class));
             })) {
            context.when(() -> IGuiceContext.get(IActiveFlagService.class)).thenReturn(active);
            context.when(() -> IGuiceContext.get(ISecurityTokenService.class)).thenReturn(tokens);
            Uni<?> result = service.createScopeRestricted(batch, system, id, new Pair<>("fixture", id.toString()), organic, scope, identity);
            assertSame(failure, assertThrows(IllegalStateException.class, () -> result.await().atMost(TIMEOUT)));
            assertEquals(failParent ? 0 : 1, people.constructed().size() + organizations.constructed().size());
        }
    }
}
