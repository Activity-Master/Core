package com.guicedee.activitymaster.core.db.abstraction;

import com.guicedee.activitymaster.core.*;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static com.guicedee.guicedinjection.GuiceContext.*;

/**
 * @param <S>
 * @param <J>
 *
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@SuppressWarnings({"Duplicates", "unchecked"})
@MappedSuperclass()
public abstract class WarehouseCoreTable<J extends WarehouseCoreTable<J, Q, I, S>,
		Q extends QueryBuilderCore<Q, J, I, S>,
		I extends Serializable,
		S extends WarehouseSecurityTable>
		extends WarehouseBaseTable<J, Q, I>
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	public WarehouseCoreTable()
	{
	
	}
	
	@SuppressWarnings("ConstantConditions")
	public void createDefaultSecurity(ISystems<?> system, UUID... identity)
	{
		createDefaultAdministratorSecurityAccess(system, identity);
		createDefaultEveryoneSecurityAccess(system, identity);
		createDefaultEverywhereSecurityAccess(system, identity);
		createDefaultSystemsSecurityAccess(system, identity);
		createDefaultApplicationsSecurityAccess(system, identity);
		createDefaultPluginsSecurityAccess(system, identity);
		createDefaultGuestReadSecurityAccess(system, identity);
	}
	
	public void updateSecurity(J newCoreTable, Systems system)
	{
		S stAdmin = get(findPersistentSecurityClass());
		@SuppressWarnings("rawtypes")
		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		List<S> exists = securities.findLinkedSecurityTokens(this)
		                           .inActiveRange(system.getEnterpriseID())
		                           .inDateRange()
		                           .getAll();
		List<S> persistNewTokens = new ArrayList<>();
		
		for (S exist : exists)
		{
			exist.setId(null);
			configureDefaultsForNewToken(exist, system, system);
		}
		
		createDefaultAdministratorSecurityAccess(system);
		createDefaultEveryoneSecurityAccess(system);
		createDefaultEverywhereSecurityAccess(system);
		createDefaultSystemsSecurityAccess(system);
		createDefaultApplicationsSecurityAccess(system);
		createDefaultPluginsSecurityAccess(system);
		createDefaultGuestReadSecurityAccess(system);
	}
	
	private S createDefaultAdministratorSecurityAccess(ISystems<?> system, UUID... identity)
	{
		S stAdmin = get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) GuiceContext.get(SecurityTokenService.class)
		                                                           .getAdministratorsFolder(system, identity);
		@SuppressWarnings("rawtypes")
		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
				          //.inActiveRange(enterprise)
				          .inDateRange()
				          .setReturnFirst(true)
				          .get();
		
		if (exists.isEmpty())
		{
			ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
			                                               .getActivityMaster(system, identity);
			stAdmin = configureDefaultsForNewToken(stAdmin, system, activityMasterSystem);
			stAdmin.setSecurityTokenID(administrators);
			stAdmin.setCreateAllowed(true);
			stAdmin.setUpdateAllowed(true);
			stAdmin.setDeleteAllowed(true);
			stAdmin.setReadAllowed(true);
			stAdmin.persist();
		}
		else
		{
			stAdmin = exists.get();
		}
		return stAdmin;
	}
	
	private S createDefaultEveryoneSecurityAccess(ISystems<?> system, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getEveryoneGroup(system, identity);
		
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(system, identity);
		@SuppressWarnings("rawtypes")
		QueryBuilderSecurities<?, ?, ?> securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = (Optional<S>) securities.findLinkedSecurityToken(administrators, this)
				                        //.inActiveRange(enterprise)
				                        .inDateRange()
				                        .setReturnFirst(true)
				                        .get();
		if (exists.isEmpty())
		{
			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, system, activityMasterSystem);
			stAdmin.setCreateAllowed(false);
			stAdmin.setUpdateAllowed(false);
			stAdmin.setDeleteAllowed(false);
			stAdmin.setReadAllowed(false);
			stAdmin.persist();
		}
		else
		{
			stAdmin = exists.get();
		}
		return stAdmin;
	}
	
	private S createDefaultEverywhereSecurityAccess(ISystems<?> system, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getEverywhereGroup(system, identity);
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(system, identity);
		@SuppressWarnings("rawtypes")
		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
				          //.inActiveRange(enterprise)
				          .inDateRange()
				          .setReturnFirst(true)
				          .get();
		if (exists.isEmpty())
		{
			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, system, activityMasterSystem);
			
			stAdmin.setCreateAllowed(false);
			stAdmin.setUpdateAllowed(false);
			stAdmin.setDeleteAllowed(false);
			stAdmin.setReadAllowed(true);
			stAdmin.persist();
		}
		else
		{
			stAdmin = exists.get();
		}
		
		return stAdmin;
	}
	
	private S createDefaultSystemsSecurityAccess(ISystems<?> system, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getSystemsFolder(system, identity);
		
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(system, identity);
		@SuppressWarnings("rawtypes")
		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
				          //.inActiveRange(enterprise)
				          .inDateRange()
				          .setReturnFirst(true)
				          .get();
		if (exists.isEmpty())
		{
			
			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, system, activityMasterSystem);
			
			stAdmin.setCreateAllowed(true);
			stAdmin.setUpdateAllowed(true);
			stAdmin.setDeleteAllowed(false);
			stAdmin.setReadAllowed(true);
			stAdmin.persist();
		}
		else
		{
			stAdmin = exists.get();
		}
		return stAdmin;
	}
	
	private S createDefaultApplicationsSecurityAccess(ISystems<?> system, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getApplicationsFolder(system, identity);
		
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(system, identity);
		@SuppressWarnings("rawtypes")
		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
				          //.inActiveRange(enterprise)
				          .inDateRange()
				          .setReturnFirst(true)
				          .get();
		if (exists.isEmpty())
		{
			
			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, system, activityMasterSystem);
			
			stAdmin.setCreateAllowed(true);
			stAdmin.setUpdateAllowed(true);
			stAdmin.setDeleteAllowed(false);
			stAdmin.setReadAllowed(true);
			stAdmin.persist();
		}
		else
		{
			stAdmin = exists.get();
		}
		return stAdmin;
	}
	
	private S createDefaultPluginsSecurityAccess(ISystems<?> system, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getPluginsFolder(system, identity);
		
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(system, identity);
		@SuppressWarnings("rawtypes")
		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
				          //.inActiveRange(enterprise)
				          .inDateRange()
				          .setReturnFirst(true)
				          .get();
		if (exists.isEmpty())
		{
			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, system, activityMasterSystem);
			
			stAdmin.setCreateAllowed(true);
			stAdmin.setUpdateAllowed(true);
			stAdmin.setDeleteAllowed(false);
			stAdmin.setReadAllowed(true);
			stAdmin.persist();
		}
		else
		{
			stAdmin = exists.get();
		}
		return stAdmin;
	}
	
	private S createDefaultGuestReadSecurityAccess(ISystems<?> system, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getGuestsFolder(system, identity);
		
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(system, identity);
		@SuppressWarnings("rawtypes")
		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
				          //.inActiveRange(enterprise)
				          .inDateRange()
				          .setReturnFirst(true)
				          .get();
		if (exists.isEmpty())
		{
			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, system, activityMasterSystem);
			
			stAdmin.setCreateAllowed(false);
			stAdmin.setUpdateAllowed(false);
			stAdmin.setDeleteAllowed(false);
			stAdmin.setReadAllowed(true);
			stAdmin.persist();
		}
		else
		{
			stAdmin = exists.get();
		}
		
		return stAdmin;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	protected Class<S> findPersistentSecurityClass()
	{
		return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
	}
	
	protected S configureDefaultsForNewToken(S stAdmin, ISystems<?> system, ISystems<?> activityMasterSystem)
	{
		stAdmin.setSystemID((Systems) activityMasterSystem);
		stAdmin.setActiveFlagID(((Systems) activityMasterSystem).getActiveFlagID());
		stAdmin.setOriginalSourceSystemID((Systems) activityMasterSystem);
		stAdmin.setOriginalSourceSystemUniqueID("");
		stAdmin.setEnterpriseID((Enterprise) system.getEnterprise());
		
		return stAdmin;
	}
	
	public S createDefaultGuestNoSecurityAccess(ISystems<?> system, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getGuestsFolder(system, identity);
		
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(system, identity);
		
		@SuppressWarnings("rawtypes")
		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = ActivityMasterConfiguration.get()
		                                                .isSecurityEnabled() ? Optional.empty() :
				securities.findLinkedSecurityToken(administrators, this)
				          //.inActiveRange(enterprise)
				          .inDateRange()
				          .get();
		if (exists.isEmpty())
		{
			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, system, activityMasterSystem);
			
			stAdmin.setCreateAllowed(false);
			stAdmin.setUpdateAllowed(false);
			stAdmin.setDeleteAllowed(false);
			stAdmin.setReadAllowed(false);
			stAdmin.persist();
		}
		else
		{
			stAdmin = exists.get();
		}
		
		return stAdmin;
	}
}
