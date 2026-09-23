package com.guicedee.activitymaster.tests;

import com.guicedee.activitymaster.fsdm.api.ColumnEncryption;
import com.guicedee.activitymaster.fsdm.encryption.*;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;
import org.mockito.ArgumentCaptor;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.guicedee.activitymaster.fsdm.api.ColumnEncryption.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ResourceLock(Resources.SYSTEM_PROPERTIES)
public class TestEnterpriseEncryption
{
    private static final String CONFIG = "activitymaster.encryption.";
    private static final String OLD = "a".repeat(32);
    private static final String NEXT = "b".repeat(32);
    private static final String KEK = "https://example.vault.azure.net/keys/dek/" + "c".repeat(32);
    private final UUID enterprise = UUID.randomUUID();
    private final UUID other = UUID.randomUUID();
    private final Map<String, String> originals = new HashMap<>();
    private EnterpriseKeyWrapper wrapper;
    private EnterpriseKeyStore store;
    private EnterpriseEncryptionService service;

    @BeforeEach
    void setup()
    {
        set(CONFIG + "mode", "enterprise");
        set(CONFIG + "read-key-ids", "");
        set(CONFIG + "enterprise-reads", "false");
        set("encrypt", "true");
        wrapper = mock(EnterpriseKeyWrapper.class);
        when(wrapper.currentKeyReference()).thenReturn(KEK);
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        when(wrapper.unwrap(eq(KEK), any(byte[].class))).thenAnswer(call -> Uni.createFrom().item(key.clone()));
        store = mock(EnterpriseKeyStore.class);
        service = new EnterpriseEncryptionService(wrapper, store);
    }

    private void set(String key, String value)
    {
        if (!originals.containsKey(key)) originals.put(key, System.getProperty(key));
        System.setProperty(key, value);
    }

    @AfterEach
    void cleanup()
    {
        EnterpriseKeyCache.evict(enterprise);
        EnterpriseKeyCache.evict(other);
        originals.forEach((key, value) -> {
            if (value == null) System.clearProperty(key); else System.setProperty(key, value);
        });
    }

    @Test
    void kmsCallbacksResumeOnTheOriginalVertxContext() throws Exception
    {
        Vertx vertx = Vertx.vertx();
        try
        {
            var original = new AtomicReference<io.vertx.core.Context>();
            var wrappingStarted = new CompletableFuture<Void>();
            var unwrappingStarted = new CompletableFuture<Void>();
            var wrapped = new CompletableFuture<byte[]>();
            var unwrapped = new CompletableFuture<byte[]>();
            var finished = new CompletableFuture<Void>();
            Mutiny.Session session = session(enterprise, row(enterprise, OLD, true));
            when(wrapper.wrap(any(byte[].class))).thenAnswer(call -> {
                wrappingStarted.complete(null);
                return Uni.createFrom().completionStage(wrapped);
            });
            when(wrapper.unwrap(eq(KEK), any(byte[].class))).thenAnswer(call -> {
                unwrappingStarted.complete(null);
                return Uni.createFrom().completionStage(unwrapped);
            });
            when(store.insert(eq(session), any(EnterpriseDataKey.class))).thenAnswer(call -> {
                assertSame(original.get(), Vertx.currentContext());
                return Uni.createFrom().voidItem();
            });
            vertx.runOnContext(ignored -> {
                original.set(Vertx.currentContext());
                service.provision(session, enterprise)
                        .chain(() -> service.prepare(session, enterprise))
                        .invoke(() -> assertSame(original.get(), Vertx.currentContext()))
                        .subscribe().with(nothing -> finished.complete(null), finished::completeExceptionally);
            });
            wrappingStarted.get(5, TimeUnit.SECONDS);
            wrapped.complete(new byte[]{1, 2, 3}); // Complete off the Vert.x thread.
            unwrappingStarted.get(5, TimeUnit.SECONDS);
            unwrapped.complete(new byte[32]);
            finished.get(5, TimeUnit.SECONDS);
        }
        finally { vertx.close().toCompletionStage().toCompletableFuture().get(5, TimeUnit.SECONDS); }
    }

    private EnterpriseDataKey row(UUID tenant, String id, boolean active)
    {
        EnterpriseDataKey row = mock(EnterpriseDataKey.class);
        when(row.getEnterpriseId()).thenReturn(tenant);
        when(row.getId()).thenReturn(id);
        when(row.isActive()).thenReturn(active);
        when(row.getKeyReference()).thenReturn(KEK);
        when(row.getWrappedKey()).thenReturn(Base64.getEncoder().encodeToString(new byte[]{1, 2, 3}));
        return row;
    }

