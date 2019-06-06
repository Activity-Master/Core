package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.*;
import com.armineasy.activitymaster.activitymaster.db.entities.security.builders.SecurityTokenQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.SecurityHierarchyView;
import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.ISecurityTokenClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.UserGroupSecurityTokenClassifications;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.services.system.ISecurityTokenService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;
import lombok.extern.java.Log;
import org.hibernate.annotations.Cache;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.logging.Level;

import static com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.SecurityTokenClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.UserGroupSecurityTokenClassifications.*;
import static com.jwebmp.entityassist.enumerations.Operand.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")
@Singleton
@Log
public class SecurityTokenService
		implements ISecurityTokenService
{
	public SecurityTokensSecurityToken grantAccessToToken(SecurityToken fromToken, SecurityToken toToken,
	                                                      boolean create, boolean update, boolean delete, boolean read, Systems system)
	{
		return grantAccessToToken(fromToken, toToken, create, update, delete, read, system, null, null, null);
	}

	public SecurityTokensSecurityToken grantAccessToToken(@NotNull SecurityToken fromToken, @NotNull SecurityToken toToken,
	                                                      boolean create, boolean update, boolean delete, boolean read,
	                                                      Systems system, String originalId,
	                                                      Date effectiveFromDate, Date effectiveToDate)
	{
		SecurityTokensSecurityToken sta = new SecurityTokensSecurityToken();
		Optional<SecurityTokensSecurityToken> exists = ActivityMasterConfiguration
				                                               .get()
				                                               .isDoubleCheckDisabled() ? Optional.empty() :

		                                               sta.builder()
		                                                  .findBySecurityToken(fromToken, toToken)
		                                                  .inActiveRange(system.getEnterpriseID())
		                                                  .inDateRange()
		                                                  .get();
		if (exists.isEmpty())
		{
			sta.setSystemID(system);
			sta.setOriginalSourceSystemID(system);
			sta.setEnterpriseID(system.getEnterpriseID());
			sta.setActiveFlagID(system.getActiveFlagID());
			sta.setSecurityTokenID(fromToken);
			sta.setBase(toToken);
			sta.setCreateAllowed(create);
			sta.setUpdateAllowed(update);
			sta.setDeleteAllowed(delete);
			sta.setReadAllowed(read);
			sta.persist();
		}
		else
		{
			sta = exists.get();


		}
		return sta;
	}

	public SecurityToken create(ISecurityTokenClassification<?> classificationValue, String name, String description, ISystems system)
	{
		return create(classificationValue, name, description, system, null);
	}

	public SecurityToken create(ISecurityTokenClassification<?> classificationValue, String name, String description, ISystems system, SecurityToken parent, UUID... identityToken)
	{
		ClassificationService classificationService = get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, system.getEnterpriseID(), identityToken);

		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = ActivityMasterConfiguration
				                                 .get()
				                                 .isDoubleCheckDisabled() ? Optional.empty() :
		                                 st.builder()
		                                   .findBySecurityToken(name, classification.getEnterpriseID())
		                                   .inActiveRange(classification.getEnterpriseID())
		                                   .inDateRange()
		                                   .get();
		if (exists.isEmpty())
		{
			exists = st.builder()
			           .findByName(name)
			           .inActiveRange(classification.getEnterpriseID())
			           .inDateRange()
			           .get();

			if (exists.isEmpty())
			{
				st.setName(name);
				st.setDescription(description);
				st.setSystemID((Systems) system);
				st.setSecurityToken(UUID.randomUUID()
				                        .toString());
				st.setEnterpriseID(classification.getEnterpriseID());
				st.setSystemID(classification.getSystemID());
				st.setActiveFlagID(classification.getActiveFlagID());
				st.setOriginalSourceSystemID(classification.getSystemID());
				st.setSecurityTokenClassificationID(classification);
				st.persist();
				if (get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{
					st.createDefaultSecurity(get(ISystemsService.class)
					                                     .getActivityMaster(st.getEnterpriseID(), identityToken), identityToken);
				}
			}
			else
			{
				st = exists.get();
			}
		}
		else
		{
			st = exists.get();
		}
		if (parent == null)
		{
			return st;
		}

		link(parent, st, classification);

		return st;
	}

	public SecurityTokenXSecurityToken link(SecurityToken parent, SecurityToken child, Classification classification,UUID...identifyingToken )
	{
		SecurityTokenXSecurityToken root = new SecurityTokenXSecurityToken();
		Optional<SecurityTokenXSecurityToken> exists = root.builder()
		                                                   .findLink(parent, child, classification.getEnterpriseID())
		                                                   .withClassification(classification)
		                                                   .inActiveRange(classification.getEnterpriseID())
		                                                   .inDateRange()
		                                                   .get();
		if (exists.isEmpty())
		{
			root.setParentSecurityTokenID(parent);
			root.setChildSecurityTokenID(child);
			root.setClassificationID(classification);
			root.setSystemID(classification.getSystemID());
			root.setOriginalSourceSystemID(classification.getSystemID());
			root.setValue(child.getSecurityToken());
			root.setEnterpriseID(classification.getEnterpriseID());
			root.setActiveFlagID(classification.getActiveFlagID());
			root.persist();
			updateSecurityHierarchy(child.getId());
		}
		else
		{
			root = exists.get();
		}
		return root;
	}

	private void updateSecurityHierarchy(Long securityTokenID)
	{
		javax.sql.DataSource ds = get(javax.sql.DataSource.class, ActivityMasterDB.class);

		try (java.sql.Connection c = ds.getConnection();
		     java.sql.CallableStatement st = c.prepareCall("{call MergeHierarchy (?)}");
		     java.sql.CallableStatement stPar = c.prepareCall("{call UpdateSecurityHierarchy (?)}")
		)
		{
			st.setLong(1, securityTokenID);
			st.execute();
			stPar.setLong(1, securityTokenID);
			stPar.execute();
		}
		catch (java.sql.SQLException e)
		{
			log.log(Level.SEVERE, "Unable to execute updates to hierarchy", e);
		}
	}

	@CacheResult(cacheName = "SecuritiesGetEveryoneGroup")
	@Override
	public SecurityToken getEveryoneGroup(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .findByName(Everyone)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                                //   .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetEverywhereGroup")
	@Override
	public SecurityToken getEverywhereGroup(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .findByName(Everywhere)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                             //      .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetGuestsFolder")
	@Override
	public SecurityToken getGuestsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .findByName(Guests)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                                   //     .canRead(enterprise,identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetRegisteredGuestsFolder")
	@Override
	public SecurityToken getRegisteredGuestsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .findByName(Registered)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                               //    .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetVisitorsFolder")
	@Override
	public SecurityToken getVisitorsGuestsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .findByName(Visitors)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                              //     .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetAdministratorsFolder")
	@Override
	public SecurityToken getAdministratorsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .findByName(Administrators)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                                 //  .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetSystemsFolder")
	@Override
	public SecurityToken getSystemsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroupSecurityTokenClassifications.System, enterprise, identityToken)
		                                   .findByName(System)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                                   //.canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetPluginsFolder")
	@Override
	public SecurityToken getPluginsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(Plugin, enterprise, identityToken)
		                                   .findByName(Plugins)
		                                   .inActiveRange(enterprise, identityToken)
		                                 //  .canRead(enterprise, identityToken)
		                                   .inDateRange()
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetApplicationsFolder")
	@Override
	public SecurityToken getApplicationsFolder(@CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(Application, enterprise, identityToken)
		                                   .findByName(Applications)
		                                   .inActiveRange(enterprise, identityToken)
		                                //   .canRead(enterprise, identityToken)
		                                   .inDateRange()
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecurityGetSecurityToken")
	@Override
	public SecurityToken getSecurityToken(@CacheKey UUID identifyingToken, @CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken view = new SecurityToken().builder()
		                                        .findBySecurityToken(identifyingToken.toString())
		                                        .withEnterprise(enterprise)
		                                        .inActiveRange(enterprise, identityToken)
		                                        .inDateRange()
		                                       // .canRead(enterprise, identityToken)
		                                        .get()
		                                        .orElse(null);
		return view;
	}

	@CacheResult(cacheName = "SecurityGetSecurityTokenNoActiveFlag")
	public SecurityToken getSecurityToken(@CacheKey UUID identifyingToken, boolean overrideActiveFlag, @CacheKey IEnterprise enterprise, @CacheKey UUID... identityToken)
	{
		SecurityTokenQueryBuilder builder = new SecurityToken().builder();
		builder = builder.findBySecurityToken(identifyingToken.toString())
		                 .withEnterprise(enterprise)
		                 .inDateRange();
		if (overrideActiveFlag)
		{
			builder.inActiveRange(enterprise, identityToken);
		}

		SecurityToken view = builder
				                     .get()
				                     .orElse(null);
		return view;
	}
}
