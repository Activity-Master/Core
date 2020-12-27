package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.ActivityMasterService;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassification;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassification_;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise_;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IProgressable;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.json.LocalDateDeserializer;
import io.github.classgraph.ClassInfo;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
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
		return new Enterprise().builder()
		                       .withName(name.classificationName())
		                       .inDateRange()
		                       .get()
		                       .orElseThrow();
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
