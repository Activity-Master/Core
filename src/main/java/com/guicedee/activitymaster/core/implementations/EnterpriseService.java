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
import com.google.inject.Singleton;
import com.guicedee.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications.*;
import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

@Singleton
public class EnterpriseService
		implements IProgressable, IEnterpriseService
{

	private static final Logger log = Logger.getLogger(EnterpriseService.class.getName());

	public Enterprise create(@NotNull String name, @NotNull String description, IActivityMasterProgressMonitor progressMonitor)
	{
		Enterprise enterprise = new Enterprise();
		Optional<Enterprise> exists = enterprise.builder()
		                                        .findByName(name)
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
		List<Long> classy = new EnterpriseXClassification().builder()
		                                                   .withClassification(classification)
		                                                   .inActiveRange(classification.getEnterpriseID())
		                                                   .inDateRange()
		                                                   .selectColumn(EnterpriseXClassification_.enterpriseID)
		                                                   .getAll(Long.class);

		EnterpriseQueryBuilder builder = new Enterprise().builder();
		builder = builder.where(Enterprise_.id, InList, classy);
		return new ArrayList<>(builder.getAll());
	}

	public Optional<IEnterprise<?>> findEnterprise(IEnterpriseName<?> name)
	{
		return (Optional) new Enterprise().builder()
		                       .findByName(name.classificationName())
		                       .inDateRange()
		                       .get();
	}

	/**
	 * Gets an enterprise or throws an exception.
	 * <p>
	 * Result is cached
	 *
	 * @param name
	 * 		the name of the enterprise
	 *
	 * @return The enterprise
	 */
	@Override
	@CacheResult(cacheName = "GetEnterpriseByEnterpriseName")
	public IEnterprise<?> getEnterprise(@CacheKey IEnterpriseName<?> name)
	{
		return new Enterprise().builder()
		                       .findByName(name.classificationName())
		                       .inDateRange()
		                       .get()
		                       .orElseThrow();
	}

	public IEnterprise<?> checkRequiresUpdate(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor)
	{
		Optional<IEnterprise<?>> exists = findEnterprise(enterpriseName);
		if (exists.isEmpty())
		{
			log.log(Level.INFO, "Enterprise with name [" + enterpriseName + "] does not exist. Create a new Enterprise.");
			throw new ActivityMasterException("No Enterprise");
		}
		Enterprise enterprise = (Enterprise) exists.get();

		try
		{
			ISystems<?> activityMasterSystem = get(SystemsService.class)
					                                   .getActivityMaster(enterprise);
			UUID identityToken = get(SystemsService.class).getSecurityIdentityToken(activityMasterSystem);

			if (enterprise.has(Version, activityMasterSystem))
			{
				if (ActivityMasterConfiguration.version > enterprise.find(Version, activityMasterSystem)
				                                                    .get()
				                                                    .getValueAsDouble())
				{
					log.config("New Version Released, Running System Updates");
					GuiceContext.get(ActivityMasterService.class)
					            .runUpdatesOnEnterprise(enterpriseName, progressMonitor);
					ActivityMasterConfiguration.requiresUpdate = true;
				}
			}
		}
		catch (Throwable T)
		{
			log.config("Enterprise has not registered a version, running full update");
			GuiceContext.get(ActivityMasterService.class)
			            .runUpdatesOnEnterprise(enterpriseName, progressMonitor);
		}
		return enterprise;
	}
}
