package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
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



import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;

@Log4j2
public class RulesService
		implements IRulesService<RulesService>
{
	@Inject
	private IEnterprise<?, ?> enterprise;

	@Inject
	private IClassificationService<?> classificationService;

	public IRules<?, ?> get()
	{
		return new Rules();
	}

	@Override
	public Uni<IRules<?, ?>> createRules(String rulesType, String name, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createRules(rulesType, null, name, description, system, identityToken);
	}

	@Override
	public Uni<IRules<?, ?>> createRules(String rulesType, java.util.UUID key, String name, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		Rules rules = new Rules();
		if (key != null)
		{
			rules.setId(key);
		}
		rules.setName(name);
		rules.setDescription(description);

		rules.setEnterpriseID(enterprise);
		rules.setSystemID(system);
		rules.setOriginalSourceSystemID(system.getId());
		IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		// Get active flag using reactive pattern
		return acService.getActiveFlag(enterprise)
		        .chain(activeFlag -> {
		            rules.setActiveFlagID(activeFlag);
		            return rules.persist();
		        })
		        .chain(persisted -> {
		            // Create rule type - we'll handle this in a separate step
		            return createRulesType(rulesType, rulesType, system, identityToken);
		        })
		        .chain(ruleType -> {
		            // Start createDefaultSecurity in parallel without waiting for it
		            // We need to cast to RulesType to access the createDefaultSecurity method
		            RulesType rulesTypeImpl = (RulesType) ruleType;
		            rulesTypeImpl.createDefaultSecurity(system, identityToken)
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
	public Uni<IRules<?, ?>> find(UUID identity)
	{
		return new Rules().builder()
		                  .find(identity)
		                  .get()
		                  .onFailure().invoke(error -> log.error("Error finding rule by ID: {}", error.getMessage(), error))
		                  .onItem().ifNull().continueWith(() -> null)
		                  .map(rule -> (IRules<?, ?>) rule);
	}

	@Override
	public Uni<IRulesType<?, ?>> findType(UUID identity)
	{
		return new RulesType().builder()
		                      .find(identity)
		                      .get()
		                      .onFailure().invoke(error -> log.error("Error finding rule type by ID: {}", error.getMessage(), error))
		                      .onItem().ifNull().continueWith(() -> null)
		                      .map(ruleType -> (IRulesType<?, ?>) ruleType);
	}

	@Override
	public Uni<IRules<?, ?>> findRules(String name, IEnterprise<?, ?> enterprise, java.util.UUID... identityToken)
	{
		return new Rules().builder()
		                  .withName(name)
		                  .inActiveRange()
		                  .inDateRange()
		                  .withEnterprise(enterprise)
		                  .get()
		                  .onFailure().invoke(error -> log.error("Error finding rule by name: {}", error.getMessage(), error))
		                  .onItem().ifNull().continueWith(() -> null)
		                  .map(rule -> (IRules<?, ?>) rule);
	}

	@Override
	public Uni<IRules<?, ?>> findRules(String productName, IClassification<?, ?> classification, IEnterprise<?, ?> enterprise, java.util.UUID... identityToken)
	{
		return new Rules().builder()
		                  .withName(productName)
		                  .withClassification(classification)
		                  .inActiveRange()
		                  .inDateRange()
		                  .withEnterprise(enterprise)
		                  .get()
		                  .onFailure().invoke(error -> log.error("Error finding rule by name and classification: {}", error.getMessage(), error))
		                  .onItem().ifNull().continueWith(() -> null)
		                  .map(rule -> (IRules<?, ?>) rule);
	}

	@Override
	public Uni<IRulesType<?, ?>> createRulesType(String rulesType, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createRulesType(rulesType, rulesType, system, identityToken);
	}

	@Override
	public Uni<IRulesType<?, ?>> createRulesType(String rulesType, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createRulesType(rulesType, null, description, system, identityToken);
	}

	@Override
	public Uni<IRulesType<?, ?>> createRulesType(String rulesType, java.util.UUID key, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		RulesType et = new RulesType();

		// Check if rule type exists
		return et.builder()
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
		                 return acService.getActiveFlag(enterprise)
		                        .chain(activeFlag -> {
		                            et.setActiveFlagID(activeFlag);
		                            return et.persist();
		                        })
		                        .chain(persisted -> {
		                            // Start createDefaultSecurity in parallel without waiting for it
		                            // We need to cast to RulesType to access the createDefaultSecurity method
		                            RulesType rulesTypeImpl = (RulesType) persisted;
		                            rulesTypeImpl.createDefaultSecurity(system, identityToken)
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
		                 return findRulesTypes(rulesType, system, identityToken);
		             }
		         });
	}

	@Override
	//@CacheResult(cacheName = "RulesTypesString")
	public Uni<IRulesType<?, ?>> findRulesTypes( String rulesType,  ISystems<?, ?> system,  java.util.UUID... identityToken)
	{
		return new RulesType().builder()
		                      .withName(rulesType)
		                      .withEnterprise(enterprise)
		                      .inActiveRange()
		                      .inDateRange()
		                      //      .canRead(system, identityToken)
		                      .get()
		                      .onFailure().invoke(error -> log.error("Error finding rule type by name: {}", error.getMessage(), error))
		                      .onItem().ifNull().failWith(() -> new NoSuchElementException("Rule type not found with name: " + rulesType))
		                      .map(ruleType -> (IRulesType<?, ?>) ruleType);
	}

	@Override
	public Uni<List<IRulesType<?, ?>>> findRulesTypes(String classifications, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return classificationService.find(classifications, system, identityToken)
		        .chain(classification -> {
		            return new RulesType().builder()
		                                .withClassification((Classification) classification, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                //     .canRead(system, identityToken)
		                                .getAll()
		                                .onFailure().invoke(error -> log.error("Error finding rule types by classification: {}", error.getMessage(), error))
		                                .map(ruleTypes -> (List<IRulesType<?, ?>>) (List<?>) ruleTypes);
		        });
	}

	@Override
	public Uni<List<IRules<?, ?>>> findByRulesTypes(IRulesType<?, ?> rulesType, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink(null, (RulesType) rulesType, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll()
		                                .onFailure().invoke(error -> log.error("Error finding rules by rule type: {}", error.getMessage(), error))
		                                .map(list -> {
		                                    List<IRules<?, ?>> result = new ArrayList<>();
		                                    for (RulesXRulesType item : list) {
		                                        result.add(item.getRulesID());
		                                    }
		                                    return result;
		                                });
	}

	@Override
	public Uni<List<IRulesType<?, ?>>> findRuleTypesByRules(IRules<?, ?> rulesType, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink((Rules) rulesType, null, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll()
		                                .onFailure().invoke(error -> log.error("Error finding rule types by rule: {}", error.getMessage(), error))
		                                .map(list -> {
		                                    List<IRulesType<?, ?>> result = new ArrayList<>();
		                                    for (RulesXRulesType item : list) {
		                                        result.add(item.getRulesTypeID());
		                                    }
		                                    return result;
		                                });
	}

	@Override
	public Uni<List<IRelationshipValue<IRules<?, ?>, IRulesType<?, ?>, ?>>> findRuleTypeValuesByRules(IRules<?, ?> rulesType, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink((Rules) rulesType, null, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll()
		                                .onFailure().invoke(error -> log.error("Error finding rule type values by rule: {}", error.getMessage(), error))
		                                .map(list -> (List<IRelationshipValue<IRules<?, ?>, IRulesType<?, ?>, ?>>) (List<?>) list);
	}

	@Override
	public Uni<List<IRules<?, ?>>> findRulesByProduct(IProduct<?, ?> product, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new RulesXProduct().builder()
		                              .withClassification(classificationName, value, system, identityToken)
		                              .findLink(null, (Product) product, value)
		                              .withEnterprise(enterprise)
		                              .inActiveRange()
		                              .inDateRange()
		                              .canRead(system, identityToken)
		                              .getAll()
		                              .onFailure().invoke(error -> log.error("Error finding rules by product: {}", error.getMessage(), error))
		                              .map(list -> {
		                                  List<IRules<?, ?>> result = new ArrayList<>();
		                                  for (RulesXProduct item : list) {
		                                      result.add(item.getRulesID());
		                                  }
		                                  return result;
		                              });
	}


	@Override
	public Uni<List<IRelationshipValue<IRules<?, ?>, IResourceItem<?, ?>, ?>>> findRulesByResourceItem(IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new RulesXResourceItem().builder()
		                                   .withClassification(classificationName, system)
		                                   .findLink(null, (ResourceItem) resourceItem, value)
		                                   .withEnterprise(enterprise)
		                                   .inActiveRange()
		                                   .inDateRange()
		                                   .canRead(system, identityToken)
		                                   .getAll()
		                                   .onFailure().invoke(error -> log.error("Error finding rules by resource item: {}", error.getMessage(), error))
		                                   .map(list -> (List<IRelationshipValue<IRules<?, ?>, IResourceItem<?, ?>, ?>>) (List<?>) list);
	}
}
