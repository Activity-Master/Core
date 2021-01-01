package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassification;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.securitytokens.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.core.services.classifications.systems.SystemsClassifications;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.activitymaster.core.systems.SystemsSystem;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.Optional;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.systems.SystemsClassifications.*;


public class SystemsService
		implements ISystemsService
{
	public static final String ActivityMasterSystemName = "Activity Master System";
	public static final String ActivityMasterWebSystemName = "Activity Master Web";
	
	@Override
	@CacheResult(cacheName = "GetActivityMaster")
	public ISystems<?> getActivityMaster(@CacheKey ISystems<?> requestingSystem, @CacheKey UUID... token)
	{
		return findSystem(requestingSystem, ActivityMasterSystemName, token);
	}
	
	@Override
	@CacheResult(cacheName = "GetActivityMasterEnterprise")
	public ISystems<?> getActivityMaster(@CacheKey IEnterprise<?> requestingSystem, @CacheKey UUID... token)
	{
		return findSystem(requestingSystem, ActivityMasterSystemName, token);
	}
	
	@CacheResult(cacheName = "FindSystemEnterpriseLevel")
	@Override
	public ISystems<?> findSystem(@CacheKey IEnterprise<?> enterprise, @CacheKey String systemName, @CacheKey UUID... token)
	{
		Systems search = new Systems();
		return search.builder()
		             .withName(systemName)
		             .withEnterprise(enterprise)
		             .inActiveRange(enterprise, token)
		             .inDateRange()
		             //.canRead(enterprise, token)
		             .get()
		             .orElse(null);
	}
	
	
	@CacheResult(cacheName = "FindSystem")
	@Override
	public ISystems<?> findSystem(@CacheKey ISystems<?> requestingSystem, @CacheKey String systemName, @CacheKey UUID... token)
	{
		Systems search = new Systems();
		return search.builder()
		             .withName(systemName)
		             .withEnterprise(requestingSystem)
		             .inActiveRange(requestingSystem, token)
		             .inDateRange()
		             //.canRead(enterprise, token)
		             .get()
		             .orElse(null);
	}
	
	@CacheResult(cacheName = "FindSystemByIdentityClassification")
	@Override
	public ISystems<?> findSystem(@CacheKey ISystems<?> requestingSystem, @CacheKey UUID token, UUID... identityToken)
	{
		SystemXClassification systemClassifications = new SystemXClassification();
		Classification identifyClassification = (Classification) GuiceContext.get(ClassificationService.class)
		                                                                     .getIdentityType(requestingSystem, identityToken);
		
		Optional<SystemXClassification> exists = systemClassifications.builder()
		                                                              .findChildLink(identifyClassification, token.toString())
		                                                              .inActiveRange(requestingSystem, identityToken)
		                                                              .inDateRange()
		                                                              .withEnterprise(requestingSystem)
		                                                              .canRead(requestingSystem, identityToken)
		                                                              .get();
		if (exists.isEmpty())
		{
			return null;
		}
		else
		{
			return exists.get()
			             .getSystemID();
		}
	}
	
	@Override
	public UUID registerNewSystem(IEnterprise<?> enterprise, ISystems<?> newSystem)
	{
		//Create Security Token for the created system row
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		SecurityTokenService securityTokenService = GuiceContext.get(SecurityTokenService.class);
		
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(newSystem);
		
		UUID activityMasterSystemUUID = GuiceContext.get(SystemsService.class)
		                                            .getSecurityIdentityToken(activityMasterSystem);
		
		SecurityToken newSystemsSecurityToken = (SecurityToken) securityTokenService.create(UserGroupSecurityTokenClassifications.System,
				newSystem.getName(), newSystem.getDescription(), activityMasterSystem);
		
		SecurityToken systemsToken = (SecurityToken) securityTokenService.create(UserGroupSecurityTokenClassifications.System,
				UserGroupSecurityTokenClassifications.System.classificationName(),
				UserGroupSecurityTokenClassifications.System.classificationDescription(), activityMasterSystem);
		
		securityTokenService.link(systemsToken, newSystemsSecurityToken,
				classificationService.find(UserGroupSecurityTokenClassifications.System, activityMasterSystem,
						activityMasterSystemUUID));
		//Add the systems classifications so the UUID can be fetched
		newSystem.addOrReuse(SystemsClassifications.SystemIdentity, newSystemsSecurityToken.getSecurityToken(), newSystem,
				activityMasterSystemUUID);
		
		UUID newSystemUUID = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(newSystem, activityMasterSystemUUID);
		
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			newSystemsSecurityToken.createDefaultSecurity(activityMasterSystem, activityMasterSystemUUID);
		}
		
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			systemsToken.createDefaultSecurity(activityMasterSystem, activityMasterSystemUUID);
		}
		
		GuiceContext.get(SystemsSystem.class)
		            .createInvolvedPartyForNewSystem(newSystem);
		
		return newSystemUUID;
	}
	
	@Override
	public ISystems<?> create(IEnterprise<?> enterprise, String systemName, String systemDesc, String historyName, UUID... identityToken)
	{
		IActiveFlag<?> flag = GuiceContext.get(IActiveFlagService.class)
		                                  .getActiveFlag(enterprise);
		
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
			newSystem.setEnterpriseID((Enterprise) enterprise);
			ActiveFlag flagg = (ActiveFlag) flag;
			newSystem.setActiveFlagID(flagg);
			newSystem.persist();
			
			if (!ActivityMasterConfiguration.getCreatingNew()
			                                .get())
			{
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{
					newSystem.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
					                                            .getActivityMaster(newSystem, identityToken)
							, identityToken);
				}
			}
		}
		else
		{
			return findSystem(newSystem, systemName, identityToken);
		}
		
		return newSystem;
	}
	
	@CacheResult(cacheName = "SystemGetSecurityToken")
	public ISecurityToken<?> getSecurityToken(@CacheKey UUID uuidIdentity, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		Optional<SecurityToken> token = new SecurityToken().builder()
		                                                   .findBySecurityToken(uuidIdentity.toString(), system.getEnterpriseID())
		                                                   .inActiveRange(system.getEnterpriseID())
		                                                   .inDateRange()
		                                                   .withEnterprise(system.getEnterprise())
		                                                   .canRead(system, identityToken)
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
	public UUID getSecurityIdentityToken(@CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		Optional<IRelationshipValue<ISystems<?>, IClassification<?>, ?>> systemToken = system.findClassifications(SystemIdentity, system, identityToken);
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
