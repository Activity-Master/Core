package com.guicedee.activitymaster.fsdm.geography;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.geography.IGeography;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.types.exceptions.GeographyException;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.geography.Geography;
import com.guicedee.activitymaster.fsdm.db.entities.geography.builders.GeographyQueryBuilder;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.EntityManager;

import static com.guicedee.activitymaster.fsdm.client.types.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.geography.GeographyClassifications.*;

public class ContinentService
{
	@Inject
	@ActivityMasterDB
	private EntityManager entityManager;
	
	@CacheResult(cacheName = "GeographyContinents",skipGet = true)
	////@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IGeography<Geography, GeographyQueryBuilder> createContinent(IGeography<Geography, GeographyQueryBuilder> planet, @CacheKey String code, String description, String originalUniqueID, @CacheKey ISystems<?,?> system, @CacheKey  java.util.UUID... identityToken)
	{
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		Classification classification = (Classification) classificationService.find(Continent, system, identityToken);
		
		boolean exists = new Geography().builder(entityManager)
		                                .withClassification(classification)
		                                .withName(code)
		                                .inDateRange()
		                                .inActiveRange()
		                                .withEnterprise(system)
		                                .getCount() > 0;
		if(exists)
		{
			return findContinent(code, system, identityToken);
		}
		Geography geo = new Geography();
		geo.setEnterpriseID(classification.getEnterpriseID());
		geo.setClassification(classification);
		geo.setSystemID(system);
		geo.setOriginalSourceSystemID(system);
		geo.setName(code);
		geo.setDescription(description);
		if (originalUniqueID != null)
		{
			geo.setOriginalSourceSystemUniqueID(originalUniqueID);
		}
		geo.setActiveFlagID(classification.getActiveFlagID());
		geo.persist(entityManager);
	
			geo.createDefaultSecurity(system, identityToken);
		
		
		planet.addChild(geo,NoClassification.toString(),null, system, identityToken);
		return geo;
	}
	
	@CacheResult(cacheName = "GeographyContinents",skipGet = true)
	public IGeography<Geography, GeographyQueryBuilder> findContinent(@CacheKey String code,@CacheKey  ISystems<?,?> system,@CacheKey  java.util.UUID... identityToken)
	{
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		Classification classification = (Classification) classificationService.find(Continent, system, identityToken);
		
		return new Geography().builder(entityManager)
		                      .withClassification(classification)
		                      .withName(code)
		                      .inDateRange()
		                      .inActiveRange()
		                      .withEnterprise(system)
				.get().orElseThrow(()->new GeographyException("Cannot find continent"));
	}
}
