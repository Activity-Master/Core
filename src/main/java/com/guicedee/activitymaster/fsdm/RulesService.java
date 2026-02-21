package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRulesType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;


import java.util.List;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.applicationEnterpriseName;

@Log4j2
@Singleton
public class RulesService
		implements IRulesService<RulesService>
{
	private final java.util.Map<String, java.util.UUID> rulesTypeKeyToId = new java.util.concurrent.ConcurrentHashMap<>();

	// UUID-based lookup to leverage Hibernate 2nd-level cache
	public io.smallrye.mutiny.Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRulesType<?, ?>> getRulesTypeById(org.hibernate.reactive.mutiny.Mutiny.Session session, java.util.UUID id) {
		return (io.smallrye.mutiny.Uni) session.find(com.guicedee.activitymaster.fsdm.db.entities.rules.RulesType.class, id);
	}
	@Inject
	private IClassificationService<?> classificationService;

	public IRules<?, ?> get()
	{
		return new Rules();
	}

	@Override
	public Uni<IRules<?, ?>> createRules(Mutiny.Session session, String rulesType, String name, String description, ISystems<?, ?> system, UUID... identityToken)
	{
		return createRules(session, rulesType, null, name, description, system, identityToken);
	}

	@Override
	public Uni<IRules<?, ?>> createRules(Mutiny.Session session, String rulesType, UUID key, String name, String description, ISystems<?, ?> system, UUID... identityToken)
	{
		return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
			var createSession = tuple.getItem1();
			var createEnterprise = tuple.getItem2();
			var createSystem = tuple.getItem3();
			var createIdentityToken = tuple.getItem4();

			Rules rules = new Rules();
			if (key != null)
			{
				rules.setId(key);
			}
			rules.setName(name);
			rules.setDescription(description);
			rules.setEnterpriseID(createEnterprise);
			rules.setSystemID(createSystem);
			rules.setOriginalSourceSystemID(createSystem.getId());

			IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
					.chain(activeFlag -> {
						rules.setActiveFlagID(activeFlag);
						return createSession.persist(rules).replaceWith(Uni.createFrom().item(rules));
					})
					.chain(persisted -> {
						// Create rule type
						createRulesType(createSession, rulesType, rulesType, createSystem, createIdentityToken);
						return Uni.createFrom().item(persisted);
					})
					.chain(ruleType -> {
						// Chain createDefaultSecurity properly
						return ruleType.createDefaultSecurity(createSession, createSystem, createIdentityToken)
								.onItem().invoke(result -> log.trace("Security setup completed successfully for rules"))
								.onFailure().invoke(error -> log.error("Error in createDefaultSecurity for rules", error))
								.onFailure().recoverWithItem(() -> null)
								.replaceWith((IRules<?, ?>) rules);
					});
		});
	}

	@Override
	public Uni<IRules<?, ?>> find(Mutiny.Session session, UUID identity)
	{
		return (Uni)  new Rules().builder(session)
		                  .find(identity)
		                  .get();
	}

	@Override
	public Uni<IRulesType<?, ?>> findType(Mutiny.Session session, UUID identity)
	{
		return (Uni)  new RulesType().builder(session)
		                      .find(identity)
		                      .get();
	}

	@Override
	public Uni<IRules<?, ?>> findRules(Mutiny.Session session, String name, IEnterprise<?, ?> enterprise, UUID... identityToken)
	{
		return (Uni) new Rules().builder(session)
		                  .withName(name)
		                  .inActiveRange()
		                  .inDateRange()
		                  .withEnterprise(enterprise)
		                  .get();
	}

	@Override
	public Uni<IRules<?, ?>> findRules(Mutiny.Session session, String productName, IClassification<?, ?> classification, IEnterprise<?, ?> enterprise, UUID... identityToken)
	{
		return (Uni) new Rules().builder(session)
		                  .withName(productName)
		                  .withClassification(classification)
		                  .inActiveRange()
		                  .inDateRange()
		                  .withEnterprise(enterprise)
		                  .get();
	}

	@Override
	public Uni<IRulesType<?, ?>> createRulesType(Mutiny.Session session, String rulesType, ISystems<?, ?> system, UUID... identityToken)
	{
		return createRulesType(session, rulesType, rulesType, system, identityToken);
	}

	@Override
	public Uni<IRulesType<?, ?>> createRulesType(Mutiny.Session session, String rulesType, String description, ISystems<?, ?> system, UUID... identityToken)
	{
		return createRulesType(session, rulesType, null, description, system, identityToken);
	}

	@Override
	public Uni<IRulesType<?, ?>> createRulesType(Mutiny.Session session, String rulesType, UUID key, String description, ISystems<?, ?> system, UUID... identityToken)
	{
		return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
			var createSession = tuple.getItem1();
			var createEnterprise = tuple.getItem2();
			var createSystem = tuple.getItem3();
			var createIdentityToken = tuple.getItem4();

			RulesType et = new RulesType();

			// Check if rule type exists
			return et.builder(createSession)
					.withName(rulesType)
					.withEnterprise(createEnterprise)
					.inActiveRange()
					.inDateRange()
					.getCount()
					.onFailure().invoke(error -> log.error("Error checking if rule type exists: {}", error.getMessage(), error))
					.chain(count -> {
						boolean exists = count > 0;
						if (!exists)
						{
							// Rule type doesn't exist, create it
							if (key != null)
							{
								et.setId(key);
							}
							et.setName(rulesType);
							et.setDescription(description);
							et.setSystemID(createSystem);
							et.setEnterpriseID(createEnterprise);
							et.setOriginalSourceSystemID(createSystem.getId());

							IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

							// Get active flag using reactive pattern
							return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
									.chain(activeFlag -> {
										et.setActiveFlagID(activeFlag);
										return createSession.persist(et).replaceWith(Uni.createFrom().item(et));
									})
									.chain(persisted -> {
										// Chain createDefaultSecurity properly
										RulesType rulesTypeImpl = (RulesType) persisted;
										return rulesTypeImpl.createDefaultSecurity(createSession, createSystem, createIdentityToken)
												.onItem().invoke(result -> log.trace("Security setup completed successfully for rule type"))
												.onFailure().invoke(error -> log.error("Error in createDefaultSecurity for rule type", error))
												.onFailure().recoverWithItem(() -> null)
												.replaceWith((IRulesType<?, ?>) et);
									});
						}
						else
						{
							// Rule type exists, find and return it
							return findRulesTypes(createSession, rulesType, createSystem, createIdentityToken);
						}
					});
		});
	}

	@Override
	//@CacheResult(cacheName = "RulesTypesString")
	public Uni<IRulesType<?, ?>> findRulesTypes(Mutiny.Session session, String rulesType, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		java.util.UUID enterpriseId = null;
		java.util.UUID systemId = null;
		if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
			enterpriseId = ent.getId();
		}
		if (system instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems sys) {
			systemId = sys.getId();
		}
		String key = enterpriseId + "|" + systemId + "|" + rulesType;
		java.util.UUID cachedId = rulesTypeKeyToId.get(key);
		if (cachedId != null) {
			log.trace("🔁 RulesType cache hit for key '{}': {} — loading by UUID", key, cachedId);
			return getRulesTypeById(session, cachedId)
				.flatMap(found -> {
					if (found != null) {
						return Uni.createFrom().item(found);
					}
					rulesTypeKeyToId.remove(key);
					return new RulesType().builder(session)
										      .withName(rulesType)
										      .withEnterprise(enterprise)
										      .inActiveRange()
										      .inDateRange()
										      .get()
										      .invoke(res -> {
										        if (res != null && res.getId() != null) {
										          rulesTypeKeyToId.put(key, res.getId());
										        }
										      });
				});
		}
		return (Uni)new RulesType().builder(session)
							      .withName(rulesType)
							      .withEnterprise(enterprise)
							      .inActiveRange()
							      .inDateRange()
							      //      .canRead(system, identityToken)
							      .get()
							      .invoke(res -> {
							        if (res != null && res.getId() != null) {
							          rulesTypeKeyToId.put(key, res.getId());
							        }
							      });
	}

	@Override
	public Uni<List<IRulesType<?, ?>>> findRulesTypes(Mutiny.Session session, String classifications, String value, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return (Uni) classificationService.find(session, classifications, system, identityToken)
		        .chain(classification -> {
		            return new RulesType().builder(session)
		                                .withClassification((Classification) classification, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                //     .canRead(system, identityToken)
		                                .getAll();
		        });
	}

	@Override
	public Uni<List<IRules<?, ?>>> findByRulesTypes(Mutiny.Session session, IRulesType<?, ?> rulesType, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return (Uni) new RulesXRulesType().builder(session)
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink(null, (RulesType) rulesType, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll();
	}

	@Override
	public Uni<List<IRulesType<?, ?>>> findRuleTypesByRules(Mutiny.Session session, IRules<?, ?> rulesType, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return  (Uni)  new RulesXRulesType().builder(session)
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink((Rules) rulesType, null, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll();
	}

	@Override
	public Uni<List<IRelationshipValue<IRules<?, ?>, IRulesType<?, ?>, ?>>> findRuleTypeValuesByRules(Mutiny.Session session, IRules<?, ?> rulesType, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return (Uni)  new RulesXRulesType().builder(session)
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink((Rules) rulesType, null, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll();
	}

	@Override
	public Uni<List<IRules<?, ?>>> findRulesByProduct(Mutiny.Session session, IProduct<?, ?> product, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return (Uni)  new RulesXProduct().builder(session)
		                              .withClassification(classificationName, value, system, identityToken)
		                              .findLink(null, (Product) product, value)
		                              .withEnterprise(enterprise)
		                              .inActiveRange()
		                              .inDateRange()
		                              .canRead(system, identityToken)
		                              .getAll();
	}


	@Override
	public Uni<List<IRelationshipValue<IRules<?, ?>, IResourceItem<?, ?>, ?>>> findRulesByResourceItem(Mutiny.Session session, IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return (Uni) new RulesXResourceItem().builder(session)
		                                   .withClassification(classificationName, system)
		                                   .findLink(null, (ResourceItem) resourceItem, value)
		                                   .withEnterprise(enterprise)
		                                   .inActiveRange()
		                                   .inDateRange()
		                                   .canRead(system, identityToken)
		                                   .getAll();
	}
}

