package com.guicedee.activitymaster.core;

import com.google.inject.Inject;
import com.guicedee.activitymaster.client.implementations.Passwords;
import com.guicedee.activitymaster.client.services.*;
import com.guicedee.activitymaster.client.services.administration.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.EnterpriseClassifications;
import com.guicedee.activitymaster.client.services.classifications.SecurityTokenClassifications;
import com.guicedee.activitymaster.client.services.classifications.types.IPTypes;
import com.guicedee.activitymaster.client.services.classifications.types.IdentificationTypes;
import com.guicedee.activitymaster.client.services.events.IOnSystemInstall;
import com.guicedee.activitymaster.client.services.events.IOnSystemUpdate;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.entities.enterprise.*;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.exceptions.EnterpriseException;
import com.guicedee.activitymaster.core.systems.SystemsSystem;
import com.guicedee.activitymaster.core.updates.DatedUpdate;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IDefaultService;
import com.guicedee.guicedinjection.json.LocalDateDeserializer;
import com.guicedee.guicedinjection.json.LocalDateSerializer;
import com.guicedee.guicedinjection.pairing.Pair;
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
import static com.guicedee.activitymaster.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.client.services.classifications.EnterpriseClassifications.*;
import static com.guicedee.activitymaster.client.services.classifications.types.NameTypes.*;
import static com.guicedee.activitymaster.core.services.ActivityMasterSystemsManager.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

