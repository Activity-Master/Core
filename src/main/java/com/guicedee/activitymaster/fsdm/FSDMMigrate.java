package com.guicedee.activitymaster.fsdm;

import com.guicedee.client.Environment;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedpersistence.btm.BTMTransactionIsolation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.metamodel.EntityType;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Custom manager to copy data from one EntityManager to another
 */
@Log4j2
public class FSDMMigrate
{

    // Factory for destination EntityManager
    private static final EntityManagerFactory destinationFactory = buildEntityManagerFactory();

    // Registry to track processed entities (prevents duplication or backward looping)
    private static final Set<String> completedEntityRegistry = Collections.synchronizedSet(new HashSet<>());
    private static Consumer<String> logProgress;
    private static final int CACHE_CLEAR_BATCH_SIZE = 100;

    // Build the EntityManagerFactory for the target connection
    private static EntityManagerFactory buildEntityManagerFactory() {
        // PostgreSQL (Postgres) connection properties
        final Properties postgresProperties = new Properties();
        postgresProperties.setProperty("jakarta.persistence.jdbc.url",
                "jdbc:postgresql://" + Environment.getSystemPropertyOrEnvironment("FSDM_DBSERVER_2", "localhost:5432") + "/" + Environment.getSystemPropertyOrEnvironment("FSDM_DBNAME_2", "fsdm"));
        postgresProperties.setProperty("jakarta.persistence.jdbc.user", Environment.getSystemPropertyOrEnvironment("FSDM_USER_2", "fsdm"));
        postgresProperties.setProperty("jakarta.persistence.jdbc.password", Environment.getSystemPropertyOrEnvironment("PG_PASSWORD_2", "nopassword"));
        postgresProperties.setProperty("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");

        // Hibernate-specific properties for better configuration
        postgresProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        postgresProperties.setProperty("hibernate.hbm2ddl.auto", "none"); // Disable schema auto-generation for safety
        postgresProperties.setProperty("hibernate.show_sql", "true");
        postgresProperties.setProperty("hibernate.format_sql", "false");
        postgresProperties.setProperty("jakarta.persistence.transactionType", "RESOURCE_LOCAL");
        postgresProperties.setProperty("hibernate.transaction.coordinator_class", "jdbc"); // Force Hibernate to use direct JDBC transactions (not JTA)
        postgresProperties.setProperty("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory"); // Older Hibernate versions

        postgresProperties.setProperty("hibernate.use_sql_comments", "false");

        postgresProperties.setProperty("hibernate.cache.use_second_level_cache", "false");
        postgresProperties.setProperty("hibernate.cache.use_query_cache", "false");
        postgresProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.internal.NoCachingRegionFactory");


        // Create and return the EntityManagerFactory for the target database
        return new HibernatePersistenceProvider().createEntityManagerFactory("ActivityMaster", postgresProperties);
    }

    /**
     * Loads all entities starting from "Enterprise" and performs a recursive copy of entities to the destination.
     *
     * @param sourceEntityManager Source EntityManager
     * @param logProgress         Consumer to log progress messages
     */
    public static void migrateAll(EntityManager sourceEntityManager, Consumer<String> logProgress) {
        logProgress.accept("Starting migration...");

        EntityManager destinationEntityManager = destinationFactory.createEntityManager();
        try {
            // Begin migration with the root entity "Enterprise"
            Class<?> rootEntity = getEnterpriseEntityType(sourceEntityManager);
            if (rootEntity == null) {
                logProgress.accept("Root entity 'Enterprise' not found in the source database.");
                return;
            }

            logProgress.accept("Migrating root entity: Enterprise");
            processEntityRecursively(sourceEntityManager, destinationEntityManager, rootEntity, logProgress);
        } finally {
            if (destinationEntityManager.isOpen()) {
                destinationEntityManager.close();
            }
        }

        logProgress.accept("Migration completed.");
    }

    /**
     * Recursively processes the given entity and all its child entities.
     *
     * @param sourceEntityManager      Source persistence context
     * @param destinationEntityManager Destination persistence context
     * @param entityClass              The current entity class being processed
     * @param logProgress              Consumer to log progress messages
     */
    private static void processEntityRecursively(EntityManager sourceEntityManager, EntityManager destinationEntityManager,
                                                 Class<?> entityClass, Consumer<String> logProgress) {
        if (completedEntityRegistry.contains(entityClass.getName())) {
            logProgress.accept("Skipping already processed entity: " + entityClass.getName());
            return;
        }

        completedEntityRegistry.add(entityClass.getName());
        logProgress.accept("Starting migration for entity: " + entityClass.getName());

        // Open a transaction in the destination EntityManager
        destinationEntityManager.getTransaction().begin();
        destinationEntityManager.clear();
        Session destinationSession = destinationEntityManager.unwrap(Session.class);

        // Stream entities from the source EntityManager
        List<Object> batch = new ArrayList<>();
        var entityStream = sourceEntityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
        {

            AtomicInteger count = new AtomicInteger(0);
            for (Object entity : entityStream)
            {
                batch.add(entity);
                count.getAndIncrement();

                // Copy the current entity and persist it
                Optional<Object> copiedEntity = copyAndAssignUUID(entity);
                if (copiedEntity.isPresent())
                {
                    persistEntity(destinationSession, copiedEntity.get());
                }

                // Periodically clear cache to prevent memory issues
                if (count.get() % CACHE_CLEAR_BATCH_SIZE == 0)
                {
                    logProgress.accept("Processed " + count + " entities of type " + entityClass.getName() + ". Clearing cache...");
                    clearEntityManagerCache(sourceEntityManager, destinationEntityManager);
                    destinationSession.flush();
                    destinationSession.clear();
                }
            }
        }

        // Final flush and cache clear on remaining entities
        if (!batch.isEmpty()) {
            logProgress.accept("Finalizing batch for " + entityClass.getName());
            destinationSession.flush();
            destinationSession.clear();
            clearEntityManagerCache(sourceEntityManager, destinationEntityManager);
        }
        destinationEntityManager.getTransaction().commit();
        logProgress.accept("Finished migrating entity: " + entityClass.getName());
        for (Object o : entityStream)
        {
            // Check for and process related entities
            logProgress.accept("Traversing Children: " + entityClass.getName());
            traverseAndCopyRelatedEntities(sourceEntityManager, destinationSession, o, logProgress);
        }

    }

