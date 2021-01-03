package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassification;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassification_;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise_;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IProgressable;
import com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IRelationshipValue;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.guicedee.activitymaster.core.systems.EnterpriseSystem;
import com.guicedee.activitymaster.core.updates.DatedUpdate;
import com.guicedee.activitymaster.core.updates.ISystemUpdate;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.json.LocalDateDeserializer;
import com.guicedee.guicedinjection.json.LocalDateSerializer;
import io.github.classgraph.ClassInfo;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;


public class EnterpriseService
		implements IProgressable,
		           IEnterpriseService
{
	
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
		EnterpriseSystem es = get(EnterpriseSystem.class);
		ISystems<?> newSystem = es.getSystem(enterprise);
		UUID securityToken = es.getSystemToken(enterprise);
		
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
			o.update(enterprise, progressMonitor);
			enterprise.add(UpdateClass, o.getClass().getCanonicalName(), newSystem, securityToken);
		});
		enterprise.addOrUpdate(EnterpriseClassifications.LastUpdateDate, DateTimeFormatter.ofPattern("yyyy/MM/dd")
		                                                                                  .format(LocalDate.now()), newSystem, securityToken);
		logProgress("Update System", "Finished Updates. Last Update Date - " + new LocalDateSerializer().convert(LocalDate.now()), progressMonitor);
	}
	
	@Override
	public Set<String> getEnterpriseAppliedUpdates(IEnterprise<?> enterprise)
	{
		Set<String> set = new LinkedHashSet<>();
		EnterpriseSystem es = get(EnterpriseSystem.class);
		ISystems<?> newSystem = es.getSystem(enterprise);
		UUID identityToken = es.getSystemToken(enterprise);
		List<IRelationshipValue<IEnterprise<?>, IClassification<?>, ?>> classificationsAll = enterprise.findClassificationsAll(UpdateClass, newSystem, identityToken);
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
		                       .orElseThrow();
	}
	
	
	/**
	 * Gets an enterprise or throws an exception.
	 * <p>
	 * Result is cached
	 *
	 * @param name the name of the enterprise
	 *
	 * @return The enterprise
	 */
	@Override
	@CacheResult(cacheName = "GetEnterpriseByEnterpriseName")
	public IEnterprise<?> getEnterprise(@CacheKey IEnterpriseName<?> name)
	{
		return getEnterprise(name.classificationName());
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	@CacheResult
	public Set<IEnterpriseName<?>> getIEnterprises()
	{
		Set<IEnterpriseName<?>> output = new HashSet<>();
		for (ClassInfo classInfo : GuiceContext.instance()
		                                       .getScanResult()
		                                       .getClassesImplementing(IEnterpriseName.class.getCanonicalName()))
		{
			Class<IEnterpriseName> o = (Class<IEnterpriseName>) classInfo.loadClass();
			for (Object enumConstant : o.getEnumConstants())
			{
				IEnterpriseName<?> e = (IEnterpriseName<?>) enumConstant;
				output.add(e);
			}
		}
		return output;
	}
	
	@CacheResult
	@Override
	public IEnterpriseName<?> getIEnterprise(@CacheKey IEnterprise<?> enterprise)
	{
		for (IEnterpriseName<?> e : getIEnterprises())
		{
			if (enterprise.getName()
			              .equals(e.name()))
			{
				return e;
			}
		}
		return null;
	}
	
	@CacheResult
	@Override
	public IEnterprise<?> getIEnterpriseFromName(@CacheKey IEnterpriseName<?> enterprise)
	{
		return new Enterprise().builder()
		                       .withName(enterprise.classificationName())
		                       .get()
		                       .get();
	}
}
