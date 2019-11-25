package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.UserGroupSecurityTokenClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.systems.SystemsClassifications;
import com.armineasy.activitymaster.activitymaster.services.dto.*;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.armineasy.activitymaster.activitymaster.systems.SystemsSystem;
import com.google.inject.Singleton;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.Optional;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.ActivityMasterStatics.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.systems.SystemsClassifications.*;

@Singleton
public class SystemsService
		implements ISystemsService
{
	public static final String ActivityMasterSystemName = "Activity Master System";
	public static final String ActivityMasterWebSystemName = "Activity Master Web";

	@Override
	@CacheResult(cacheName = "GetActivityMaster")
	public ISystems<?> getActivityMaster(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... token)
	{
		return findSystem(enterprise, ActivityMasterSystemName, token);
	}

	@CacheResult(cacheName = "FindSystem")
	public ISystems<?> findSystem(@CacheKey IEnterprise<?> enterprise, @CacheKey String systemName, @CacheKey UUID... token)
	{
		Systems search = new Systems();
		return search.builder()
		             .findByName(systemName)
		             .withEnterprise(enterprise)
		             .inActiveRange(enterprise, token)
		             .inDateRange()
		             .canRead(enterprise, token)
		             .get()
		             .get();
	}

	@CacheResult(cacheName = "FindSystemByIdentityClassification")
	@Override
	public ISystems<?> findSystem(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID token, UUID... identityToken)
	{
		SystemXClassification systemClassifications = new SystemXClassification();
		Classification identifyClassification = (Classification) GuiceContext.get(ClassificationService.class)
		                                                                     .getIdentityType(enterprise, identityToken);

		Optional<SystemXClassification> exists = systemClassifications.builder()
		                                                              .findChildLink(identifyClassification, token.toString())
		                                                              .inActiveRange(enterprise, identityToken)
		                                                              .inDateRange()
		                                                              .withEnterprise(enterprise)
		                                                              .canRead(enterprise, identityToken)
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
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,timeout = transactionTimeout)
	public UUID registerNewSystem(IEnterprise<?> enterprise, ISystems<?> newSystem)
	{
		//Create Security Token for the created system row
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		SecurityTokenService securityTokenService = GuiceContext.get(SecurityTokenService.class);

		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);
		UUID activityMasterSystemUUID = GuiceContext.get(SystemsService.class)
		                                            .getSecurityIdentityToken(activityMasterSystem);

		SecurityToken newSystemsSecurityToken = (SecurityToken) securityTokenService.create(UserGroupSecurityTokenClassifications.System,
		                                                                                    newSystem.getName(), newSystem.getDescription(), newSystem);

		SecurityToken systemsToken = (SecurityToken) securityTokenService.create(UserGroupSecurityTokenClassifications.System,
		                                                                         UserGroupSecurityTokenClassifications.System.classificationName(),
		                                                                         UserGroupSecurityTokenClassifications.System.classificationDescription(), activityMasterSystem);

		securityTokenService.link(systemsToken, newSystemsSecurityToken,
		                          (Classification) classificationService.find(UserGroupSecurityTokenClassifications.System, activityMasterSystem.getEnterpriseID(),
		                                                                      activityMasterSystemUUID));
		//Add the systems classifications so the UUID can be fetched
		newSystem.addOrReuse(SystemsClassifications.SystemIdentity, newSystemsSecurityToken.getSecurityToken(), newSystem,
		                     activityMasterSystemUUID);

		UUID newSystemUUID = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(newSystem, activityMasterSystemUUID);

		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			newSystemsSecurityToken.createDefaultSecurity(activityMasterSystem,activityMasterSystemUUID);
		}

		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			systemsToken.createDefaultSecurity(activityMasterSystem,activityMasterSystemUUID);
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
		                                    .findByName(systemName)
		                                    .get();
		if (exists.isEmpty())
		{
			newSystem.setName(systemName);
			newSystem.setDescription(systemDesc);
			newSystem.setSystemHistoryName(historyName);
			newSystem.setEnterpriseID((Enterprise) enterprise);
			newSystem.setActiveFlagID((ActiveFlag)flag);
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
	public SecurityToken getSecurityToken(@CacheKey UUID uuidIdentity, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		Optional<SecurityToken> token = new SecurityToken().builder()
		                                                   .findBySecurityToken(uuidIdentity.toString(), system.getEnterpriseID())
		                                                   .inActiveRange(system.getEnterpriseID())
		                                                   .inDateRange()
		                                                   .withEnterprise(system.getEnterprise())
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
	public UUID getSecurityIdentityToken(@CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		Optional<IRelationshipValue<ISystems<?>, IClassification<?>,?>> systemToken = system.find(SystemIdentity, system, identityToken);
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
