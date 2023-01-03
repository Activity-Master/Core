package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.systems.ActiveFlagSystem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serial;
import java.sql.Types;

import static com.guicedee.guicedinjection.GuiceContext.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @since 08 Dec 2016
 */
@MappedSuperclass

public abstract class WarehouseSecurityTable<J extends WarehouseSecurityTable<J, Q, I>,
		Q extends QueryBuilderSecurities<Q, J, I>, I extends java.lang.String>
		extends WarehouseBaseTable<J, Q, I>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "CreateAllowed")
	@JdbcTypeCode(Types.INTEGER)
	private boolean createAllowed;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "UpdateAllowed")
	@JdbcTypeCode(Types.INTEGER)
	private boolean updateAllowed;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "DeleteAllowed")
	@JdbcTypeCode(Types.INTEGER)
	private boolean deleteAllowed;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "ReadAllowed")
	@JdbcTypeCode(Types.INTEGER)
	private boolean readAllowed;
	
	@JoinColumn(name = "SecurityTokenID",
	            referencedColumnName = "SecurityTokenID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = LAZY)
	private SecurityToken securityTokenID;
	
	@JoinColumn(name = "ActiveFlagID",
	            referencedColumnName = "ActiveFlagID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = LAZY)
	private ActiveFlag activeFlagID;
	
	@JoinColumn(name = "EnterpriseID",
	            referencedColumnName = "EnterpriseID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = LAZY)
	
	private Enterprise enterpriseID;
	
	@JoinColumn(name = "SystemID",
	            referencedColumnName = "SystemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = LAZY)
	
	private Systems systemID;
	
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "OriginalSourceSystemUniqueID")
	private String originalSourceSystemUniqueID;
	
	@JoinColumn(name = "OriginalSourceSystemID",
	            referencedColumnName = "SystemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = LAZY)
	private Systems originalSourceSystemID;
	
	
	//===========================================================================================================================
	
	
	public WarehouseSecurityTable()
	{
	
	}
	
	public J remove()
	{
		setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
				.getDeletedFlag(getEnterpriseID(), get(ActiveFlagSystem.class)
						.getSystemToken(getEnterpriseID())));
		setEffectiveToDate(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		update(ActivityMasterConfiguration.entityManager().get());
		return (J) this;
	}
	
	public @NotNull boolean isCreateAllowed()
	{
		return createAllowed;
	}
	
	public WarehouseSecurityTable<J, Q, I> setCreateAllowed(@NotNull boolean createAllowed)
	{
		this.createAllowed = createAllowed;
		return this;
	}
	
	public @NotNull boolean isUpdateAllowed()
	{
		return updateAllowed;
	}
	
	public WarehouseSecurityTable<J, Q, I> setUpdateAllowed(@NotNull boolean updateAllowed)
	{
		this.updateAllowed = updateAllowed;
		return this;
	}
	
	public @NotNull boolean isDeleteAllowed()
	{
		return deleteAllowed;
	}
	
	public WarehouseSecurityTable<J, Q, I> setDeleteAllowed(@NotNull boolean deleteAllowed)
	{
		this.deleteAllowed = deleteAllowed;
		return this;
	}
	
	public @NotNull boolean isReadAllowed()
	{
		return readAllowed;
	}
	
	public WarehouseSecurityTable<J, Q, I> setReadAllowed(@NotNull boolean readAllowed)
	{
		this.readAllowed = readAllowed;
		return this;
	}
	
	public SecurityToken getSecurityTokenID()
	{
		return securityTokenID;
	}
	
	public WarehouseSecurityTable<J, Q, I> setSecurityTokenID(SecurityToken securityTokenID)
	{
		this.securityTokenID = securityTokenID;
		return this;
	}
	
	public ActiveFlag getActiveFlagID()
	{
		return activeFlagID;
	}
	
	public WarehouseSecurityTable<J, Q, I> setActiveFlagID(ActiveFlag activeFlagID)
	{
		this.activeFlagID = activeFlagID;
		return this;
	}
	
	public Enterprise getEnterpriseID()
	{
		return enterpriseID;
	}
	
	public WarehouseSecurityTable<J, Q, I> setEnterpriseID(IEnterprise<?, ?> enterpriseID)
	{
		this.enterpriseID = (Enterprise) enterpriseID;
		return this;
	}
	
	public Systems getSystemID()
	{
		return systemID;
	}
	
	public WarehouseSecurityTable<J, Q, I> setSystemID(Systems systemID)
	{
		this.systemID = systemID;
		return this;
	}
	
	public @NotNull String getOriginalSourceSystemUniqueID()
	{
		return originalSourceSystemUniqueID;
	}
	
	public WarehouseSecurityTable<J, Q, I> setOriginalSourceSystemUniqueID(@NotNull String originalSourceSystemUniqueID)
	{
		this.originalSourceSystemUniqueID = originalSourceSystemUniqueID;
		return this;
	}
	
	public Systems getOriginalSourceSystemID()
	{
		return originalSourceSystemID;
	}
	
	public WarehouseSecurityTable<J, Q, I> setOriginalSourceSystemID(Systems originalSourceSystemID)
	{
		this.originalSourceSystemID = originalSourceSystemID;
		return this;
	}
}
