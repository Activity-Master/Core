package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.Optional;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.classifications.systems.SystemsClassifications.*;

@Singleton
public class SystemsService
		implements ISystemsService
{
	public static final String ActivityMasterSystemName = "Activity Master System";
	public static final String ActivityMasterWebSystemName = "Activity Master Web";

	@Override
	@CacheResult(cacheName = "GetActivityMaster")
	public Systems getActivityMaster(@CacheKey IEnterprise enterprise, @CacheKey UUID... token)
	{
		return findSystem(enterprise, ActivityMasterSystemName, token);
	}

	@CacheResult(cacheName = "FindSystem")
	public Systems findSystem(@CacheKey IEnterprise enterprise, @CacheKey String systemName, @CacheKey UUID... token)
	{
		Systems search = new Systems();
		return search.builder()
		             .findByName(systemName)
		             .withEnterprise((Enterprise) enterprise)
		             .inActiveRange((Enterprise) enterprise, token)
		             .inDateRange()
		             .canRead((Enterprise) enterprise, token)
		             .get()
		             .get();
	}

	@CacheResult(cacheName = "FindSystemByIdentityClassification")
	@Override
	public Systems findSystem(@CacheKey IEnterprise enterprise, @CacheKey UUID token, UUID... identityToken)
	{
		SystemXClassification systemClassifications = new SystemXClassification();
		Classification identifyClassification = GuiceContext.get(ClassificationService.class)
		                                                    .getIdentityType(enterprise, identityToken);

		Optional<SystemXClassification> exists = systemClassifications.builder()
		                                                              .findChildLink(identifyClassification, token.toString())
		                                                              .inActiveRange((Enterprise) enterprise, identityToken)
		                                                              .inDateRange()
		                                                              .canRead((Enterprise) enterprise, identityToken)
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

	public Systems create(IEnterprise enterprise, String systemName, String systemDesc, String historyName, UUID... identityToken)
	{
		ActiveFlag flag = GuiceContext.get(IActiveFlagService.class)
		                              .getActiveFlag((Enterprise) enterprise);
		Systems newSystem = new Systems();
		Optional<Systems> exists = ActivityMasterConfiguration
				                           .get()
				                           .isDoubleCheckDisabled() ? Optional.empty() :

		                           newSystem.builder()
		                                    .findByName(systemName)
		                                    .get();
		if (exists.isEmpty())
		{
			newSystem.setName(systemName);
			newSystem.setDescription(systemDesc);
			newSystem.setSystemHistoryName(historyName);
			newSystem.setEnterpriseID((Enterprise) enterprise);
			newSystem.setActiveFlagID(flag);
			newSystem.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				newSystem.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
				                                            .getActivityMaster(newSystem.getEnterpriseID(), identityToken)
						, identityToken);
			}
		}
		else
		{
			newSystem = exists.get();
		}

		return newSystem;
	}

	@CacheResult(cacheName = "SystemGetSecurityToken")
	public SecurityToken getSecurityToken(@CacheKey UUID uuidIdentity, @CacheKey Systems system, @CacheKey UUID... identityToken)
	{
		Optional<SecurityToken> token = new SecurityToken().builder()
		                                                   .findBySecurityToken(uuidIdentity.toString(), system.getEnterpriseID())
		                                                   .inActiveRange(system.getEnterpriseID())
		                                                   .inDateRange()
		                                                   .canRead(system.getEnterpriseID(), identityToken)
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
	public UUID getSecurityIdentityToken(@CacheKey ISystems system, @CacheKey UUID... identityToken)
	{
		Optional<SystemXClassification> systemToken = system.findClassification(SystemIdentity, system, identityToken);
		if (systemToken.isEmpty())
		{
			return null;
		}
		else
		{
			UUID id = UUID.fromString(systemToken.get()
			                                     .getValue());
			return id;
		}
	}

}
