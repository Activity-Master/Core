package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.guicedpersistence.lambda.TransactionalBiConsumer;
import com.guicedee.guicedpersistence.lambda.TransactionalSupplier;
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
import lombok.extern.java.Log;
import org.jboss.logmanager.Level;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;

@Log
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
	public CompletableFuture<IRules<?, ?>> createRules(String rulesType, String name, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createRules(rulesType, null, name, description, system, identityToken);
	}
	
	@Override
	
	public CompletableFuture<IRules<?, ?>> createRules(String rulesType, java.lang.String key, String name, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		TransactionalSupplier<IRules<?,?>> ts = IGuiceContext.get(TransactionalSupplier.class);
		ts.setConsumer(() -> {
			Rules rules = new Rules();
			if (key != null)
			{
				rules.setId(key.toString());
			}
			rules.setName(name);
			rules.setDescription(description);
			
			rules.setEnterpriseID(enterprise);
			rules.setSystemID(system);
			rules.setOriginalSourceSystemID(system);
			IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
			rules.setActiveFlagID(activeFlag);
			rules.persist();
			
			return rules;
		});
		CompletableFuture<IRules<?,?>> cf = CompletableFuture.supplyAsync(ts);
		
		cf.whenCompleteAsync(IGuiceContext.get(TransactionalBiConsumer.class).setConsumer((response,error)->{
			createRulesType(rulesType, rulesType, system, identityToken)
					.whenCompleteAsync(IGuiceContext.get(TransactionalBiConsumer.class)
					                           .setConsumer((rulesResponse, rulesError) -> {
						                           if (rulesError != null)
						                           {
							                           log.log(Level.SEVERE, "Cannot create rule", rulesError);
						                           }
						                           else
						                           {
							                           Rules rulesTypes = (Rules) rulesResponse;
							                           rulesTypes.createDefaultSecurity(system, identityToken);
							                           rulesTypes.addRuleTypes(rulesType, "", NoClassification.toString(), system, identityToken);
						                           }
					                           }));
		}));
		return cf;
	}
	
	@Override
	public IRules<?, ?> find(java.lang.String identity)
	{
		return new Rules().builder()
		                  .find(identity)
		                  .get()
		                  .orElse(null);
	}
	
	@Override
	public IRulesType<?, ?> findType(java.lang.String identity)
	{
		return new RulesType().builder()
		                      .find(identity)
		                      .get()
		                      .orElse(null);
	}
	
	@Override
	public IRules<?, ?> findRules(String name, IEnterprise<?, ?> enterprise, java.util.UUID... identityToken)
	{
		return new Rules().builder()
		                  .withName(name)
		                  .inActiveRange()
		                  .inDateRange()
		                  .withEnterprise(enterprise)
		                  .get()
		                  .orElse(null);
	}
	
	@Override
	public IRules<?, ?> findRules(String productName, IClassification<?, ?> classification, IEnterprise<?, ?> enterprise, java.util.UUID... identityToken)
	{
		return new Rules().builder()
		                  .withName(productName)
		                  .withClassification(classification)
		                  .inActiveRange()
		                  .inDateRange()
		                  .withEnterprise(enterprise)
		                  .get()
		                  .orElse(null);
	}
	
	@Override
	public CompletableFuture<IRulesType<?, ?>> createRulesType(String rulesType, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createRulesType(rulesType, rulesType, system, identityToken);
	}
	
	@Override
	public CompletableFuture<IRulesType<?, ?>> createRulesType(String rulesType, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createRulesType(rulesType, null, description, system, identityToken);
	}
	
	@Override
	public CompletableFuture<IRulesType<?, ?>> createRulesType(String rulesType, java.lang.String key, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		RulesType et = new RulesType();
		
		boolean exists = et.builder()
		                   .withName(rulesType)
		                   .withEnterprise(enterprise)
		                   .inActiveRange()
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			TransactionalSupplier<IRulesType<?, ?>> ts = IGuiceContext.get(TransactionalSupplier.class);
			ts.setConsumer(() -> {
				if (key != null)
				{
					et.setId(key.toString());
				}
				et.setName(rulesType);
				et.setDescription(description);
				et.setSystemID(system);
				et.setEnterpriseID(enterprise);
				IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
				IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
				et.setActiveFlagID(activeFlag);
				et.setOriginalSourceSystemID(system);
				et.persist();
				et.createDefaultSecurity(system, identityToken);
				
				return et;
			});
			return CompletableFuture.supplyAsync(ts);
		}
		else
		{
			return (CompletableFuture) CompletableFuture.completedFuture(findRulesTypes(rulesType, system, identityToken));
		}
	}
	
	@Override
	@CacheResult(cacheName = "RulesTypesString")
	public IRulesType<?, ?> findRulesTypes(@CacheKey String rulesType, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		return new RulesType().builder()
		                      .withName(rulesType)
		                      .withEnterprise(enterprise)
		                      .inActiveRange()
		                      .inDateRange()
		                      //      .canRead(system, identityToken)
		                      .get()
		                      .orElseThrow();
	}
	
	@Override
	public List<IRulesType<?, ?>> findRulesTypes(String classifications, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(classifications, system, identityToken);
		@SuppressWarnings({"UnnecessaryLocalVariable", "rawtypes"})
		List all = new RulesType().builder()
		                          .withClassification((Classification) classification, value)
		                          .withEnterprise(enterprise)
		                          .inActiveRange()
		                          .inDateRange()
		                          //     .canRead(system, identityToken)
		                          .getAll();
		//noinspection unchecked
		return all;
	}
	
	@Override
	public List<IRules<?, ?>> findByRulesTypes(IRulesType<?, ?> rulesType, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		List res = new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink(null, (RulesType) rulesType, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll()
		                                .stream()
		                                .map(RulesXRulesType::getRulesID)
		                                .collect(Collectors.toList());
		return res;
	}
	
	@Override
	public List<IRulesType<?, ?>> findRuleTypesByRules(IRules<?, ?> rulesType, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		List res = new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink((Rules) rulesType, null, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll()
		                                .stream()
		                                .map(RulesXRulesType::getRulesTypeID)
		                                .collect(Collectors.toList());
		return res;
	}
	
	@Override
	public List<IRelationshipValue<IRules<?, ?>, IRulesType<?, ?>, ?>> findRuleTypeValuesByRules(IRules<?, ?> rulesType, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		List res = new RulesXRulesType().builder()
		                                .withClassification(classificationName, value, system, identityToken)
		                                .findLink((Rules) rulesType, null, value)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .getAll();
		return res;
	}
	
	@Override
	public List<IRules<?, ?>> findRulesByProduct(IProduct<?, ?> product, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		List res = new RulesXProduct().builder()
		                              .withClassification(classificationName, value, system, identityToken)
		                              .findLink(null, (Product) product, value)
		                              .withEnterprise(enterprise)
		                              .inActiveRange()
		                              .inDateRange()
		                              .canRead(system, identityToken)
		                              .getAll()
		                              .stream()
		                              .map(RulesXProduct::getRulesID)
		                              .collect(Collectors.toList());
		return res;
	}
	
	
	@Override
	public List<IRelationshipValue<IRules<?, ?>, IResourceItem<?, ?>, ?>> findRulesByResourceItem(IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		List res = new RulesXResourceItem().builder()
		                                   .withClassification(classificationName, system)
		                                   .findLink(null, (ResourceItem) resourceItem, value)
		                                   .withEnterprise(enterprise)
		                                   .inActiveRange()
		                                   .inDateRange()
		                                   .canRead(system, identityToken)
		                                   .getAll();
		return res;
	}
}
