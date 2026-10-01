package com.guicedee.activitymaster.tests;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.api.ColumnEncryption;
import com.guicedee.activitymaster.fsdm.api.EncryptedValuePredicate;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXInvolvedPartyIdentificationType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;
import org.mockito.ArgumentCaptor;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.guicedee.activitymaster.fsdm.api.ColumnEncryption.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ResourceLock(Resources.SYSTEM_PROPERTIES)
public class TestColumnEncryption
{
    private static final String CONFIG = "activitymaster.encryption.";
    private final Map<String, String> originals = new LinkedHashMap<>();

    @BeforeEach
    void configure()
    {
        set("encrypt", "true");
        set(CONFIG + "mode", "legacy");
        set(CONFIG + "key-id", "primary");
        set(CONFIG + "read-key-ids", "");
        set(CONFIG + "key.primary", randomKey());
        set(CONFIG + "key.next", randomKey());
    }

    private static String randomKey()
    {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    private void set(String name, String value)
    {
        if (!originals.containsKey(name)) originals.put(name, System.getProperty(name));
        System.setProperty(name, value);
    }

    @AfterEach
    void restore()
    {
        originals.forEach((name, value) -> {
            if (value == null) System.clearProperty(name);
            else System.setProperty(name, value);
        });
    }

    @Test
    void legacyDefaultsAndPlaintextEscapeHatchRemainUnchanged()
    {
        String value = "TestFarm";
        String old = new Passwords().integerEncrypt(value.getBytes());
        assertFalse(strongWrites());
        assertFalse(searchableEncryption());
        assertEquals(old, encrypt(value, IDENTIFICATION));
        assertEquals(value, decrypt(old, IDENTIFICATION));
        assertEquals(value, decrypt(value, ADDRESS));
        assertNull(encrypt(null, ADDRESS));
        assertNull(decrypt(null, ADDRESS));
        assertEquals("", encrypt("", IDENTIFICATION));
        set("encrypt", "false");
        assertEquals(value, encrypt(value, ADDRESS));
        assertEquals(old, decrypt(old, ADDRESS));
    }

    @Test
    void strongWritesAreRandomizedAndRoundTripUnicodeAndEmptyValues()
    {
        set(CONFIG + "mode", "aes-gcm");
        for (String value : new String[]{"TestFarm", "München 東京 🔐", ""})
        {
            String first = encrypt(value, ADDRESS);
            String second = encrypt(value, ADDRESS);
            assertTrue(first.startsWith("amenc:1:primary:"));
            assertNotEquals(first, second);
            assertEquals(value, decrypt(first, ADDRESS));
            assertEquals(value, decrypt(second, ADDRESS));
            assertTrue(first.startsWith(searchPrefixes(value, ADDRESS).getFirst()));
            assertFalse(first.startsWith(searchPrefixes(value, IDENTIFICATION).getFirst()));
        }
    }

    @Test
    void bothEntitiesUseConfiguredProtectionAndLegacyReaders()
    {
        Address oldAddress = new Address().setValue("old@example.com");
        var oldLink = new InvolvedPartyXInvolvedPartyIdentificationType();
        oldLink.setValue("OldFarm");
        set(CONFIG + "mode", "aes-gcm");
        assertEquals("old@example.com", oldAddress.getValue());
        assertEquals("OldFarm", oldLink.getValue());
        Address address = new Address().setValue("new@example.com");
        var link = new InvolvedPartyXInvolvedPartyIdentificationType();
        link.setValue("NewFarm");
        assertEquals("new@example.com", address.getValue());
        assertEquals("NewFarm", link.getValue());
        set(CONFIG + "mode", "legacy");
        set("encrypt", "false");
        assertEquals("new@example.com", address.getValue());
        assertEquals("NewFarm", link.getValue());
    }

    @Test
    void strongModeCannotBeDisabledByLegacyGate()
    {
        set(CONFIG + "mode", "aes-gcm");
        set("encrypt", "false");
        assertTrue(encrypt("secret", ADDRESS).startsWith("amenc:"));
    }

    @Test
    void tamperingWrongContextAndUnknownVersionsFailClosed()
    {
        set(CONFIG + "mode", "aes-gcm");
        String stored = encrypt("secret", ADDRESS);
        int delimiter = stored.lastIndexOf(':');
        byte[] payload = Base64.getDecoder().decode(stored.substring(delimiter + 1));
        payload[payload.length - 1] ^= 1;
        String tampered = stored.substring(0, delimiter + 1) + Base64.getEncoder().encodeToString(payload);
        assertThrows(IllegalStateException.class, () -> decrypt(tampered, ADDRESS));
        assertThrows(IllegalStateException.class, () -> decrypt(stored, IDENTIFICATION));
        assertThrows(IllegalStateException.class, () -> decrypt(stored.replace("amenc:1:", "amenc:2:"), ADDRESS));
        assertThrows(IllegalStateException.class, () -> decrypt("amenc:1:broken", ADDRESS));
        int tokenStart = "amenc:1:primary:".length();
        String changedHeader = stored.substring(0, tokenStart)
                + (stored.charAt(tokenStart) == '0' ? '1' : '0') + stored.substring(tokenStart + 1);
        assertThrows(IllegalStateException.class, () -> decrypt(changedHeader, ADDRESS));
        set(CONFIG + "key.primary", randomKey());
        assertThrows(IllegalStateException.class, () -> decrypt(stored, ADDRESS));
    }

    @Test
    void missingInvalidKeysAndUnknownModesNeverDowngrade()
    {
        set(CONFIG + "mode", "aes-gcm");
        String stored = encrypt("secret", ADDRESS);
        for (String key : new String[]{"", "not-base64", Base64.getEncoder().encodeToString(new byte[16])})
        {
            set(CONFIG + "key.primary", key);
            assertThrows(IllegalStateException.class, () -> encrypt("secret", ADDRESS));
            assertThrows(IllegalStateException.class, () -> decrypt(stored, ADDRESS));
            assertThrows(IllegalStateException.class, () -> searchPrefixes("secret", ADDRESS));
        }
        set(CONFIG + "mode", "aes-typo");
        assertThrows(IllegalStateException.class, () -> encrypt("secret", ADDRESS));
        assertThrows(IllegalStateException.class, ColumnEncryption::searchableEncryption);
    }

    @Test
    void rotationAndRollbackKeepOldRowsReadableAndSearchable()
    {
        set(CONFIG + "mode", "aes-gcm");
        String old = encrypt("same", ADDRESS);
        set(CONFIG + "key-id", "next");
        set(CONFIG + "read-key-ids", "primary");
        String next = encrypt("same", ADDRESS);
        assertEquals("same", decrypt(old, ADDRESS));
        assertEquals("same", decrypt(next, ADDRESS));
        var prefixes = searchPrefixes("same", ADDRESS);
        assertEquals(2, prefixes.size());
        assertTrue(prefixes.stream().anyMatch(old::startsWith));
        assertTrue(prefixes.stream().anyMatch(next::startsWith));
        set(CONFIG + "mode", "legacy");
        set(CONFIG + "read-key-ids", "primary,next");
        assertTrue(searchableEncryption());
        assertEquals(2, searchPrefixes("same", ADDRESS).size());
        assertEquals("same", decrypt(next, ADDRESS));
        assertEquals(legacyObfuscatedValue("same"), encrypt("same", ADDRESS));
    }

    @Test
    void columnCapacityIsCheckedBeforePersistence()
    {
        set(CONFIG + "mode", "aes-gcm");
        assertTrue(encrypt("x".repeat(101), ADDRESS).length() <= 255);
        assertThrows(IllegalArgumentException.class, () -> encrypt("x".repeat(102), ADDRESS));
        String profileText = "Private medical information ".repeat(32);
        assertThrows(IllegalArgumentException.class, () -> encrypt(profileText, IDENTIFICATION));
        assertThrows(IllegalArgumentException.class, () -> encrypt("東".repeat(34), ADDRESS));
    }

    @Test
    @SuppressWarnings("unchecked")
    void equalityGroupsAllFormatsAndNegationAppliesToTheWholeGroup()
    {
        set(CONFIG + "mode", "aes-gcm");
        CriteriaBuilder builder = mock(CriteriaBuilder.class);
        Expression<String> column = mock(Expression.class);
        Predicate clause = mock(Predicate.class);
        Predicate group = mock(Predicate.class);
        Predicate negated = mock(Predicate.class);
        Expression<String> extracted = mock(Expression.class);
        Expression<String> lookupHeader = mock(Expression.class);
        when(builder.function(eq("regexp_substr"), eq(String.class), any(Expression[].class))).thenReturn(extracted);
        when(builder.coalesce(extracted, "")).thenReturn(lookupHeader);
        when(builder.equal(eq(column), anyString())).thenReturn(clause);
        when(builder.equal(eq(lookupHeader), anyString())).thenReturn(clause);
        when(builder.or(any(Predicate[].class))).thenReturn(group);
        when(builder.not(group)).thenReturn(negated);
        assertSame(group, EncryptedValuePredicate.create(builder, column, Operand.Equals, "same", ADDRESS));
        verify(builder).equal(column, "same");
        verify(builder).equal(column, legacyObfuscatedValue("same"));
        verify(builder).equal(lookupHeader, searchPrefixes("same", ADDRESS).getFirst());
        verify(builder, never()).like(any(Expression.class), anyString());
        ArgumentCaptor<Predicate[]> captured = ArgumentCaptor.forClass(Predicate[].class);
        verify(builder).or(captured.capture());
        assertEquals(3, captured.getValue().length);
        assertSame(negated, EncryptedValuePredicate.create(builder, column, Operand.NotEquals, "same", ADDRESS));
        assertThrows(IllegalArgumentException.class,
                () -> EncryptedValuePredicate.create(builder, column, Operand.Like, "%same%", ADDRESS));
        EncryptedValuePredicate.create(builder, column, Operand.Null, null, ADDRESS);
        verify(builder).isNull(column);
        EncryptedValuePredicate.create(builder, column, Operand.NotNull, null, ADDRESS);
        verify(builder).isNotNull(column);
    }
}
