package com.guicedee.activitymaster.fsdm.geography;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.types.exceptions.GeographyException;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.EntityManager;

import static com.guicedee.activitymaster.fsdm.client.types.classifications.EnterpriseClassificationDataConcepts.*;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.geography.GeographyClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;


public class CurrencyService
{
	@Inject
	@ActivityMasterDB
	private EntityManager entityManager;
	
	@CacheResult(cacheName = "GeographyCurrencies", skipGet = true)
	////@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IClassification<?,?> createCurrency(@CacheKey String code, String description, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		
		boolean exists = new Classification().builder(entityManager)
		                                     .withName(code)
		                                     .withConcept(Currency.concept(),system,identityToken)
		                                     .inActiveRange()
		                                     .inDateRange()
		                                     .withEnterprise(system)
		                                     .getCount() > 0;
		if (exists)
		{
			return findCurrency(code, system, identityToken);
		}
		
		return classificationService.create(code, description,
				ClassificationXClassification,
				system, 0,
				Currency.toString(),
				identityToken);
	}
	
	@CacheResult(cacheName = "GeographyCurrencies")
	public IClassification<?,?> findCurrency(@CacheKey String code, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		return new Classification().builder(entityManager)
		                           .withName(code)
		                           .withConcept(Currency.concept(),system,identityToken)
		                           .inActiveRange()
		                           .inDateRange()
		                           .withEnterprise(system)
		                           .get()
		                           .orElseThrow(() -> new GeographyException("Cannot find currency with code : " + code));
	}
	
	@CacheResult(cacheName = "GeographyCurrencies", skipGet = true)
	////@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IClassification<?,?> updateCurrency(@CacheKey String code, String description, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		IClassification<?,?> toUpdate = findCurrency(code, system, identityToken);
		if (description != null)
		{
			com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification update = new Classification();
			update.setId(toUpdate.getId());
			update.setDescription(description);
			update.update(entityManager);
		}
		
		return toUpdate;
	}
}
