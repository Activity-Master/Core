package com.guicedee.activitymaster.fsdm;

import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActiveFlagException;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;


import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
	public Uni<IActiveFlag<?,?>> create(IEnterprise<?,?> enterprise, String name, String description, java.util.UUID... identifyingToken)
	{
		return Uni.createFrom().emitter(emitter -> {
			try {
				doesFlagExist(name, enterprise, identifyingToken)
					.subscribe().with(
						exists -> {
							try {
								if (!exists) {
									ActiveFlag af = new ActiveFlag();
									af.setName(name);
									af.setDescription(description);
									af.setAllowAccess(true);
									af.setEnterpriseID((Enterprise) enterprise);
									af.builder()
									  .persist(af)
									  .subscribe().with(
										persisted -> {
											emitter.complete(af);
										},
										emitter::fail
									  );
								} else {
									findFlagByName(name, enterprise, identifyingToken)
										.subscribe().with(
											emitter::complete,
											emitter::fail
										);
								}
							} catch (Exception e) {
								emitter.fail(e);
							}
						},
						emitter::fail
					);
			} catch (Exception e) {
				emitter.fail(e);
			}
		});
	}

	//@Transactional()
	Uni<Boolean> doesFlagExist(String flagName, IEnterprise<?,?> enterprise, java.util.UUID... identifyingToken)
	{
		return new ActiveFlag().builder()
		                       .withName(flagName)
		                       .inDateRange()
		                       //    .canRead(enterprise, identifyingToken)
		                       .withEnterprise(enterprise)
		                       .getCount()
		                       .onFailure().invoke(error -> log.error("Error checking if flag exists: {}", error.getMessage(), error))
		                       .map(count -> count > 0);
	}

	//@Transactional()
	@Override
	public Uni<IActiveFlag<?,?>> findFlagByName(com.entityassist.enumerations.ActiveFlag flag, IEnterprise<?,?> enterprise, java.util.UUID... identifyingToken)
	{
		return findFlagByName(flag.name(), enterprise, identifyingToken);
	}

	//@Transactional()
	//@CacheResult(cacheName = "FindActiveByName")
	@Override
	public Uni<IActiveFlag<?,?>> findFlagByName( String flag,  IEnterprise<?,?> enterprise,  java.util.UUID... identifyingToken)
	{
		return new ActiveFlag().builder()
		                       .withName(flag)
		                       .inDateRange()
		                       //    .canRead(enterprise, identifyingToken)
		                       .withEnterprise(enterprise)
		                       .get()
		                       .onItem().ifNull().failWith(() -> 
		                           new ActiveFlagException("Unable to find a flag by name of - " + flag + " - in enterprise - " + enterprise)
		                       )
		                       .map(activeFlag -> (IActiveFlag<?,?>) activeFlag);
	}

	//@Transactional()
	@Override
	//@CacheResult(cacheName = "FindActiveFlagRange")
	public Uni<List<IActiveFlag<?,?>>> findActiveRange( IEnterprise<?,?> enterprise,  java.util.UUID... identifyingToken)
	{
		return find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getActiveRangeAndUp()), enterprise, identifyingToken)
		       .map(list -> (List<IActiveFlag<?,?>>) (List<?>) list);
	}

	//@Transactional()
	Uni<List<ActiveFlag>> find(String[] name, IEnterprise<?,?> enterprise, java.util.UUID... identifyingToken)
	{
		return new ActiveFlag().builder()
		                       .withName(name)
		                       .inDateRange()
		                       //     .canRead(enterprise, true, identifyingToken)
		                       .withEnterprise(enterprise)
		                       .getAll()
		                       .onItem().invoke(list -> {
		                           if (list.isEmpty()) {
		                               throw new ActiveFlagException("No flags were found for the named list - " + Arrays.toString(name));
		                           }
		                       });
	}

	private String[] getNamesForFlags(Set<com.entityassist.enumerations.ActiveFlag> flags)
	{
		return com.entityassist.enumerations.ActiveFlag.activeFlagToStrings(flags)
		                                               .toArray(new String[]{});
	}

	@Override
	//@CacheResult(cacheName = "GetVisibleRange")
	public Uni<List<IActiveFlag<?,?>>> getVisibleRange( IEnterprise<?,?> enterprise,  java.util.UUID... identifyingToken)
	{
		return find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getVisibleRangeAndUp()), enterprise, identifyingToken)
		       .map(list -> (List<IActiveFlag<?,?>>) (List<?>) list);
	}

	@Override
	//@CacheResult
	public Uni<List<IActiveFlag<?,?>>> getRemovedRange( IEnterprise<?,?> enterprise,  java.util.UUID... identifyingToken)
	{
		return find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getRemovedRange()), enterprise, identifyingToken)
		       .map(list -> (List<IActiveFlag<?,?>>) (List<?>) list);
	}

	@Override
	//@CacheResult(cacheName = "GetArchivedRange")
	public Uni<List<IActiveFlag<?,?>>> getArchiveRange( IEnterprise<?,?> enterprise,  java.util.UUID... identifyingToken)
	{
		return find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getArchivedRange()), enterprise, identifyingToken)
		       .map(list -> (List<IActiveFlag<?,?>>) (List<?>) list);
	}

	@Override
	//@CacheResult(cacheName = "GetHighlightedRange")
	public Uni<List<IActiveFlag<?,?>>> getHighlightedRange( IEnterprise<?,?> enterprise,  java.util.UUID... identifyingToken)
	{
		return find(getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getHighlightedRange()), enterprise, identifyingToken)
		       .map(list -> (List<IActiveFlag<?,?>>) (List<?>) list);
	}

	@Override
	//@CacheResult(cacheName = "GetActiveFlag")
	public Uni<IActiveFlag<?,?>> getActiveFlag( IEnterprise<?,?> enterprise,  java.util.UUID... identifyingToken)
	{
		return findFlagByName(com.entityassist.enumerations.ActiveFlag.Active, enterprise, identifyingToken);
	}

	@Override
	//@CacheResult(cacheName = "GetArchivedFlag")
	public Uni<IActiveFlag<?,?>> getArchivedFlag( IEnterprise<?,?> enterprise,  java.util.UUID... identifyingToken)
	{
		return findFlagByName(com.entityassist.enumerations.ActiveFlag.Archived, enterprise, identifyingToken);
	}

	@Override
	//@CacheResult(cacheName = "GetDeletedFlag")
	public Uni<IActiveFlag<?,?>> getDeletedFlag( IEnterprise<?,?> enterprise,  java.util.UUID... identifyingToken)
	{
		return findFlagByName(com.entityassist.enumerations.ActiveFlag.Deleted, enterprise, identifyingToken);
	}
}
