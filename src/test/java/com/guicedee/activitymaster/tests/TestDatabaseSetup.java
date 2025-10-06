package com.guicedee.activitymaster.tests;

import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
public abstract class TestDatabaseSetup
{

    public static void runScript(Mutiny.SessionFactory sessionFactory, String resourcePath)
    {
        String sql;
        try (InputStream in = TestActivityMaster.class.getClassLoader()
                                      .getResourceAsStream(resourcePath))
        {
            if (in == null)
            {
                throw new IllegalArgumentException("Script not found: " + resourcePath);
            }
            sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to read SQL script: " + resourcePath, e);
        }

        // Split by semicolon only if not inside function bodies (simplified)
        List<String> statements = Arrays.stream(sql.split(";\\s*(?=(?:[^']*'[^']*')*[^']*$)"))
                                          .map(String::trim)
                                          .filter(s -> !s.isEmpty())
                                          .toList()
                ;

        sessionFactory.withSession(session1 -> {
                    return session1.withTransaction(tx -> {
                        List<Uni<?>> executions = new ArrayList<>();
                        for (String stmt : statements)
                        {
                            executions.add(session1.createNativeQuery(stmt)
                                                   .executeUpdate()
                                                   .log("Executed: " + stmt));
                        }
                        return Uni.combine()
                                       .all()
                                       .unis(executions)
                                       .discardItems();
                    });
                })
                .await()
                .atMost(Duration.of(50L, ChronoUnit.SECONDS))
        ;
        System.out.println("Done");
    }
}