public class EnterpriseService
		implements IProgressable,
		           IEnterpriseService<EnterpriseService>
{
	@Inject
	private ActivityMasterConfiguration configuration;
	
	private static final Logger log = Logger.getLogger(EnterpriseService.class.getName());
	
	public Enterprise create(@NotNull String name, @NotNull String description, IActivityMasterProgressMonitor progressMonitor)
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
		
		return enterprise;
	}
	
	@Override
	public void loadUpdates(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Map<LocalDate, Class<? extends ISystemUpdate>> availableUpdates = getUpdates(enterprise);
		
		log.config(MessageFormat.format("There are {0} required updates", availableUpdates.size()));
		
		progressMonitor.setCurrentTask(0);
		int tasks = 0;
		List<LocalDate> toSort = new ArrayList<>(availableUpdates.keySet());
		toSort.sort(null);
		for (LocalDate a : toSort)
		{
			Class<? extends ISystemUpdate> v = availableUpdates.get(a);
			tasks += v.getAnnotation(DatedUpdate.class)
			          .taskCount() + 1;
		}
		@SuppressWarnings({"unchecked"})
		ISystems<?,?> system = get(ISystemsService.class).getActivityMaster(enterprise);
		
		@SuppressWarnings({"rawtypes", "unchecked"})
		Set<IOnSystemUpdate> systemUpdateEventHandlers = IDefaultService.loaderToSet(ServiceLoader.load(IOnSystemUpdate.class));
		
		progressMonitor.setTotalTasks(tasks);
		availableUpdates.forEach((key, value) -> {
			try
			{
				logProgress("Update System", "Starting updates for " + value.getSimpleName(), progressMonitor);
				for (IOnSystemUpdate<?> systemUpdateEventHandler : systemUpdateEventHandlers)
				{
					systemUpdateEventHandler.onSystemUpdateStart(value);
				}
				ISystemUpdate o = GuiceContext.get(value);
				performUpdate(o, enterprise, progressMonitor);
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
		logProgress("Update System", "Finished Updates. Last Update Date - " + new LocalDateSerializer().convert(LocalDate.now()), progressMonitor);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private void performUpdate(ISystemUpdate o, IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		@SuppressWarnings({ "unchecked"})
		ISystems<?,?> system = get(ISystemsService.class).getActivityMaster(enterprise);
		o.update(enterprise, progressMonitor);
		enterprise.addClassification(UpdateClass.toString(), o.getClass()
		                             .getCanonicalName(), system);
	}
	
	@Override
	public Set<String> getEnterpriseAppliedUpdates(IEnterprise<?,?> enterprise)
	{
		Set<String> set = new LinkedHashSet<>();
		@SuppressWarnings({"unchecked"})
		ISystems<?,?> system = get(ISystemsService.class).getActivityMaster(enterprise);
		List<? extends IRelationshipValue<?, IClassification<?, ?>, ?>> classificationsAll = enterprise.findClassifications(UpdateClass.toString(), system);
		for (IRelationshipValue<?, IClassification<?, ?>, ?> rel : classificationsAll)
		{
			set.add(rel.getValue());
		}
		return set;
	}
	
	@Override
	public Map<LocalDate, Class<? extends ISystemUpdate>> getUpdates(IEnterprise<?,?> enterprise)
	{
		Map<LocalDate, Class<? extends ISystemUpdate>> availableUpdates = new TreeMap<>();
		for (ClassInfo classInfo : GuiceContext.instance()
		                                       .getScanResult()
		                                       .getClassesWithAnnotation(DatedUpdate.class.getCanonicalName()))
		{
			if (classInfo.isAbstract() || classInfo.isInterface())
			{
				continue;
			}
			
			@SuppressWarnings("unchecked")
			Class<? extends ISystemUpdate> clazz = (Class<? extends ISystemUpdate>) classInfo.loadClass();
			DatedUpdate du = clazz.getAnnotation(DatedUpdate.class);
			LocalDate updateDate = null;
			updateDate = new LocalDateDeserializer().convert(du.date());
			availableUpdates.put(updateDate, clazz);
		}
		
		Set<String> enterpriseAppliedUpdates = getEnterpriseAppliedUpdates(enterprise);
		Map<LocalDate, Class<? extends ISystemUpdate>> applicableUpdates = new TreeMap<>();
		
		availableUpdates.forEach((key, value) -> {
			if (!enterpriseAppliedUpdates.contains(value.getCanonicalName()))
			{
				applicableUpdates.put(key, value);
			}
		});
		return applicableUpdates;
	}
	
	
	@Override
	public Map<LocalDate, Class<? extends ISystemUpdate>> getAllUpdates()
	{
		Map<LocalDate, Class<? extends ISystemUpdate>> availableUpdates = new TreeMap<>();
		for (ClassInfo classInfo : GuiceContext.instance()
		                                       .getScanResult()
		                                       .getClassesWithAnnotation(DatedUpdate.class.getCanonicalName()))
		{
			if (classInfo.isAbstract() || classInfo.isInterface())
			{
				continue;
			}
			
			@SuppressWarnings("unchecked")
			Class<? extends ISystemUpdate> clazz = (Class<? extends ISystemUpdate>) classInfo.loadClass();
			DatedUpdate du = clazz.getAnnotation(DatedUpdate.class);
			LocalDate updateDate = null;
			updateDate = new LocalDateDeserializer().convert(du.date());
			availableUpdates.put(updateDate, clazz);
		}
		Map<LocalDate, Class<? extends ISystemUpdate>> applicableUpdates = new TreeMap<>();
		availableUpdates.forEach((key, value) -> {
				applicableUpdates.put(key, value);
			
		});
		return applicableUpdates;
	}
	
	@Override
	@CacheResult(cacheName = "FindEnterpriseWithClassifications")
	public List<IEnterprise> findEnterprisesWithClassification(@CacheKey IClassification<?,?> classification)
	{
		List<UUID> classy = new EnterpriseXClassification().builder()
		                                                   .withClassification(classification)
		                                                   .inActiveRange(classification.getEnterpriseID())
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
	public boolean doesEnterpriseExist(String name)
	{
		return new Enterprise().builder()
		                       .withName(name)
		                       .inDateRange()
		                       .getCount() > 0;
	}
	
	@Override
	public Set<IEnterprise> getIEnterprises()
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
	                                         @NotNull String adminUserName, @NotNull String adminPassword, IActivityMasterProgressMonitor progressMonitor)
	{
		return startNewEnterprise(enterpriseName, adminUserName, adminPassword, null, progressMonitor);
	}
	
	@Override
	public IEnterprise<?,?> startNewEnterprise(String enterpriseName,
	                                         @NotNull String adminUserName, @NotNull String adminPassword, UUID uuidIdentifier, IActivityMasterProgressMonitor progressMonitor)
	{
		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(false);
		
		Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();
		
		int totalTasks = allSystems.stream()
		                           .mapToInt(IActivityMasterSystem::totalTasks)
		                           .sum() + 1;
		
		logProgress("Create Enterprise", "Creating Enterprise", 0, totalTasks, progressMonitor);
		
		Enterprise enterprise = installEnterprise(enterpriseName, progressMonitor);
		createNewEnterprise(enterprise, progressMonitor);
		
		ISystems<?,?> activityMasterSystem = get(ISystemsService.class).getActivityMaster(enterprise);
		createAdminAndCreatorUserForEnterprise(activityMasterSystem, adminUserName, adminPassword, uuidIdentifier, progressMonitor);
		wipeCaches();
		return enterprise;
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private Enterprise installEnterprise(String enterpriseName, IActivityMasterProgressMonitor progressMonitor)
	{
		Enterprise enterprise = create(enterpriseName, enterpriseName, progressMonitor);
		get(ActivityMasterConfiguration.class)
				.setEnterpriseName(enterpriseName);
		return enterprise;
	}

	@Override
	public void createNewEnterprise(@NotNull IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(false);
		Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();
		wipeCaches();
		createBase(progressMonitor, allSystems, enterprise);
		wipeCaches();
		createBaseSystems(progressMonitor, allSystems, enterprise);
		wipeCaches();
		installSystems(progressMonitor, allSystems, enterprise);
		wipeCaches();
		progressMonitor.setCurrentTask(0);
		logProgress("System Configuration", "Starting system updates", 1, progressMonitor);
		loadUpdates(enterprise, progressMonitor);
		logProgress("System Configuration", "Done", 1, progressMonitor);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private void installSystems(IActivityMasterProgressMonitor progressMonitor, Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?,?> enterprise)
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
				                                       .getSimpleName(), progressMonitor);
				performSystemInstall(progressMonitor, enterprise, allSystem);
			}
		}
	}
	
	private void performSystemInstall(IActivityMasterProgressMonitor progressMonitor, IEnterprise<?,?> enterprise, IActivityMasterSystem<?> allSystem)
	{
		String nameC = cleanName(allSystem.getClass()
		                                  .getSimpleName());
		IActivityMasterSystem<?> registeredSystem = get(allSystem.getClass());
		
		@SuppressWarnings({"rawtypes", "unchecked"})
		Set<IOnSystemInstall> systemInstallEventListeners = IDefaultService.loaderToSet(ServiceLoader.load(IOnSystemInstall.class));
		systemInstallEventListeners.forEach(a->{
			a.onSystemInstallStart(registeredSystem.getSystemName());
		});
		
		registeredSystem.registerSystem(enterprise, progressMonitor);
		registeredSystem.createDefaults(enterprise, progressMonitor);
		systemInstallEventListeners.forEach(a->{
			a.onSystemInstallEnd(registeredSystem.getSystemName());
		});
		
		logProgress("Installed System", nameC, 1, progressMonitor);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private void createBaseSystems(IActivityMasterProgressMonitor progressMonitor, Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?,?> enterprise)
	{
		logProgress("Creating Base Systems", "Initializing Base Systems", progressMonitor);
		for (Iterator<IActivityMasterSystem<?>> iterator = allSystems.iterator(); iterator.hasNext(); )
		{
			IActivityMasterSystem<?> allSystem = iterator.next();
			if (allSystem.getClass()
			             .isAssignableFrom(SystemsSystem.class))
			{
				break;
			}
			performSystemInstall(progressMonitor, enterprise, allSystem);
		}
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private void createBase(IActivityMasterProgressMonitor progressMonitor, Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?,?> enterprise)
	{
		logProgress("Creating Core", "Initializing Core Systems", progressMonitor);
		for (Iterator<IActivityMasterSystem<?>> iterator = allSystems.iterator(); iterator.hasNext(); )
		{
			IActivityMasterSystem<?> allSystem = iterator.next();
			if (allSystem.getClass()
			             .isAssignableFrom(SystemsSystem.class))
			{
				break;
			}
			performSystemInstall(progressMonitor, enterprise, allSystem);
		}
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	protected IInvolvedParty<?,?> createAdminAndCreatorUserForEnterprise(ISystems<?,?> system, String adminUserName,
	                                                                   @NotNull String adminPassword, UUID existingLocalKey, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Checking base administrator user", "The default user is being checked for compliance", 1, progressMonitor);
		
		UUID token = get(ISystemsService.class).getSecurityIdentityToken(system);
		
		SecurityToken administratorsGroup = (SecurityToken) get(SecurityTokenService.class).getAdministratorsFolder(system);
		
		InvolvedPartyService service = get(InvolvedPartyService.class);
		
		Pair<String, String> pair = new Pair<>(
				IdentificationTypes.IdentificationTypeEnterpriseCreatorRole.toString(),
				new Passwords().integerEncrypt(adminUserName.getBytes()));
		Optional<InvolvedParty> exists = new InvolvedParty().builder()
		                                                    .findByIdentificationType(system,
				                                                    IdentificationTypes.IdentificationTypeEnterpriseCreatorRole,
				                                                    new Passwords().integerEncrypt(adminUserName.getBytes()))
		                                                    .get();
		
		IInvolvedParty<?,?> administratorUser;
		if (exists.isEmpty())
		{
			IInvolvedParty<?,?> adminUser = service.create(system, pair, true);
			
			adminUser.addOrReuseInvolvedPartyIdentificationType( NoClassification.toString(),IdentificationTypes.IdentificationTypeUserName.toString(),
					adminUserName, system, token);
			
			adminUser.addOrReuseInvolvedPartyType(NoClassification.toString(),IPTypes.TypeIndividual.toString(),  "Creator Individual", system, token);
			adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(),PreferredNameType.toString(),  "Enterprise Creator", system, token);
			adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(),CommonNameType.toString(),  "Enterprise Creator", system, token);
			adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(),FullNameType.toString(),  "Enterprise Creator", system, token);
			adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(),FirstNameType.toString(),  "Administrator", system, token);
			
			get(SecurityTokenService.class).create(SecurityTokenClassifications.Identity.toString(),
					adminUserName,
					"The creator of the enterprise", system, administratorsGroup, token);
			
			adminUser.addOrReuseInvolvedPartyIdentificationType(NoClassification.toString(),IdentificationTypes.IdentificationTypeEnterpriseCreatorRole.toString(),
					adminUserName, system,					token);
			if (existingLocalKey != null)
			{
				adminUser.addOrReuseInvolvedPartyIdentificationType( NoClassification.toString(),"IdentificationTypeWebClientUUID",
						existingLocalKey.toString(), system,
						token);
			}
			
			service.addUpdateUsernamePassword(adminUserName, adminPassword, adminUser, system, token);
			adminUser.createDefaultSecurity(system, token);
			administratorUser = adminUser;
		}
		else
		{
			administratorUser = exists.get();
		}
		
		logProgress("Systems", "Running Systems Post Startups", 1, progressMonitor);
		
		Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();
		allSystems.forEach(a->{
			a.postStartup(system.getEnterprise(),progressMonitor);
		});
		return administratorUser;
	}
}
