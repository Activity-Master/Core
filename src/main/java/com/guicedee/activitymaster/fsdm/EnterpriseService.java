package com.guicedee.activitymaster.fsdm;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IPasswordsService;
import com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassifications;
import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemInstall;
import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.systems.IMasterSystem;
import com.guicedee.activitymaster.fsdm.client.services.systems.IProgressable;
import com.guicedee.activitymaster.fsdm.client.services.systems.ISystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.systems.SortedUpdate;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.EnterpriseXClassification;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.EnterpriseXClassification_;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise_;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.fsdm.systems.EventsSystem;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.client.scopes.CallScoper;
import io.github.classgraph.ClassInfo;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.entityassist.enumerations.Operand.InList;
import static com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.applicationEnterpriseName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassifications.UpdateClass;

@SuppressWarnings("DuplicatedCode")
@Log4j2
@Singleton
public class EnterpriseService
        implements IProgressable,
        IEnterpriseService<EnterpriseService>
{

    @com.google.inject.Inject
    private Mutiny.SessionFactory sessionFactory;

    // Local, lightweight name -> ID cache to enable fast UUID lookups that can hit 2nd-level cache
    private final Map<String, UUID> enterpriseNameToId = new ConcurrentHashMap<>();
    private final Map<String, IEnterprise<?, ?>> enterpriseNameToEntity = new ConcurrentHashMap<>();
    private final Map<UUID, IEnterprise<?, ?>> enterpriseIdToEntity = new ConcurrentHashMap<>();

    public IEnterprise<?, ?> get()
    {
        return new Enterprise();
    }


    /**
     * Find-or-create the lean enterprise record on a {@link Mutiny.StatelessSession}.
     * <p>
     * A stateless session has no persistence context, so the insert is a pure JDBC write
     * (batchable, no dirty-checking). Idempotent: an existing in-date-range enterprise with the
     * same name is returned instead of inserting a duplicate.
     */
    @Override
    public Uni<IEnterprise<?, ?>> create(Mutiny.StatelessSession session, @NotNull String name, @NotNull String description)
    {
        Enterprise enterprise = new Enterprise();
        enterprise.setName(name);
        enterprise.setDescription(description);
        //noinspection unchecked
        return (Uni) enterprise
                .builder(session)
                .withName(name)
                .get()
                .onFailure(NoResultException.class)
                .recoverWithUni(u -> enterprise
                        .persist(session)
                        .replaceWith(enterprise));
    }

    @Override
    public Uni<Integer> loadUpdates(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
    {
        ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);

        return getUpdates(session, enterprise)
                .chain(availableUpdates -> {
                    log.info(MessageFormat.format("There are {0} required updates (stateless) - {1}",
                                                  availableUpdates.size(),
                                                  availableUpdates
                    ));

                    setCurrentTask(0);
                    int tasks = 0;
                    for (Map.Entry<Integer, Class<? extends ISystemUpdate>> entry : availableUpdates.entrySet()) {
                        Class<? extends ISystemUpdate> aClass = entry.getValue();
                        tasks += aClass
                                .getAnnotation(SortedUpdate.class)
                                .taskCount();
                    }

                    @SuppressWarnings({"rawtypes", "unchecked"})
                    Set<IOnSystemUpdate> systemUpdateEventHandlers = IGuiceContext.loaderToSet(ServiceLoader.load(
                            IOnSystemUpdate.class));

                    setTotalTasks(tasks);

                    return systemsService
                            .getActivityMaster(session, enterprise)
                            .chain(system -> {
                                // Process updates sequentially using recursion
                                return processUpdates(session,
                                                      new ArrayList<>(availableUpdates.entrySet()),
                                                      0,
                                                      enterprise,
                                                      system,
                                                      systemUpdateEventHandlers
                                )
                                        .chain(() -> {
                                            return updateLastUpdateDate(session, enterprise, system)
                                                    .invoke(a -> {
                                                        logProgress("Update System",
                                                                    "Finished Updates (stateless). Last Update Date - " + DateTimeFormatter
                                                                            .ofPattern("yyyy/MM/dd")
                                                                            .format(LocalDate.now())
                                                        );
                                                    })
                                                    .chain(a -> Uni
                                                            .createFrom()
                                                            .item(availableUpdates.size()));
                                        });
                            });
                });
    }

    private Uni<Void> processUpdates(Mutiny.StatelessSession session, List<Map.Entry<Integer, Class<? extends ISystemUpdate>>> updates,
                                     int index,
                                     IEnterprise<?, ?> enterprise,
                                     ISystems<?, ?> system,
                                     Set<IOnSystemUpdate> systemUpdateEventHandlers
    )
    {
        // Base case: if we've processed all updates, return a completed Uni
        if (index >= updates.size()) {
            return Uni
                    .createFrom()
                    .voidItem();
        }

        // Get the current update
        Map.Entry<Integer, Class<? extends ISystemUpdate>> entry = updates.get(index);
        Class<? extends ISystemUpdate> value = entry.getValue();

        // Create a Uni that processes the current update
        return Uni
                .createFrom()
                .item(() -> {
                    logProgress("Update System", "Starting updates (stateless) for " + value.getSimpleName());
                    for (IOnSystemUpdate<?> systemUpdateEventHandler : systemUpdateEventHandlers) {
                        systemUpdateEventHandler.onSystemUpdateStart(value);
                    }
                    return com.guicedee.client.IGuiceContext.get(value);
                })
                .chain(o -> {
                    // Perform the update
                    return performUpdate(session, o, enterprise)
                            .onItem()
                            .invoke(() -> {
                                for (IOnSystemUpdate<?> a : systemUpdateEventHandlers) {
                                    a.onSystemUpdateEnd(value);
                                }
                            });
                })
                // Propagate the failure to the transaction owner; never commit a partial sweep.
                .onFailure().invoke(err -> {
                    for (IOnSystemUpdate<?> handler : systemUpdateEventHandlers) {
                        try {
                            handler.onSystemUpdateFail(value);
                        } catch (Throwable notificationFailure) {
                            if (notificationFailure != err) err.addSuppressed(notificationFailure);
                        }
                    }
                    log.error("System update failed: " + value.getName(), err);
                })
                // Process the next update recursively
                .chain(() -> processUpdates(session,
                                            updates,
                                            index + 1,
                                            enterprise,
                                            system,
                                            systemUpdateEventHandlers
                ));
    }

    Uni<Void> updateLastUpdateDate(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
    {
        return enterprise.addOrUpdateClassification(session,
                                                    EnterpriseClassifications.LastUpdateDate.toString(),
                                                    DateTimeFormatter
                                                            .ofPattern("yyyy/MM/dd")
                                                            .format(LocalDate.now()),
                                                    system
                         )
                         .replaceWithVoid();
    }


    Uni<Void> performUpdate(Mutiny.StatelessSession session, ISystemUpdate o, IEnterprise<?, ?> enterprise)
    {
        ISystemsService<?> systemsService = com.guicedee.client.IGuiceContext.get(ISystemsService.class);
        return systemsService
                .getActivityMaster(session, enterprise)
                .chain(activityMasterSystem -> {
                    // Explicitly cast to ISystems<?, ?>
                    ISystems<?, ?> system = (ISystems<?, ?>) activityMasterSystem;
                    return o
                            .update(session, enterprise)
                            .chain(updateResult -> {
                                if (!Boolean.TRUE.equals(updateResult)) {
                                    return Uni.createFrom().failure(new IllegalStateException(
                                            "Update did not report success: " + o.getClass().getName()));
                                }
                                return enterprise
                                        .addClassification(session,
                                                           UpdateClass.toString(),
                                                           o
                                                                   .getClass()
                                                                   .getCanonicalName(),
                                                           system
                                        )
                                        .replaceWithVoid();
                            });
                });

    }

    @Override
    public Uni<Set<String>> getEnterpriseAppliedUpdates(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
    {
        ISystemsService<?> systemsService = com.guicedee.client.IGuiceContext.get(ISystemsService.class);

        return systemsService
                .getActivityMaster(session, enterprise)
                .chain(system -> {
                    return enterprise
                            .findClassifications(session, UpdateClass.toString(), system)
                            .map(classificationsAll -> {
                                Set<String> set = new LinkedHashSet<>();
                                for (IRelationshipValue<?, IClassification<?, ?>, ?> rel : classificationsAll) {
                                    String classValue = rel.getValue();
                                    if (classValue.contains("$$EnhancerByGuice$$")) {
                                        classValue = classValue.substring(0, classValue.indexOf("$$EnhancerByGuice$$"));
                                    }
                                    set.add(classValue);
                                }
                                return set;
                            });
                });
    }

    @Override
    public Uni<Map<Integer, Class<? extends ISystemUpdate>>> getUpdates(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
    {
        // First, synchronously collect all available updates
        return Uni
                .createFrom()
                .item(() -> {
                    Map<Integer, Class<? extends ISystemUpdate>> availableUpdates = new TreeMap<>();
                    for (ClassInfo classInfo : GuiceContext
                            .instance()
                            .getScanResult()
                            .getClassesWithAnnotation(SortedUpdate.class.getCanonicalName())) {
                        if (classInfo.isAbstract() || classInfo.isInterface()) {
                            continue;
                        }

                        @SuppressWarnings("unchecked")
                        Class<? extends ISystemUpdate> clazz = (Class<? extends ISystemUpdate>) classInfo.loadClass();
                        SortedUpdate du = clazz.getAnnotation(SortedUpdate.class);
                        availableUpdates.put(du.sortOrder(), clazz);
                    }
                    return availableUpdates;
                })
                // Then chain with getting enterprise applied updates
                .chain(availableUpdates -> {
                    return getEnterpriseAppliedUpdates(session, enterprise)
                            .map(enterpriseAppliedUpdates -> {
                                // Filter available updates to get applicable ones
                                Map<Integer, Class<? extends ISystemUpdate>> applicableUpdates = new TreeMap<>();
                                for (Map.Entry<Integer, Class<? extends ISystemUpdate>> entry : availableUpdates.entrySet()) {
                                    Integer key = entry.getKey();
                                    Class<? extends ISystemUpdate> value = entry.getValue();
                                    String classValue = value.getCanonicalName();
                                    if (classValue.contains("$$EnhancerByGuice$$")) {
                                        classValue = classValue.substring(0, classValue.indexOf("$$EnhancerByGuice$$"));
                                    }
                                    SortedUpdate du = value.getAnnotation(SortedUpdate.class);
                                    if (!enterpriseAppliedUpdates.contains(classValue) || du.force()) {
                                        applicableUpdates.put(key, value);
                                    }
                                }
                                return applicableUpdates;
                            });
                });
    }


    @Override
    public Map<Integer, Class<? extends ISystemUpdate>> getAllUpdates()
    {
        Map<Integer, Class<? extends ISystemUpdate>> availableUpdates = new TreeMap<>();
        for (ClassInfo classInfo : GuiceContext
                .instance()
                .getScanResult()
                .getClassesWithAnnotation(SortedUpdate.class.getCanonicalName())) {
            if (classInfo.isAbstract() || classInfo.isInterface()) {
                continue;
            }

            @SuppressWarnings("unchecked")
            Class<? extends ISystemUpdate> clazz = (Class<? extends ISystemUpdate>) classInfo.loadClass();
            SortedUpdate du = clazz.getAnnotation(SortedUpdate.class);
            availableUpdates.put(du.sortOrder(), clazz);
        }
        Map<Integer, Class<? extends ISystemUpdate>> applicableUpdates = new TreeMap<>();
        for (Map.Entry<Integer, Class<? extends ISystemUpdate>> entry : availableUpdates.entrySet()) {
            Integer key = entry.getKey();
            Class<? extends ISystemUpdate> value = entry.getValue();
            applicableUpdates.put(key, value);
        }
        return applicableUpdates;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Uni<List<IEnterprise<?, ?>>> findEnterprisesWithClassification(Mutiny.StatelessSession session, IClassification<?, ?> classification)
    {
        return (Uni) new EnterpriseXClassification()
                .builder(session)
                .withClassification(classification)
                .inActiveRange()
                .inDateRange()
                .selectColumn(EnterpriseXClassification_.enterpriseID)
                .getAll()
                .map(list -> {
                    List<UUID> ids = new ArrayList<>();
                    for (Object o : list) {
                        if (o instanceof UUID) {
                            ids.add((UUID) o);
                        }
                        else if (o instanceof IEnterprise) {
                            ids.add((UUID) ((IEnterprise) o).getId());
                        }
                    }
                    return ids;
                })
                .chain(
                        classy -> {
                            if (classy.isEmpty()) {
                                return Uni.createFrom()
                                          .item(Collections.emptyList());
                            }
                            EnterpriseQueryBuilder builder = new Enterprise().builder(session);
                            builder = builder.where(Enterprise_.id, InList, classy);
                            return builder.getAll();
                        });

    }


    @Override
    @CacheResult(cacheName = "EnterpriseGetByNameStateless")
    public Uni<IEnterprise<?, ?>> getEnterprise(Mutiny.StatelessSession session, @CacheKey String name)
    {
        log.trace(" Session & transaction started for enterprise lookup: {}", name);
        //noinspection unchecked,rawtypes
        return (Uni) new Enterprise()
                .builder(session)
                .withName(name)
                .inDateRange()
                //.setCacheName("getEnterpriseByName","default")
                .get();
    }


    @Override
    @CacheResult(cacheName = "EnterpriseGetByIdStateless")
    public Uni<IEnterprise<?, ?>> getEnterprise(Mutiny.StatelessSession session, @CacheKey UUID uuid)
    {
        //noinspection unchecked
        return (Uni) session.get(Enterprise.class, uuid);
    }

    private void cacheEnterprise(IEnterprise<?, ?> enterprise)
    {
        if (enterprise == null || enterprise.getId() == null || enterprise.getName() == null) {
            return;
        }
        Enterprise prepped = new Enterprise(enterprise.getId(), enterprise.getName(), enterprise.getDescription());
        prepped.setFake(false);
        enterpriseNameToId.put(prepped.getName(), prepped.getId());
        enterpriseNameToEntity.put(prepped.getName(), prepped);
        enterpriseIdToEntity.put(prepped.getId(), prepped);
    }

    @Override
    public Uni<IEnterprise<?, ?>> startNewEnterprise(Mutiny.StatelessSession session, String enterpriseName,
                                                     @NotNull String adminUserName, @NotNull String adminPassword
    )
    {
        return startNewEnterprise(session, enterpriseName, adminUserName, adminPassword, null);
    }

    @Override
    public Uni<IEnterprise<?, ?>> startNewEnterprise(Mutiny.StatelessSession session, String enterpriseName,
                                                     @NotNull String adminUserName, @NotNull String adminPassword, UUID uuidIdentifier
    )
    {
        // Genuine stateless entry point — NO bridge to a managed session. Every phase runs in its own
        // stateless transaction (each commits before the next, so FK references made by later phases see
        // the earlier committed rows), and within a phase all systems share one stateless session
        // (stateless inserts execute immediately → read-your-writes). Base systems and security
        // prerequisites are created before createNewEnterprise registers the complete system set.
        //
        // This method is a top-level entry point and must not be nested inside another open transaction.
        return bootstrapScope(() -> {
            IGuiceContext.get(ActivityMasterConfiguration.class)
                         .setSecurityEnabled(false);

            Set<IMasterSystem<?>> allSystems = ActivityMasterConfiguration.get()
                                                                          .getAllSystems();
            int totalTasks = allSystems.stream()
                                       .mapToInt(IMasterSystem::totalTasks)
                                       .sum() + 1;
            logProgress("Create Enterprise", "Creating Enterprise", 0, totalTasks);
            return sessionFactory.withStatelessTransaction(s -> installEnterprise(s, enterpriseName))
                                 .chain(enterprise -> createNewEnterprise(session, enterprise))
                                 .chain(enterprise -> sessionFactory.withStatelessTransaction(s -> {
                                     ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                                     return systemsService.getActivityMaster(s, enterprise)
                                                          .chain(activityMasterSystem -> {
                                                              ISystems<?, ?> system = (ISystems<?, ?>) activityMasterSystem;
                                                              IPasswordsService<?> passwordsService = IGuiceContext.get(IPasswordsService.class);
                                                              return passwordsService.createAdminAndCreatorUserForEnterprise(s,
                                                                                                                             system,
                                                                                                                             adminUserName,
                                                                                                                             adminPassword,
                                                                                                                             uuidIdentifier
                                                              );
                                                          })
                                                          .replaceWith(enterprise);
                                 }))
                                 .chain(enterprise -> {
                                     logProgress("Systems", "Running Systems Post Startups", 1);
                                     return sessionFactory.withStatelessTransaction(s -> performPostStartup(s, enterprise))
                                                          .replaceWith(enterprise);
                                 });
        });
    }

    /**
     * Stateless variant of {@link #installEnterprise(Mutiny.StatelessSession, String)}.
     */
    private Uni<IEnterprise<?, ?>> installEnterprise(Mutiny.StatelessSession session, String enterpriseName)
    {
        com.guicedee.client.IGuiceContext
                .get(ActivityMasterConfiguration.class)
                .setApplicationEnterpriseName(enterpriseName);
        return create(session, enterpriseName, enterpriseName);
    }

    @Override
    public Uni<IEnterprise<?, ?>> createNewEnterprise(Mutiny.StatelessSession session, @NotNull IEnterprise<?, ?> enterprise)
    {
        // Genuine stateless lifecycle: every phase runs in its own stateless transaction (each commits
        // before the next), mirroring the managed createNewEnterprise phasing exactly — only the session
        // kind differs. No bridge to a managed session.
        return bootstrapScope(() -> {
            IGuiceContext.get(ActivityMasterConfiguration.class)
                         .setSecurityEnabled(false);
            Set<IMasterSystem<?>> allSystems = ActivityMasterConfiguration.get()
                                                                          .getAllSystems();

            return sessionFactory.withStatelessTransaction(s1 -> create(s1, enterprise.getName(), enterprise.getName()))
                                 .chain(ent -> sessionFactory.withStatelessTransaction(s2 -> createBase(s2, allSystems, ent))
                                                             .replaceWith(ent))
                                 .chain(ent -> sessionFactory.withStatelessTransaction(s3 -> createBaseSystems(s3, allSystems, ent))
                                                             .replaceWith(ent))
                                 .chain(ent -> sessionFactory.withStatelessTransaction(s4 -> installSystems(s4, allSystems, ent))
                                                             .replaceWith(ent))
                                 .invoke(() -> {
                                     setCurrentTask(0);
                                     logProgress("System Configuration", "Done", 1);
                                 })
                                 .map(ent -> (IEnterprise<?, ?>) ent);
        });
    }

    /** Bootstrap privileges exist only while subscribed work is active, including nested installs. */
    static <T> Uni<T> bootstrapScope(java.util.function.Supplier<Uni<T>> operation) {
        return Uni.createFrom().deferred(() -> {
            CallScoper scoper = IGuiceContext.get(CallScoper.class);
            boolean owned = !scoper.isStartedScope();
            if (owned) scoper.enter();
            var configuration = ActivityMasterConfiguration.get();
            boolean previous = configuration.isSecurityEnabled();
            Runnable restore = () -> {
                configuration.setSecurityEnabled(previous);
                if (owned) scoper.exit();
            };
            try {
                configuration.setSecurityEnabled(false);
                return Objects.requireNonNull(operation.get(), "Bootstrap operation returned no Uni").eventually(restore);
            } catch (Throwable failure) {
                restore.run();
                return Uni.createFrom().failure(failure);
            }
        });
    }

    // ============================================================================================
    // Stateless install loop — mirrors the managed install methods but threads a Mutiny.StatelessSession.
    // ============================================================================================

    private Uni<Void> createBase(Mutiny.StatelessSession session, Set<IMasterSystem<?>> allSystems, IEnterprise<?, ?> enterprise)
    {
        logProgress("Creating Core", "Initializing Core Systems");
        List<IMasterSystem<?>> filtered = allSystems.stream()
                                                    .takeWhile(system -> !SystemsSystem.class.isAssignableFrom(system.getClass()))
                                                    .toList();
        if (filtered.isEmpty()) {
            return Uni.createFrom()
                      .voidItem();
        }
        Uni<Void> chain = Uni.createFrom()
                             .voidItem();
        for (IMasterSystem<?> system : filtered) {
            final IMasterSystem<?> current = system;
            chain = chain.chain(() -> performSystemInstall(session, enterprise, current, false));
        }
        return chain.invoke(() -> log.info("✅ (stateless) Core systems installed: {}", filtered.size()));
    }

    private Uni<Void> createBaseSystems(Mutiny.StatelessSession session, Set<IMasterSystem<?>> allSystems, IEnterprise<?, ?> enterprise)
    {
        logProgress("Creating Base Systems", "Initializing Base Systems");
        List<IMasterSystem<?>> filtered = allSystems.stream()
                                                    .filter(a -> a.getClass()
                                                                  .equals(SystemsSystem.class))
                                                    .toList();
        if (filtered.isEmpty()) {
            return Uni.createFrom()
                      .voidItem();
        }
        Uni<Void> chain = Uni.createFrom()
                             .voidItem();
        for (IMasterSystem<?> system : filtered) {
            final IMasterSystem<?> current = system;
            chain = chain.chain(() -> performSystemInstall(session, enterprise, current, false));
        }
        return chain.invoke(() -> log.info("✅ (stateless) Base systems installed: {}", filtered.size()));
    }

    private Uni<Void> installSystems(Mutiny.StatelessSession session, Set<IMasterSystem<?>> allSystems, IEnterprise<?, ?> enterprise)
    {
        // Full ordered system set — used for the registration pass so that EVERY system (including the
        // ones that sort AFTER EventsSystem, e.g. ResourceItem / Products / Rules) gets its Systems row.
        // Previously the categorisation loop broke at EventsSystem, so those trailing systems were never
        // registered here; their only remaining registration attempt happened during post-startup
        // (getSystemId on a not-yet-registered system → NoResultException), which aborted the whole
        // stateless enterprise start with no Systems rows for the trailing systems.
        List<IMasterSystem<?>> orderedSystems = new ArrayList<>(allSystems);

        List<IMasterSystem<?>> filteredBeforeEvents = new ArrayList<>();
        List<IMasterSystem<?>> filteredUpToEvents = new ArrayList<>();

        for (IMasterSystem<?> system : orderedSystems) {
            boolean isEventsSystem = EventsSystem.class.isAssignableFrom(system.getClass());
            filteredUpToEvents.add(system);
            if (!isEventsSystem) {
                filteredBeforeEvents.add(system);
            }
            else {
                break;
            }
        }

        if (orderedSystems.isEmpty()) {
            log.warn("⚠️ (stateless) No systems found to install.");
            return Uni.createFrom()
                      .voidItem();
        }

        logProgress("Installing Systems",
                    "Starting installation process with " + orderedSystems.size() + " systems"
        );
        // Phase 1: createDefaults for the systems up to (but excluding) EventsSystem — this runs the
        //          SecurityTokenSystem bootstrap that provisions ActivityMaster + the security matrix.
        // Phase 2: register EVERY system (ActivityMaster now exists, so registerNewSystem succeeds for all).
        // Phase 3: createDefaults for the up-to-Events set (matches the managed createDefaults scope).
        return installSystemsSequentially(session, filteredBeforeEvents, enterprise, false)
                .chain(() -> registerSystemsSequentially(session, orderedSystems, enterprise))
                .chain(() -> installSystemsSequentially(session, filteredUpToEvents, enterprise, true))
                .invoke(v -> log.info("✅ (stateless) Completed all installation steps for systems"));
    }

    private Uni<Void> installSystemsSequentially(Mutiny.StatelessSession session, List<IMasterSystem<?>> systems, IEnterprise<?, ?> enterprise, boolean registerSystem)
    {
        if (systems.isEmpty()) {
            return Uni.createFrom()
                      .voidItem();
        }
        Uni<Void> result = installSystem(session, systems.get(0), enterprise, registerSystem);
        for (int i = 1; i < systems.size(); i++) {
            final int index = i;
            result = result.chain(() -> installSystem(session, systems.get(index), enterprise, registerSystem));
        }
        return result.invoke(v -> log.info("✅ (stateless) Processed " + systems.size() + " systems"));
    }

    private Uni<Void> registerSystemsSequentially(Mutiny.StatelessSession session, List<IMasterSystem<?>> systems, IEnterprise<?, ?> enterprise)
    {
        if (systems.isEmpty()) {
            return Uni.createFrom()
                      .voidItem();
        }
        Uni<Void> result = systems.get(0)
                                  .registerSystem(session, enterprise)
                                  .replaceWithVoid();
        for (int i = 1; i < systems.size(); i++) {
            final int index = i;
            var sys = systems.get(index);
            result = result.chain(() -> sys.registerSystem(session, enterprise)
                                           .replaceWithVoid());
        }
        return result.invoke(v -> log.info("✅ (stateless) Registered " + systems.size() + " systems"));
    }

    private Uni<Void> installSystem(Mutiny.StatelessSession session, IMasterSystem<?> system, IEnterprise<?, ?> enterprise, boolean registerSystem)
    {
        String className = system.getClass()
                                 .getSimpleName();
        logProgress("Running System", className);
        return performSystemInstall(session, enterprise, system, registerSystem);
    }

    private Uni<Void> performSystemInstall(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, IMasterSystem<?> system, boolean registerSystem)
    {
        String systemName = system.getSystemName();
        String cleanedName = cleanName(system.getClass()
                                             .getSimpleName());
        log.info("➡️ (stateless) Starting install for: " + systemName + " [" + cleanedName + "]");

        @SuppressWarnings({"rawtypes", "unchecked"})
        Set<IOnSystemInstall> listeners = IGuiceContext.loaderToSet(ServiceLoader.load(IOnSystemInstall.class));
        List<IOnSystemInstall> listenersList = new ArrayList<>(listeners);

        Uni<Void> startListenersChain = Uni.createFrom()
                                           .voidItem();
        for (IOnSystemInstall listener : listenersList) {
            final IOnSystemInstall currentListener = listener;
            startListenersChain = startListenersChain.chain(() -> {
                currentListener.onSystemInstallStart(systemName);
                return Uni.createFrom()
                          .voidItem();
            });
        }

        // Prefer the stateless createDefaults; fall back to the managed overload only for any system that
        // has not been converted yet (signalled via UnsupportedOperationException). No io.smallrye.mutiny.Uni.createFrom().voidItem() —
        // stateless inserts execute immediately.
        Uni<Void> installChain = startListenersChain
                .chain(() -> system.createDefaults(session, enterprise)
                                   .invoke(() -> log.trace("✅ (stateless) Defaults created for: " + systemName)))
                .replaceWithVoid();

        return installChain
                .chain(() -> {
                    Uni<Void> endListenersChain = Uni.createFrom()
                                                     .voidItem();
                    for (IOnSystemInstall listener : listenersList) {
                        final IOnSystemInstall currentListener = listener;
                        endListenersChain = endListenersChain.chain(() -> {
                            currentListener.onSystemInstallEnd(systemName);
                            return Uni.createFrom()
                                      .voidItem();
                        });
                    }
                    return endListenersChain;
                })
                .invoke(() -> {
                    logProgress("Installed System", cleanedName, 1);
                    log.info("✅ (stateless) Finished install: " + systemName + " [" + cleanedName + "]");
                });
    }


    @Override
    public Uni<IEnterprise<?, ?>> isEnterpriseReady(Mutiny.StatelessSession session)
    {
        return getEnterprise(session, applicationEnterpriseName);
    }

}
