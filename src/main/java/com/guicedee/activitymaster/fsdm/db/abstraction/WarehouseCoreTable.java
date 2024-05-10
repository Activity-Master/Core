package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.SecurityTokenService;
import com.guicedee.activitymaster.fsdm.SystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static com.guicedee.client.IGuiceContext.*;


/**
 * @param <J>
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@SuppressWarnings({"Duplicates", "unchecked"})
@MappedSuperclass()
public abstract class WarehouseCoreTable<J extends WarehouseCoreTable<J, Q, I,S>,
		Q extends QueryBuilderCore<Q, J, I>,
		I extends java.lang.String,
		S extends IWarehouseSecurityTable<S,?>
		>
		extends WarehouseBaseTable<J, Q, I>
		implements IWarehouseCoreTable<J, Q, I>
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	public WarehouseCoreTable()
	{
	
	}
	
	public void createDefaultSecurity(ISystems<?, ?> system, java.util.UUID... identity)
	{
		if (ActivityMasterConfiguration.get()
		                               .isSecurityEnabled())
		{
			createDefaultAdministratorSecurityAccess(system, identity);
			createDefaultEveryoneSecurityAccess(system, identity);
			createDefaultEverywhereSecurityAccess(system, identity);
			createDefaultSystemsSecurityAccess(system, identity);
			createDefaultApplicationsSecurityAccess(system, identity);
			createDefaultPluginsSecurityAccess(system, identity);
			createDefaultGuestReadSecurityAccess(system, identity);
		}
	}
	public void updateSecurity(J newCoreTable, Systems system)
	{
		S stAdmin = get(findPersistentSecurityClass());
		@SuppressWarnings("rawtypes")
		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		List<S> exists = securities.findLinkedSecurityTokens(this)
		                           //  .inActiveRange()
		                           .inDateRange()
		                           .getAll();
		List<S> persistNewTokens = new ArrayList<>();
		for (S exist : exists)
		{
			//audit security change
			exist.setId(null);
			
			configureDefaultsForNewToken(exist, system, system);
		}
	}
	
	private S createDefaultAdministratorSecurityAccess(ISystems<?,?> system, java.util.UUID... identity)
	{
		S stAdmin = get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
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
			ISystems<?,?> activityMasterSystem = get(SystemsService.class)
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
	
	private S createDefaultEveryoneSecurityAccess(ISystems<?,?> system, java.util.UUID... identity)
	{
		S stAdmin = get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getEveryoneGroup(system, identity);
		
		ISystems<?,?> activityMasterSystem = get(SystemsService.class)
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
	
	private S createDefaultEverywhereSecurityAccess(ISystems<?,?> system, java.util.UUID... identity)
	{
		S stAdmin = get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getEverywhereGroup(system, identity);
		ISystems<?,?> activityMasterSystem = get(SystemsService.class)
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
	
	private S createDefaultSystemsSecurityAccess(ISystems<?,?> system, java.util.UUID... identity)
	{
		S stAdmin = get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getSystemsFolder(system, identity);
		
		ISystems<?,?> activityMasterSystem = get(SystemsService.class)
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
	
	private S createDefaultApplicationsSecurityAccess(ISystems<?,?> system, java.util.UUID... identity)
	{
		S stAdmin = get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getApplicationsFolder(system, identity);
		
		ISystems<?,?> activityMasterSystem = get(SystemsService.class)
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
	
	private S createDefaultPluginsSecurityAccess(ISystems<?,?> system, java.util.UUID... identity)
	{
		S stAdmin = get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getPluginsFolder(system, identity);
		
		ISystems<?,?> activityMasterSystem = get(SystemsService.class)
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
	
	private S createDefaultGuestReadSecurityAccess(ISystems<?,?> system, java.util.UUID... identity)
	{
		S stAdmin = get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getGuestsFolder(system, identity);
		
		ISystems<?,?> activityMasterSystem = get(SystemsService.class)
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
	
	protected S configureDefaultsForNewToken(S stAdmin, ISystems<?,?> system, ISystems<?,?> activityMasterSystem)
	{
		stAdmin.setSystemID((Systems) activityMasterSystem);
		stAdmin.setActiveFlagID(((Systems) activityMasterSystem).getActiveFlagID());
		stAdmin.setOriginalSourceSystemID((Systems) activityMasterSystem);
		stAdmin.setOriginalSourceSystemUniqueID("");
		stAdmin.setEnterpriseID((Enterprise) system.getEnterprise());
		
		return stAdmin;
	}
	
	public S createDefaultGuestNoSecurityAccess(ISystems<?,?> system, java.util.UUID... identity)
	{
		S stAdmin = get(findPersistentSecurityClass());
		SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
				.getGuestsFolder(system, identity);
		
		ISystems<?,?> activityMasterSystem = get(SystemsService.class)
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
