package com.guicedee.activitymaster.core;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.*;
import com.guicedee.activitymaster.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.SystemsClassifications;
import com.guicedee.activitymaster.client.services.classifications.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.client.services.exceptions.SystemsException;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.db.entities.systems.SystemsXClassification;
import com.guicedee.activitymaster.core.systems.SystemsSystem;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.Optional;
import java.util.UUID;

import static com.guicedee.activitymaster.client.services.classifications.SystemsClassifications.*;

public class SystemsService
		implements ISystemsService<SystemsService>
{
	public static final String ActivityMasterSystemName = "Activity Master System";
	public static final String ActivityMasterWebSystemName = "Activity Master Web";
	
	@Inject
	@Named("Active")
	private IActiveFlag<?,?> activeFlag;
	
	@Inject
	private IEnterprise<?,?> enterprise;
	
	@Inject
	private IClassificationService<?> classificationService;
	
	@Inject
	private ISecurityTokenService<?> securityTokenService;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private Provider<UUID> activityMasterSystemUUID;
	
	@Override
	@CacheResult(cacheName = "GetActivityMaster")
	public ISystems<?,?> getActivityMaster(@CacheKey ISystems<?,?> requestingSystem, @CacheKey UUID... token)
	{
		return findSystem(requestingSystem, ActivityMasterSystemName, token);
	}
	
	@Override
	@CacheResult(cacheName = "GetActivityMasterEnterprise")
	public ISystems<?,?> getActivityMaster(@CacheKey IEnterprise<?,?> requestingSystem, @CacheKey UUID... token)
	{
		return findSystem(requestingSystem, ActivityMasterSystemName, token);
	}
	
	@Override
	public boolean doesSystemExist(IEnterprise<?,?> enterprise, String systemName, UUID... token)
	{
		return new Systems().builder()
		                    .withName(systemName)
		                    .withEnterprise(enterprise)
		                    //    .inActiveRange(enterprise, token)
		                    .inDateRange()
		                    .getCount() > 0;
	}
	
	@CacheResult(cacheName = "FindSystemEnterpriseLevel")
	@Override
	public ISystems<?,?> findSystem(@CacheKey IEnterprise<?,?> enterprise, @CacheKey String systemName, @CacheKey UUID... token)
	{
		Systems search = new Systems();
		return search.builder()
		             .withName(systemName)
		             .withEnterprise(enterprise)
		             .inActiveRange(enterprise, token)
		             .inDateRange()
		             //.canRead(enterprise, token)
		             .get()
		             .orElseThrow(() -> new SystemsException("Cannot find a system named - " + systemName + " - in enterprise - " + enterprise));
	}
	
	
	@CacheResult(cacheName = "FindSystem")
	@Override
	public ISystems<?,?> findSystem(ISystems<?,?> requestingSystem, @CacheKey String systemName, UUID... token)
	{
		Systems search = new Systems();
		return search.builder()
		             .withName(systemName)
		             .withEnterprise(enterprise)
		             .inActiveRange(enterprise, token)
		             .inDateRange()
		             //.canRead(enterprise, token)
		             .get()
		             .orElseThrow(() -> new SystemsException("Cannot find a system named - " + systemName + " - in enterprise - " + enterprise));
	}
	
	@CacheResult(cacheName = "FindSystemByIdentityClassification")
	@Override
	public ISystems<?,?> findSystem(@CacheKey ISystems<?,?> requestingSystem, @CacheKey UUID token, UUID... identityToken)
	{
		SystemsXClassification systemClassifications = new SystemsXClassification();
		Classification identifyClassification = (Classification) classificationService.getIdentityType(requestingSystem, identityToken);
		
		return systemClassifications.builder()
		                            .findLink(null,identifyClassification, token.toString())
		                            .inActiveRange(enterprise, identityToken)
		                            .inDateRange()
		                            .withEnterprise(enterprise)
		                            .canRead(requestingSystem, identityToken)
		                            .get()
		                            .orElseThrow(() -> new SystemsException("Cannot find a child system for - " + requestingSystem + " - in enterprise - " + enterprise))
		                            .getSystemID();
	}
	
	@Override
	public UUID registerNewSystem(IEnterprise<?,?> enterprise, ISystems<?,?> newSystem)
	{
		//Create Security Token for the created system row
		
		SecurityToken newSystemsSecurityToken = (SecurityToken) securityTokenService.create(UserGroupSecurityTokenClassifications.System.toString(),
				newSystem.getName(), newSystem.getDescription(), activityMasterSystem);
		
		SecurityToken systemsToken = (SecurityToken) securityTokenService.create(UserGroupSecurityTokenClassifications.System.toString(),
				UserGroupSecurityTokenClassifications.System.toString(),
				UserGroupSecurityTokenClassifications.System.classificationDescription(), activityMasterSystem);
		
		securityTokenService.link(systemsToken, newSystemsSecurityToken,
				classificationService.find(UserGroupSecurityTokenClassifications.System, activityMasterSystem,
						activityMasterSystemUUID.get()));
		//Add the systems classifications so the UUID can be fetched
		newSystem.addOrReuseClassification(SystemsClassifications.SystemIdentity, newSystemsSecurityToken.getSecurityToken(), newSystem,
				activityMasterSystemUUID.get());
		
		UUID newSystemUUID = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(newSystem, activityMasterSystemUUID.get());
		newSystemsSecurityToken.createDefaultSecurity(activityMasterSystem, activityMasterSystemUUID.get());
		
		systemsToken.createDefaultSecurity(activityMasterSystem, activityMasterSystemUUID.get());
		
		
		GuiceContext.get(SystemsSystem.class)
		            .createInvolvedPartyForNewSystem(newSystem);
		
		return newSystemUUID;
	}
	
	@Override
	public ISystems<?,?> create(IEnterprise<?,?> enterprise, String systemName, String systemDesc, UUID... identityToken)
	{
		return create(enterprise, systemName, systemDesc, systemName, identityToken);
	}
	
	@Override
	public ISystems<?,?> create(IEnterprise<?,?> enterprise, String systemName, String systemDesc, String historyName, UUID... identityToken)
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
			newSystem.setActiveFlagID(activeFlag);
			newSystem.persist();
			newSystem.createDefaultSecurity(activityMasterSystem, identityToken);
		}
		else
		{
			return findSystem(newSystem, systemName, identityToken);
		}
		
		return newSystem;
	}
	
	@CacheResult(cacheName = "SystemGetSecurityToken")
	public ISecurityToken<?,?> getSecurityToken(@CacheKey UUID uuidIdentity, @CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		Optional<SecurityToken> token = new SecurityToken().builder()
		                                                   .findBySecurityToken(uuidIdentity.toString(), enterprise)
		                                                   .inActiveRange(enterprise)
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
	public UUID getSecurityIdentityToken(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
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
