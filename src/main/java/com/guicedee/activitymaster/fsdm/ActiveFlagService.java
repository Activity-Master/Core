package com.guicedee.activitymaster.fsdm;

import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActiveFlagException;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.*;

public class ActiveFlagService
		implements IActiveFlagService<ActiveFlagService>
{
	@Override
	public IActiveFlag<?,?> get()
	{
		return new ActiveFlag();
	}
	
	@Transactional()
	public IActiveFlag<?,?> create(IEnterprise<?,?> enterprise, String name, String description, java.util.UUID... identifyingToken)
	{
		ActiveFlag af = new ActiveFlag();
		if (!doesFlagExist(name, enterprise, identifyingToken))
		{
			af.setName(name);
			af.setDescription(description);
			af.setAllowAccess(true);
			af.setEnterpriseID((Enterprise) enterprise);
			af.builder()
			  .persist(af);
			return af;
		}
		else
		{
			return findFlagByName(name, enterprise, identifyingToken);
		}
	}
	
	@Transactional()
	boolean doesFlagExist(String flagName, IEnterprise<?,?> enterprise, java.util.UUID... identifyingToken)
	{
		return new ActiveFlag().builder()
		                       .withName(flagName)
		                       .inDateRange()
		                       //    .canRead(enterprise, identifyingToken)
		                       .withEnterprise(enterprise)
		                       .getCount() > 0;
	}
	
	@Transactional()
	@Override
	public IActiveFlag<?,?> findFlagByName(com.entityassist.enumerations.ActiveFlag flag, IEnterprise<?,?> enterprise, java.util.UUID... identifyingToken)
	{
		return findFlagByName(flag.name(), enterprise, identifyingToken);
	}
	
	@Transactional()
	@CacheResult(cacheName = "FindActiveByName")
	@Override
	public IActiveFlag<?,?> findFlagByName(@CacheKey String flag, @CacheKey IEnterprise<?,?> enterprise, @CacheKey java.util.UUID... identifyingToken)
	{
		return new ActiveFlag().builder()
		                       .withName(flag)
		                       .inDateRange()
		                       //    .canRead(enterprise, identifyingToken)
		                       .withEnterprise(enterprise)
		                       .get()
		                       .orElseThrow(() -> new ActiveFlagException("Unable to find a flag by name of - " + flag + " - in enterprise - " + enterprise));
	}
	@Transactional()
	@Override
	@CacheResult(cacheName = "FindActiveFlagRange")
	public List<IActiveFlag<?,?>> findActiveRange(@CacheKey IEnterprise<?,?> enterprise, @CacheKey java.util.UUID... identifyingToken)
	{
		return (List) find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getActiveRangeAndUp()), enterprise, identifyingToken);
	}
	
	@Transactional()
	List<ActiveFlag> find(String[] name, IEnterprise<?,?> enterprise, java.util.UUID... identifyingToken)
	{
		ActiveFlag search = new ActiveFlag();
		List<ActiveFlag> l = search.builder()
		                           .withName(name)
		                           .inDateRange()
		                           //     .canRead(enterprise, true, identifyingToken)
		                           .withEnterprise(enterprise)
		                           .getAll();
		if (l.isEmpty())
		{
			throw new ActiveFlagException("No flags were found for the named list - " + Arrays.toString(name));
		}
		return l;
	}
	
	private String[] getNamesForFlags(Set<com.entityassist.enumerations.ActiveFlag> flags)
	{
		return com.entityassist.enumerations.ActiveFlag.activeFlagToStrings(flags)
		                                               .toArray(new String[]{});
	}
	
	@Override
	@CacheResult(cacheName = "GetVisibleRange")
	public List<IActiveFlag<?,?>> getVisibleRange(@CacheKey IEnterprise<?,?> enterprise, @CacheKey java.util.UUID... identifyingToken)
	{
		return (List) find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getVisibleRangeAndUp()), enterprise, identifyingToken);
	}
	
	@Override
	@CacheResult
	public List<IActiveFlag<?,?>> getRemovedRange(@CacheKey IEnterprise<?,?> enterprise, @CacheKey java.util.UUID... identifyingToken)
	{
		return (List) find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getRemovedRange()), enterprise, identifyingToken);
	}
	
	@Override
	@CacheResult(cacheName = "GetArchivedRange")
	public List<IActiveFlag<?,?>> getArchiveRange(@CacheKey IEnterprise<?,?> enterprise, @CacheKey java.util.UUID... identifyingToken)
	{
		return (List) find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getArchivedRange()), enterprise, identifyingToken);
	}
	
	@Override
	@CacheResult(cacheName = "GetHighlightedRange")
	public List<IActiveFlag<?,?>> getHighlightedRange(@CacheKey IEnterprise<?,?> enterprise, @CacheKey java.util.UUID... identifyingToken)
	{
		return (List) find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getHighlightedRange()), enterprise, identifyingToken);
	}
	
	@Override
	@CacheResult(cacheName = "GetActiveFlag")
	public IActiveFlag<?,?> getActiveFlag(@CacheKey IEnterprise<?,?> enterprise, @CacheKey java.util.UUID... identifyingToken)
	{
		return findFlagByName(com.entityassist.enumerations.ActiveFlag.Active, enterprise, identifyingToken);
	}
	
	@Override
	@CacheResult(cacheName = "GetArchivedFlag")
	public IActiveFlag<?,?> getArchivedFlag(@CacheKey IEnterprise<?,?> enterprise, @CacheKey java.util.UUID... identifyingToken)
	{
		return findFlagByName(com.entityassist.enumerations.ActiveFlag.Archived, enterprise, identifyingToken);
	}
	
	@Override
	@CacheResult(cacheName = "GetDeletedFlag")
	public IActiveFlag<?,?> getDeletedFlag(@CacheKey IEnterprise<?,?> enterprise, @CacheKey java.util.UUID... identifyingToken)
	{
		return findFlagByName(com.entityassist.enumerations.ActiveFlag.Deleted, enterprise, identifyingToken);
	}
}
