package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
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

@SuppressWarnings("unchecked")
@Log4j2
public class RulesService
		implements IRulesService<RulesService>
{
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
		Rules rules = new Rules();
		if (key != null)
		{
			rules.setId(key);
		}
		rules.setName(name);
		rules.setDescription(description);
		var enterprise = system.getEnterprise();

		rules.setEnterpriseID(enterprise);
		rules.setSystemID(system);
		rules.setOriginalSourceSystemID(system.getId());
		IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		// Get active flag using reactive pattern
		return acService.getActiveFlag(session, enterprise)
		        .chain(activeFlag -> {
		            rules.setActiveFlagID(activeFlag);
		            return session.persist(rules).replaceWith(Uni.createFrom().item(rules));
		        })
		        .chain(persisted -> {
		            // Create rule type - we'll handle this in a separate step
		            createRulesType(session, rulesType, rulesType, system, identityToken);
					return Uni.createFrom().item(persisted);
		        })
		        .chain(ruleType -> {
		            // Start createDefaultSecurity in parallel without waiting for it
		            // We need to cast to RulesType to access the createDefaultSecurity method
                    ruleType.createDefaultSecurity(session, system, identityToken)
		                .subscribe().with(
		                    result -> {
		                        // Security setup completed successfully
		                    },
		                    error -> {
		                        // Log error but don't fail the main operation
		                        log.error("Error in createDefaultSecurity for rule type", error);
		                    }
		                );

		            // Return the rules object
		            return Uni.createFrom().item((IRules<?, ?>) rules);
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
		RulesType et = new RulesType();

		var enterprise = system.getEnterprise();
		// Check if rule type exists
		return et.builder(session)
		         .withName(rulesType)
		         .withEnterprise(enterprise)
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
		                 et.setSystemID(system);
		                 et.setEnterpriseID(enterprise);
		                 et.setOriginalSourceSystemID(system.getId());

		                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		                 // Get active flag using reactive pattern
		                 return acService.getActiveFlag(session, enterprise)
		                        .chain(activeFlag -> {
		                            et.setActiveFlagID(activeFlag);
		                            return session.persist(et).replaceWith(Uni.createFrom().item(et));
		                        })
		                        .chain(persisted -> {
		                            // Start createDefaultSecurity in parallel without waiting for it
		                            // We need to cast to RulesType to access the createDefaultSecurity method
		                            RulesType rulesTypeImpl = (RulesType) persisted;
		                            rulesTypeImpl.createDefaultSecurity(session, system, identityToken)
		                              .subscribe().with(
		                                  result -> {
		                                      // Security setup completed successfully
		                                  },
		                                  error -> {
		                                      // Log error but don't fail the main operation
		                                      log.error("Error in createDefaultSecurity for rule type", error);
		                                  }
		                              );

		                            // Return the persisted rule type immediately
		                            return Uni.createFrom().item((IRulesType<?, ?>) et);
		                        });
		             }
		             else
		             {
		                 // Rule type exists, find and return it
		                 return findRulesTypes(session, rulesType, system, identityToken);
		             }
		         });
	}

	@Override
	//@CacheResult(cacheName = "RulesTypesString")
	public Uni<IRulesType<?, ?>> findRulesTypes(Mutiny.Session session, String rulesType, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return (Uni) new RulesType().builder(session)
		                      .withName(rulesType)
		                      .withEnterprise(enterprise)
		                      .inActiveRange()
		                      .inDateRange()
		                      //      .canRead(system, identityToken)
		                      .get();
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
