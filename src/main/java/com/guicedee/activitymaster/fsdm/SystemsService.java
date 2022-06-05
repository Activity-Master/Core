package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.SystemsException;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.db.entities.systems.SystemsXClassification;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.Optional;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications.*;

public class SystemsService
		implements ISystemsService<SystemsService>
{
	@Inject
	private IEnterprise<?,?> enterprise;
	
	@Inject
	private IClassificationService<?> classificationService;
	
	@Inject
	private ISecurityTokenService<?> securityTokenService;
	
	public ISystems<?,?> get()
	{
		return new Systems();
	}
	
	@Override
	@CacheResult(cacheName = "GetActivityMaster")
	public ISystems<?,?> getActivityMaster(@CacheKey ISystems<?,?> requestingSystem, @CacheKey java.util.UUID... identityToken)
	{
		return findSystem(requestingSystem, ActivityMasterSystemName, identityToken);
	}
	
	@Override
	@CacheResult(cacheName = "GetActivityMasterEnterprise")
	public ISystems<?,?> getActivityMaster(@CacheKey IEnterprise<?,?> requestingSystem, java.util.UUID... identityToken)
	{
		return findSystem(requestingSystem, ActivityMasterSystemName, identityToken);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public boolean doesSystemExist(IEnterprise<?,?> enterprise, String systemName, java.util.UUID... identityToken)
	{
		return new Systems().builder()
		                    .withName(systemName)
		                    .withEnterprise(enterprise)
		                    //    .inActiveRange()
		                    .inDateRange()
		                    .getCount() > 0;
	}
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "FindSystemEnterpriseLevel")
	@Override
	public ISystems<?,?> findSystem(@CacheKey IEnterprise<?,?> enterprise, @CacheKey String systemName, java.util.UUID... identityToken)
	{
		Systems search = new Systems();
		return search.builder()
		             .withName(systemName)
		             .withEnterprise(enterprise)
		            // .inActiveRange()
		             .inDateRange()
		             //.canRead(enterprise, identityToken)
		             .get()
		             .orElseThrow(() -> new SystemsException("Cannot find a system named - " + systemName + " - in enterprise - " + enterprise));
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "FindSystemByIdentityClassification")
	@Override
	public ISystems<?,?> findSystem(@CacheKey ISystems<?,?> requestingSystem, @CacheKey String token, java.util.UUID... identityToken)
	{
		SystemsXClassification systemClassifications = new SystemsXClassification();
		Classification identifyClassification = (Classification) classificationService.getIdentityType(requestingSystem, identityToken);
		
		return systemClassifications.builder()
		                            .findLink(null,identifyClassification, token.toString())
		                          //  .inActiveRange()
		                            .inDateRange()
		                            .withEnterprise(enterprise)
		                            .canRead(requestingSystem, identityToken)
		                            .get()
		                            .orElseThrow(() -> new SystemsException("Cannot find a child system for - " + requestingSystem + " - in enterprise - " + enterprise))
		                            .getSystemID();
	}
	
	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public String registerNewSystem(IEnterprise<?,?> enterprise, ISystems<?,?> newSystem)
	{
		//Create Security Token for the created system row
		ISystems<?, ?> activityMasterSystem = getISystem(ActivityMasterSystemName);
		UUID activityMasterSystemUUID = getISystemToken(ActivityMasterSystemName);
		ISecurityTokenService<?> iSecurityTokenService = securityTokenService;
		SecurityToken newSystemsSecurityToken = (SecurityToken) iSecurityTokenService.create(UserGroupSecurityTokenClassifications.System.toString(),
				newSystem.getName(), newSystem.getDescription(), activityMasterSystem);
		
		SecurityToken systemsToken = (SecurityToken) iSecurityTokenService.create(UserGroupSecurityTokenClassifications.System.toString(),
				UserGroupSecurityTokenClassifications.System.toString(),
				UserGroupSecurityTokenClassifications.System.classificationDescription(), activityMasterSystem);
		
		iSecurityTokenService.link(systemsToken, newSystemsSecurityToken,
				classificationService.find(UserGroupSecurityTokenClassifications.System, activityMasterSystem,
						activityMasterSystemUUID));
		//Add the systems classifications so the UUID can be fetched
		newSystem.addOrReuseClassification(SystemsClassifications.SystemIdentity, newSystemsSecurityToken.getSecurityToken(), newSystem,
				activityMasterSystemUUID);
		
		UUID newSystemUUID = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(newSystem, activityMasterSystemUUID);
		newSystemsSecurityToken.createDefaultSecurity(activityMasterSystem, activityMasterSystemUUID);
		
		systemsToken.createDefaultSecurity(activityMasterSystem, activityMasterSystemUUID);
		
		
		GuiceContext.get(SystemsSystem.class)
		            .createInvolvedPartyForNewSystem(newSystem);
		
		return newSystemUUID.toString();
	}
	
	@Override
	public ISystems<?,?> create(IEnterprise<?,?> enterprise, String systemName, String systemDesc, java.util.UUID... identityToken)
	{
		return create(enterprise, systemName, systemDesc,systemName, identityToken);
	}

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public ISystems<?,?> create(IEnterprise<?, ?> enterprise, String systemName, String systemDesc, String historyName, java.util.UUID... identityToken)
	{
		Systems newSystem = new Systems();
		Optional<Systems> exists = newSystem.builder()
		                                    .withEnterprise(enterprise)
		                                    .withName(systemName)
		                                    .get();
		if (exists.isEmpty())
		{
			newSystem.setName(systemName);
			newSystem.setDescription(systemDesc);
			newSystem.setSystemHistoryName(historyName);
			newSystem.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
			newSystem.setActiveFlagID(activeFlag);
			newSystem.persist();
			
			newSystem.createDefaultSecurity(newSystem, identityToken);
		}
		else
		{
			return findSystem(newSystem, systemName, identityToken);
		}
		
		return newSystem;
	}
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SystemGetSecurityToken")
	public ISecurityToken<?,?> getSecurityToken(@CacheKey String uuidIdentity, @CacheKey ISystems<?,?> system, java.util.UUID... identityToken)
	{
		Optional<SecurityToken> token = new SecurityToken().builder()
		                                                   .findBySecurityToken(uuidIdentity.toString(), enterprise)
		                                                   .inActiveRange()
		                                                   .inDateRange()
		                                                   .withEnterprise(enterprise)
		                                             //      .canRead(system, identityToken)
		                                                   .get();
		if (token.isEmpty())
		{
			return null;
		}
		else
		{
			return token.get();
		}
	}
	
	
	@CacheResult(cacheName = "SystemSetSecurityTokenUUID")
	@Override
	public UUID getSecurityIdentityToken(@CacheKey ISystems<?,?> system, java.util.UUID... identityToken)
	{
		Optional<? extends IRelationshipValue<?, IClassification<?, ?>, ?>> systemToken = system.findClassification(SystemIdentity, system, identityToken);
		if (systemToken.isEmpty())
		{
			return null;
		}
		else
		{
			return systemToken.get()
			                  .getValueAsUUID();
		}
	}
	
}
