package com.guicedee.activitymaster.fsdm.api;

import com.guicedee.client.Environment;
import com.guicedee.activitymaster.fsdm.encryption.EnterpriseKeyCache;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Pattern;

/** Versioned protection for reversible values only; never used for password hashes. */
public final class ColumnEncryption
{
    public static final String ADDRESS = "Address.Value";
    public static final String IDENTIFICATION = "Party.InvolvedPartyXInvolvedPartyIdentificationType.Value";
    private static final String CONFIG = "activitymaster.encryption.";
    private static final String PREFIX = "amenc:";
    private static final Pattern LEGACY = Pattern.compile("(-?\\d+\\|)+");
    private static final SecureRandom RANDOM = new SecureRandom();

    private ColumnEncryption() { }

    public static boolean strongWrites()
    {
        String mode = Environment.getProperty(CONFIG + "mode", "legacy");
        return switch (mode)
        {
            case "legacy" -> false;
            case "aes-gcm", "enterprise" -> true;
            default -> throw new IllegalStateException("Unknown ActivityMaster encryption mode");
        };
    }

    public static boolean searchableEncryption()
    {
        return enterpriseReads() || !searchKeyIds().isEmpty();
    }

    public static boolean enterpriseWrites()
    {
        return "enterprise".equals(Environment.getProperty(CONFIG + "mode", "legacy"));
    }

    public static boolean enterpriseReads()
    {
        return enterpriseWrites() || Boolean.parseBoolean(Environment.getProperty(CONFIG + "enterprise-reads", "false"));
    }

    private static Set<String> searchKeyIds()
    {
        Set<String> ids = new LinkedHashSet<>();
        if (strongWrites() && !enterpriseWrites()) ids.add(activeId());
        String configured = Environment.getProperty(CONFIG + "read-key-ids", "");
        if (!configured.isBlank())
        {
            for (String id : configured.split(",", -1)) ids.add(validId(id.trim()));
        }
        return ids;
    }

    private static String activeId()
    {
        return validId(Environment.getProperty(CONFIG + "key-id", "primary"));
    }

    private static String validId(String id)
    {
        if (!id.matches("[a-z0-9]{1,32}"))
            throw new IllegalStateException("Invalid ActivityMaster encryption key ID");
        return id;
    }

