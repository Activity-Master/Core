package com.guicedee.activitymaster.fsdm.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

/**
 * The canonical way to create an ActivityMaster FSDM database.
 * <p>
 * The schema is defined by the ordered scripts in {@code db/} on this module's classpath, and this
 * class is the single place that says what they are and in what order they run. Anything that
 * builds a database — an installer, a migration tool, a test container — asks here rather than
 * keeping its own copy of the list, which is how every module's test bootstrap used to drift.
 * <p>
 * The scripts replace the older {@code postgres_fsdm.sql} plus {@code postgres_structure.sql} pair.
 * That pair was a snapshot dumped from a live database, so it captured whatever Hibernate had
 * generated at the time rather than anything anybody had reviewed; these are maintained by hand and
 * are the definition.
 *
 * <h2>Reading the scripts</h2>
 * The scripts live beside classes in this module but not inside a package that contains classes, so
 * they are not encapsulated and {@link #read(String)} works from any module. Reading the bytes and
 * handing them to a consumer also means callers do not care whether this module is exploded on disk
 * or packaged in a jar, which classpath-file helpers do care about.
 */
public final class FsdmSchema
{
	/** Classpath directory holding the ordered scripts. */
	public static final String DIRECTORY = "db";
	public static final String HISTORY_SCRIPT = "00.0.schema-update-history.sql";

	private static final List<String> ORDERED = List.of(
			HISTORY_SCRIPT,
			"00.1.setup.sql",
			"01.enterprise.sql",
			"02.activeflag.sql",
			"03.systems.sql",
			"04.classification-data-concept.sql",
			"05.classification.sql",
			"06.address.sql",
			"07.arrangement.sql",
			"08.product.sql",
			"09.resourceitem.sql",
			"10.party.sql",
			"11.rules.sql",
			"12.securitytoken.sql",
			"13.event.sql",
			"14.geography.sql",
			"15.time.sql",
			"16.transactions.sql",
			"17.foreign-key-indexes.sql",
			"18.query-indexes.sql",
			"19.structured-party-addresses.sql",
			"20.1.setup.sql",
			"21.uwe-query-indexes.sql",
			"22.relationship-indexes.sql",
			"23.document-query-indexes.sql",
			"24.domain-query-indexes.sql",
			"25.forum-notification-query-indexes.sql");

	private FsdmSchema()
	{
	}

	/**
	 * The schema scripts, in the order they must run.
	 *
	 * @return the ordered script file names
	 */
	public static List<String> orderedScripts()
	{
		return ORDERED;
	}

	/**
	 * Reads one schema script.
	 *
	 * @param script a file name from {@link #orderedScripts()}
	 * @return the script contents
	 * @throws IllegalStateException when the script is not on the classpath
	 */
	public static String read(String script)
	{
		String resource = "/" + DIRECTORY + "/" + script;
		try (InputStream stream = FsdmSchema.class.getResourceAsStream(resource))
		{
			if (stream == null)
			{
				throw new IllegalStateException("FSDM schema script is missing from the classpath: " + resource);
			}
			return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			throw new UncheckedIOException("Could not read FSDM schema script " + resource, e);
		}
	}

	/**
	 * Runs one schema script somewhere.
	 */
	@FunctionalInterface
	public interface ScriptExecutor
	{
		/**
		 * @param script the script file name
		 * @param sql    the complete tracked update, including its transaction
		 * @throws Exception whatever the underlying execution mechanism throws
		 */
		void run(String script, String sql) throws Exception;
	}

	/**
	 * Hands tracked updates, in order, to an executor. Already-applied updates are
	 * skipped by the database; changed checksums or gaps in history stop execution.
	 * <p>
	 * The executor decides how to run it — over JDBC, through {@code psql} in a container, or into a
	 * migration tool — so this class needs no database dependency of its own. Checked exceptions are
	 * wrapped so a caller can write the loop without ceremony, and the failure names the script.
	 *
	 * Execute the complete SQL with autocommit enabled, outside any caller transaction.
	 * Each update manages its own transaction and records success atomically. Executors
	 * must propagate SQL errors (for psql use ON_ERROR_STOP=1). Use read() for raw SQL.
	 * @param executor receives each script's file name and tracked SQL, in order
	 */
	public static void forEachScript(ScriptExecutor executor)
	{
		for (String script : ORDERED)
		{
			try
			{
				executor.run(script, updateSql(script));
			}
			catch (RuntimeException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new IllegalStateException("Failed applying FSDM schema script " + script, e);
			}
		}
	}

