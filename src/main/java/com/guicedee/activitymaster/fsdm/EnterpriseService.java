package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IPasswordsService;
import com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassifications;
import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemInstall;
import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
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
import io.github.classgraph.ClassInfo;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.entityassist.enumerations.Operand.InList;
import static com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.applicationEnterpriseName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassifications.UpdateClass;

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
    enterprise.setName(name);
    enterprise.setDescription(description);
    return enterprise.builder(session)
               .withName(name)
               .get()
               .onFailure(NoResultException.class)
               .recoverWithUni(u -> {
                 return session.persist(enterprise)
                            .replaceWith(Uni.createFrom()
                                             .item(enterprise));
               });
  }

  @Override
  public Uni<Integer> loadUpdates(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    @SuppressWarnings({"unchecked"})
    ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);

    return getUpdates(session, enterprise)
               .chain(availableUpdates -> {
                 log.info(MessageFormat.format("There are {0} required updates", availableUpdates.size()));

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
                                           return updateLastUpdateDate(session, enterprise, system)
                                                      .invoke(a -> {
                                                        logProgress("Update System", "Finished Updates. Last Update Date - " + DateTimeFormatter.ofPattern("yyyy/MM/dd")
                                                                                                                                   .format(LocalDate.now()));
                                                      })
                                                      .chain(a -> Uni.createFrom()
                                                                      .item(availableUpdates.size()));
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
  Uni<? extends IWarehouseRelationshipClassificationTable<?, ?, ?, IClassification<?, ?>, UUID, ?>> updateLastUpdateDate(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
  {
    return enterprise.addOrUpdateClassification(session, EnterpriseClassifications.LastUpdateDate.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd")
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
                 return o.update(session, enterprise)
                            .chain(updateResult -> {
                              return enterprise.addClassification(session, UpdateClass.toString(), o.getClass()
                                                                                                       .getCanonicalName(), system)
                                         .replaceWithVoid();
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
    // First, synchronously collect all available updates
    return Uni.createFrom()
               .item(() -> {
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
                 return availableUpdates;
               })
               // Then chain with getting enterprise applied updates
               .chain(availableUpdates -> {
                 return getEnterpriseAppliedUpdates(session, enterprise)
                            .map(enterpriseAppliedUpdates -> {
                              // Log the applied updates
                              for (String enterpriseAppliedUpdate : enterpriseAppliedUpdates)
                              {
                                log.info("System Installed Update [{}]", enterpriseAppliedUpdate);
                              }
                              
                              // Filter available updates to get applicable ones
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
                              return applicableUpdates;
                            });
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
    return (Uni) new EnterpriseXClassification().builder(session)
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
    log.debug("🔍 Starting fetch for enterprise: {}", name);

    log.debug("📦 Session & transaction started for enterprise lookup: {}", name);

    return (Uni) new Enterprise().builder(session)
                     .withName(name)
                     .inDateRange()
                     //.setCacheName("getEnterpriseByName","default")
                     .get()
                     .onFailure()
                     .invoke(error ->
                                 log.error("❌ Failed to fetch enterprise '{}': {}", name, error.getMessage(), error)
                     )
                     .invoke(ent -> {
                       if (ent != null)
                       {
                         log.debug("✅ Enterprise found: {} (ID: {})", ent.getName(), ent.getId());
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

    return create(session, enterprise.getName(), enterprise.getName())
               .chain(entUni -> {
                 return createBase(session, allSystems, entUni)
                            .chain(() -> createBaseSystems(session, allSystems, entUni))
                            .chain(() -> installSystems(session, allSystems, entUni))
                            .invoke(() -> {
                              setCurrentTask(0);
                              logProgress("System Configuration", "Starting system updates", 1);
                            })
                            //.chain(() -> loadUpdates(session, entUni))
                            .invoke(() -> {
                              logProgress("System Configuration", "Done", 1);
                              // 🔒 Re-enable security here if needed
                              // IGuiceContext.get(ActivityMasterConfiguration.class).setSecurityEnabled(true);
                            })
                            .onFailure()
                            .invoke(err ->
                                        log.error("❌ Failed during createNewEnterprise()", err)
                            )
                            .map(res -> entUni);
               });
  }

  //@Transactional()
  @Override
  public Uni<IEnterprise<?, ?>> isEnterpriseReady(Mutiny.Session session)
  {
    return getEnterprise(session, applicationEnterpriseName);
  }

  private Uni<Void> installSystems(Mutiny.Session session, Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?, ?> enterprise)
  {
    boolean found = true;
    List<IActivityMasterSystem<?>> filteredBeforeEvents = new ArrayList<>();
    List<IActivityMasterSystem<?>> filteredUpToEvents = new ArrayList<>();
    List<IActivityMasterSystem<?>> allFilteredSystems = new ArrayList<>();
    
    // First pass: categorize systems
    for (IActivityMasterSystem<?> system : allSystems)
    {
      // Start collecting from SystemsSystem
      if (!found && SystemsSystem.class.isAssignableFrom(system.getClass()))
      {
        found = true;
      }

      if (found)
      {
        // Add to the complete list of systems
        allFilteredSystems.add(system);
        
        // Check if this is EventsSystem
        boolean isEventsSystem = EventsSystem.class.isAssignableFrom(system.getClass());
        
        // Add to the list of systems up to EventsSystem (inclusive)
        filteredUpToEvents.add(system);
        
        // Add to the list of systems before EventsSystem (exclusive)
        if (!isEventsSystem)
        {
          filteredBeforeEvents.add(system);
        }
        else
        {
          // Stop adding to filteredBeforeEvents once we reach EventsSystem
          break;
        }
      }
    }

    if (allFilteredSystems.isEmpty())
    {
      log.warn("⚠️ No systems found from SystemsSystem onward to install.");
      return Uni.createFrom()
                 .voidItem();
    }

    logProgress("Installing Systems", "Starting installation process with " + allFilteredSystems.size() + " systems");

    // Step 1: Install systems up to but not including EventsSystem
    log.debug("🔄 Step 1: Installing systems up to but not including EventsSystem");
    Uni<Void> step1 = installSystemsSequentially(session, filteredBeforeEvents, enterprise, false);
    
    // Step 2: Register all systems up to EventsSystem
    return step1.chain(() -> {
      log.debug("🔄 Step 2: Registering all systems up to EventsSystem");
      return registerSystemsSequentially(session, filteredUpToEvents, enterprise);
    })
    // Step 3: Rerun installSystems for all available systems
    .chain(() -> {
      log.debug("🔄 Step 3: Installing all available systems");
      return installSystemsSequentially(session, allFilteredSystems, enterprise, false);
    })
    .invoke(v -> log.info("✅ Completed all installation steps for systems"));
  }
  
  private Uni<Void> installSystemsSequentially(Mutiny.Session session, List<IActivityMasterSystem<?>> systems, IEnterprise<?, ?> enterprise, boolean registerSystem)
  {
    if (systems.isEmpty())
    {
      return Uni.createFrom().voidItem();
    }
    
    // Start with the first system
    Uni<Void> result = installSystem(session, systems.get(0), enterprise, registerSystem);
    
    // Chain the rest of the systems sequentially
    for (int i = 1; i < systems.size(); i++)
    {
      final int index = i;
      result = result.chain(() -> installSystem(session, systems.get(index), enterprise, registerSystem));
    }
    
    return result.invoke(v -> log.info("✅ Processed " + systems.size() + " systems"));
  }
  
  private Uni<Void> registerSystemsSequentially(Mutiny.Session session, List<IActivityMasterSystem<?>> systems, IEnterprise<?, ?> enterprise)
  {
    if (systems.isEmpty())
    {
      return Uni.createFrom().voidItem();
    }
    
    // Start with the first system
    Uni<Void> result =  systems.get(0).registerSystem(session, enterprise).replaceWithVoid();// installSystem(session, systems.get(0), enterprise, true);
    
    // Chain the rest of the systems sequentially
    for (int i = 1; i < systems.size(); i++)
    {
      final int index = i;
      var sys = systems.get(index);
      result = result.chain(() -> sys.registerSystem(session, enterprise)
                                      .replaceWithVoid());// installSystem(session, systems.get(index), enterprise, true));
    }
    
    return result.invoke(v -> log.info("✅ Registered " + systems.size() + " systems"));
  }

  private Uni<Void> installSystem(Mutiny.Session session, IActivityMasterSystem<?> system, IEnterprise<?, ?> enterprise,boolean registerSystem)
  {
    String className = system.getClass()
                           .getSimpleName();
    logProgress("Running System", className);
    log.debug("🚀 Starting single system install: " + className);

    return performSystemInstall(session, enterprise, system,registerSystem)
               .invoke(() -> log.debug("✅ System install completed: " + className))
               .onFailure()
               .invoke(err -> log.error("❌ System install failed: " + className, err));
  }

  private Uni<Void> performSystemInstall(Mutiny.Session session, IEnterprise<?, ?> enterprise, IActivityMasterSystem<?> system,boolean registerSystem)
  {
    String className = system.getClass()
                           .getSimpleName();
    String systemName = system.getSystemName();
    String cleanedName = cleanName(className);
    IActivityMasterSystem<?> registeredSystem = system;

    log.debug("➡️ Starting install for: " + systemName + " [" + cleanedName + "]");

    // Get all system installation listeners
    @SuppressWarnings({"rawtypes", "unchecked"})
    Set<IOnSystemInstall> listeners = IGuiceContext.loaderToSet(ServiceLoader.load(IOnSystemInstall.class));
    log.debug("🔧 Notifying " + listeners.size() + " install listeners for system: " + systemName);

    // Process start listeners sequentially
    List<IOnSystemInstall> listenersList = new ArrayList<>(listeners);
    Uni<Void> startListenersChain = Uni.createFrom().voidItem();
    
    // Chain each start listener sequentially
    for (IOnSystemInstall listener : listenersList) {
        final IOnSystemInstall currentListener = listener; // Create final reference for lambda
        startListenersChain = startListenersChain.chain(() -> {
            try {
                currentListener.onSystemInstallStart(systemName);
                log.trace("🟢 Start: " + currentListener.getClass().getSimpleName());
            } catch (Exception e) {
                log.warn("⚠️ Start listener failed: " + currentListener.getClass().getSimpleName(), e);
            }
            return Uni.createFrom().voidItem();
        });
    }
    
    Uni<Void> installChain = startListenersChain;
    
    // If registerSystem is true, skip createDefaults and only call registerSystem
    if (false) {
      log.debug("🔄 Only registering system (skipping createDefaults): " + systemName);
      installChain = installChain.chain(() -> {
        return registeredSystem.registerSystem(session, enterprise).replaceWithVoid();
      });
    } else {
      // Normal installation process - call createDefaults then registerSystem
      installChain = installChain.chain(() -> {
        return registeredSystem.createDefaults(session, enterprise)
                   .onItem()
                   .invoke(() -> log.info("✅ Defaults created for: " + systemName))
                   .onFailure()
                   .recoverWithItem(e -> {
                     log.error("❌ Failed to create defaults for: " + systemName, e);
                     throw new RuntimeException(e);
                   });
      }).replaceWithVoid();
    }
    
    return installChain
               .chain(createResult -> {
                 // Process end listeners sequentially
                 Uni<Void> endListenersChain = Uni.createFrom().voidItem();
                 
                 // Chain each end listener sequentially
                 for (IOnSystemInstall listener : listenersList) {
                     final IOnSystemInstall currentListener = listener; // Create final reference for lambda
                     endListenersChain = endListenersChain.chain(() -> {
                         try {
                             currentListener.onSystemInstallEnd(systemName);
                             log.trace("🟢 End: " + currentListener.getClass().getSimpleName());
                         } catch (Exception e) {
                             log.warn("⚠️ End listener failed: " + currentListener.getClass().getSimpleName(), e);
                         }
                         return Uni.createFrom().voidItem();
                     });
                 }
                 
                 return endListenersChain;
               })
               .invoke(() -> {
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
                                                  .filter(a -> a.getClass()
                                                                   .equals(SystemsSystem.class))
                                                  .toList();

    log.debug("📋 Processing {} base systems sequentially", filtered.size());
    
    // If no systems to process, return immediately
    if (filtered.isEmpty()) {
        log.info("✅ No base systems to install");
        return Uni.createFrom().voidItem();
    }
    
    // Process systems sequentially
    Uni<Void> systemsChain = Uni.createFrom().voidItem();
    
    // Chain each system installation sequentially
    for (IActivityMasterSystem<?> system : filtered) {
        final IActivityMasterSystem<?> currentSystem = system; // Create final reference for lambda
        systemsChain = systemsChain.chain(() -> {
            log.debug("🔄 Installing base system: {}", currentSystem.getSystemName());
            return performSystemInstall(session, enterprise, currentSystem, false);
        });
    }
    
    return systemsChain
               .invoke(() -> log.info("✅ Base systems installed: {}", filtered.size()))
               .onFailure()
               .invoke(err -> log.error("❌ Error during base system installs", err));
  }

  private Uni<Void> createBase(Mutiny.Session session, Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?, ?> enterprise)
  {
    logProgress("Creating Core", "Initializing Core Systems");

    List<IActivityMasterSystem<?>> filtered = allSystems.stream()
                                                  .takeWhile(system -> !SystemsSystem.class.isAssignableFrom(system.getClass()))
                                                  .toList();

    log.debug("📋 Processing {} core systems sequentially", filtered.size());
    
    // If no systems to process, return immediately
    if (filtered.isEmpty()) {
        log.info("✅ No core systems to install");
        return Uni.createFrom().voidItem();
    }
    
    // Process systems sequentially
    Uni<Void> systemsChain = Uni.createFrom().voidItem();
    
    // Chain each system installation sequentially
    for (IActivityMasterSystem<?> system : filtered) {
        final IActivityMasterSystem<?> currentSystem = system; // Create final reference for lambda
        systemsChain = systemsChain.chain(() -> {
            log.debug("🔄 Installing core system: {}", currentSystem.getSystemName());
            return performSystemInstall(session, enterprise, currentSystem, false);
        });
    }
    
    // Properly chain the registerSystem call for the first system
    return systemsChain
               .invoke(() -> log.info("✅ All core systems installed successfully: {} systems", filtered.size()))
               /*.chain(() -> {
                   if (!filtered.isEmpty()) {
                       log.debug("🔄 Registering first core system: {}", filtered.getFirst().getSystemName());
                       return filtered.getFirst().registerSystem(session, enterprise)
                                     .onItem()
                                     .invoke(result -> log.debug("✅ Successfully registered first core system"))
                                     .onFailure()
                                     .invoke(err -> log.error("❌ Failed to register first core system", err))
                                     .replaceWithVoid();
                   } else {
                       return Uni.createFrom().voidItem();
                   }
               })
               .onFailure()*/
               //.invoke(err -> log.error("❌ Failed during system installation", err)
               ;
  }

}