    private static byte[] derivedKey(String id, String purpose) throws GeneralSecurityException
    {
        validId(id);
        // Environment logs .env values at DEBUG; never route secret material through it.
        String encoded = System.getProperty(CONFIG + "key." + id);
        if (encoded == null) encoded = System.getenv("ACTIVITYMASTER_ENCRYPTION_KEY_" + id.toUpperCase(Locale.ROOT));
        if (encoded == null || encoded.isBlank())
            throw new IllegalStateException("Missing ActivityMaster encryption key");
        byte[] master;
        try { master = Base64.getDecoder().decode(encoded); }
        catch (IllegalArgumentException e) { throw new IllegalStateException("Invalid ActivityMaster encryption key encoding"); }
        try
        {
            if (master.length != 32) throw new IllegalStateException("ActivityMaster encryption keys must contain 32 bytes");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(master, "HmacSHA256"));
            return mac.doFinal(("ActivityMaster:column:v1:" + purpose).getBytes(StandardCharsets.UTF_8));
        }
        finally { Arrays.fill(master, (byte) 0); }
    }

    private static byte[] derivedKey(String id, String purpose, UUID enterprise) throws GeneralSecurityException
    {
        if (enterprise == null) return derivedKey(id, purpose);
        byte[] master = EnterpriseKeyCache.key(enterprise, id);
        try
        {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(master, "HmacSHA256"));
            return mac.doFinal(("ActivityMaster:column:v2:" + enterprise + ":" + purpose).getBytes(StandardCharsets.UTF_8));
        }
        finally { Arrays.fill(master, (byte) 0); }
    }

    private static String header(String value, String context, String id) throws GeneralSecurityException
    {
        return header(value, context, id, null);
    }

    private static String header(String value, String context, String id, UUID enterprise) throws GeneralSecurityException
    {
        byte[] key = derivedKey(id, "lookup", enterprise);
        try
        {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            byte[] token = mac.doFinal((context + "\u0000" + value).getBytes(StandardCharsets.UTF_8));
            return PREFIX + (enterprise == null ? "1:" : "2:") + id + ":" + HexFormat.of().formatHex(token) + ":";
        }
        finally { Arrays.fill(key, (byte) 0); }
    }

    public static String encrypt(String value, String context)
    {
        return encrypt(value, context, null);
    }

    public static String encrypt(String value, String context, UUID enterpriseId)
    {
        boolean identification = IDENTIFICATION.equals(context);
        boolean strong = strongWrites();
        if (value == null) return null;
        if (!strong) return legacySearchValue(value);
        if (enterpriseWrites() && enterpriseId == null)
            throw new IllegalStateException("Assign enterprise before encrypting protected values");
        UUID enterprise = enterpriseWrites() ? enterpriseId : null;
        if (enterprise != null) context = context + ":" + enterprise;
        String id = enterprise == null ? activeId() : EnterpriseKeyCache.activeId(enterprise);
        byte[] key = null;
        try
        {
            String header = header(value, context, id, enterprise);
            key = derivedKey(id, "encryption", enterprise);
            byte[] nonce = new byte[12];
            RANDOM.nextBytes(nonce);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, nonce));
            cipher.updateAAD((context + "\u0000" + header).getBytes(StandardCharsets.UTF_8));
            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            byte[] payload = Arrays.copyOf(nonce, nonce.length + encrypted.length);
            System.arraycopy(encrypted, 0, payload, nonce.length, encrypted.length);
            String stored = header + Base64.getEncoder().encodeToString(payload);
            int capacity = identification ? 200 : 255;
            if (stored.length() > capacity)
                throw new IllegalArgumentException("Encrypted value exceeds the existing " + capacity + "-character column capacity");
            return stored;
        }
        catch (GeneralSecurityException e) { throw new IllegalStateException("Column encryption failed", e); }
        finally { if (key != null) Arrays.fill(key, (byte) 0); }
    }

    public static String decrypt(String stored, String context)
    {
        return decrypt(stored, context, null);
    }

    public static String decrypt(String stored, String context, UUID enterpriseId)
    {
        if (stored == null) return null;
        if (!stored.startsWith(PREFIX))
        {
            if (!"true".equals(System.getProperty("encrypt", "true")) || !LEGACY.matcher(stored).matches()) return stored;
            try { return new String(new Passwords().integerDecrypt(stored)); }
            catch (NumberFormatException e) { return stored; }
        }
        byte[] key = null;
        try
        {
            String[] parts = stored.split(":", -1);
            if (parts.length != 5 || !(parts[1].equals("1") || parts[1].equals("2")) || !parts[3].matches("[0-9a-f]{64}"))
                throw new IllegalArgumentException();
            UUID enterprise = null;
            if (parts[1].equals("2"))
            {
                if (enterpriseId == null) throw new IllegalStateException("Enterprise ID is required to read encrypted values");
                enterprise = enterpriseId;
                context = context + ":" + enterprise;
            }
            String id = validId(parts[2]);
            byte[] payload = Base64.getDecoder().decode(parts[4]);
            if (payload.length < 28) throw new IllegalArgumentException();
            String header = stored.substring(0, stored.lastIndexOf(':') + 1);
            key = derivedKey(id, "encryption", enterprise);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, payload, 0, 12));
            cipher.updateAAD((context + "\u0000" + header).getBytes(StandardCharsets.UTF_8));
            return new String(cipher.doFinal(payload, 12, payload.length - 12), StandardCharsets.UTF_8);
        }
        catch (GeneralSecurityException | IllegalArgumentException e)
        {
            throw new IllegalStateException("Encrypted column could not be authenticated or decoded", e);
        }
        finally { if (key != null) Arrays.fill(key, (byte) 0); }
    }

    /** Preserve the original default-charset obfuscation for existing deployments. */
    public static String legacySearchValue(String value)
    {
        return value != null && "true".equals(System.getProperty("encrypt", "true"))
                ? legacyObfuscatedValue(value) : value;
    }

    public static String legacyObfuscatedValue(String value)
    {
        return new Passwords().integerEncrypt(value.getBytes());
    }

    /** Complete format/key/HMAC headers for exact indexed equality, across retained read keys. */
    public static List<String> searchPrefixes(String value, String context)
    {
        return searchPrefixes(value, context, null);
    }

    public static List<String> searchPrefixes(String value, String context, UUID enterprise)
    {
        List<String> prefixes = new ArrayList<>();
        try
        {
            for (String id : searchKeyIds()) prefixes.add(header(value, context, id));
            if (enterpriseReads())
            {
                for (String id : EnterpriseKeyCache.keyIds(enterprise))
                    prefixes.add(header(value, context + ":" + enterprise, id, enterprise));
            }
            return prefixes;
        }
        catch (GeneralSecurityException e) { throw new IllegalStateException("Encrypted lookup failed", e); }
    }
}


