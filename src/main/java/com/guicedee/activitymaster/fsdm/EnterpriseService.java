package com.guicedee.activitymaster.fsdm;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassifications;
import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemInstall;
import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.EnterpriseException;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.SystemsException;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.*;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.fsdm.services.providers.EnterpriseProvider;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedinjection.GuiceContext;
import io.github.classgraph.ClassInfo;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;


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

    public IEnterprise<?, ?> get()
    {
        return new Enterprise();
    }

    //@Transactional()
    public Uni<Enterprise> create(@NotNull String name, @NotNull String description)
    {
        Enterprise enterprise = new Enterprise();
        return enterprise.builder()
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
                               return enterprise.persist()
                                              .map(persisted -> {
                                                  EnterpriseProvider.loadedEnterprise = persisted;
                                                  return persisted;
                                              });
                           }
                           else
                           {
                               // Enterprise exists, use it
                               EnterpriseProvider.loadedEnterprise = exists;
                               return Uni.createFrom()
                                              .item(exists);
                           }
                       });
    }

    @Override
    public Uni<Integer> loadUpdates(IEnterprise<?, ?> enterprise)
    {
        @SuppressWarnings({"unchecked"})
        ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);

        return getUpdates(enterprise)
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

                           return systemsService.getActivityMaster(enterprise)
                                          .chain(system -> {
                                              // Process updates sequentially using recursion
                                              return processUpdates(new ArrayList<>(availableUpdates.entrySet()), 0, enterprise, system, systemUpdateEventHandlers)
                                                             .chain(() -> {
                                                                 updateLastUpdateDate(enterprise, system);
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
     * @param updates                   List of updates to process
     * @param index                     Current index in the list
     * @param enterprise                Enterprise to update
     * @param system                    System to use
     * @param systemUpdateEventHandlers Event handlers to notify
     * @return Uni that completes when all updates are processed
     */
    private Uni<Void> processUpdates(List<Map.Entry<Integer, Class<? extends ISystemUpdate>>> updates,
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
                           return performUpdate(o, enterprise)
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
                       .chain(() -> processUpdates(updates, index + 1, enterprise, system, systemUpdateEventHandlers));
    }

    //@jakarta.transaction.Transactional
    void updateLastUpdateDate(IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
    {
        enterprise.addOrUpdateClassification(EnterpriseClassifications.LastUpdateDate.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd")
                                                                                                          .format(LocalDate.now()), system);
    }

    //@Transactional()
    Uni<Void> performUpdate(ISystemUpdate o, IEnterprise<?, ?> enterprise)
    {
        @SuppressWarnings({"unchecked"})
        ISystemsService<?> systemsService = com.guicedee.client.IGuiceContext.get(ISystemsService.class);
        return systemsService.getActivityMaster(enterprise)
                       .chain(activityMasterSystem -> {
                           // Explicitly cast to ISystems<?, ?>
                           ISystems<?, ?> system = (ISystems<?, ?>) activityMasterSystem;

                           // Convert Vert.x Future to Mutiny Uni
                           return Uni.createFrom()
                                          .emitter(emitter -> {
                                              o.update(enterprise)
                                                      .onItemOrFailure()
                                                      .invoke((result,error)->{
                                                          if (error != null)
                                                          {
                                                              emitter.fail(error);
                                                          }else {
                                                              emitter.complete(true);
                                                          }
                                                      })
                                                      ;
                                          })
                                          .chain(updateResult -> {
                                              return enterprise.addClassification(UpdateClass.toString(), o.getClass()
                                                                                                                  .getCanonicalName(), system)
                                                             .map(result -> null);
                                          });
                       });
    }

    @Override
    public Uni<Set<String>> getEnterpriseAppliedUpdates(IEnterprise<?, ?> enterprise)
    {
        ISystemsService<?> systemsService = com.guicedee.client.IGuiceContext.get(ISystemsService.class);

        return systemsService.getActivityMaster(enterprise)
                       .chain(system -> {
                           return enterprise.findClassifications(UpdateClass.toString(), system)
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
    public Uni<Map<Integer, Class<? extends ISystemUpdate>>> getUpdates(IEnterprise<?, ?> enterprise)
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

                               getEnterpriseAppliedUpdates(enterprise)
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
    @Override
    //@CacheResult(cacheName = "FindEnterpriseWithClassifications")
    public Uni<List<IEnterprise<?, ?>>> findEnterprisesWithClassification(IClassification<?, ?> classification)
    {
        return Uni.createFrom()
                       .emitter(emitter -> {
                           try
                           {
                               new EnterpriseXClassification().builder()
                                       .withClassification(classification)
                                       .inActiveRange()
                                       .inDateRange()
                                       .selectColumn(EnterpriseXClassification_.enterpriseID)
                                       .getAll(UUID.class)
                                       .subscribe()
                                       .with(
                                               classy -> {
                                                   try
                                                   {
                                                       EnterpriseQueryBuilder builder = new Enterprise().builder();
                                                       builder = builder.where(Enterprise_.id, InList, classy);
                                                       builder.getAll()
                                                               .subscribe()
                                                               .with(
                                                                       enterprises -> {
                                                                           try
                                                                           {
                                                                               emitter.complete(new ArrayList<>(enterprises));
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

    //@Transactional()
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IEnterprise<?, ?>> findEnterprise(String name)
    {
        return new Enterprise().builder()
                       .withName(name)
                       .inDateRange()
                       .get()
                       .onFailure()
                       .invoke(error -> log.error("Error finding enterprise by name: {}", error.getMessage(), error))
                       .map(enterprise -> enterprise);
    }

    //@Transactional()
    @Override
    //@CacheResult(cacheName = "GetEnterpriseByEnterpriseNameString")
    public Uni<IEnterprise<?, ?>> getEnterprise(String name)
    {
        return new Enterprise().builder()
                       .withName(name)
                       .inDateRange()
                       .get()
                       .onFailure()
                       .invoke(error -> log.error("Error getting enterprise by name: {}", error.getMessage(), error))
                       .map(enterprise -> enterprise);
    }

    //@Transactional()
    @Override
    //@CacheResult(cacheName = "GetEnterpriseByEnterpriseByUUID")
    public Uni<IEnterprise<?, ?>> getEnterprise(UUID uuid)
    {
        return new Enterprise().builder()
                       .find(uuid)
                       .inDateRange()
                       .get()
                       .onFailure()
                       .invoke(error -> log.error("Error getting enterprise by UUID: {}", error.getMessage(), error))
                       .map(enterprise -> enterprise);
    }

    //@Transactional()
    @Override
    public Uni<Boolean> doesEnterpriseExist(String name)
    {
        return new Enterprise().builder()
                       .withName(name)
                       .inDateRange()
                       .getCount()
                       .onFailure()
                       .invoke(error -> log.error("Error checking if enterprise exists: {}", error.getMessage(), error))
                       .map(count -> count > 0);
    }

    //@Transactional()
    @Override
    public Uni<Set<IEnterprise<?, ?>>> getIEnterprises()
    {
        return new Enterprise().builder()
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
    public Uni<IEnterprise<?, ?>> getIEnterpriseFromName(String enterprise)
    {
        return new Enterprise().builder()
                       .withName(enterprise)
                       .get()
                       .onFailure()
                       .invoke(error -> log.error("Error getting enterprise from name: {}", error.getMessage(), error))
                       .map(enterprises -> enterprises);
    }

    //@Transactional()
    //@CacheResult
    @Override
    public Uni<IEnterprise<?, ?>> getIEnterpriseFromID(UUID enterprise)
    {
        return new Enterprise().builder()
                       .find(enterprise)
                       .get()
                       .onFailure()
                       .invoke(error -> log.error("Error getting enterprise from ID: {}", error.getMessage(), error))
                       .map(enterprises -> enterprises);
    }

    @Override
    public Uni<IEnterprise<?, ?>> startNewEnterprise(String enterpriseName,
                                                     @NotNull String adminUserName, @NotNull String adminPassword)
    {
        return startNewEnterprise(enterpriseName, adminUserName, adminPassword, null);
    }

    @Override
    public Uni<IEnterprise<?, ?>> startNewEnterprise(String enterpriseName,
                                                     @NotNull String adminUserName, @NotNull String adminPassword, UUID uuidIdentifier)
    {
        com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
                .setSecurityEnabled(false);

        Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();

        int totalTasks = allSystems.stream()
                                 .mapToInt(IActivityMasterSystem::totalTasks)
                                 .sum() + 1;

        logProgress("Create Enterprise", "Creating Enterprise", 0, totalTasks);

        return installEnterprise(enterpriseName)
                       .chain(enterprise -> {
                           return createNewEnterprise(enterprise)
                                          .chain(() -> {
                                              ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                                              return systemsService
                                                             .getActivityMaster(enterprise)
                                                             .chain(activityMasterSystem -> {
                                                                 // Explicitly cast to ISystems<?, ?>
                                                                 ISystems<?, ?> system = (ISystems<?, ?>) activityMasterSystem;

                                                                 IPasswordsService<?> passwordsService = com.guicedee.client.IGuiceContext.get(IPasswordsService.class);
                                                                 // createAdminAndCreatorUserForEnterprise returns IInvolvedParty directly, not a Uni
                                                                 // Wrap it in a Uni to continue the reactive chain
                                                                 return passwordsService.createAdminAndCreatorUserForEnterprise(system, adminUserName, adminPassword, uuidIdentifier);
                                                             })
                                                             .chain(user -> {
                                                                 wipeCaches();
                                                                 logProgress("Systems", "Running Systems Post Startups", 1);
                                                                 return performPostStartup(enterprise)
                                                                                .map(v -> enterprise);
                                                             });
                                          });
                       });
    }


    private Uni<Enterprise> installEnterprise(String enterpriseName)
    {
        com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
                .setApplicationEnterpriseName(enterpriseName);
        return create(enterpriseName, enterpriseName);
    }

    @Override
    public Uni<Void> createNewEnterprise(@NotNull IEnterprise<?, ?> enterprise)
    {
        return Uni.createFrom()
                       .emitter(emitter -> {
                           try
                           {
                               com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
                                       .setSecurityEnabled(false);
                               Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();
                               createBase(allSystems, enterprise);
                               createBaseSystems(allSystems, enterprise);
                               installSystems(allSystems, enterprise);
                               setCurrentTask(0);
                               logProgress("System Configuration", "Starting system updates", 1);

                               loadUpdates(enterprise)
                                       .subscribe()
                                       .with(
                                               result -> {
                                                   logProgress("System Configuration", "Done", 1);
                                                   //todo securities
                                                   //com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
                                                   //		.setSecurityEnabled(true);
                                                   emitter.complete(null);
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

    //@Transactional()
    @Override
    public Uni<IEnterprise<?, ?>> isEnterpriseReady()
    {
        return Uni.createFrom()
                       .emitter(emitter -> {
                           try
                           {
                               if (!Strings.isNullOrEmpty(applicationEnterpriseName))
                               {
                                   IEnterpriseService<?> enterpriseService = com.guicedee.client.IGuiceContext.get(IEnterpriseService.class);

                                   enterpriseService.getEnterprise(applicationEnterpriseName)
                                           .subscribe()
                                           .with(
                                                   enterprise -> {
                                                       try
                                                       {
                                                           // Get system synchronously since it's not reactive yet
                                                           ISystems<?, ?> system = com.guicedee.client.IGuiceContext.get(IActivityMasterSystem.class)
                                                                                           .getSystem(applicationEnterpriseName);

                                                           // Check if enterprise has the LastUpdateDate classification
                                                           enterprise.builder()
                                                                   .hasClassification(EnterpriseClassifications.LastUpdateDate.toString(), system)
                                                                   .getCount()
                                                                   .subscribe()
                                                                   .with(
                                                                           count -> {
                                                                               try
                                                                               {
                                                                                   emitter.complete(enterprise);
                                                                               }
                                                                               catch (Exception e)
                                                                               {
                                                                                   log.warn("Error completing emitter", e);
                                                                                   emitter.complete(enterprise);
                                                                               }
                                                                           },
                                                                           error -> {
                                                                               log.warn("Error checking classification count", error);
                                                                               emitter.complete(enterprise);
                                                                           }
                                                                   )
                                                           ;
                                                       }
                                                       catch (SystemsException e)
                                                       {
                                                           log.warn("System is not ready", e);
                                                           emitter.fail(e);
                                                       }
                                                       catch (Exception e)
                                                       {
                                                           log.warn("Error in enterprise ready check", e);
                                                           emitter.fail(e);
                                                       }
                                                   },
                                                   error -> {
                                                       if (error instanceof EnterpriseException)
                                                       {
                                                           log.warn("Enterprise is not ready", error);
                                                       }
                                                       else
                                                       {
                                                           log.warn("Error getting enterprise", error);
                                                       }
                                                       emitter.fail(error);
                                                   }
                                           )
                                   ;
                               }
                               else
                               {
                                   IEnterpriseService<?> enterpriseService = com.guicedee.client.IGuiceContext.get(IEnterpriseService.class);
                                   var enterprise = enterpriseService.getEnterprise(applicationEnterpriseName);
                                   enterprise.onItem()
                                           .invoke(emitter::complete)
                                           .await()
                                           .atMost(Duration.of(60L, ChronoUnit.SECONDS))
                                   ;
                               }
                           }
                           catch (Exception e)
                           {
                               log.warn("Unexpected error in isEnterpriseReady", e);
                               emitter.fail(e);
                           }
                       });
    }

    private void installSystems(Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?, ?> enterprise)
    {
        //then from classifications data service do both
        boolean tillHere = false;
        for (Iterator<IActivityMasterSystem<?>> iterator = allSystems.iterator(); iterator.hasNext(); )
        {
            IActivityMasterSystem<?> allSystem = iterator.next();
            if (allSystem.getClass()
                        .isAssignableFrom(SystemsSystem.class))
            {
                tillHere = true;
            }
            if (tillHere)
            {
                logProgress("Running System ", allSystem.getClass()
                                                       .getSimpleName());
                installSystem(allSystem, enterprise);
            }
        }
    }

    private void installSystem(IActivityMasterSystem<?> system, IEnterprise<?, ?> enterprise)
    {
        logProgress("Running System ", system.getClass()
                                               .getSimpleName());
        performSystemInstall(enterprise, system);
    }


    private void performSystemInstall(IEnterprise<?, ?> enterprise, IActivityMasterSystem<?> allSystem)
    {
        String nameC = cleanName(allSystem.getClass()
                                         .getSimpleName());

        IActivityMasterSystem<?> registeredSystem = allSystem;//com.guicedee.client.IGuiceContext.get(allSystem.getClass());

        @SuppressWarnings({"rawtypes", "unchecked"})
        Set<IOnSystemInstall> systemInstallEventListeners = IGuiceContext.loaderToSet(ServiceLoader.load(IOnSystemInstall.class));
        for (IOnSystemInstall systemInstallEventListener : systemInstallEventListeners)
        {
            systemInstallEventListener.onSystemInstallStart(registeredSystem.getSystemName());
        }

        ISystems<?, ?> registerSystem = registeredSystem.registerSystem(enterprise);

        registeredSystem.createDefaults(enterprise);
        for (IOnSystemInstall a : systemInstallEventListeners)
        {
            a.onSystemInstallEnd(registeredSystem.getSystemName());
        }

        logProgress("Installed System", nameC, 1);
    }

    private void createBaseSystems(Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?, ?> enterprise)
    {
        logProgress("Creating Base Systems", "Initializing Base Systems");
        for (IActivityMasterSystem<?> allSystem : allSystems)
        {
            if (allSystem.getClass()
                        .isAssignableFrom(SystemsSystem.class))
            {
                break;
            }
            performSystemInstall(enterprise, allSystem);
        }
    }

    private void createBase(Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?, ?> enterprise)
    {
        logProgress("Creating Core", "Initializing Core Systems");
        for (IActivityMasterSystem<?> allSystem : allSystems)
        {
            if (allSystem.getClass()
                        .isAssignableFrom(SystemsSystem.class))
            {
                break;
            }
            performSystemInstall(enterprise, allSystem);
        }
    }
}
