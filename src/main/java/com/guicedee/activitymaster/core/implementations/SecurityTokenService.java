package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXSecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokensSecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.builders.SecurityTokenQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.securitytokens.ISecurityTokenClassification;
import com.guicedee.activitymaster.core.services.classifications.securitytokens.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISecurityToken;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.ISecurityTokenService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.google.inject.Singleton;
import com.guicedee.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static com.guicedee.activitymaster.core.services.classifications.securitytokens.SecurityTokenClassifications.*;
import static com.guicedee.activitymaster.core.services.classifications.securitytokens.UserGroupSecurityTokenClassifications.System;
import static com.guicedee.activitymaster.core.services.classifications.securitytokens.UserGroupSecurityTokenClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")

public class SecurityTokenService
		implements ISecurityTokenService<SecurityTokenService>
{
	private static final Logger log = Logger.getLogger(SecurityTokenService.class.getName());

	public SecurityTokensSecurityToken grantAccessToToken(SecurityToken fromToken, SecurityToken toToken,
	                                                      boolean create, boolean update, boolean delete, boolean read, ISystems<?> system)
	{
		return grantAccessToToken(fromToken, toToken, create, update, delete, read, system, null, null, null);
	}

	public SecurityTokensSecurityToken grantAccessToToken(@NotNull SecurityToken fromToken, @NotNull SecurityToken toToken,
	                                                      boolean create, boolean update, boolean delete, boolean read,
	                                                      ISystems<?> system, String originalId,
	                                                      Date effectiveFromDate, Date effectiveToDate)
	{
		SecurityTokensSecurityToken sta = new SecurityTokensSecurityToken();
		Optional<SecurityTokensSecurityToken> exists = sta.builder()
		                                                  .withEnterprise(system.getEnterprise())
		                                                  .findBySecurityToken(fromToken, toToken)
		                                                  .inActiveRange(system.getEnterpriseID())
		                                                  .inDateRange()
		                                                  .get();
		if (exists.isEmpty())
		{
			sta.setSystemID((Systems) system);
			sta.setOriginalSourceSystemID((Systems) system);
			sta.setEnterpriseID((Enterprise) system.getEnterpriseID());
			sta.setActiveFlagID((ActiveFlag)GuiceContext.get(IActiveFlagService.class).getActiveFlag(sta.getEnterpriseID()));
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

	@Override
	public ISecurityToken<?> create(ISecurityTokenClassification<?> classificationValue, String name, String description, ISystems<?> system)
	{
		return create(classificationValue, name, description, system, null);
	}

	@Override
	public ISecurityToken<?> create(ISecurityTokenClassification<?> classificationValue, String name, String description, ISystems<?> system, ISecurityToken<?> parent, UUID... identityToken)
	{
		ClassificationService classificationService = get(ClassificationService.class);
		Classification classification = (Classification) classificationService.find(classificationValue, system.getEnterpriseID(), identityToken);

		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .withEnterprise(system.getEnterprise())
		                                   .findBySecurityToken(name, classification.getEnterpriseID())
		                                   .inActiveRange(classification.getEnterpriseID())
		                                   .inDateRange()
		                                   .withEnterprise(system.getEnterprise())
		                                   .get();
		if (exists.isEmpty())
		{
			exists = st.builder()
			           .withName(name)
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

		link((SecurityToken) parent, st, classification);

		return st;
	}

	public SecurityTokenXSecurityToken link(SecurityToken parent, SecurityToken child, IClassification<?> classification, UUID... identifyingToken)
	{
		SecurityTokenXSecurityToken root = new SecurityTokenXSecurityToken();
		Optional<SecurityTokenXSecurityToken> exists = root.builder()
		                                                   .withEnterprise(classification.getEnterprise())
		                                                   .findLink(parent, child)
		                                                   .withClassification(classification)
		                                                   .inActiveRange(classification.getEnterpriseID())
		                                                   .inDateRange()
		                                                   .get();
		if (exists.isEmpty())
		{
			root.setParentSecurityTokenID((SecurityToken) parent);
			root.setChildSecurityTokenID((SecurityToken) child);
			root.setClassificationID((Classification) classification);
			root.setSystemID(parent.getSystemID());
			root.setOriginalSourceSystemID(parent.getSystemID());
			root.setValue(child.getSecurityToken());
			root.setEnterpriseID(parent.getEnterpriseID());
			root.setActiveFlagID(parent.getActiveFlagID());
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

		//TODO hierarchy updates? i wonder
		/*javax.sql.DataSource ds = get(javax.sql.DataSource.class, ActivityMasterDB.class);

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
		}*/
	}

	@CacheResult(cacheName = "SecuritiesGetEveryoneGroup")
	@Override
	public ISecurityToken<?> getEveryoneGroup(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .withName(Everyone)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                                   .withEnterprise(enterprise)
		                                   //   .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetEverywhereGroup")
	@Override
	public ISecurityToken<?> getEverywhereGroup(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .withName(Everywhere)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                                   .withEnterprise(enterprise)
		                                   //      .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetGuestsFolder")
	@Override
	public ISecurityToken<?> getGuestsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .withName(Guests)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                                   .withEnterprise(enterprise)
		                                   //     .canRead(enterprise,identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetRegisteredGuestsFolder")
	@Override
	public ISecurityToken<?> getRegisteredGuestsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .withName(Registered)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                                   .withEnterprise(enterprise)
		                                   //    .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetVisitorsFolder")
	@Override
	public ISecurityToken<?> getVisitorsGuestsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .withName(Visitors)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                                   .withEnterprise(enterprise)
		                                   //     .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetAdministratorsFolder")
	@Override
	public ISecurityToken<?> getAdministratorsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise, identityToken)
		                                   .withName(Administrators)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                                   .withEnterprise(enterprise)
		                                   //  .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetSystemsFolder")
	@Override
	public ISecurityToken<?> getSystemsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroupSecurityTokenClassifications.System, enterprise, identityToken)
		                                   .withName(System)
		                                   .inActiveRange(enterprise, identityToken)
		                                   .inDateRange()
		                                   .withEnterprise(enterprise)
		                                   //.canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetPluginsFolder")
	@Override
	public ISecurityToken<?> getPluginsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(Plugin, enterprise, identityToken)
		                                   .withName(Plugins)
		                                   .inActiveRange(enterprise, identityToken)
		                                   //  .canRead(enterprise, identityToken)
		                                   .inDateRange()
		                                   .withEnterprise(enterprise)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetApplicationsFolder")
	@Override
	public ISecurityToken<?> getApplicationsFolder(@CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(Application, enterprise, identityToken)
		                                   .withName(Applications)
		                                   .inActiveRange(enterprise, identityToken)
		                                   //   .canRead(enterprise, identityToken)
		                                   .inDateRange()
		                                   .withEnterprise(enterprise)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecurityGetSecurityToken")
	@Override
	public ISecurityToken<?> getSecurityToken(@CacheKey UUID identifyingToken, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		SecurityToken view = new SecurityToken().builder()
		                                        .findBySecurityToken(identifyingToken.toString())
		                                        .withEnterprise(enterprise)
		                                        .inActiveRange(enterprise, identityToken)
		                                        .inDateRange()
		                                        .withEnterprise(enterprise)
		                                        // .canRead(enterprise, identityToken)
		                                        .get()
		                                        .orElse(null);
		return view;
	}

	@CacheResult(cacheName = "SecurityGetSecurityTokenNoActiveFlag")
	public ISecurityToken<?> getSecurityToken(@CacheKey UUID identifyingToken, boolean overrideActiveFlag, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
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
