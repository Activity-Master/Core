package com.guicedee.activitymaster.fsdm.api;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Modern, self-describing password hashing for ActivityMaster.
 *
 * <p>Uses {@code PBKDF2-HMAC-SHA256} with an OWASP-recommended work factor (600,000 iterations as
 * of 2023+). The encoded value is a PHC-style, self-describing string so the algorithm, work
 * factor, salt and digest all travel together inside a single stored value:</p>
 *
 * <pre>
 *   $pbkdf2-sha256$i=600000$&lt;base64Salt&gt;$&lt;base64Hash&gt;
 * </pre>
 *
 * <p>Because the parameters are embedded in the value, the salt no longer needs a separate
 * classification, the work factor can be raised over time, and {@link #needsRehash(String)} will
 * report when a stored hash should be upgraded on the next successful authentication. Verification
 * uses {@link MessageDigest#isEqual(byte[], byte[])} for a constant-time comparison.</p>
 *
 * <p>This class intentionally only depends on the JDK ({@code javax.crypto} / {@code java.security}),
 * keeping it free of external dependencies while remaining FIPS-friendly. The legacy
 * {@link Passwords} utility (PBKDF2-HMAC-SHA1 with a separately stored salt) remains available so
 * existing credentials can still be verified and migrated.</p>
 */
public final class PasswordEncoder
{
	/** Algorithm identifier embedded in the encoded value. */
	public static final String ALGORITHM_ID = "pbkdf2-sha256";

	private static final String JCA_ALGORITHM = "PBKDF2WithHmacSHA256";
	/** OWASP 2023 minimum work factor for PBKDF2-HMAC-SHA256. */
	private static final int DEFAULT_ITERATIONS = 600_000;
	private static final int SALT_BYTES = 16;
	private static final int HASH_BYTES = 32; // 256-bit derived key
	private static final SecureRandom RANDOM = new SecureRandom();

	private final int iterations;

	public PasswordEncoder()
	{
		this(DEFAULT_ITERATIONS);
	}

	public PasswordEncoder(int iterations)
	{
		if (iterations < 1)
		{
			throw new IllegalArgumentException("iterations must be positive");
		}
		this.iterations = iterations;
	}

	/**
	 * Hashes a raw password into a self-describing PHC-style string.
	 *
	 * @param rawPassword the plaintext password
	 * @return the encoded {@code $pbkdf2-sha256$i=...$salt$hash} value
	 */
	public String encode(String rawPassword)
	{
		if (rawPassword == null)
		{
			throw new IllegalArgumentException("password must not be null");
		}
		byte[] salt = new byte[SALT_BYTES];
		RANDOM.nextBytes(salt);
		byte[] hash = pbkdf2(rawPassword.toCharArray(), salt, iterations, HASH_BYTES);
		return "$" + ALGORITHM_ID + "$i=" + iterations + "$" + b64(salt) + "$" + b64(hash);
	}

	/**
	 * Returns {@code true} if the stored value is in the modern PHC-style format produced by
	 * {@link #encode(String)} (as opposed to a legacy integer-encoded hash).
	 */
	public boolean isEncoded(String stored)
	{
		return stored != null && stored.startsWith("$" + ALGORITHM_ID + "$");
	}

	/**
	 * Constant-time verification of a raw password against a modern encoded value.
	 *
	 * @param rawPassword the plaintext password to check
	 * @param encoded     the stored {@code $pbkdf2-sha256$...} value
	 * @return {@code true} if the password matches
	 */
	public boolean matches(String rawPassword, String encoded)
	{
		if (rawPassword == null || !isEncoded(encoded))
		{
			return false;
		}
		try
		{
			// ["", "pbkdf2-sha256", "i=600000", b64salt, b64hash]
			String[] parts = encoded.split("\\$");
			if (parts.length != 5 || !parts[2].startsWith("i="))
			{
				return false;
			}
			int iters = Integer.parseInt(parts[2].substring("i=".length()));
			byte[] salt = unb64(parts[3]);
			byte[] expected = unb64(parts[4]);
			byte[] actual = pbkdf2(rawPassword.toCharArray(), salt, iters, expected.length);
			return MessageDigest.isEqual(actual, expected);
		}
		catch (RuntimeException e)
		{
			return false;
		}
	}

	/**
	 * Returns {@code true} when a stored value should be re-hashed on the next successful login —
	 * either because it is not in the modern format, or because it was produced with a lower work
	 * factor than the current default.
	 */
	public boolean needsRehash(String encoded)
	{
		if (!isEncoded(encoded))
		{
			return true;
		}
		try
		{
			String[] parts = encoded.split("\\$");
			if (parts.length != 5 || !parts[2].startsWith("i="))
			{
				return true;
			}
			int iters = Integer.parseInt(parts[2].substring("i=".length()));
			return iters < iterations;
		}
		catch (RuntimeException e)
		{
			return true;
		}
	}

	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int hashBytes)
	{
		PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, hashBytes * 8);
		try
		{
			SecretKeyFactory skf = SecretKeyFactory.getInstance(JCA_ALGORITHM);
			return skf.generateSecret(spec)
			          .getEncoded();
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e)
		{
			throw new IllegalStateException("Unable to hash password: " + e.getMessage(), e);
		}
		finally
		{
			spec.clearPassword();
		}
	}

	private static String b64(byte[] data)
	{
		return Base64.getEncoder()
		             .encodeToString(data);
	}

	private static byte[] unb64(String data)
	{
		return Base64.getDecoder()
		             .decode(data);
	}
}

