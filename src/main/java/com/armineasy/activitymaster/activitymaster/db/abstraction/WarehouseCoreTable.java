package com.armineasy.activitymaster.activitymaster.db.abstraction;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.SecurityTokenService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.jwebmp.guicedinjection.interfaces.JobService;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.jwebmp.guicedinjection.GuiceContext.*;

/**
 * @param <S>
 * @param <J>
 *
 * @author GedMarc
 * @version 1.0
 * @since 06 Dec 2016
 */
@SuppressWarnings({"Duplicates", "unchecked"})
@MappedSuperclass()
public abstract class WarehouseCoreTable<J extends WarehouseCoreTable<J, Q, I, S>,
		                                        Q extends QueryBuilder<Q, J, I, S>,
		                                        I extends Serializable,
		                                        S extends WarehouseSecurityTable>
		extends WarehouseBaseTable<J, Q, I>
{
	private static final long serialVersionUID = 1L;

	public WarehouseCoreTable()
	{

	}

	public void createDefaultSecurity(ISystems system, UUID... identity)
	{
		boolean async = ActivityMasterConfiguration.get()
		                                           .isAsyncEnabled();
		if (async)
		{
			JobService.getInstance()
			          .addJob("SecurityTokenStore",
			                  () -> createDefaultAdministratorSecurityAccess(system.getEnterpriseID(), identity));
		}
		else
		{
			createDefaultAdministratorSecurityAccess(system.getEnterpriseID(), identity);
		}

		if (async)
		{
			JobService.getInstance()
			          .addJob("SecurityTokenStore",
			                  () -> createDefaultEveryoneSecurityAccess(system.getEnterpriseID(), identity));
		}
		else
		{
			createDefaultEveryoneSecurityAccess(system.getEnterpriseID(), identity);
		}


		if (async)
		{
			JobService.getInstance()
			          .addJob("SecurityTokenStore",
			                  () -> createDefaultEverywhereSecurityAccess(system.getEnterpriseID(), identity));
		}
		else
		{
			createDefaultEverywhereSecurityAccess(system.getEnterpriseID(), identity);
		}

		if (async)
		{
			JobService.getInstance()
			          .addJob("SecurityTokenStore",
			                  () -> createDefaultSystemsSecurityAccess(system.getEnterpriseID(), identity));
		}
		else
		{
			createDefaultSystemsSecurityAccess(system.getEnterpriseID(), identity);
		}

		if (async)
		{
			JobService.getInstance()
			          .addJob("SecurityTokenStore",
			                  () -> createDefaultApplicationsSecurityAccess(system.getEnterpriseID(), identity));
		}
		else
		{
			createDefaultApplicationsSecurityAccess(system.getEnterpriseID(), identity);
		}

		if (async)
		{
			JobService.getInstance()
			          .addJob("SecurityTokenStore",
			                  () -> createDefaultPluginsSecurityAccess(system.getEnterpriseID(), identity));
		}
		else
		{
			createDefaultPluginsSecurityAccess(system.getEnterpriseID(), identity);
		}
		if (async)
		{
			JobService.getInstance()
			          .addJob("SecurityTokenStore",
			                  () -> createDefaultGuestReadSecurityAccess(system.getEnterpriseID(), identity));
		}
		else
		{
			createDefaultGuestReadSecurityAccess(system.getEnterpriseID(), identity);
		}
	}

	public void updateSecurity(J newCoreTable, Systems system)
	{
		S stAdmin = get(findPersistentSecurityClass());
		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		List<S> exists = securities.findLinkedSecurityTokens(this)
		                           .inActiveRange(system.getEnterpriseID())
		                           .inDateRange()
		                           .getAll();
		List<S> persistNewTokens = new ArrayList<>();

		for (S exist : exists)
		{
			exist.setId(null);
			configureDefaultsForNewToken(exist, system.getEnterpriseID(), system);
		}


		createDefaultAdministratorSecurityAccess(system.getEnterpriseID());
		createDefaultEveryoneSecurityAccess(system.getEnterpriseID());
		createDefaultEverywhereSecurityAccess(system.getEnterpriseID());
		createDefaultSystemsSecurityAccess(system.getEnterpriseID());
		createDefaultApplicationsSecurityAccess(system.getEnterpriseID());
		createDefaultPluginsSecurityAccess(system.getEnterpriseID());
		createDefaultGuestReadSecurityAccess(system.getEnterpriseID());
	}


	private S createDefaultAdministratorSecurityAccess(IEnterprise enterprise, UUID... identity)
	{
		S stAdmin = get(findPersistentSecurityClass());
		SecurityToken administrators = get(SecurityTokenService.class)
				                               .getAdministratorsFolder(enterprise, identity);
		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = ActivityMasterConfiguration.get()
		                                                .isDoubleCheckDisabled() ? Optional.empty() :
		                     securities.findLinkedSecurityToken(administrators, this)
		                               //.inActiveRange(enterprise)
		                               .inDateRange()
		                               .setReturnFirst(true)
		                               .get();

		if (exists.isEmpty())
		{
			Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
			                                           .getActivityMaster(enterprise);
			stAdmin = configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);
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

	private S createDefaultEveryoneSecurityAccess(IEnterprise enterprise, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = get(SecurityTokenService.class)
				                               .getEveryoneGroup(enterprise, identity);

		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		QueryBuilderSecurities<?, ?, ?> securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = ActivityMasterConfiguration.get()
		                                                .isDoubleCheckDisabled() ? Optional.empty() :
		                     (Optional<S>) securities.findLinkedSecurityToken(administrators, this)
		                                             //.inActiveRange(enterprise)
		                                             .inDateRange()
		                                             .setReturnFirst(true)
		                                             .get();
		if (exists.isEmpty())
		{
			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);
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

	private S createDefaultEverywhereSecurityAccess(IEnterprise enterprise, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = get(SecurityTokenService.class)
				                               .getEverywhereGroup(enterprise, identity);
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = ActivityMasterConfiguration.get()
		                                                .isDoubleCheckDisabled() ? Optional.empty() :
		                     securities.findLinkedSecurityToken(administrators, this)
		                               //.inActiveRange(enterprise)
		                               .inDateRange()
		                               .setReturnFirst(true)
		                               .get();
		if (exists.isEmpty())
		{
			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);

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

	private S createDefaultSystemsSecurityAccess(IEnterprise enterprise, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = get(SecurityTokenService.class)
				                               .getSystemsFolder(enterprise, identity);

		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = ActivityMasterConfiguration.get()
		                                                .isDoubleCheckDisabled() ? Optional.empty() :
		                     securities.findLinkedSecurityToken(administrators, this)
		                               //.inActiveRange(enterprise)
		                               .inDateRange()
		                               .setReturnFirst(true)
		                               .get();
		if (exists.isEmpty())
		{

			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);

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

	private S createDefaultApplicationsSecurityAccess(IEnterprise enterprise, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = get(SecurityTokenService.class)
				                               .getApplicationsFolder(enterprise, identity);

		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = ActivityMasterConfiguration.get()
		                                                .isDoubleCheckDisabled() ? Optional.empty() :
		                     securities.findLinkedSecurityToken(administrators, this)
		                               //.inActiveRange(enterprise)
		                               .inDateRange()
		                               .setReturnFirst(true)
		                               .get();
		if (exists.isEmpty())
		{

			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);

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

	private S createDefaultPluginsSecurityAccess(IEnterprise enterprise, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = get(SecurityTokenService.class)
				                               .getPluginsFolder(enterprise, identity);

		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = ActivityMasterConfiguration.get()
		                                                .isDoubleCheckDisabled() ? Optional.empty() :
		                     securities.findLinkedSecurityToken(administrators, this)
		                               //.inActiveRange(enterprise)
		                               .inDateRange()
		                               .setReturnFirst(true)
		                               .get();
		if (exists.isEmpty())
		{
			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);

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

	private S createDefaultGuestReadSecurityAccess(IEnterprise enterprise, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = get(SecurityTokenService.class)
				                               .getGuestsFolder(enterprise, identity);

		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();
		Optional<S> exists = ActivityMasterConfiguration.get()
		                                                .isDoubleCheckDisabled() ? Optional.empty() :
		                     securities.findLinkedSecurityToken(administrators, this)
		                               //.inActiveRange(enterprise)
		                               .inDateRange()
		                               .setReturnFirst(true)
		                               .get();
		if (exists.isEmpty())
		{
			stAdmin.setSecurityTokenID(administrators);
			stAdmin = configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);

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

	protected S configureDefaultsForNewToken(S stAdmin, IEnterprise enterprise, ISystems activityMasterSystem)
	{
		stAdmin.setSystemID((Systems) activityMasterSystem);
		stAdmin.setActiveFlagID(((Systems)activityMasterSystem).getActiveFlagID());
		stAdmin.setOriginalSourceSystemID((Systems) activityMasterSystem);
		stAdmin.setOriginalSourceSystemUniqueID("");
		stAdmin.setEnterpriseID((Enterprise) enterprise);

		return stAdmin;
	}

	public S createDefaultGuestNoSecurityAccess(Enterprise enterprise, UUID... identity)
	{
		S stAdmin = GuiceContext.get(findPersistentSecurityClass());
		SecurityToken administrators = get(SecurityTokenService.class)
				                               .getGuestsFolder(enterprise, identity);

		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

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
			stAdmin = configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem);

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