    /**
     * Traverses and copies all related child entities for the given entity recursively.
     *
     * @param sourceEntityManager Source persistence context
     * @param destinationSession  Destination persistence context
     * @param entity              The current entity being processed
     * @param logProgress         Consumer to log progress messages
     */
    private static void traverseAndCopyRelatedEntities(EntityManager sourceEntityManager, Session destinationSession,
                                                       Object entity, Consumer<String> logProgress) {
        // Use reflection to find child entities
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true); // Allow access to private fields

            if (Collection.class.isAssignableFrom(field.getType())) {
                try {
                    Collection<?> relatedEntities = (Collection<?>) field.get(entity);
                    if (relatedEntities == null || relatedEntities.isEmpty()) {
                        continue;
                    }

                    logProgress.accept("Found related collection: " + field.getName() + " for entity: " + entity.getClass().getName());
                    for (Object childEntity : relatedEntities) {
                        // Copy child entity to destination
                        Optional<Object> copiedChild = copyAndAssignUUID(childEntity);
                        if(copiedChild.isPresent())
                        persistEntity(destinationSession, copiedChild.get());

                        // Recursively process child entities
                        //traverseAndCopyRelatedEntities(sourceEntityManager, destinationSession, childEntity, logProgress);
                    }
                } catch (Exception ex) {
                    logProgress.accept("Error while traversing field: " + field.getName() + " - " + ex.getMessage());
                }
            }
        }
    }

    /**
     * Clears the persistence context (cache) of the provided EntityManager instances.
     *
     * @param sourceEntityManager      Source EntityManager
     * @param destinationEntityManager Destination EntityManager
     */
    private static void clearEntityManagerCache(EntityManager sourceEntityManager, EntityManager destinationEntityManager) {
        sourceEntityManager.clear();
        destinationEntityManager.clear();
        if (sourceEntityManager.getEntityManagerFactory().getCache() != null) {
            sourceEntityManager.getEntityManagerFactory().getCache().evictAll();
        }
        if (destinationEntityManager.getEntityManagerFactory().getCache() != null) {
            destinationEntityManager.getEntityManagerFactory().getCache().evictAll();
        }
    }

    /**
     * Finds the root "Enterprise" entity type from the metamodel.
     *
     * @param sourceEntityManager Source EntityManager
     * @return "Enterprise" entity type or null if not found
     */
    private static Class<?> getEnterpriseEntityType(EntityManager sourceEntityManager) {
        for (EntityType<?> entityType : sourceEntityManager.getMetamodel().getEntities()) {
            if (entityType.getName().equalsIgnoreCase("Enterprise")) {
                return entityType.getJavaType();
            }
        }
        return null;
    }


    private static Optional<Object> copyAndAssignUUID(Object sourceEntity) {
        if (sourceEntity == null) {
            return Optional.empty(); // Base case for null entities
        }

        try {
            // Create a new instance of the entity's class
            Class<?> entityClass = sourceEntity.getClass();
            Object newEntity = entityClass.getDeclaredConstructor().newInstance();

            // Iterate over fields in the source entity
            for (Field field : entityClass.getDeclaredFields()) {
                // Skip fields that are final or static, as they cannot be modified
                int modifiers = field.getModifiers();
                if (
                        Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers) ||
                        field.getName().startsWith("$$_")
                ) {
                    continue; // Skip processing this field
                }

                field.setAccessible(true); // Access private fields

                Object fieldValue = field.get(sourceEntity);

                if(field.getName().equals("activeId"))
                {
                    System.out.println("Here");
                }
                // Handle UUID field specifically, assign a new UUID
               if (Collection.class.isAssignableFrom(field.getType())) {
                    /*Collection<?> sourceCollection = (Collection<?>) fieldValue;
                    if (sourceCollection != null) {
                        Collection<Object> newCollection = createNewCollectionInstance(field.getType());
                        for (Object childEntity : sourceCollection) {
                            // Recursively copy child entities
                            newCollection.add(copyAndAssignUUID(childEntity));
                        }
                        field.set(newEntity, newCollection); // Set copied collection
                    }*/
                }
                // Handle Maps
                else if (Map.class.isAssignableFrom(field.getType())) {
                   /* Map<?, ?> sourceMap = (Map<?, ?>) fieldValue;
                    if (sourceMap != null) {
                        Map<Object, Object> newMap = createNewMapInstance(field.getType());
                        for (Map.Entry<?, ?> entry : sourceMap.entrySet()) {
                            // Recursively copy keys and values
                            Object newKey = copyAndAssignUUID(entry.getKey());
                            Object newValue = copyAndAssignUUID(entry.getValue());
                            newMap.put(newKey, newValue);
                        }
                        field.set(newEntity, newMap); // Set copied map
                    }*/
                }
                // Check for nested child entities (non-primitive, non-final classes)
                else if (!field.getType().isPrimitive() && !isWrapperType(field.getType()) && !field.getType().equals(String.class) && !field.getType().equals(UUID.class)  && !field.getType().equals(OffsetDateTime.class) && !field.getType().equals(LocalDate.class)) {
                    // Recursively copy related entity
                   Optional<Object> o = copyAndAssignUUID(fieldValue);
                   if(o.isPresent())
                    field.set(newEntity,o.get());
                } else {
                    // Copy primitive or immutable fields directly
                    field.set(newEntity, fieldValue);
                }
            }
            return Optional.of(newEntity);
        } catch (Exception e) {
            log.error("Error copying entity: " + sourceEntity.getClass().getSimpleName(), e);
            if(logProgress != null)
                logProgress.accept("Error copying entity: " + sourceEntity.getClass().getSimpleName() + " - " + e.getMessage());
            return Optional.empty();
        }
    }

    // Helper method to create a new instance of a collection
    private static Collection<Object> createNewCollectionInstance(Class<?> collectionType) {
        if (Set.class.isAssignableFrom(collectionType)) { // For Set types
            return new HashSet<>();
        } else if (List.class.isAssignableFrom(collectionType)) { // For List types
            return new ArrayList<>();
        }
        throw new UnsupportedOperationException("Unsupported collection type: " + collectionType);
    }

    // Helper method to create a new instance of a map
    private static Map<Object, Object> createNewMapInstance(Class<?> mapType) {
        if (Map.class.isAssignableFrom(mapType)) {
            return new HashMap<>();
        }
        throw new UnsupportedOperationException("Unsupported map type: " + mapType);
    }

    // Helper method to check for wrappers of primitive types
    private static boolean isWrapperType(Class<?> type) {
        return type.equals(Boolean.class) || type.equals(Integer.class) ||
                type.equals(Character.class) || type.equals(Byte.class) ||
                type.equals(Short.class) || type.equals(Double.class) ||
                type.equals(Long.class) || type.equals(Float.class);
    }

    /**
     * Persists the given entity in the destination session.
     *
     * @param destinationSession Destination persistence context
     * @param entity             The entity to persist
     */
    private static void persistEntity(Session destinationSession, Object entity) {
        destinationSession.merge(entity);
    }

    /**
     * Traverses and copies related entities for the given entity.
     *
     * @param sourceEntityManager Source persistence context
     * @param destinationSession  Destination persistence context
     * @param entity              The current entity being processed
     */
    private static void traverseAndCopyRelatedEntities(EntityManager sourceEntityManager, Session destinationSession, Object entity)
    {
        Class<?> entityClass = entity.getClass();

        for (Field field : entityClass.getDeclaredFields())
        {
            field.setAccessible(true);

            try
            {
                // Check if the field is a collection of entities
                if (List.class.isAssignableFrom(field.getType()))
                {
                    @SuppressWarnings("unchecked")
                    List<Object> relatedEntities = (List<Object>) field.get(entity);

                    if (relatedEntities != null)
                    {
                        for (Object relatedEntity : relatedEntities)
                        {
                            // Check if related entity is already registered
                            String relatedEntityKey = generateEntityKey(relatedEntity);
                            if (completedEntityRegistry.contains(relatedEntityKey))
                            {
                                continue; // Skip already processed related entities
                            }

                            // Register the related entity as processed
                            completedEntityRegistry.add(relatedEntityKey);

                            // Process and persist the related entity
                            Object copiedEntity = copyAndAssignUUID(relatedEntity);
                            persistEntity(destinationSession, copiedEntity);

                            // Recursive processing
                            traverseAndCopyRelatedEntities(sourceEntityManager, destinationSession, relatedEntity);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException("Error processing related entities for " + entityClass.getSimpleName(), e);
            }
        }
    }

    /**
     * Generates a unique key for an entity based on its class and UUID.
     *
     * @param entity The entity
     * @return A string representing the entity's unique key
     */
    private static String generateEntityKey(Object entity)
    {
        try
        {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            UUID idValue = (UUID) idField.get(entity);

            return entity.getClass().getSimpleName() + "-" + idValue;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to generate key for entity: " + entity.getClass().getSimpleName(), e);
        }
    }
}