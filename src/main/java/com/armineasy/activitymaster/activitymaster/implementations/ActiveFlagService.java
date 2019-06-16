package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.IActiveFlag;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.*;

@Singleton
public class ActiveFlagService
		implements IActiveFlagService
{

	public IActiveFlag create(IEnterprise<?> enterprise, String name, String description, @CacheKey UUID... identifyingToken)
	{
		ActiveFlag af = new ActiveFlag();
		Optional<ActiveFlag> exists = ActivityMasterConfiguration
				                              .get()
				                              .isDoubleCheckDisabled() ? Optional.empty() :
		                              findFlagByName(name, enterprise, identifyingToken);
		if (exists.isEmpty())
		{
			af.setName(name);
			af.setDescription(description);
			af.setAllowAccess(true);
			af.setEnterpriseID((Enterprise) enterprise);
			af.builder()
			  .persist(af);
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				af.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
				                                     .getActivityMaster(af.getEnterpriseID(), identifyingToken), identifyingToken);
			}
		}
		else
		{
			af = exists.get();
		}
		return af;
	}

	private Optional<ActiveFlag> findFlagByName(String flag, IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		Optional<ActiveFlag> op = new ActiveFlag().builder()
		                                          .findByName(flag)
		                                          .inDateRange()
		                                          .canRead(enterprise, identifyingToken)
		                                          .withEnterprise(enterprise)
		                                          .get();
		if (op.isEmpty())
		{
			System.out.println("Empty Flag!");
		}
		return op;
	}

	@Override
	@CacheResult(cacheName = "FindActiveFlagRange")
	public List<ActiveFlag> findActiveRange(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return (List<ActiveFlag>) find(getNamesForFlags(com.jwebmp.entityassist.enumerations.ActiveFlag.getActiveRangeAndUp()), enterprise, identifyingToken);
	}

	@CacheResult(cacheName = "findActiveFlags")
	private Collection<ActiveFlag> find(@CacheKey String[] name, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		ActiveFlag search = new ActiveFlag();


		List<ActiveFlag> ac = search.builder()
		                            .findByName(name)
		                            .inDateRange()
		                            // .canRead(enterprise,true, identifyingToken)
		                            .withEnterprise(enterprise)
		                            .getAll();
		if (ac.isEmpty())
		{
			System.out.println("Empty List!");
		}
		return ac;
	}

	private String[] getNamesForFlags(Set<com.jwebmp.entityassist.enumerations.ActiveFlag> flags)
	{
		return com.jwebmp.entityassist.enumerations.ActiveFlag.activeFlagToStrings(flags)
		                                                      .toArray(new String[]{});
	}

	@Override
	@CacheResult(cacheName = "GetVisibleRange")
	public List<ActiveFlag> getVisibleRange(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return (List<ActiveFlag>) find(getNamesForFlags(com.jwebmp.entityassist.enumerations.ActiveFlag.getVisibleRangeAndUp()), enterprise, identifyingToken);
	}

	@Override
	@CacheResult
	public List<ActiveFlag> getRemovedRange(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return (List<ActiveFlag>) find(getNamesForFlags(com.jwebmp.entityassist.enumerations.ActiveFlag.getRemovedRange()), enterprise, identifyingToken);
	}

	@Override
	@CacheResult(cacheName = "GetArchivedRange")
	public List<ActiveFlag> getArchiveRange(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return (List<ActiveFlag>) find(getNamesForFlags(com.jwebmp.entityassist.enumerations.ActiveFlag.getArchivedRange()), enterprise, identifyingToken);
	}

	@Override
	@CacheResult(cacheName = "GetHighlightedRange")
	public List<ActiveFlag> getHighlightedRange(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return (List<ActiveFlag>) find(getNamesForFlags(com.jwebmp.entityassist.enumerations.ActiveFlag.getHighlightedRange()), enterprise, identifyingToken);
	}

	@Override
	@CacheResult(cacheName = "GetActiveFlag")
	public ActiveFlag getActiveFlag(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		Optional<ActiveFlag> flag = findFlagByName(com.jwebmp.entityassist.enumerations.ActiveFlag.Active, enterprise, identifyingToken);
		return flag.orElse(null);
	}

	@Override
	@CacheResult(cacheName = "GetArchivedFlag")
	public ActiveFlag getArchivedFlag(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		Optional<ActiveFlag> flag = findFlagByName(com.jwebmp.entityassist.enumerations.ActiveFlag.Archived, enterprise, identifyingToken);
		return flag.orElse(null);
	}

	@Override
	@CacheResult(cacheName = "GetDeletedFlag")
	public ActiveFlag getDeletedFlag(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		Optional<ActiveFlag> flag = findFlagByName(com.jwebmp.entityassist.enumerations.ActiveFlag.Deleted, enterprise, identifyingToken);
		return flag.orElse(null);
	}

	@Override
	public Optional<ActiveFlag> findFlagByName(@CacheKey com.jwebmp.entityassist.enumerations.ActiveFlag flag, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identifyingToken)
	{
		return findFlagByName(flag.name(), enterprise, identifyingToken);
	}
}
