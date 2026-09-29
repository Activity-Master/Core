package com.guicedee.activitymaster.fsdm.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
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

	private static final List<String> ORDERED = List.of(
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
			"20.1.setup.sql");

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
		 * @param sql    its contents
		 * @throws Exception whatever the underlying execution mechanism throws
		 */
		void run(String script, String sql) throws Exception;
	}

	/**
	 * Hands every script, in order, to an executor.
	 * <p>
	 * The executor decides how to run it — over JDBC, through {@code psql} in a container, or into a
	 * migration tool — so this class needs no database dependency of its own. Checked exceptions are
	 * wrapped so a caller can write the loop without ceremony, and the failure names the script.
	 *
	 * @param executor receives each script's file name and contents, in order
	 */
	public static void forEachScript(ScriptExecutor executor)
	{
		for (String script : ORDERED)
		{
			try
			{
				executor.run(script, read(script));
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
}