    private Mutiny.Session session(UUID tenant, EnterpriseDataKey... rows)
    {
        Mutiny.Session session = mock(Mutiny.Session.class);
        when(store.load(session, tenant)).thenReturn(Uni.createFrom().item(List.of(rows)));
        return session;
    }

    private void prepare(UUID tenant, EnterpriseDataKey... rows)
    {
        service.prepare(session(tenant, rows), tenant).await().atMost(Duration.ofSeconds(5));
    }

    @Test
    void identicalRawKeysStillCannotCrossEnterpriseBoundaries()
    {
        // Deliberately use the SAME raw DEK and ID: context binding must still protect isolation.
        prepare(enterprise, row(enterprise, OLD, true));
        prepare(other, row(other, OLD, true));
        String stored = encrypt("same", ADDRESS, enterprise);
        assertTrue(stored.startsWith("amenc:2:"));
        assertEquals("same", decrypt(stored, ADDRESS, enterprise));
        assertNotEquals(searchPrefixes("same", ADDRESS, enterprise), searchPrefixes("same", ADDRESS, other));
        assertThrows(IllegalStateException.class, () -> decrypt(stored, ADDRESS, other));
        assertThrows(IllegalStateException.class, () -> decrypt(stored, IDENTIFICATION, enterprise));
        assertThrows(IllegalStateException.class, () -> decrypt(stored, ADDRESS));
    }

    @Test
    void rotationKeepsOldReadsAndBothSearchTokens()
    {
        prepare(enterprise, row(enterprise, OLD, true));
        String old = encrypt("value", IDENTIFICATION, enterprise);
        prepare(enterprise, row(enterprise, OLD, false), row(enterprise, NEXT, true));
        String next = encrypt("value", IDENTIFICATION, enterprise);
        assertTrue(next.startsWith("amenc:2:" + NEXT));
        assertEquals("value", decrypt(old, IDENTIFICATION, enterprise));
        assertEquals("value", decrypt(next, IDENTIFICATION, enterprise));
        assertEquals(2, searchPrefixes("value", IDENTIFICATION, enterprise).size());
        set(CONFIG + "mode", "legacy");
        set(CONFIG + "enterprise-reads", "true");
        assertEquals("value", decrypt(next, IDENTIFICATION, enterprise));
        assertEquals(legacyObfuscatedValue("value"), encrypt("value", IDENTIFICATION, enterprise));
        assertEquals(2, searchPrefixes("value", IDENTIFICATION, enterprise).size());
        EnterpriseKeyCache.evict(enterprise);
        assertThrows(IllegalStateException.class, () -> searchPrefixes("value", IDENTIFICATION, enterprise));
    }

    @Test
    void failedUnwrapOrInvalidRingNeverFallsBackToOldCache()
    {
        prepare(enterprise, row(enterprise, OLD, true));
        when(wrapper.unwrap(eq(KEK), any(byte[].class))).thenReturn(Uni.createFrom().failure(new IllegalStateException("KMS unavailable")));
        assertThrows(IllegalStateException.class, () -> prepare(enterprise, row(enterprise, NEXT, true)));
        assertFalse(EnterpriseKeyCache.contains(enterprise));
        assertThrows(IllegalStateException.class, () -> encrypt("secret", ADDRESS, enterprise));
        assertThrows(IllegalStateException.class, () -> prepare(other));
        assertThrows(IllegalStateException.class, () -> prepare(other, row(other, OLD, true), row(other, NEXT, true)));
    }

    @Test
    void missingContextAndCapacityAreChecked()
    {
        assertThrows(IllegalStateException.class, () -> encrypt("value", ADDRESS));
        assertThrows(IllegalStateException.class, () -> encrypt("value", ADDRESS, enterprise));
        prepare(enterprise, row(enterprise, OLD, true));
        assertTrue(encrypt("x".repeat(83), ADDRESS, enterprise).length() <= 255);
        assertThrows(IllegalArgumentException.class, () -> encrypt("x".repeat(84), ADDRESS, enterprise));
        assertEquals("plain", decrypt("plain", ADDRESS, enterprise));
        assertEquals("old", decrypt(legacyObfuscatedValue("old"), ADDRESS, enterprise));
    }

