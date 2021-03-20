package com.guicedee.activitymaster.core;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.IClassificationService;
import com.guicedee.activitymaster.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.security.*;
import com.guicedee.activitymaster.core.db.entities.security.builders.SecurityTokenQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.logging.Logger;

import static com.guicedee.activitymaster.client.services.classifications.SecurityTokenClassifications.*;
import static com.guicedee.activitymaster.client.services.classifications.UserGroupSecurityTokenClassifications.System;
import static com.guicedee.activitymaster.client.services.classifications.UserGroupSecurityTokenClassifications.*;

@SuppressWarnings("Duplicates")

public class SecurityTokenService
		implements ISecurityTokenService<SecurityTokenService>
{
	private static final Logger log = Logger.getLogger(SecurityTokenService.class.getName());
	
	@Inject
	@Named("Active")
	private IActiveFlag<?,?> activeFlag;
	
	@Inject
	private IEnterprise<?,?> enterprise;
	
	@Inject
	private IClassificationService<?> classificationService;
	
	@Override
	public void grantAccessToToken(ISecurityToken<?,?> fromToken, ISecurityToken<?,?> toToken,
	                               boolean create, boolean update, boolean delete, boolean read, ISystems<?,?> system)
	
	{
		grantAccessToToken(fromToken, toToken, create, update, delete, read, system, null, null, null);
	}
	
	@Override
	public void grantAccessToToken(@NotNull ISecurityToken<?,?> fromToken, @NotNull ISecurityToken<?,?> toToken,
	                               boolean create, boolean update, boolean delete, boolean read,
	                               ISystems<?,?> system, String originalId,
	                               Date effectiveFromDate, Date effectiveToDate)
	{
		SecurityTokensSecurityToken sta = new SecurityTokensSecurityToken();
		Optional<SecurityTokensSecurityToken> exists = sta.builder()
		                                                  .withEnterprise(enterprise)
		                                                  .findBySecurityToken((SecurityToken) fromToken, (SecurityToken) toToken)
		                                                  .inActiveRange(enterprise)
		                                                  .inDateRange()
		                                                  .get();
		if (exists.isEmpty())
		{
			sta.setSystemID((Systems) system);
			sta.setOriginalSourceSystemID((Systems) system);
			sta.setEnterpriseID((Enterprise) enterprise);
			sta.setActiveFlagID((ActiveFlag) activeFlag);
			sta.setSecurityTokenID((SecurityToken) fromToken);
			sta.setBase((SecurityToken) toToken);
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
	}
	
	@Override
	public ISecurityToken<?,?> create(String classificationValue, String name, String description, ISystems<?,?> system)
	{
		return create(classificationValue, name, description, system, null);
	}
	
	@Override
	public ISecurityToken<?,?> create(String classificationValue, String name, String description, ISystems<?,?> system, ISecurityToken<?,?> parent, UUID... identityToken)
	{
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .withEnterprise(enterprise)
		                                   .findBySecurityToken(name, enterprise)
		                                   .inActiveRange(enterprise)
		                                   .inDateRange()
		                                   .withEnterprise(enterprise)
		                                   .get();
		if (exists.isEmpty())
		{
			exists = st.builder()
			           .withName(name)
			           .inActiveRange(enterprise)
			           .inDateRange()
			           .get();
			
			if (exists.isEmpty())
			{
				st.setName(name);
				st.setDescription(description);
				st.setSystemID((Systems) system);
				st.setSecurityToken(UUID.randomUUID()
				                        .toString());
				st.setEnterpriseID((Enterprise) enterprise);
				st.setSystemID(classification.getSystemID());
				st.setActiveFlagID(activeFlag);
				st.setOriginalSourceSystemID(classification.getSystemID());
				st.setSecurityTokenClassificationID(classification);
				st.persist();
				st.createDefaultSecurity(system, identityToken);
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
	
	@Override
	public void link(ISecurityToken<?,?> parent, ISecurityToken<?,?> child, IClassification<?,?> classification, UUID... identifyingToken)
	{
		SecurityTokenXSecurityToken root = new SecurityTokenXSecurityToken();
		Optional<SecurityTokenXSecurityToken> exists = root.builder()
		                                                   .withEnterprise(enterprise)
		                                                   .findLink((SecurityToken) parent, (SecurityToken) child,null)
		                                                   .withClassification(classification)
		                                                   .inActiveRange(enterprise)
		                                                   .inDateRange()
		                                                   .get();
		if (exists.isEmpty())
		{
			root.setParentSecurityTokenID((SecurityToken) parent);
			root.setChildSecurityTokenID((SecurityToken) child);
			root.setClassificationID((Classification) classification);
			root.setSystemID(((SecurityToken) parent).getSystemID());
			root.setOriginalSourceSystemID(((SecurityToken) parent).getSystemID());
			root.setValue(child.getSecurityToken());
			root.setEnterpriseID((Enterprise) enterprise);
			root.setActiveFlagID(activeFlag);
			root.persist();
			updateSecurityHierarchy(child.getId());
		}
		else
		{
			root = exists.get();
		}
	}
	
	private void updateSecurityHierarchy(UUID securityTokenID)
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
	public ISecurityToken<?,?> getEveryoneGroup(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup.toString(), system, identityToken)
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
	public ISecurityToken<?,?> getEverywhereGroup(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup.toString(), system, identityToken)
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
	public ISecurityToken<?,?> getGuestsFolder(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup.toString(), system, identityToken)
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
	public ISecurityToken<?,?> getRegisteredGuestsFolder(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup.toString(), system, identityToken)
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
	public ISecurityToken<?,?> getVisitorsGuestsFolder(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup.toString(), system, identityToken)
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
	public ISecurityToken<?,?> getAdministratorsFolder(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup.toString(), system, identityToken)
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
	public ISecurityToken<?,?> getSystemsFolder(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroupSecurityTokenClassifications.System.toString(), system, identityToken)
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
	public ISecurityToken<?,?> getPluginsFolder(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(Plugin.toString(), system, identityToken)
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
	public ISecurityToken<?,?> getApplicationsFolder(@CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(Application.toString(), system, identityToken)
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
	public ISecurityToken<?,?> getSecurityToken(@CacheKey UUID identifyingToken, @CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
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
	public ISecurityToken<?,?> getSecurityToken(@CacheKey UUID identifyingToken, boolean overrideActiveFlag, @CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
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
