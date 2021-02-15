package com.guicedee.activitymaster.core;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.*;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.services.*;
import com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.classifications.securitytokens.SecurityTokenClassifications;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;
import com.guicedee.activitymaster.core.services.exceptions.EnterpriseException;
import com.guicedee.activitymaster.core.services.security.Passwords;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.activitymaster.core.services.types.IPTypes;
import com.guicedee.activitymaster.core.services.types.IdentificationTypes;
import com.guicedee.activitymaster.core.systems.SystemsSystem;
import com.guicedee.activitymaster.core.updates.DatedUpdate;
import com.guicedee.activitymaster.core.updates.ISystemUpdate;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.json.LocalDateDeserializer;
import com.guicedee.guicedinjection.json.LocalDateSerializer;
import com.guicedee.guicedinjection.pairing.Pair;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import io.github.classgraph.ClassInfo;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications.*;
import static com.guicedee.activitymaster.core.services.types.NameTypes.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

public class EnterpriseService
		implements IProgressable,
		           IEnterpriseService<EnterpriseService>
{
	@Inject
	@Named(EnterpriseSystemName)
	private ISystems<?> system;
	
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
	public void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Map<LocalDate, Class<? extends ISystemUpdate>> availableUpdates = getUpdates(enterprise);
		
		log.config("There are " + availableUpdates.size() + " required updates");
		
		progressMonitor.setCurrentTask(0);
		int tasks = 0;
		List<LocalDate> toSort = new ArrayList<>();
		for (LocalDate a : availableUpdates.keySet())
		{
			toSort.add(a);
		}
		toSort.sort(null);
		for (LocalDate a : toSort)
		{
			Class<? extends ISystemUpdate> v = availableUpdates.get(a);
			tasks += v.getAnnotation(DatedUpdate.class)
			          .taskCount() + 1;
		}
		progressMonitor.setTotalTasks(tasks);
		availableUpdates.forEach((key, value) -> {
			logProgress("Update System", "Starting updates for " + value.getSimpleName(), progressMonitor);
			ISystemUpdate o = GuiceContext.get(value);
			performUpdate(o, enterprise, progressMonitor);
		});
		enterprise.addOrUpdate(EnterpriseClassifications.LastUpdateDate, DateTimeFormatter.ofPattern("yyyy/MM/dd")
		                                                                                  .format(LocalDate.now()), system);
		logProgress("Update System", "Finished Updates. Last Update Date - " + new LocalDateSerializer().convert(LocalDate.now()), progressMonitor);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private void performUpdate(ISystemUpdate o, IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		o.update(enterprise, progressMonitor);
		enterprise.add(UpdateClass, o.getClass()
		                             .getCanonicalName(), system);
	}
	
	@Override
	public Set<String> getEnterpriseAppliedUpdates(IEnterprise<?> enterprise)
	{
		Set<String> set = new LinkedHashSet<>();
		List<IRelationshipValue<IEnterprise<?>, IClassification<?>, ?>> classificationsAll = enterprise.findClassificationsAll(UpdateClass, system);
		for (IRelationshipValue<IEnterprise<?>, IClassification<?>, ?> rel : classificationsAll)
		{
			set.add(rel.getValue());
		}
		return set;
	}
	
	@Override
	public Map<LocalDate, Class<? extends ISystemUpdate>> getUpdates(IEnterprise<?> enterprise)
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
	@CacheResult(cacheName = "FindEnterpriseWithClassifications")
	public List<IEnterprise<?>> findEnterprisesWithClassification(@CacheKey Classification classification)
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
	public Optional<IEnterprise<?>> findEnterprise(IEnterpriseName<?> name)
	{
		return (Optional) new Enterprise().builder()
		                                  .withName(name.classificationName())
		                                  .inDateRange()
		                                  .get();
	}
	
	@Override
	@CacheResult(cacheName = "GetEnterpriseByEnterpriseNameString")
	public IEnterprise<?> getEnterprise(@CacheKey String name)
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
	
	/**
	 * Gets an enterprise or throws an exception.
	 * <p>
	 * Result is cached
	 *
	 * @param name the name of the enterprise
	 * @return The enterprise
	 */
	@Override
	@CacheResult(cacheName = "GetEnterpriseByEnterpriseName")
	public IEnterprise<?> getEnterprise(@CacheKey IEnterpriseName<?> name)
	{
		return getEnterprise(name.classificationName());
	}
	
	@Override
	public Set<IEnterprise<?>> getIEnterprises()
	{
		return new TreeSet<>(new Enterprise().builder()
		                                     .inDateRange()
		                                     .getAll());
	}
	
	@CacheResult
	@Override
	public IEnterprise<?> getIEnterpriseFromName(@CacheKey IEnterpriseName<?> enterprise)
	{
		return new Enterprise().builder()
		                       .withName(enterprise.classificationName())
		                       .get()
		                       .orElseThrow(() -> new EnterpriseException("No Enterprise for the given name"));
	}
	
	@CacheResult
	@Override
	public IEnterprise<?> getIEnterpriseFromID(@CacheKey UUID enterprise)
	{
		return new Enterprise().builder()
		                       .find(enterprise)
		                       .get()
		                       .orElseThrow(() -> new EnterpriseException("No Enterprise for the given UUID"));
	}
	
	@Override
	public IEnterprise<?> startNewEnterprise(IEnterpriseName<?> enterpriseName,
	                                         @NotNull String adminUserName, @NotNull String adminPassword, IActivityMasterProgressMonitor progressMonitor)
	{
		return startNewEnterprise(enterpriseName.classificationName(), adminUserName, adminPassword, null, progressMonitor);
	}
	
	@Override
	public IEnterprise<?> startNewEnterprise(String enterpriseName,
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
		
		//Create Involved Party for Enterprise
		ISystems<?> activityMasterSystem = get(ISystemsService.class).getActivityMaster(enterprise);
		createAdminAndCreatorUserForEnterprise(activityMasterSystem, adminUserName, adminPassword, uuidIdentifier, progressMonitor);
		
		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(true);
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
	public void createNewEnterprise(@NotNull IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(false);
		Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();
		
		createBase(progressMonitor, allSystems, enterprise);
		
		createBaseSystems(progressMonitor, allSystems, enterprise);
		
		installSystems(progressMonitor, allSystems, enterprise);
		
		progressMonitor.setCurrentTask(0);
		logProgress("System Configuration", "Starting system updates", 1, progressMonitor);
		loadUpdates(enterprise, progressMonitor);
		logProgress("System Configuration", "Done", 1, progressMonitor);
		/*get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(true);*/
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private void installSystems(IActivityMasterProgressMonitor progressMonitor, Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?> enterprise)
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
				logProgress("Running System", allSystem.getClass()
				                                       .getSimpleName(), progressMonitor);
				String nameC = cleanName(allSystem.getClass()
				                                  .getSimpleName());
				IActivityMasterSystem<?> registeredSystem = get(allSystem.getClass());
				registeredSystem.registerSystem(enterprise, progressMonitor);
				
				registeredSystem.createDefaults(enterprise, progressMonitor);
				logProgress("Completed with System", nameC, 1, progressMonitor);
			}
		}
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private void createBaseSystems(IActivityMasterProgressMonitor progressMonitor, Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?> enterprise)
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
			
			String nameC = cleanName(allSystem.getClass()
			                                  .getSimpleName());
			IActivityMasterSystem<?> registeredSystem = get(allSystem.getClass());
			registeredSystem.registerSystem(enterprise, progressMonitor);
			//registeredSystem.createDefaults(enterprise, progressMonitor);
			logProgress("Completed with System", nameC, 1, progressMonitor);
		}
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	private void createBase(IActivityMasterProgressMonitor progressMonitor, Set<IActivityMasterSystem<?>> allSystems, IEnterprise<?> enterprise)
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
			String nameC = cleanName(allSystem.getClass()
			                                  .getSimpleName());
			IActivityMasterSystem<?> registeredSystem = get(allSystem.getClass());
			//	registeredSystem.registerSystem(enterprise, progressMonitor);
			
			registeredSystem.createDefaults(enterprise, progressMonitor);
			logProgress("Completed with Core", nameC, 1, progressMonitor);
		}
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class, timeout = 30)
	protected IInvolvedParty<?> createAdminAndCreatorUserForEnterprise(ISystems<?> system, String adminUserName,
	                                                                   @NotNull String adminPassword, UUID existingLocalKey, IActivityMasterProgressMonitor progressMonitor)
	{
		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(false);
		
		logProgress("Checking base administrator user", "The default user is being checked for compliance", 1, progressMonitor);
		
		UUID token = get(ISystemsService.class).getSecurityIdentityToken(system);
		
		SecurityToken administratorsGroup = (SecurityToken) get(SecurityTokenService.class).getAdministratorsFolder(system);
		
		InvolvedPartyService service = get(InvolvedPartyService.class);
		
		Pair<IIdentificationType<?>, String> pair = new Pair<>(
				IdentificationTypes.IdentificationTypeEnterpriseCreatorRole,
				new Passwords().integerEncrypt(adminUserName.getBytes()));
		Optional<InvolvedParty> exists = new InvolvedParty().builder()
		                                                    .findByIdentificationType(system,
				                                                    IdentificationTypes.IdentificationTypeEnterpriseCreatorRole,
				                                                    new Passwords().integerEncrypt(adminUserName.getBytes()))
		                                                    .get();
		
		IInvolvedParty<?> administratorUser;
		if (exists.isEmpty())
		{
			IInvolvedParty<?> adminUser = service.create(system, pair, true);
			
			adminUser.addOrReuseIdentificationType(IdentificationTypes.IdentificationTypeUserName, NoClassification.classificationName(),
					new Passwords().integerEncrypt(adminUserName.getBytes()), system, token);
			
			adminUser.addOrReuseType(IPTypes.TypeIndividual, NoClassification.classificationName(), "Creator Individual", system, token);
			adminUser.addOrReuseNameType(PreferredNameType, NoClassification.name(), "Enterprise Creator", system, token);
			adminUser.addOrReuseNameType(CommonNameType, NoClassification.name(), "Enterprise Creator", system, token);
			adminUser.addOrReuseNameType(FullNameType, NoClassification.name(), "Enterprise Creator", system, token);
			adminUser.addOrReuseNameType(FirstNameType, NoClassification.name(), "Administrator", system, token);
			
			SecurityToken myToken = (SecurityToken) get(SecurityTokenService.class).create(SecurityTokenClassifications.Identity,
					adminUserName,
					"The creator of the enterprise", system, administratorsGroup, token);
			
			adminUser.addOrReuseIdentificationType(IdentificationTypes.IdentificationTypeEnterpriseCreatorRole, NoClassification.classificationName(),
					new Passwords().integerEncrypt(adminUserName.getBytes()), system,
					token);
			if (existingLocalKey != null)
			{
				adminUser.addOrReuseIdentificationType("IdentificationTypeWebClientUUID", NoClassification.classificationName(),
						existingLocalKey.toString(), system,
						token);
			}
			
			service.addUpdateUsernamePassword(null, adminUserName, adminPassword, adminUser, system, token);
			adminUser.createDefaultSecurity(system, token);
			administratorUser = adminUser;
		}
		else
		{
			administratorUser = exists.get();
		}
		get(ActivityMasterConfiguration.class)
				.setSecurityEnabled(true);
		return administratorUser;
	}
}
