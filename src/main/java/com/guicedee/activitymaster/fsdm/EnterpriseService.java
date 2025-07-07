package com.guicedee.activitymaster.fsdm;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
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
import io.vertx.core.Future;
import jakarta.validation.constraints.NotNull;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.*;
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

	public IEnterprise<?, ?> get()
	{
		return new Enterprise();
	}

	//@Transactional()
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
	public int loadUpdates(IEnterprise<?, ?> enterprise)
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
		ISystems<?, ?> system = com.guicedee.client.IGuiceContext.get(ISystemsService.class)
		                                                         .getActivityMaster(enterprise);

		@SuppressWarnings({"rawtypes", "unchecked"})
		Set<IOnSystemUpdate> systemUpdateEventHandlers = IGuiceContext.loaderToSet(ServiceLoader.load(IOnSystemUpdate.class));

		setTotalTasks(tasks);

		for (Map.Entry<Integer, Class<? extends ISystemUpdate>> entry : availableUpdates.entrySet())
		{
			Integer key = entry.getKey();
			Class<? extends ISystemUpdate> value = entry.getValue();
			try
			{
				logProgress("Update System", "Starting updates for " + value.getSimpleName());
				for (IOnSystemUpdate<?> systemUpdateEventHandler : systemUpdateEventHandlers)
				{
					systemUpdateEventHandler.onSystemUpdateStart(value);
				}
				ISystemUpdate o = com.guicedee.client.IGuiceContext.get(value);
				performUpdate(o, enterprise);
				for (IOnSystemUpdate<?> a : systemUpdateEventHandlers)
				{
					a.onSystemUpdateEnd(value);
				}
			}
			catch (Throwable T)
			{
				log.log(Level.SEVERE, "Unable to perform update", T);
				for (IOnSystemUpdate<?> a : systemUpdateEventHandlers)
				{
					a.onSystemUpdateFail(value);
				}
			}
		}
		updateLastUpdateDate(enterprise, system);
		logProgress("Update System", "Finished Updates. Last Update Date - " + DateTimeFormatter.ofPattern("yyyy/MM/dd")
		                                                                                        .format(LocalDate.now()));
		return availableUpdates.size();
	}

	@jakarta.transaction.Transactional
	void updateLastUpdateDate(IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
	{
		enterprise.addOrUpdateClassification(EnterpriseClassifications.LastUpdateDate.toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd")
		                                                                                                           .format(LocalDate.now()), system);
	}

	//@Transactional()
	void performUpdate(ISystemUpdate o, IEnterprise<?,?> enterprise)
	{
		@SuppressWarnings({ "unchecked"})
		ISystems<?,?> system = com.guicedee.client.IGuiceContext.get(ISystemsService.class).getActivityMaster(enterprise);
		Future<Boolean> updateFuture = o.update(enterprise);
		// Wait for the future to complete to ensure sequential execution
		updateFuture.result(); // This blocks until the future completes
		enterprise.addClassification(UpdateClass.toString(), o.getClass()
		                             .getCanonicalName(), system);
	}

	@Override
	public Set<String> getEnterpriseAppliedUpdates(IEnterprise<?,?> enterprise)
	{
		Set<String> set = new LinkedHashSet<>();
		@SuppressWarnings({"unchecked"})
		ISystems<?,?> system = com.guicedee.client.IGuiceContext.get(ISystemsService.class).getActivityMaster(enterprise);
		List<? extends IRelationshipValue<?, IClassification<?, ?>, ?>> classificationsAll = enterprise.findClassifications(UpdateClass.toString(), system);
		for (IRelationshipValue<?, IClassification<?, ?>, ?> rel : classificationsAll)
		{
			String classValue = rel.getValue();
			if(classValue.contains("$$EnhancerByGuice$$"))
			{
				classValue = classValue.substring(0, classValue.indexOf("$$EnhancerByGuice$$"));
			}
			set.add(classValue);
		}
		return set;
	}

	@Override
	@jakarta.transaction.Transactional
	public Map<Integer, Class<? extends ISystemUpdate>> getUpdates(IEnterprise<?,?> enterprise)
	{
		Map<Integer, Class<? extends ISystemUpdate>> availableUpdates = new TreeMap<>();
		for (ClassInfo classInfo : GuiceContext.instance().getScanResult().getClassesWithAnnotation(SortedUpdate.class.getCanonicalName()))
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
		for (String enterpriseAppliedUpdate : enterpriseAppliedUpdates)
		{
			log.config("System Installed Update [" + enterpriseAppliedUpdate+ "]");
		}
		Map<Integer, Class<? extends ISystemUpdate>> applicableUpdates = new TreeMap<>();
		for (Map.Entry<Integer, Class<? extends ISystemUpdate>> entry : availableUpdates.entrySet())
		{
			Integer key = entry.getKey();
			Class<? extends ISystemUpdate> value = entry.getValue();
			String classValue = value.getCanonicalName();
			if(classValue.contains("$$EnhancerByGuice$$"))
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
	}


	@Override
	public Map<Integer, Class<? extends ISystemUpdate>> getAllUpdates()
	{
		Map<Integer, Class<? extends ISystemUpdate>> availableUpdates = new TreeMap<>();
		for (ClassInfo classInfo : GuiceContext.instance().getScanResult().getClassesWithAnnotation(SortedUpdate.class.getCanonicalName()))
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
	//@Transactional()
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Optional<IEnterprise<?,?>> findEnterprise(String name)
	{
		return (Optional) new Enterprise().builder()
		                                  .withName(name)
		                                  .inDateRange()
		                                  .get();
	}
	//@Transactional()
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

	//@Transactional()
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
	//@Transactional()
	@Override
	public boolean doesEnterpriseExist(String name)
	{
		return new Enterprise().builder()
		                       .withName(name)
		                       .inDateRange()
		                       .getCount() > 0;
	}
	//@Transactional()
	@Override
	public Set<IEnterprise<?,?>> getIEnterprises()
	{
		return new TreeSet<>(new Enterprise().builder()
		                                     .inDateRange()
		                                     .getAll());
	}
	//@Transactional()
	@CacheResult
	@Override
	public IEnterprise<?,?> getIEnterpriseFromName(@CacheKey String enterprise)
	{
		return new Enterprise().builder()
		                       .withName(enterprise)
		                       .get()
		                       .orElseThrow(() -> new EnterpriseException("No Enterprise for the given name"));
	}
	//@Transactional()
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
		com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(false);

		Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();

		int totalTasks = allSystems.stream()
		                           .mapToInt(IActivityMasterSystem::totalTasks)
		                           .sum() + 1;

		logProgress("Create Enterprise", "Creating Enterprise", 0, totalTasks);

		Enterprise enterprise = installEnterprise(enterpriseName);
		createNewEnterprise(enterprise);

		ISystems<?,?> activityMasterSystem = com.guicedee.client.IGuiceContext.get(ISystemsService.class).getActivityMaster(enterprise);


		IPasswordsService<?> passwordsService = com.guicedee.client.IGuiceContext.get(IPasswordsService.class);
		passwordsService.createAdminAndCreatorUserForEnterprise(activityMasterSystem, adminUserName, adminPassword, uuidIdentifier);
		wipeCaches();

		logProgress("Systems", "Running Systems Post Startups", 1);

		performPostStartup(enterprise);
		return enterprise;
	}


	private Enterprise installEnterprise(String enterpriseName)
	{
		Enterprise enterprise = create(enterpriseName, enterpriseName);
		com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
				.setApplicationEnterpriseName(enterpriseName);
		return enterprise;
	}

	@Override
	public void createNewEnterprise(@NotNull IEnterprise<?,?> enterprise)
	{
		com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(false);
		Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();
		createBase(allSystems, enterprise);
		createBaseSystems(allSystems, enterprise);
		installSystems(allSystems, enterprise);
		setCurrentTask(0);
		logProgress("System Configuration", "Starting system updates", 1);
		loadUpdates(enterprise);
		logProgress("System Configuration", "Done", 1);
		//todo securities
	//	com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
	//	                                 .setSecurityEnabled(true);
	}

	//@Transactional()
	@Override
	public boolean isEnterpriseReady()
	{
		if (!Strings.isNullOrEmpty(applicationEnterpriseName))
		{
			IEnterpriseService<?> enterpriseService = com.guicedee.client.IGuiceContext.get(IEnterpriseService.class);
			try
			{
				IEnterprise<?, ?> enterprise = enterpriseService.getEnterprise(applicationEnterpriseName);
				ISystems<?, ?> system = com.guicedee.client.IGuiceContext.get(IActivityMasterSystem.class)
				                                    .getSystem(applicationEnterpriseName);
				return enterprise.builder()
				                                   .hasClassification(EnterpriseClassifications.LastUpdateDate.toString(), system)
				                                   .getCount() > 0;

			}
			catch (SystemsException e)
			{
				log.log(Level.WARNING, "System is not ready");
			}
			catch (EnterpriseException e)
			{
				log.log(Level.WARNING, "Enterprise is not ready");
			}
		}
		return false;
	}

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
				installSystem(allSystem,enterprise);
			}
		}
	}

	private void installSystem(IActivityMasterSystem<?> system, IEnterprise<?,?> enterprise)
	{
		logProgress("Running System ", system.getClass()
		                                        .getSimpleName());
		performSystemInstall(enterprise, system);
	}


	private void performSystemInstall( IEnterprise<?,?> enterprise, IActivityMasterSystem<?> allSystem)
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

	private void createBaseSystems( Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?,?> enterprise)
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

	private void createBase( Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?,?> enterprise)
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
