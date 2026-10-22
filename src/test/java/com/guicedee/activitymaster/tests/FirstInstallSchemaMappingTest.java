package com.guicedee.activitymaster.tests;

import com.guicedee.activitymaster.fsdm.db.entities.time.Quarters;
import com.guicedee.activitymaster.fsdm.transactions.TransactionType;
import jakarta.persistence.Entity;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.mapping.Column;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/** Hibernate must resolve the reported first-install mappings without an invented join table. */
class FirstInstallSchemaMappingTest {
    static StandardServiceRegistry registry;
    static Metadata metadata;

    @BeforeAll static void mappings() throws Exception {
        registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .applySetting("hibernate.boot.allow_jdbc_metadata_access", false)
                .build();
        MetadataSources sources = new MetadataSources(registry);
        Path root = Path.of(Quarters.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        try (var classes = Files.walk(root.resolve("com/guicedee/activitymaster/fsdm"))) {
            for (Path file : classes.filter(p -> p.toString().endsWith(".class")).toList()) {
                String name = root.relativize(file).toString().replace('\\', '.').replace('/', '.');
                Class<?> type = Class.forName(name.substring(0, name.length() - 6), false, Quarters.class.getClassLoader());
                if (type.isAnnotationPresent(Entity.class)) sources.addAnnotatedClass(type);
            }
        }
        metadata = sources.buildMetadata();
        assertTrue(metadata.getEntityBindings().size() >= 225, "Include the complete core entity graph");
    }

    @AfterAll static void close() {
        if (registry != null) StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test void transactionTypesMatchCanonicalSql() {
        var type = metadata.getEntityBinding(TransactionType.class.getName());
        Column code = type.getProperty("code").getColumns().getFirst();
        Column description = type.getProperty("description").getColumns().getFirst();
        assertEquals("varchar(255)", code.getSqlType(metadata));
        assertEquals("varchar(255)", description.getSqlType(metadata));
    }

    @Test void quarterMonthsUsesTheExistingForeignKeyColumn() {
        var months = metadata.getCollectionBinding(Quarters.class.getName() + ".lUMonthsList");
        assertTrue(months.isInverse());
        assertEquals("months", months.getCollectionTable().getName().toLowerCase(Locale.ROOT));
        assertEquals("time", months.getCollectionTable().getSchema().toLowerCase(Locale.ROOT));
        assertEquals("quarterid", months.getKey().getColumns().getFirst().getName().toLowerCase(Locale.ROOT));
        for (var namespace : metadata.getDatabase().getNamespaces()) {
            for (var table : namespace.getTables()) {
                assertNotEquals("quarters_months", table.getName().toLowerCase(Locale.ROOT));
            }
        }
    }
}
