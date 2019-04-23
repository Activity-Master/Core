package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.*;
import com.armineasy.activitymaster.activitymaster.db.entities.security.builders.SecurityTokenQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.SecurityHierarchyView;
import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.ISecurityTokenClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.UserGroupSecurityTokenClassifications;
import com.armineasy.activitymaster.activitymaster.services.system.ISecurityTokenService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;
import org.hibernate.annotations.Cache;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.SecurityTokenClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.UserGroupSecurityTokenClassifications.*;
import static com.jwebmp.entityassist.enumerations.Operand.*;

@SuppressWarnings("Duplicates")
@Singleton
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
		Optional<SecurityTokensSecurityToken> exists = sta.builder()
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

	public SecurityToken create(ISecurityTokenClassification<?> classificationValue, String name, String description, Systems system)
	{
		return create(classificationValue, name, description, system, null);
	}

	public SecurityToken create(ISecurityTokenClassification<?> classificationValue, String name, String description, Systems system, SecurityToken parent, UUID... identityToken)
	{
		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		Classification classification = classificationService.find(classificationValue, system.getEnterpriseID(), identityToken);

		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
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
				st.setSystemID(system);
				st.setSecurityToken(UUID.randomUUID()
				                        .toString());
				st.setEnterpriseID(classification.getEnterpriseID());
				st.setSystemID(classification.getSystemID());
				st.setActiveFlagID(classification.getActiveFlagID());
				st.setOriginalSourceSystemID(classification.getSystemID());
				st.setSecurityTokenClassificationID(classification);
				st.persist();
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{
					st.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
					                                     .getActivityMaster(st.getEnterpriseID(), identityToken),identityToken);
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

	public SecurityTokenXSecurityToken link(SecurityToken parent, SecurityToken child, Classification classification)
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
		}
		else
		{
			root = exists.get();
		}
		return root;
	}

	public void updateSecurityHierarchy()
	{
		javax.sql.DataSource ds = GuiceContext.get(javax.sql.DataSource.class, com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB.class);
		try(java.sql.Connection c = ds.getConnection(); java.sql.CallableStatement  st = c.prepareCall("{call MergeHierarchy}"))
		{
			st.execute();
		}
		catch (java.sql.SQLException e)
		{
			e.printStackTrace();
		}
	}

	@CacheResult(cacheName = "SecuritiesGetEveryoneGroup")
	public SecurityToken getEveryoneGroup(@CacheKey Enterprise enterprise,@CacheKey UUID...identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup, enterprise,identityToken)
		                                   .findByName(Everyone)
		                                   .inActiveRange(enterprise,identityToken)
		                                   .inDateRange()
		                                   .canRead(enterprise,identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetEverywhereGroup")
	public SecurityToken getEverywhereGroup(@CacheKey Enterprise enterprise,@CacheKey UUID...identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup,enterprise, identityToken)
		                                   .findByName(Everywhere)
		                                   .inActiveRange(enterprise,identityToken)
		                                   .inDateRange()
		                                   .canRead(enterprise,identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetGuestsFolder")
	public SecurityToken getGuestsFolder(@CacheKey Enterprise enterprise,@CacheKey UUID...identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup,enterprise, identityToken)
		                                   .findByName(Guests)
		                                   .inActiveRange(enterprise,identityToken)
		                                   .inDateRange()
		                              //     .canRead(enterprise,identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetRegisteredGuestsFolder")
	public SecurityToken getRegisteredGuestsFolder(@CacheKey Enterprise enterprise,@CacheKey UUID...identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup,enterprise, identityToken)
		                                   .findByName(Registered)
		                                   .inActiveRange(enterprise,identityToken)
		                                   .inDateRange()
		                                   .canRead(enterprise,identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetVisitorsFolder")
	public SecurityToken getVisitorsGuestsFolder(@CacheKey Enterprise enterprise,@CacheKey UUID...identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup,enterprise, identityToken)
		                                   .findByName(Visitors)
		                                   .inActiveRange(enterprise,identityToken)
		                                   .inDateRange()
		                                   .canRead(enterprise,identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetAdministratorsFolder")
	public SecurityToken getAdministratorsFolder(@CacheKey Enterprise enterprise,@CacheKey UUID...identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroup,enterprise, identityToken)
		                                   .findByName(Administrators)
		                                   .inActiveRange(enterprise,identityToken)
		                                   .inDateRange()
		                                   .canRead(enterprise,identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetSystemsFolder")
	public SecurityToken getSystemsFolder(@CacheKey Enterprise enterprise,@CacheKey UUID...identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(UserGroupSecurityTokenClassifications.System,enterprise, identityToken)
		                                   .findByName(System)
		                                   .inActiveRange(enterprise,identityToken)
		                                   .inDateRange()
		                                   .canRead(enterprise,identityToken)
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetPluginsFolder")
	public SecurityToken getPluginsFolder(@CacheKey Enterprise enterprise,@CacheKey UUID...identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(Plugin,enterprise, identityToken)
		                                   .findByName(Plugins)
		                                   .inActiveRange(enterprise,identityToken)
		                                   .canRead(enterprise,identityToken)
		                                   .inDateRange()
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecuritiesGetApplicationsFolder")
	public SecurityToken getApplicationsFolder(@CacheKey Enterprise enterprise,@CacheKey UUID...identityToken)
	{
		SecurityToken st = new SecurityToken();
		Optional<SecurityToken> exists = st.builder()
		                                   .findFolder(Application,enterprise, identityToken)
		                                   .findByName(Applications)
		                                   .inActiveRange(enterprise,identityToken)
		                                   .canRead(enterprise,identityToken)
		                                   .inDateRange()
		                                   .get();
		return exists.orElseThrow();
	}

	@CacheResult(cacheName = "SecurityGetSecurityToken")
	@Override
	public SecurityToken getSecurityToken(@CacheKey UUID identifyingToken, @CacheKey Enterprise enterprise,@CacheKey UUID...identityToken)
	{
		SecurityToken view = new SecurityToken().builder()
		                                        .findBySecurityToken(identifyingToken.toString())
		                                        .withEnterprise(enterprise)
		                                        .inActiveRange(enterprise,identityToken)
		                                        .inDateRange()
		                                        .canRead(enterprise,identityToken)
		                                        .get()
		                                        .orElse(null);
		return view;
	}

	@CacheResult(cacheName = "SecurityGetSecurityTokenNoActiveFlag")
	public SecurityToken getSecurityToken(@CacheKey UUID identifyingToken, boolean overrideActiveFlag, @CacheKey Enterprise enterprise,@CacheKey UUID...identityToken)
	{
		SecurityTokenQueryBuilder builder = new SecurityToken().builder();
		builder = builder.findBySecurityToken(identifyingToken.toString())
		                 .withEnterprise(enterprise)
		                 .inDateRange();
		if(overrideActiveFlag)
		builder.inActiveRange(enterprise,identityToken);

		SecurityToken view = builder
				                     .get()
				                     .orElse(null);
		return view;
	}

/*
	@CacheResult(cacheName = "SecurityIdentityHierarchyForToken")
	public SecurityHierarchyView getSecurityIdentities(@CacheKey SecurityToken identifyingToken)
	{
		SecurityHierarchyView view = new SecurityHierarchyView().builder()
		                                                        .findMyHierarchy(identifyingToken.getId())
		                                                        .get()
		                                                        .orElse(null);
		return view;
	}*/
/*

	@CacheResult(cacheName = "SecurityTokenHierarchy")
	public List<SecurityToken> findSecurityTokensOrdered(@CacheKey SecurityToken myToken)
	{
		SecurityHierarchyView securityHierarchyView = GuiceContext.get(SecurityTokenService.class)
		                                                          .getSecurityIdentities(myToken);

		String path = securityHierarchyView.getPath();
		List<Long> ids = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(path, "/");
		while (st.hasMoreElements())
		{
			String id = st.nextToken();
			ids.add(Long.parseLong(id));
		}
		List<SecurityToken> tokens = new ArrayList<>();
		for (int i = ids.size() - 1; i >= 0; i--)
		{
			Long aLong = ids.get(i);
			SecurityToken token = findById(aLong, myToken.getEnterpriseID());
			tokens.add(token);
		}
		return tokens;
	}
*/
//
//	@CacheResult(cacheName = "SecurityTokenFindByID")
//	SecurityToken findById(@CacheKey Long id, @CacheKey Enterprise enterprise, @CacheKey UUID... identityToken)
//	{
//		return new SecurityToken().builder()
//		                          .where(SecurityToken_.id, Equals, id)
//		                          .inActiveRange(enterprise)
//		                          .inDateRange()
//		                          //This is a find by ID (id is known, so don't apply security)
//		                          // .canRead(enterprise,identityToken)
//		                          .get()
//		                          .get();
//	}
}
