package com.guicedee.activitymaster.fsdm;

import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActiveFlagException;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;


import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Log4j2
public class ActiveFlagService
		implements IActiveFlagService<ActiveFlagService>
{

	@Override
	public IActiveFlag<?,?> get()
	{
		return new ActiveFlag();
	}

 //@Transactional()
	public Uni<IActiveFlag<?,?>> create(Mutiny.Session session, IEnterprise<?,?> enterprise, String name, String description, UUID... identifyingToken)
	{
		return findFlagByName(session, name, enterprise, identifyingToken)
			.onFailure(NoResultException.class).recoverWithUni(() -> {
				ActiveFlag af = new ActiveFlag();
				af.setName(name);
				af.setDescription(description);
				af.setAllowAccess(true);
				af.setEnterpriseID((Enterprise) enterprise);
				return af.builder(session)
					.persist(af).replaceWith(Uni.createFrom().item(af));
			});
	}


	//@Transactional()
	@Override
	public Uni<IActiveFlag<?,?>> findFlagByName(Mutiny.Session session, com.entityassist.enumerations.ActiveFlag flag, IEnterprise<?,?> enterprise, UUID... identifyingToken)
	{
		return findFlagByName(session, flag.name(), enterprise, identifyingToken);
	}

	//@Transactional()
	//@CacheResult(cacheName = "FindActiveByName")
	@Override
	public Uni<IActiveFlag<?,?>> findFlagByName(Mutiny.Session session, String flag, IEnterprise<?,?> enterprise, UUID... identifyingToken)
	{
		return (Uni) new ActiveFlag().builder(session)
		                       .withName(flag)
		                       .inDateRange()
		                       //    .canRead(enterprise, identifyingToken)
		                       .withEnterprise(enterprise)
		                       .get();
	}

	//@Transactional()
	@Override
	//@CacheResult(cacheName = "FindActiveFlagRange")
	public Uni<List<IActiveFlag<?,?>>> findActiveRange(Mutiny.Session session, IEnterprise<?,?> enterprise, UUID... identifyingToken)
	{
		return (Uni) find(session, getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getActiveRangeAndUp()), enterprise, identifyingToken);
	}

	//@Transactional()
	Uni<List<ActiveFlag>> find(Mutiny.Session session, String[] name, IEnterprise<?,?> enterprise, UUID... identifyingToken)
	{
		return new ActiveFlag().builder(session)
		                       .withName(name)
		                       .inDateRange()
		                       //     .canRead(enterprise, true, identifyingToken)
		                       .withEnterprise(enterprise)
		                       .getAll();
	}

	private String[] getNamesForFlags(Set<com.entityassist.enumerations.ActiveFlag> flags)
	{
		return com.entityassist.enumerations.ActiveFlag.activeFlagToStrings(flags)
		                                               .toArray(new String[]{});
	}

	@Override
	//@CacheResult(cacheName = "GetVisibleRange")
	public Uni<List<IActiveFlag<?,?>>> getVisibleRange(Mutiny.Session session, IEnterprise<?,?> enterprise, UUID... identifyingToken)
	{
		return (Uni) find(session, getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getVisibleRangeAndUp()), enterprise, identifyingToken);
	}

	@Override
	//@CacheResult
	public Uni<List<IActiveFlag<?,?>>> getRemovedRange(Mutiny.Session session, IEnterprise<?,?> enterprise, UUID... identifyingToken)
	{
		return (Uni) find(session, getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getRemovedRange()), enterprise, identifyingToken);
	}

	@Override
	//@CacheResult(cacheName = "GetArchivedRange")
	public Uni<List<IActiveFlag<?,?>>> getArchiveRange(Mutiny.Session session, IEnterprise<?,?> enterprise, UUID... identifyingToken)
	{
		return (Uni) find(session, getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getArchivedRange()), enterprise, identifyingToken);
	}

	@Override
	//@CacheResult(cacheName = "GetHighlightedRange")
	public Uni<List<IActiveFlag<?,?>>> getHighlightedRange(Mutiny.Session session, IEnterprise<?,?> enterprise, UUID... identifyingToken)
	{
		return (Uni)find(session, getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getHighlightedRange()), enterprise, identifyingToken);
	}

	@Override
	//@CacheResult(cacheName = "GetActiveFlag")
	public Uni<IActiveFlag<?,?>> getActiveFlag(Mutiny.Session session, IEnterprise<?,?> enterprise, UUID... identifyingToken)
	{
		return findFlagByName(session, com.entityassist.enumerations.ActiveFlag.Active, enterprise, identifyingToken);
	}

	@Override
	//@CacheResult(cacheName = "GetArchivedFlag")
	public Uni<IActiveFlag<?,?>> getArchivedFlag(Mutiny.Session session, IEnterprise<?,?> enterprise, UUID... identifyingToken)
	{
		return findFlagByName(session, com.entityassist.enumerations.ActiveFlag.Archived, enterprise, identifyingToken);
	}

	@Override
	//@CacheResult(cacheName = "GetDeletedFlag")
	public Uni<IActiveFlag<?,?>> getDeletedFlag(Mutiny.Session session, IEnterprise<?,?> enterprise, UUID... identifyingToken)
	{
		return findFlagByName(session, com.entityassist.enumerations.ActiveFlag.Deleted, enterprise, identifyingToken);
	}
}
