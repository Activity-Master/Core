package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassifications;
import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemInstall;
import com.guicedee.activitymaster.fsdm.client.services.events.IOnSystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.EnterpriseException;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.*;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.fsdm.services.providers.EnterpriseProvider;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IDefaultService;
import com.guicedee.guicedinjection.json.LocalDateSerializer;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import io.github.classgraph.ClassInfo;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.validation.constraints.NotNull;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassifications.*;
import static com.guicedee.activitymaster.fsdm.services.ActivityMasterSystemsManager.*;

@SuppressWarnings("DuplicatedCode")
public class EnterpriseService
		implements IProgressable,
		           IEnterpriseService<EnterpriseService>
{
	@Inject
	private ActivityMasterConfiguration configuration;
	
	private static final Logger log = Logger.getLogger(EnterpriseService.class.getName());
	
	public IEnterprise<?,?> get()
	{
		return new Enterprise();
	}
	
	public Enterprise create(@NotNull String name, @NotNull String description)
	{
		Enterprise enterprise = new Enterprise();
		Optional<Enterprise> exists = enterprise.builder()
		                                        .withName(name)
		                                        .get();
		
		if (exists.isEmpty())
		{
			enterprise.setName(name);
			enterprise.setDescription(description);
			enterprise.persist();
		}
		else
		{
			enterprise = exists.get();
		}
		EnterpriseProvider.loadedEnterprise = enterprise;
		return enterprise;
	}
	
	@Override
	public void loadUpdates(IEnterprise<?,?> enterprise)
	{
		Map<Integer, Class<? extends ISystemUpdate>> availableUpdates = getUpdates(enterprise);
		
		log.config(MessageFormat.format("There are {0} required updates", availableUpdates.size()));
		
		setCurrentTask(0);
		int tasks = 0;
		for (Map.Entry<Integer, Class<? extends ISystemUpdate>> entry : availableUpdates.entrySet())
		{
			Class<? extends ISystemUpdate> aClass = entry.getValue();
			tasks += aClass.getAnnotation(SortedUpdate.class)
			               .taskCount();
		}
		
		@SuppressWarnings({"unchecked"})
		ISystems<?,?> system = GuiceContext.get(ISystemsService.class).getActivityMaster(enterprise);
		
		@SuppressWarnings({"rawtypes", "unchecked"})
		Set<IOnSystemUpdate> systemUpdateEventHandlers = IDefaultService.loaderToSet(ServiceLoader.load(IOnSystemUpdate.class));
		
		setTotalTasks(tasks);
		availableUpdates.forEach((key, value) -> {
			try
			{
				logProgress("Update System", "Starting updates for " + value.getSimpleName());
				for (IOnSystemUpdate<?> systemUpdateEventHandler : systemUpdateEventHandlers)
				{
					systemUpdateEventHandler.onSystemUpdateStart(value);
				}
				ISystemUpdate o = GuiceContext.get(value);
				performUpdate(o, enterprise);
				for (IOnSystemUpdate<?> a : systemUpdateEventHandlers)
				{
					a.onSystemUpdateEnd(value);
				}
			}catch (Throwable T)
			{
				log.log(Level.SEVERE,"Unable to perform update",T);
				for (IOnSystemUpdate<?> a : systemUpdateEventHandlers)
				{
					a.onSystemUpdateFail(value);
				}
			}
		});
		enterprise.addOrUpdateClassification(EnterpriseClassifications.LastUpdateDate.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd")
		                                                                                  .format(LocalDate.now()), system);
		logProgress("Update System", "Finished Updates. Last Update Date - " + new LocalDateSerializer().convert(LocalDate.now()));
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	private void performUpdate(ISystemUpdate o, IEnterprise<?,?> enterprise)
	{
		@SuppressWarnings({ "unchecked"})
		ISystems<?,?> system = GuiceContext.get(ISystemsService.class).getActivityMaster(enterprise);
		o.update(enterprise);
		enterprise.addClassification(UpdateClass.toString(), o.getClass()
		                             .getCanonicalName(), system);
	}
	
	@Override
	public Set<String> getEnterpriseAppliedUpdates(IEnterprise<?,?> enterprise)
	{
		Set<String> set = new LinkedHashSet<>();
		@SuppressWarnings({"unchecked"})
		ISystems<?,?> system = GuiceContext.get(ISystemsService.class).getActivityMaster(enterprise);
		List<? extends IRelationshipValue<?, IClassification<?, ?>, ?>> classificationsAll = enterprise.findClassifications(UpdateClass.toString(), system);
		for (IRelationshipValue<?, IClassification<?, ?>, ?> rel : classificationsAll)
		{
			set.add(rel.getValue());
		}
		return set;
	}
	
	@Override
	public Map<Integer, Class<? extends ISystemUpdate>> getUpdates(IEnterprise<?,?> enterprise)
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
		
		Set<String> enterpriseAppliedUpdates = getEnterpriseAppliedUpdates(enterprise);
		Map<Integer, Class<? extends ISystemUpdate>> applicableUpdates = new TreeMap<>();
		
		availableUpdates.forEach((key, value) -> {
			if (!enterpriseAppliedUpdates.contains(value.getCanonicalName()))
			{
				applicableUpdates.put(key, value);
			}
		});
		return applicableUpdates;
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
		availableUpdates.forEach((key, value) -> {
				applicableUpdates.put(key, value);
			
		});
		return applicableUpdates;
	}
	
	@Override
	@CacheResult(cacheName = "FindEnterpriseWithClassifications")
	public List<IEnterprise<?,?>> findEnterprisesWithClassification(@CacheKey IClassification<?,?> classification)
	{
		List<UUID> classy = new EnterpriseXClassification().builder()
		                                                   .withClassification(classification)
		                                                   .inActiveRange()
		                                                   .inDateRange()
		                                                   .selectColumn(EnterpriseXClassification_.enterpriseID)
		                                                   .getAll(UUID.class);
		
		EnterpriseQueryBuilder builder = new Enterprise().builder();
		builder = builder.where(Enterprise_.id, InList, classy);
		return new ArrayList<>(builder.getAll());
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Optional<IEnterprise<?,?>> findEnterprise(String name)
	{
		return (Optional) new Enterprise().builder()
		                                  .withName(name)
		                                  .inDateRange()
		                                  .get();
	}
	
	@Override
	@CacheResult(cacheName = "GetEnterpriseByEnterpriseNameString")
	public IEnterprise<?,?> getEnterprise(@CacheKey String name)
	{
		return new Enterprise().builder()
		                       .withName(name)
		                       .inDateRange()
		                       .get()
		                       .orElseThrow(() -> new EnterpriseException("No Such Enterprise - " + name));
	}
	
	
	@Override
	@CacheResult(cacheName = "GetEnterpriseByEnterpriseByUUID")
	public IEnterprise<?,?> getEnterprise(@CacheKey UUID uuid)
	{
		return new Enterprise().builder()
		                       .find(uuid)
		                       .inDateRange()
		                       .get()
		                       .orElseThrow(() -> new EnterpriseException("No Such Enterprise - " + uuid));
	}
	
	@Override
	public boolean doesEnterpriseExist(String name)
	{
		return new Enterprise().builder()
		                       .withName(name)
		                       .inDateRange()
		                       .getCount() > 0;
	}
	
	@Override
	public Set<IEnterprise<?,?>> getIEnterprises()
	{
		return new TreeSet<>(new Enterprise().builder()
		                                     .inDateRange()
		                                     .getAll());
	}
	
	@CacheResult
	@Override
	public IEnterprise<?,?> getIEnterpriseFromName(@CacheKey String enterprise)
	{
		return new Enterprise().builder()
		                       .withName(enterprise)
		                       .get()
		                       .orElseThrow(() -> new EnterpriseException("No Enterprise for the given name"));
	}
	
	@CacheResult
	@Override
	public IEnterprise<?,?> getIEnterpriseFromID(@CacheKey UUID enterprise)
	{
		return new Enterprise().builder()
		                       .find(enterprise)
		                       .get()
		                       .orElseThrow(() -> new EnterpriseException("No Enterprise for the given UUID"));
	}
	
	@Override
	public IEnterprise<?,?> startNewEnterprise(String enterpriseName,
	                                         @NotNull String adminUserName, @NotNull String adminPassword)
	{
		return startNewEnterprise(enterpriseName, adminUserName, adminPassword, null);
	}
	
	@Override
	public IEnterprise<?,?> startNewEnterprise(String enterpriseName,
	                                         @NotNull String adminUserName, @NotNull String adminPassword, UUID uuidIdentifier)
	{
		GuiceContext.get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(false);
		
		Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();
		
		int totalTasks = allSystems.stream()
		                           .mapToInt(IActivityMasterSystem::totalTasks)
		                           .sum() + 1;
		
		logProgress("Create Enterprise", "Creating Enterprise", 0, totalTasks);
		
		Enterprise enterprise = installEnterprise(enterpriseName);
		createNewEnterprise(enterprise);
		
		ISystems<?,?> activityMasterSystem = GuiceContext.get(ISystemsService.class).getActivityMaster(enterprise);
		IPasswordsService<?> passwordsService = GuiceContext.get(IPasswordsService.class);
		passwordsService.createAdminAndCreatorUserForEnterprise(activityMasterSystem, adminUserName, adminPassword, uuidIdentifier);
		wipeCaches();
		
		logProgress("Systems", "Running Systems Post Startups", 1);
		
		performPostStartup(enterprise);
		return enterprise;
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private Enterprise installEnterprise(String enterpriseName)
	{
		Enterprise enterprise = create(enterpriseName, enterpriseName);
		GuiceContext.get(ActivityMasterConfiguration.class)
				.setApplicationEnterpriseName(enterpriseName);
		return enterprise;
	}

	@Override
	public void createNewEnterprise(@NotNull IEnterprise<?,?> enterprise)
	{
		GuiceContext.get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(false);
		Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();
		
		wipeCaches();
		createBase(allSystems, enterprise);
		wipeCaches();
		createBaseSystems(allSystems, enterprise);
		wipeCaches();
		installSystems(allSystems, enterprise);
		wipeCaches();
		setCurrentTask(0);
		logProgress("System Configuration", "Starting system updates", 1);
		loadUpdates(enterprise);
		logProgress("System Configuration", "Done", 1);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private void installSystems( Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?,?> enterprise)
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
				performSystemInstall(enterprise, allSystem);
			}
		}
	}
	
	private void performSystemInstall( IEnterprise<?,?> enterprise, IActivityMasterSystem<?> allSystem)
	{
		String nameC = cleanName(allSystem.getClass()
		                                  .getSimpleName());
		IActivityMasterSystem<?> registeredSystem = GuiceContext.get(allSystem.getClass());
		
		@SuppressWarnings({"rawtypes", "unchecked"})
		Set<IOnSystemInstall> systemInstallEventListeners = IDefaultService.loaderToSet(ServiceLoader.load(IOnSystemInstall.class));
		systemInstallEventListeners.forEach(a->{
			a.onSystemInstallStart(registeredSystem.getSystemName());
		});
		ISystems<?, ?> registerSystem = registeredSystem.registerSystem(enterprise);
		
		registeredSystem.createDefaults(enterprise);
		systemInstallEventListeners.forEach(a->{
			a.onSystemInstallEnd(registeredSystem.getSystemName());
		});
		
		logProgress("Installed System", nameC, 1);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private void createBaseSystems( Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?,?> enterprise)
	{
		logProgress("Creating Base Systems", "Initializing Base Systems");
		for (Iterator<IActivityMasterSystem<?>> iterator = allSystems.iterator(); iterator.hasNext(); )
		{
			IActivityMasterSystem<?> allSystem = iterator.next();
			if (allSystem.getClass()
			             .isAssignableFrom(SystemsSystem.class))
			{
				break;
			}
			performSystemInstall(enterprise, allSystem);
		}
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private void createBase( Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?,?> enterprise)
	{
		logProgress("Creating Core", "Initializing Core Systems");
		for (Iterator<IActivityMasterSystem<?>> iterator = allSystems.iterator(); iterator.hasNext(); )
		{
			IActivityMasterSystem<?> allSystem = iterator.next();
			if (allSystem.getClass()
			             .isAssignableFrom(SystemsSystem.class))
			{
				break;
			}
			performSystemInstall(enterprise, allSystem);
		}
	}
}