    @Test
    void provisioningPersistsWrappedMaterialAndDoesNotPublishUncommittedKeys()
    {
        List<byte[]> generated = new ArrayList<>();
        List<byte[]> liveBuffers = new ArrayList<>();
        byte[] wrapped = new byte[]{42, 43, 44};
        when(wrapper.wrap(any(byte[].class))).thenAnswer(call -> {
            byte[] key = call.getArgument(0);
            generated.add(key.clone());
            liveBuffers.add(key);
            return Uni.createFrom().item(wrapped.clone());
        });
        Mutiny.Session session = mock(Mutiny.Session.class);
        when(store.insert(eq(session), any(EnterpriseDataKey.class))).thenReturn(Uni.createFrom().voidItem());
        service.provision(session, enterprise).await().atMost(Duration.ofSeconds(5));
        service.provision(session, other).await().atMost(Duration.ofSeconds(5));
        assertEquals(32, generated.getFirst().length);
        assertFalse(Arrays.equals(generated.getFirst(), generated.getLast()));
        liveBuffers.forEach(key -> assertArrayEquals(new byte[32], key));
        assertFalse(EnterpriseKeyCache.contains(enterprise));
        ArgumentCaptor<EnterpriseDataKey> persisted = ArgumentCaptor.forClass(EnterpriseDataKey.class);
        verify(store, times(2)).insert(eq(session), persisted.capture());
        EnterpriseDataKey row = persisted.getAllValues().getFirst();
        assertEquals(enterprise, row.getEnterpriseId());
        assertEquals(KEK, row.getKeyReference());
        assertArrayEquals(wrapped, Base64.getDecoder().decode(row.getWrappedKey()));
        assertTrue(row.isActive());
        assertTrue(row.getId().matches("[a-f0-9]{32}"));
    }

    @Test
    void rotationDoesNotDeactivateOnWrapFailureOrPublishBeforeCommit()
    {
        prepare(enterprise, row(enterprise, OLD, true));
        Mutiny.Session session = mock(Mutiny.Session.class);
        when(wrapper.wrap(any(byte[].class))).thenReturn(Uni.createFrom().failure(new IllegalStateException("KMS unavailable")));
        assertThrows(IllegalStateException.class, () -> service.rotate(session, enterprise).await().atMost(Duration.ofSeconds(5)));
        verify(store, never()).deactivate(session, enterprise);
        when(wrapper.wrap(any(byte[].class))).thenReturn(Uni.createFrom().item(new byte[]{1, 2, 3}));
        when(store.deactivate(session, enterprise)).thenReturn(Uni.createFrom().voidItem());
        when(store.insert(eq(session), any(EnterpriseDataKey.class))).thenReturn(Uni.createFrom().voidItem());
        service.rotate(session, enterprise).await().atMost(Duration.ofSeconds(5));
        var order = inOrder(store);
        order.verify(store).deactivate(session, enterprise);
        order.verify(store).insert(eq(session), any(EnterpriseDataKey.class));
        assertEquals(OLD, EnterpriseKeyCache.activeId(enterprise));
    }

    @Test
    void preparedRingAvoidsRepeatedKmsCallsAndRejectsForeignMetadata()
    {
        Mutiny.Session session = session(enterprise, row(enterprise, OLD, true));
        service.ensurePrepared(session, enterprise).await().atMost(Duration.ofSeconds(5));
        service.ensurePrepared(session, enterprise).await().atMost(Duration.ofSeconds(5));
        verify(store, times(1)).load(session, enterprise);
        verify(wrapper, times(1)).unwrap(eq(KEK), any(byte[].class));
        assertThrows(IllegalStateException.class, () -> prepare(other, row(enterprise, OLD, true)));
        assertFalse(EnterpriseKeyCache.contains(other));
    }

    @Test
    void rejectsUntrustedVaultUrlsBeforeCredentialOrNetworkUse()
    {
        for (String url : List.of("http://example.vault.azure.net/keys/dek/" + OLD,
                "https://example.com/keys/dek/" + OLD, "https://example.vault.azure.net/keys/dek",
                KEK + "?redirect=evil"))
            assertThrows(IllegalArgumentException.class, () -> new AzureKeyVaultKeyWrapper(url, Set.of(), null));
        AzureKeyVaultKeyWrapper azure = new AzureKeyVaultKeyWrapper(KEK, Set.of(), null);
        assertThrows(IllegalStateException.class, () -> azure.unwrap("https://untrusted.example/keys/key/version", new byte[32])
                .await().atMost(Duration.ofSeconds(5)));
    }
}


