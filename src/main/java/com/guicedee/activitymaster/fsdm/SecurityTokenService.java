package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.types.classifications.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.*;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokenQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;

import java.util.*;

import static com.guicedee.activitymaster.fsdm.client.types.classifications.SecurityTokenClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.UserGroupSecurityTokenClassifications.System;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.UserGroupSecurityTokenClassifications.*;

@SuppressWarnings("Duplicates")

public class SecurityTokenService
		implements ISecurityTokenService<SecurityTokenService>
{
	@Inject
	private IClassificationService<?> classificationService;
	
	@Inject
	@com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB
	private EntityManager entityManager;
	
	@Override
	public ISecurityToken<?, ?> get()
	{
		return new SecurityToken();
	}
	
	@Override
	public void grantAccessToToken(ISecurityToken<?,?> fromToken, ISecurityToken<?,?> toToken,
	                               boolean create, boolean update, boolean delete, boolean read, ISystems<?,?> system)
	
	{
		grantAccessToToken(fromToken, toToken, create, update, delete, read, system, null, null, null);
	}
	
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void grantAccessToToken(@NotNull ISecurityToken<?,?> fromToken, @NotNull ISecurityToken<?,?> toToken,
	                               boolean create, boolean update, boolean delete, boolean read,
	                               ISystems<?,?> system, String originalId,
	                               Date effectiveFromDate, Date effectiveToDate)
	{
		SecurityTokensSecurityToken sta = new SecurityTokensSecurityToken();
		Optional<SecurityTokensSecurityToken> exists = sta.builder(entityManager)
		                                                  .withEnterprise(system.getEnterpriseID())
		                                                  .findBySecurityToken((SecurityToken) fromToken, (SecurityToken) toToken)
		                                                  .inActiveRange()
		                                                  .inDateRange()
		                                                  .get();
		if (exists.isEmpty())
		{
			sta.setSystemID((Systems) system);
			sta.setOriginalSourceSystemID((Systems) system);
			sta.setEnterpriseID(system.getEnterpriseID());
			sta.setOriginalSourceSystemUniqueID("");
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
			sta.setActiveFlagID((ActiveFlag) activeFlag);
			sta.setSecurityTokenID((SecurityToken) fromToken);
			sta.setBase((SecurityToken) toToken);
			sta.setCreateAllowed(create);
			sta.setUpdateAllowed(update);
			sta.setDeleteAllowed(delete);
			sta.setReadAllowed(read);
			sta.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
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
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public ISecurityToken<?,?> create(String classificationValue, String name, String description, ISystems<?,?> system, ISecurityToken<?,?> parent, java.util.UUID... identityToken)
	{
		Classification classification = (Classification) classificationService.find(classificationValue, system, identityToken);
		
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder(entityManager)
		                                   .withEnterprise(system.getEnterpriseID())
		                                   .findBySecurityToken(name, system.getEnterpriseID())
		                                   .inActiveRange()
		                                   .inDateRange()
		                                   .withEnterprise(system.getEnterpriseID())
		                                   .get();
		if (exists.isEmpty())
		{
			exists = st.builder(entityManager)
			           .withName(name)
			           .inActiveRange()
			           .inDateRange()
			           .get();
			
			if (exists.isEmpty())
			{
				st.setName(name);
				st.setDescription(description);
				st.setSystemID(system);
				st.setSecurityToken(UUID.randomUUID()
				                        .toString());
				st.setEnterpriseID(system.getEnterpriseID());
				st.setSystemID(classification.getSystemID());
				IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
				IActiveFlag<?,?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
				st.setActiveFlagID(activeFlag);
				st.setOriginalSourceSystemID(classification.getSystemID());
				st.setSecurityTokenClassificationID(classification);
				st.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
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
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void link(ISecurityToken<?,?> parent, ISecurityToken<?,?> child, IClassification<?,?> classification, java.lang.String... identifyingToken)
	{
		SecurityTokenXSecurityToken root = new SecurityTokenXSecurityToken();
		Optional<SecurityTokenXSecurityToken> exists = root.builder(entityManager)
		                                                   .withEnterprise(parent.getEnterpriseID())
		                                                   .findLink((SecurityToken) parent, (SecurityToken) child,null)
		                                                   .withClassification(classification)
		                                                   .inActiveRange()
		                                                   .inDateRange()
		                                                   .get();
		if (exists.isEmpty())
		{
			root.setParentSecurityTokenID((SecurityToken) parent);
			root.setChildSecurityTokenID((SecurityToken) child);
			root.setClassificationID(classification);
			root.setSystemID(((SecurityToken) parent).getSystemID());
			root.setOriginalSourceSystemID(((SecurityToken) parent).getSystemID());
			root.setValue(child.getSecurityToken());
			root.setEnterpriseID(parent.getEnterpriseID());
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(parent.getEnterpriseID());
			root.setActiveFlagID(activeFlag);
			root.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
			updateSecurityHierarchy(child.getId());
		}
		else
		{
			root = exists.get();
		}
	}
	
	private void updateSecurityHierarchy(java.lang.String securityTokenID)
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
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SecuritiesGetEveryoneGroup")
	@Override
	public ISecurityToken<?,?> getEveryoneGroup(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder(entityManager)
		                                   .findFolder(UserGroup.toString(), system, identityToken)
		                                   .withName(Everyone)
		                                   .inActiveRange()
		                                   .inDateRange()
		                                   .withEnterprise(system.getEnterpriseID())
		                                   //   .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SecuritiesGetEverywhereGroup")
	@Override
	public ISecurityToken<?,?> getEverywhereGroup(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder(entityManager)
		                                   .findFolder(UserGroup.toString(), system, identityToken)
		                                   .withName(Everywhere)
		                                   .inActiveRange()
		                                   .inDateRange()
		                                   .withEnterprise(system.getEnterpriseID())
		                                   //      .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SecuritiesGetGuestsFolder")
	@Override
	public ISecurityToken<?,?> getGuestsFolder(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder(entityManager)
		                                   .findFolder(UserGroup.toString(), system, identityToken)
		                                   .withName(Guests)
		                                   .inActiveRange()
		                                   .inDateRange()
		                                   .withEnterprise(system.getEnterpriseID())
		                                   //     .canRead(enterprise,identityToken)
		                                   .get();
		return exists.orElseThrow();
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SecuritiesGetRegisteredGuestsFolder")
	@Override
	public ISecurityToken<?,?> getRegisteredGuestsFolder(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder(entityManager)
		                                   .findFolder(UserGroup.toString(), system, identityToken)
		                                   .withName(Registered)
		                                   .inActiveRange()
		                                   .inDateRange()
		                                   .withEnterprise(system.getEnterpriseID())
		                                   //    .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SecuritiesGetVisitorsFolder")
	@Override
	public ISecurityToken<?,?> getVisitorsGuestsFolder(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder(entityManager)
		                                   .findFolder(UserGroup.toString(), system, identityToken)
		                                   .withName(Visitors)
		                                   .inActiveRange()
		                                   .inDateRange()
		                                   .withEnterprise(system.getEnterpriseID())
		                                   //     .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SecuritiesGetAdministratorsFolder")
	@Override
	public ISecurityToken<?,?> getAdministratorsFolder(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder(entityManager)
		                                   .findFolder(UserGroup.toString(), system, identityToken)
		                                   .withName(Administrators)
		                                   .inActiveRange()
		                                   .inDateRange()
		                                   .withEnterprise(system.getEnterpriseID())
		                                   //  .canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SecuritiesGetSystemsFolder")
	@Override
	public ISecurityToken<?,?> getSystemsFolder(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder(entityManager)
		                                   .findFolder(UserGroupSecurityTokenClassifications.System.toString(), system, identityToken)
		                                   .withName(System)
		                                   .inActiveRange()
		                                   .inDateRange()
		                                   .withEnterprise(system.getEnterpriseID())
		                                   //.canRead(enterprise, identityToken)
		                                   .get();
		return exists.orElseThrow();
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SecuritiesGetPluginsFolder")
	@Override
	public ISecurityToken<?,?> getPluginsFolder(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder(entityManager)
		                                   .findFolder(Plugin.toString(), system, identityToken)
		                                   .withName(Plugins)
		                                   .inActiveRange()
		                                   //  .canRead(enterprise, identityToken)
		                                   .inDateRange()
		                                   .withEnterprise(system.getEnterpriseID())
		                                   .get();
		return exists.orElseThrow();
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SecuritiesGetApplicationsFolder")
	@Override
	public ISecurityToken<?,?> getApplicationsFolder(@CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder(entityManager)
		                                   .findFolder(Application.toString(), system, identityToken)
		                                   .withName(Applications)
		                                   .inActiveRange()
		                                   //   .canRead(enterprise, identityToken)
		                                   .inDateRange()
		                                   .withEnterprise(system.getEnterpriseID())
		                                   .get();
		return exists.orElseThrow();
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SecurityGetSecurityToken")
	@Override
	public ISecurityToken<?,?> getSecurityToken(@CacheKey UUID identifyingToken, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		SecurityToken view = new SecurityToken().builder(entityManager)
		                                        .findBySecurityToken(identifyingToken.toString())
		                                        .withEnterprise(system.getEnterpriseID())
		                                        .inActiveRange()
		                                        .inDateRange()
		                                        // .canRead(enterprise, identityToken)
		                                        .get()
		                                        .orElse(null);
		return view;
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "SecurityGetSecurityTokenNoActiveFlag")
	public ISecurityToken<?,?> getSecurityToken(@CacheKey UUID identifyingToken, boolean overrideActiveFlag, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		SecurityTokenQueryBuilder builder = new SecurityToken().builder(entityManager);
		builder = builder.findBySecurityToken(identifyingToken.toString())
		                 .withEnterprise(system.getEnterpriseID())
		                 .inDateRange();
		if (overrideActiveFlag)
		{
			builder.inActiveRange();
		}
		
		SecurityToken view = builder
				.get()
				.orElse(null);
		return view;
	}
}
