package com.guicedee.activitymaster.core.implementations;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.events.EventInvolvedPartiesClassifications.*;


@SuppressWarnings("unchecked")
public class ActiveFlagService
		implements IActiveFlagService
{
	public IActiveFlag<?> create(IEnterprise<?> enterprise, String name, String description, UUID... identifyingToken)
	{
		ActiveFlag af = new ActiveFlag();
		Optional<ActiveFlag> exists = findFlagByName(name, enterprise, identifyingToken);
		if (exists.isEmpty())
		{
			af.setName(name);
			af.setDescription(description);
			af.setAllowAccess(true);
			af.setEnterpriseID((Enterprise) enterprise);
			af.builder()
			  .persist(af);
			
			if(!ActivityMasterConfiguration.getCreatingNew().get())
			{
				ISystems<?> activityMaster = GuiceContext.get(ISystemsService.class)
				                                         .getActivityMaster(af.getEnterpriseID(), identifyingToken);
				
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{
					af.createDefaultSecurity(activityMaster, identifyingToken);
				}
				
				if (EventThread.event.get() != null)
				{
					EventThread.event.get()
					                 .add((IEventClassification<?>) Created, "Active Flag - " + name, activityMaster, identifyingToken);
				}
			}
			return af;
		}
		else
		{
			return findFlagByName(name, enterprise, identifyingToken)
					.orElseThrow();
		}
	}
	
	@CacheResult(cacheName = "FindActiveByName")
	private Optional<ActiveFlag> findFlagByName(@CacheKey String flag, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return new ActiveFlag().builder()
		                       .withName(flag)
		                       .inDateRange()
		                       .canRead(enterprise, identifyingToken)
		                       .withEnterprise(enterprise)
		                       .get();
	}
	
	@Override
	@CacheResult(cacheName = "FindActiveFlagRange")
	public List<IActiveFlag<?>> findActiveRange(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return (List) find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getActiveRangeAndUp()), enterprise, identifyingToken);
	}
	
	private List<ActiveFlag> find(String[] name, IEnterprise<?> enterprise, UUID... identifyingToken)
	{
		ActiveFlag search = new ActiveFlag();
		return search.builder()
		             .withName(name)
		             .inDateRange()
		             .canRead(enterprise, true, identifyingToken)
		             .withEnterprise(enterprise)
		             .getAll();
	}
	
	private String[] getNamesForFlags(Set<com.entityassist.enumerations.ActiveFlag> flags)
	{
		return com.entityassist.enumerations.ActiveFlag.activeFlagToStrings(flags)
		                                               .toArray(new String[]{});
	}
	
	@Override
	@CacheResult(cacheName = "GetVisibleRange")
	public List<IActiveFlag<?>> getVisibleRange(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return (List) find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getVisibleRangeAndUp()), enterprise, identifyingToken);
	}
	
	@Override
	@CacheResult
	public List<IActiveFlag<?>> getRemovedRange(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return (List) find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getRemovedRange()), enterprise, identifyingToken);
	}
	
	@Override
	@CacheResult(cacheName = "GetArchivedRange")
	public List<IActiveFlag<?>> getArchiveRange(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return (List) find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getArchivedRange()), enterprise, identifyingToken);
	}
	
	@Override
	@CacheResult(cacheName = "GetHighlightedRange")
	public List<IActiveFlag<?>> getHighlightedRange(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return (List) find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getHighlightedRange()), enterprise, identifyingToken);
	}
	
	@Override
	@CacheResult(cacheName = "GetActiveFlag")
	public IActiveFlag<?> getActiveFlag(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		Optional<IActiveFlag<?>> flag = findFlagByName(com.entityassist.enumerations.ActiveFlag.Active, enterprise, identifyingToken);
		return flag.orElse(null);
	}
	
	@Override
	@CacheResult(cacheName = "GetArchivedFlag")
	public IActiveFlag<?> getArchivedFlag(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		Optional<IActiveFlag<?>> flag = findFlagByName(com.entityassist.enumerations.ActiveFlag.Archived, enterprise, identifyingToken);
		return flag.orElse(null);
	}
	
	@Override
	@CacheResult(cacheName = "GetDeletedFlag")
	public IActiveFlag<?> getDeletedFlag(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		Optional<IActiveFlag<?>> flag = findFlagByName(com.entityassist.enumerations.ActiveFlag.Deleted, enterprise, identifyingToken);
		return flag.orElse(null);
	}
	
	@Override
	public Optional<IActiveFlag<?>> findFlagByName(@CacheKey com.entityassist.enumerations.ActiveFlag flag, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		Optional<ActiveFlag> fl = findFlagByName(flag.name(), enterprise, identifyingToken);
		return Optional.ofNullable(fl.orElse(null));
	}
}
