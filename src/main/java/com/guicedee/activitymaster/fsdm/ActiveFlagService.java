package com.guicedee.activitymaster.fsdm;

/**
 * Reactivity Migration Checklist:
 * 
 * [✓] One action per Mutiny.Session at a time
 *     - All operations on a session are sequential
 *     - No parallel operations on the same session
 * 
 * [✓] Pass Mutiny.Session through the chain
 *     - All methods accept session as parameter
 *     - Session is passed to all dependent operations
 * 
 * [✓] No await() usage
 *     - Using reactive chains instead of blocking operations
 * 
 * [✓] Synchronous execution of reactive chains
 *     - All reactive chains execute synchronously
 *     - No fire-and-forget operations with subscribe().with()
 * 
 * [✓] No parallel operations on a session
 *     - Not using Uni.combine().all().unis() with operations that share the same session
 * 
 * [✓] No session/transaction creation in libraries
 *     - Sessions are passed in from the caller
 *     - No sessionFactory.withTransaction() in methods
 * 
 * See ReactivityMigrationGuide.md for more details on these rules.
 */

import com.google.inject.Singleton;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Singleton
public class ActiveFlagService
		implements IActiveFlagService<ActiveFlagService>
{
	// Local cache: key = enterpriseId + '|' + flagName, value = ActiveFlag UUID
	private final Map<String, UUID> flagKeyToId = new ConcurrentHashMap<>();

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
		UUID enterpriseId = null;
		if (enterprise instanceof Enterprise ent) {
			enterpriseId = ent.getId();
		}
		String key = enterpriseId + "|" + flag;
		UUID cachedId = flagKeyToId.get(key);
		if (cachedId != null) {
			log.trace("🔁 ActiveFlag cache hit for key '{}': {} — loading by UUID", key, cachedId);
			return (Uni) getFlagById(session, cachedId)
				.flatMap(found -> {
					if (found != null) {
						return Uni.createFrom().item(found);
					}
					// Stale mapping: remove and fallback to name+enterprise query
					flagKeyToId.remove(key);
					return (Uni) new ActiveFlag().builder(session)
							.withName(flag)
							.inDateRange()
							//    .canRead(enterprise, identifyingToken)
							.withEnterprise(enterprise)
							.get()
							.invoke(ent -> {
								if (ent != null && ent.getId() != null) {
									flagKeyToId.put(key, ent.getId());
								}
							});
				});
		}

		// Cold path: query by name+enterprise and remember UUID
		return (Uni) new ActiveFlag().builder(session)
						.withName(flag)
						.inDateRange()
						//    .canRead(enterprise, identifyingToken)
						.withEnterprise(enterprise)
						.get()
						.invoke(ent -> {
							if (ent != null && ent.getId() != null) {
								flagKeyToId.put(key, ent.getId());
							}
						});
	}

	// UUID-based lookup to leverage L2 cache (@Cacheable on entity + L2 cache enabled)
	public Uni<IActiveFlag<?,?>> getFlagById(Mutiny.Session session, UUID id) {
		//noinspection unchecked
		return (Uni) session.find(ActiveFlag.class, id);
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
