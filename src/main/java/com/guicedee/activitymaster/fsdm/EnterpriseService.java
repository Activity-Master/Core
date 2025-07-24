package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;

import java.util.ArrayList;

import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassifications;
import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemInstall;
import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.*;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedinjection.GuiceContext;
import io.github.classgraph.ClassInfo;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;


import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassifications.*;
import static com.guicedee.activitymaster.fsdm.services.ActivityMasterSystemsManager.*;

@SuppressWarnings("DuplicatedCode")
@Log4j2
public class EnterpriseService
        implements IProgressable,
                           IEnterpriseService<EnterpriseService>
{
    @Inject
    private ActivityMasterConfiguration configuration;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    public IEnterprise<?, ?> get()
    {
        return new Enterprise();
    }

    //@Transactional()
    public Uni<Enterprise> create(Mutiny.Session session, @NotNull String name, @NotNull String description)
    {
        Enterprise enterprise = new Enterprise();
        return enterprise.builder(session)
                       .withName(name)
                       .get()
                       .onFailure()
                       .recoverWithItem(() -> {
                           // If get() fails (no enterprise found), create a new one
                           enterprise.setName(name);
                           enterprise.setDescription(description);
                           return null; // This null will be handled in the chain below
                       })
                       .onFailure()
                       .invoke(error -> log.error("Error checking if enterprise exists: {}", error.getMessage(), error))
                       .chain(exists -> {
                           if (exists == null)
                           {
                               // Enterprise doesn't exist or get() failed, create a new one
                               return session.persist(enterprise)
                                              .replaceWith(Uni.createFrom()
                                                                   .item(enterprise));
                           }
                           else
                           {
                               return Uni.createFrom()
                                              .item(exists);
                           }
                       });
    }

    @Override
    public Uni<Integer> loadUpdates(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        @SuppressWarnings({"unchecked"})
        ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);

        return getUpdates(session, enterprise)
                       .chain(availableUpdates -> {
                           log.info(MessageFormat.format("There are {} required updates", availableUpdates.size()));

                           setCurrentTask(0);
                           int tasks = 0;
                           for (Map.Entry<Integer, Class<? extends ISystemUpdate>> entry : availableUpdates.entrySet())
                           {
                               Class<? extends ISystemUpdate> aClass = entry.getValue();
                               tasks += aClass.getAnnotation(SortedUpdate.class)
                                                .taskCount();
                           }

                           @SuppressWarnings({"rawtypes", "unchecked"})
                           Set<IOnSystemUpdate> systemUpdateEventHandlers = IGuiceContext.loaderToSet(ServiceLoader.load(IOnSystemUpdate.class));

                           setTotalTasks(tasks);

                           return systemsService.getActivityMaster(session, enterprise)
                                          .chain(system -> {
                                              // Process updates sequentially using recursion
                                              return processUpdates(session, new ArrayList<>(availableUpdates.entrySet()), 0, enterprise, system, systemUpdateEventHandlers)
                                                             .chain(() -> {
                                                                 updateLastUpdateDate(session, enterprise, system);
                                                                 logProgress("Update System", "Finished Updates. Last Update Date - " + DateTimeFormatter.ofPattern("yyyy/MM/dd")
                                                                                                                                                .format(LocalDate.now()));
                                                                 return Uni.createFrom()
                                                                                .item(availableUpdates.size());
                                                             });
                                          });
                       });
    }

    /**
     * Process updates sequentially using recursion
     *
     * @param session
     * @param updates                   List of updates to process
     * @param index                     Current index in the list
     * @param enterprise                Enterprise to update
     * @param system                    System to use
     * @param systemUpdateEventHandlers Event handlers to notify
     * @return Uni that completes when all updates are processed
     */
    private Uni<Void> processUpdates(Mutiny.Session session, List<Map.Entry<Integer, Class<? extends ISystemUpdate>>> updates,
                                     int index,
                                     IEnterprise<?, ?> enterprise,
                                     ISystems<?, ?> system,
                                     Set<IOnSystemUpdate> systemUpdateEventHandlers)
    {
        // Base case: if we've processed all updates, return a completed Uni
        if (index >= updates.size())
        {
            return Uni.createFrom()
                           .voidItem();
        }

        // Get the current update
        Map.Entry<Integer, Class<? extends ISystemUpdate>> entry = updates.get(index);
        Class<? extends ISystemUpdate> value = entry.getValue();

        // Create a Uni that processes the current update
        return Uni.createFrom()
                       .item(() -> {
                           try
                           {
                               logProgress("Update System", "Starting updates for " + value.getSimpleName());
                               for (IOnSystemUpdate<?> systemUpdateEventHandler : systemUpdateEventHandlers)
                               {
                                   systemUpdateEventHandler.onSystemUpdateStart(value);
                               }
                               return com.guicedee.client.IGuiceContext.get(value);
                           }
                           catch (Throwable T)
                           {
                               log.error("Unable to perform update", T);
                               for (IOnSystemUpdate<?> a : systemUpdateEventHandlers)
                               {
                                   a.onSystemUpdateFail(value);
                               }
                               throw new RuntimeException("Failed to process update", T);
                           }
                       })
                       .chain(o -> {
                           // Perform the update
                           return performUpdate(session, o, enterprise)
                                          .onItem()
                                          .invoke(() -> {
                                              for (IOnSystemUpdate<?> a : systemUpdateEventHandlers)
                                              {
                                                  a.onSystemUpdateEnd(value);
                                              }
                                          })
                                          .onFailure()
                                          .recoverWithItem(() -> {
                                              log.error("Unable to perform update");
                                              for (IOnSystemUpdate<?> a : systemUpdateEventHandlers)
                                              {
                                                  a.onSystemUpdateFail(value);
                                              }
                                              return null;
                                          });
                       })
                       // Process the next update recursively
                       .chain(() -> processUpdates(session, updates, index + 1, enterprise, system, systemUpdateEventHandlers));
    }

    //@jakarta.transaction.Transactional
    void updateLastUpdateDate(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
    {
        enterprise.addOrUpdateClassification(session, EnterpriseClassifications.LastUpdateDate.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd")
                                                                                                          .format(LocalDate.now()), system);
    }

    //@Transactional()
    Uni<Void> performUpdate(Mutiny.Session session, ISystemUpdate o, IEnterprise<?, ?> enterprise)
    {
        @SuppressWarnings({"unchecked"})
        ISystemsService<?> systemsService = com.guicedee.client.IGuiceContext.get(ISystemsService.class);
        return systemsService.getActivityMaster(session, enterprise)
                       .chain(activityMasterSystem -> {
                           // Explicitly cast to ISystems<?, ?>
                           ISystems<?, ?> system = (ISystems<?, ?>) activityMasterSystem;

                           // Convert Vert.x Future to Mutiny Uni
                           return Uni.createFrom()
                                          .emitter(emitter -> {
                                              o.update(session, enterprise)
                                                      .onItemOrFailure()
                                                      .invoke((result, error) -> {
                                                          if (error != null)
                                                          {
                                                              emitter.fail(error);
                                                          }
                                                          else
                                                          {
                                                              emitter.complete(true);
                                                          }
                                                      })
                                              ;
                                          })
                                          .chain(updateResult -> {
                                              return enterprise.addClassification(session, UpdateClass.toString(), o.getClass()
                                                                                                                  .getCanonicalName(), system)
                                                             .map(result -> null);
                                          });
                       });
    }

    @Override
    public Uni<Set<String>> getEnterpriseAppliedUpdates(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        ISystemsService<?> systemsService = com.guicedee.client.IGuiceContext.get(ISystemsService.class);

        return systemsService.getActivityMaster(session, enterprise)
                       .chain(system -> {
                           return enterprise.findClassifications(session, UpdateClass.toString(), system)
                                          .map(classificationsAll -> {
                                              Set<String> set = new LinkedHashSet<>();
                                              for (IRelationshipValue<?, IClassification<?, ?>, ?> rel : classificationsAll)
                                              {
                                                  String classValue = rel.getValue();
                                                  if (classValue.contains("$$EnhancerByGuice$$"))
                                                  {
                                                      classValue = classValue.substring(0, classValue.indexOf("$$EnhancerByGuice$$"));
                                                  }
                                                  set.add(classValue);
                                              }
                                              return set;
                                          });
                       });
    }

    @Override
    //@jakarta.transaction.Transactional
    public Uni<Map<Integer, Class<? extends ISystemUpdate>>> getUpdates(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        return Uni.createFrom()
                       .emitter(emitter -> {
                           try
                           {
                               Map<Integer, Class<? extends ISystemUpdate>> availableUpdates = new TreeMap<>();
                               for (ClassInfo classInfo : GuiceContext.instance()
                                                                  .getScanResult()
                                                                  .getClassesWithAnnotation(SortedUpdate.class.getCanonicalName()))
                               {
                                   if (classInfo.isAbstract() || classInfo.isInterface())
                                   {
                                       continue;
                                   }

                                   @SuppressWarnings("unchecked")
                                   Class<? extends ISystemUpdate> clazz = (Class<? extends ISystemUpdate>) classInfo.loadClass();
                                   SortedUpdate du = clazz.getAnnotation(SortedUpdate.class);
                                   availableUpdates.put(du.sortOrder(), clazz);
                               }

                               getEnterpriseAppliedUpdates(session, enterprise)
                                       .subscribe()
                                       .with(
                                               enterpriseAppliedUpdates -> {
                                                   try
                                                   {
                                                       for (String enterpriseAppliedUpdate : enterpriseAppliedUpdates)
                                                       {
                                                           log.info("System Installed Update [{}]", enterpriseAppliedUpdate);
                                                       }
                                                       Map<Integer, Class<? extends ISystemUpdate>> applicableUpdates = new TreeMap<>();
                                                       for (Map.Entry<Integer, Class<? extends ISystemUpdate>> entry : availableUpdates.entrySet())
                                                       {
                                                           Integer key = entry.getKey();
                                                           Class<? extends ISystemUpdate> value = entry.getValue();
                                                           String classValue = value.getCanonicalName();
                                                           if (classValue.contains("$$EnhancerByGuice$$"))
                                                           {
                                                               classValue = classValue.substring(0, classValue.indexOf("$$EnhancerByGuice$$"));
                                                           }
                                                           SortedUpdate du = value.getAnnotation(SortedUpdate.class);
                                                           if (!enterpriseAppliedUpdates.contains(classValue) || du.force())
                                                           {
                                                               applicableUpdates.put(key, value);
                                                           }
                                                       }
                                                       emitter.complete(applicableUpdates);
                                                   }
                                                   catch (Exception e)
                                                   {
                                                       emitter.fail(e);
                                                   }
                                               },
                                               emitter::fail
                                       )
                               ;
                           }
                           catch (Exception e)
                           {
                               emitter.fail(e);
                           }
                       });
    }


    @Override
    public Map<Integer, Class<? extends ISystemUpdate>> getAllUpdates()
    {
        Map<Integer, Class<? extends ISystemUpdate>> availableUpdates = new TreeMap<>();
        for (ClassInfo classInfo : GuiceContext.instance()
                                           .getScanResult()
                                           .getClassesWithAnnotation(SortedUpdate.class.getCanonicalName()))
        {
            if (classInfo.isAbstract() || classInfo.isInterface())
            {
                continue;
            }

            @SuppressWarnings("unchecked")
            Class<? extends ISystemUpdate> clazz = (Class<? extends ISystemUpdate>) classInfo.loadClass();
            SortedUpdate du = clazz.getAnnotation(SortedUpdate.class);
            availableUpdates.put(du.sortOrder(), clazz);
        }
        Map<Integer, Class<? extends ISystemUpdate>> applicableUpdates = new TreeMap<>();
        for (Map.Entry<Integer, Class<? extends ISystemUpdate>> entry : availableUpdates.entrySet())
        {
            Integer key = entry.getKey();
            Class<? extends ISystemUpdate> value = entry.getValue();
            applicableUpdates.put(key, value);
        }
        return applicableUpdates;
    }

    //@Transactional()
    @SuppressWarnings("unchecked")
    @Override
    //@CacheResult(cacheName = "FindEnterpriseWithClassifications")
    public Uni<List<IEnterprise<?, ?>>> findEnterprisesWithClassification(Mutiny.Session session, IClassification<?, ?> classification)
    {
        return (Uni)new EnterpriseXClassification().builder(session)
                       .withClassification(classification)
                       .inActiveRange()
                       .inDateRange()
                       .selectColumn(EnterpriseXClassification_.enterpriseID)
                       .getAll(UUID.class)
                       .chain(
                               classy -> {
                                   EnterpriseQueryBuilder builder = new Enterprise().builder(session);
                                   builder = builder.where(Enterprise_.id, InList, classy);
                                   return builder.getAll()
                                   ;
                               });

    }

    //@Transactional()
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IEnterprise<?, ?>> findEnterprise(Mutiny.Session session, String name)
    {
        return new Enterprise().builder(session)
                       .withName(name)
                       .inDateRange()
                       .get()
                       .onFailure()
                       .invoke(error -> log.error("Error finding enterprise by name: {}", error.getMessage(), error))
                       .map(enterprise -> enterprise);
    }

    //@Transactional()
    @SuppressWarnings("unchecked")
    @Override
    //@CacheResult(cacheName = "GetEnterpriseByEnterpriseNameString")
    public Uni<IEnterprise<?, ?>> getEnterprise(Mutiny.Session session, String name)
    {
        log.info("🔍 Starting fetch for enterprise: {}", name);

        log.debug("📦 Session & transaction started for enterprise lookup: {}", name);

        return (Uni) new Enterprise().builder(session)
                             .withName(name)
                             .inDateRange()
                             .get()
                             .onFailure()
                             .invoke(error ->
                                             log.error("❌ Failed to fetch enterprise '{}': {}", name, error.getMessage(), error)
                             )
                             .invoke(ent -> {
                                 if (ent != null)
                                 {
                                     log.info("✅ Enterprise found: {} (ID: {})", ent.getName(), ent.getId());
                                 }
                                 else
                                 {
                                     log.warn("⚠️ No enterprise found for: {}", name);
                                 }
                             })
                             .onFailure()
                             .invoke(err ->
                                             log.error("❌ Session/transaction failure during getEnterprise('{}')", name, err)
                             );
    }

    //@Transactional()
    @Override
    //@CacheResult(cacheName = "GetEnterpriseByEnterpriseByUUID")
    public Uni<IEnterprise<?, ?>> getEnterprise(Mutiny.Session session, UUID uuid)
    {
        return new Enterprise().builder(session)
                       .find(uuid)
                       .inDateRange()
                       .get()
                       .onFailure()
                       .invoke(error -> log.error("Error getting enterprise by UUID: {}", error.getMessage(), error))
                       .map(enterprise -> enterprise);
    }

    //@Transactional()
    @Override
    public Uni<Boolean> doesEnterpriseExist(Mutiny.Session session, String name)
    {
        return new Enterprise().builder(session)
                       .withName(name)
                       .inDateRange()
                       .getCount()
                       .onFailure()
                       .invoke(error -> log.error("Error checking if enterprise exists: {}", error.getMessage(), error))
                       .map(count -> count > 0);
    }

    //@Transactional()
    @Override
    public Uni<Set<IEnterprise<?, ?>>> getIEnterprises(Mutiny.Session session)
    {
        return new Enterprise().builder(session)
                       .inDateRange()
                       .orderBy(Enterprise_.name)
                       .getAll()
                       .onFailure()
                       .invoke(error -> log.error("Error getting all enterprises: {}", error.getMessage(), error))
                       .map(LinkedHashSet::new);
    }

    //@Transactional()
    //@CacheResult
    @Override
    public Uni<IEnterprise<?, ?>> getIEnterpriseFromName(Mutiny.Session session, String enterprise)
    {
        return new Enterprise().builder(session)
                       .withName(enterprise)
                       .get()
                       .onFailure()
                       .invoke(error -> log.error("Error getting enterprise from name: {}", error.getMessage(), error))
                       .map(enterprises -> enterprises);
    }

    //@Transactional()
    //@CacheResult
    @Override
    public Uni<IEnterprise<?, ?>> getIEnterpriseFromID(Mutiny.Session session, UUID enterprise)
    {
        return new Enterprise().builder(session)
                       .find(enterprise)
                       .get()
                       .onFailure()
                       .invoke(error -> log.error("Error getting enterprise from ID: {}", error.getMessage(), error))
                       .map(enterprises -> enterprises);
    }

    @Override
    public Uni<IEnterprise<?, ?>> startNewEnterprise(Mutiny.Session session, String enterpriseName,
                                                     @NotNull String adminUserName, @NotNull String adminPassword)
    {
        return startNewEnterprise(session, enterpriseName, adminUserName, adminPassword, null);
    }

    @Override
    public Uni<IEnterprise<?, ?>> startNewEnterprise(Mutiny.Session session, String enterpriseName,
                                                     @NotNull String adminUserName, @NotNull String adminPassword, UUID uuidIdentifier)
    {
        com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
                .setSecurityEnabled(false);

        Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();

        int totalTasks = allSystems.stream()
                                 .mapToInt(IActivityMasterSystem::totalTasks)
                                 .sum() + 1;

        logProgress("Create Enterprise", "Creating Enterprise", 0, totalTasks);

        return installEnterprise(session, enterpriseName)
                       .chain(enterprise -> {
                           return createNewEnterprise(session, enterprise)
                                          .chain(() -> {
                                              ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                                              return systemsService
                                                             .getActivityMaster(session, enterprise)
                                                             .chain(activityMasterSystem -> {
                                                                 // Explicitly cast to ISystems<?, ?>
                                                                 ISystems<?, ?> system = (ISystems<?, ?>) activityMasterSystem;

                                                                 IPasswordsService<?> passwordsService = com.guicedee.client.IGuiceContext.get(IPasswordsService.class);
                                                                 // createAdminAndCreatorUserForEnterprise returns IInvolvedParty directly, not a Uni
                                                                 // Wrap it in a Uni to continue the reactive chain
                                                                 return passwordsService.createAdminAndCreatorUserForEnterprise(session, system, adminUserName, adminPassword, uuidIdentifier);
                                                             })
                                                             .chain(user -> {
                                                                 wipeCaches();
                                                                 logProgress("Systems", "Running Systems Post Startups", 1);
                                                                 return performPostStartup(session, enterprise)
                                                                                .map(v -> enterprise);
                                                             });
                                          });
                       });
    }


    private Uni<Enterprise> installEnterprise(Mutiny.Session session, String enterpriseName)
    {
        com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
                .setApplicationEnterpriseName(enterpriseName);
        return create(session, enterpriseName, enterpriseName);
    }

    @Override
    public Uni<IEnterprise<?, ?>> createNewEnterprise(Mutiny.Session session, @NotNull IEnterprise<?, ?> enterprise)
    {
        // 🔓 Disable security before starting
        IGuiceContext.get(ActivityMasterConfiguration.class)
                .setSecurityEnabled(false);

        Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();

        return createBase(session, allSystems, enterprise)
                       .chain(() -> createBaseSystems(session, allSystems, enterprise))
                       .chain(() -> installSystems(session, allSystems, enterprise))
                       .invoke(() -> {
                           setCurrentTask(0);
                           logProgress("System Configuration", "Starting system updates", 1);
                       })
                       .chain(() -> loadUpdates(session, enterprise))
                       .invoke(() -> {
                           logProgress("System Configuration", "Done", 1);
                           // 🔒 Re-enable security here if needed
                           // IGuiceContext.get(ActivityMasterConfiguration.class).setSecurityEnabled(true);
                       })
                       .onFailure()
                       .invoke(err ->
                                       log.error("❌ Failed during createNewEnterprise()", err)
                       )
                       .map(res -> enterprise);
    }

    //@Transactional()
    @Override
    public Uni<IEnterprise<?, ?>> isEnterpriseReady(Mutiny.Session session)
    {
        return getEnterprise(session, applicationEnterpriseName);
    }

    private Uni<Void> installSystems(Mutiny.Session session, Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?, ?> enterprise)
    {
        boolean found = false;
        List<IActivityMasterSystem<?>> filtered = new ArrayList<>();
        for (IActivityMasterSystem<?> system : allSystems)
        {
            if (!found && SystemsSystem.class.isAssignableFrom(system.getClass()))
            {
                found = true;
            }

            if (found)
            {
                filtered.add(system);
            }
        }

        if (filtered.isEmpty())
        {
            log.warn("⚠️ No systems found from SystemsSystem onward to install.");
            return Uni.createFrom()
                           .voidItem();
        }

        logProgress("Installing Systems", "Starting from SystemsSystem");

        return Multi.createFrom()
                       .iterable(filtered)
                       .onItem()
                       .transformToUni(system -> installSystem(session, system, enterprise))
                       .merge()
                       .collect()
                       .asList()
                       .invoke(installed -> log.info("✅ Installed " + installed.size() + " systems starting from SystemsSystem."))
                       .replaceWithVoid()
                       .onFailure()
                       .invoke(err -> log.error("❌ Error during post-SystemsSystem installs", err));
    }

    private Uni<Void> installSystem(Mutiny.Session session, IActivityMasterSystem<?> system, IEnterprise<?, ?> enterprise)
    {
        String className = system.getClass()
                                   .getSimpleName();
        logProgress("Running System", className);
        log.info("🚀 Starting single system install: " + className);

        return performSystemInstall(session, enterprise, system)
                       .invoke(() -> log.info("✅ System install completed: " + className))
                       .onFailure()
                       .invoke(err -> log.error("❌ System install failed: " + className, err));
    }


    private Uni<Void> performSystemInstall(Mutiny.Session session, IEnterprise<?, ?> enterprise, IActivityMasterSystem<?> system)
    {
        return Uni.createFrom()
                       .voidItem()
                       .invoke(() -> {
                           String className = system.getClass()
                                                      .getSimpleName();
                           String systemName = system.getSystemName();
                           String cleanedName = cleanName(className);

                           log.info("➡️ Starting install for: " + systemName + " [" + cleanedName + "]");

                           IActivityMasterSystem<?> registeredSystem = system;

                           @SuppressWarnings({"rawtypes", "unchecked"})
                           Set<IOnSystemInstall> listeners = IGuiceContext.loaderToSet(ServiceLoader.load(IOnSystemInstall.class));
                           log.debug("🔧 Notifying " + listeners.size() + " install listeners for system: " + systemName);

                           for (IOnSystemInstall listener : listeners)
                           {
                               try
                               {
                                   listener.onSystemInstallStart(systemName);
                                   log.trace("🟢 Start: " + listener.getClass()
                                                                   .getSimpleName());
                               }
                               catch (Exception e)
                               {
                                   log.warn("⚠️ Start listener failed: " + listener.getClass()
                                                                                   .getSimpleName(), e);
                               }
                           }

                           try
                           {
                               registeredSystem.createDefaults(session, enterprise);
                               log.info("✅ Defaults created for: " + systemName);
                           }
                           catch (Exception e)
                           {
                               log.error("❌ Failed to create defaults for: " + systemName, e);
                               throw e;
                           }

                           for (IOnSystemInstall listener : listeners)
                           {
                               try
                               {
                                   listener.onSystemInstallEnd(systemName);
                                   log.trace("🟢 End: " + listener.getClass()
                                                                 .getSimpleName());
                               }
                               catch (Exception e)
                               {
                                   log.warn("⚠️ End listener failed: " + listener.getClass()
                                                                                 .getSimpleName(), e);
                               }
                           }

                           logProgress("Installed System", cleanedName, 1);
                           log.info("✅ Finished install: " + systemName + " [" + cleanedName + "]");
                       })
                       .onFailure()
                       .invoke(err ->
                                       log.error("❌ Exception during install for: " + system.getSystemName(), err)
                       );
    }

    private Uni<Void> createBaseSystems(Mutiny.Session session, Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?, ?> enterprise)
    {
        logProgress("Creating Base Systems", "Initializing Base Systems");

        List<IActivityMasterSystem<?>> filtered = allSystems.stream()
                                                          .takeWhile(system -> !SystemsSystem.class.isAssignableFrom(system.getClass()))
                                                          .toList()
                ;

        return Multi.createFrom()
                       .iterable(filtered)
                       .onItem()
                       .transformToUni(system -> performSystemInstall(session, enterprise, system))
                       .merge()
                       .collect()
                       .asList()
                       .invoke(installed -> log.info("✅ Base systems installed: " + installed.size()))
                       .replaceWithVoid()
                       .onFailure()
                       .invoke(err -> log.error("❌ Error during base system installs", err));
    }

    private Uni<Void> createBase(Mutiny.Session session, Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?, ?> enterprise)
    {
        logProgress("Creating Core", "Initializing Core Systems");

        List<IActivityMasterSystem<?>> filtered = allSystems.stream()
                                                          .takeWhile(system -> !SystemsSystem.class.isAssignableFrom(system.getClass()))
                                                          .toList()
                ;

        return Multi.createFrom()
                       .iterable(filtered)
                       .onItem()
                       .transformToUni(system -> performSystemInstall(session, enterprise, system))
                       .merge()
                       .collect()
                       .asList()
                       .invoke(installs -> log.info("✅ All core systems installed successfully: " + installs.size() + " systems"))
                       .replaceWithVoid()
                       .onFailure()
                       .invoke(err -> log.error("❌ Failed during system installation", err));
    }
}