	/** PostgreSQL SQL that applies this script exactly once and records its success. */
	public static String updateSql(String script)
	{
		int sequence = ORDERED.indexOf(script);
		if (sequence < 0) throw new IllegalArgumentException("Unknown FSDM script: " + script);
		String body = normalized(read(script));
		String checksum = checksum(body);
		String previous = sequence == 0 ? "NULL" : literal(ORDERED.get(sequence - 1));
		return transactionStart() + "DO " + dollarQuote("""
				DECLARE recorded_checksum text; recorded_sequence integer;
				BEGIN
				  SELECT checksum, scriptsequence INTO recorded_checksum, recorded_sequence
				    FROM dbo.fsdmschemaupdate WHERE scriptname = %s;
				  IF FOUND THEN
				    IF recorded_sequence <> %d THEN
				      RAISE EXCEPTION 'FSDM update sequence changed for %%; append updates at the end', %s;
				    END IF;
				    IF recorded_checksum <> %s THEN
				      RAISE EXCEPTION 'FSDM script %% changed after application; append a new update instead', %s;
				    END IF;
				  ELSE
				    IF %s IS NOT NULL AND NOT EXISTS
				       (SELECT 1 FROM dbo.fsdmschemaupdate WHERE scriptname = %s AND scriptsequence = %d) THEN
				      RAISE EXCEPTION 'Previous FSDM update must be applied before %%', %s;
				    END IF;
				    IF EXISTS (SELECT 1 FROM dbo.fsdmschemaupdate WHERE scriptsequence >= %d) THEN
				      RAISE EXCEPTION 'FSDM update history is out of order at %%', %s;
				    END IF;
				    EXECUTE %s;
				    INSERT INTO dbo.fsdmschemaupdate (scriptname, scriptsequence, checksum, executionmode)
				      VALUES (%s, %d, %s, 'applied');
				  END IF;
				END;
				""".formatted(literal(script), sequence, literal(script), literal(checksum), literal(script),
				previous, previous, sequence - 1, literal(script), sequence, literal(script),
				dollarQuote(body), literal(script), sequence, literal(checksum))) + ";\nCOMMIT;\n";
	}

	/**
	 * Adopt an existing database's known last script without replaying its creation SQL.
	 * This is an explicit one-time assertion by the administrator, not schema inference.
	 */
	public static String baselineSql(String lastAppliedScript)
	{
		int last = ORDERED.indexOf(lastAppliedScript);
		if (last < 0) throw new IllegalArgumentException("Unknown FSDM baseline: " + lastAppliedScript);
		StringBuilder inserts = new StringBuilder();
		for (int sequence = 0; sequence <= last; sequence++)
		{
			String script = ORDERED.get(sequence);
			inserts.append("INSERT INTO dbo.fsdmschemaupdate (scriptname, scriptsequence, checksum, executionmode) VALUES (")
					.append(literal(script)).append(", ").append(sequence).append(", ")
					.append(literal(checksum(normalized(read(script))))).append(", 'baseline');\n");
		}
		return transactionStart() + "DO " + dollarQuote("""
				BEGIN
				  IF EXISTS (SELECT 1 FROM dbo.fsdmschemaupdate) THEN
				    RAISE EXCEPTION 'FSDM update history already exists; baselining would replace recorded progress';
				  END IF;
				  IF to_regclass('dbo.enterprise') IS NULL THEN
				    RAISE EXCEPTION 'Cannot baseline an empty database; run the updates normally';
				  END IF;
				%s
				END;
				""".formatted(inserts)) + ";\nCOMMIT;\n";
	}

	private static String transactionStart()
	{
		// One lock shared by all FSDM updaters in this database, automatically released
		// on commit or rollback, including when two tools apply the same update at once.
		return "BEGIN;\nSELECT pg_advisory_xact_lock(1179862093, 1);\n" + read(HISTORY_SCRIPT) + "\n";
	}

	private static String normalized(String sql)
	{
		return sql.replace("\r\n", "\n").replace("\r", "\n");
	}

	private static String checksum(String sql)
	{
		try
		{
			return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
					.digest(sql.getBytes(StandardCharsets.UTF_8)));
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new IllegalStateException("SHA-256 is unavailable", e);
		}
	}

	private static String literal(String value)
	{
		return "'" + value.replace("'", "''") + "'";
	}

	private static String dollarQuote(String body)
	{
		String tag = "$fsdm_body$";
		while (body.contains(tag)) tag = tag.substring(0, tag.length() - 1) + "_$";
		return tag + "\n" + body + "\n" + tag;
	}

	/** Write an update bundle or a one-time baseline for psql; no application restart required. */
	public static void main(String[] args) throws IOException
	{
		if (args.length == 2 && "--updates".equals(args[0]))
		{
			StringBuilder sql = new StringBuilder();
			forEachScript((script, update) -> sql.append(update).append('\n'));
			Files.writeString(Path.of(args[1]), sql, StandardCharsets.UTF_8);
		}
		else if (args.length == 3 && "--baseline".equals(args[0]))
		{
			Files.writeString(Path.of(args[2]), baselineSql(args[1]), StandardCharsets.UTF_8);
		}
		else
		{
			throw new IllegalArgumentException("Usage: FsdmSchema --updates output.sql | --baseline last-script.sql output.sql");
		}
	}
}
