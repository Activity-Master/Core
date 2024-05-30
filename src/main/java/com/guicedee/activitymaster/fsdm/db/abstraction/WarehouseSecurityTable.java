package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.systems.ActiveFlagSystem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serial;
import java.sql.Types;

import static com.guicedee.client.IGuiceContext.*;

/**
 * @author Marc Magon
 * @since 08 Dec 2016
 */
@SuppressWarnings("unchecked")
@MappedSuperclass

public abstract class WarehouseSecurityTable<J extends WarehouseSecurityTable<J, Q, I>,
		Q extends QueryBuilderSecurities<Q, J, I>,
		I extends java.lang.String
		>
		extends WarehouseSCDTable<J, Q, I, J>
		implements IWarehouseSecurityTable<J, Q, I>
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
	           fetch = FetchType.LAZY)
	private SecurityToken securityTokenID;
		/*
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
	*/
	
	//===========================================================================================================================
	
	
	public WarehouseSecurityTable()
	{
	
	}
	
	/**
	 * Marks the row as removed active flag, does not delete the record from the db
	 *
	 * @return
	 */
	@SuppressWarnings("UnusedReturnValue")
	public J remove()
	{
		setActiveFlagID(get(IActiveFlagService.class)
				.getDeletedFlag(getEnterpriseID(), get(ActiveFlagSystem.class)
						.getSystemToken(getEnterpriseID())));
		setEffectiveToDate(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		update();
		return (J) this;
	}
	
	@Override
	public @NotNull boolean isCreateAllowed()
	{
		return createAllowed;
	}
	
	@Override
	public J setCreateAllowed(@NotNull boolean createAllowed)
	{
		this.createAllowed = createAllowed;
		return (J) this;
	}
	
	@Override
	public @NotNull boolean isUpdateAllowed()
	{
		return updateAllowed;
	}
	
	@Override
	public J setUpdateAllowed(@NotNull boolean updateAllowed)
	{
		this.updateAllowed = updateAllowed;
		return (J) this;
	}
	
	@Override
	public @NotNull boolean isDeleteAllowed()
	{
		return deleteAllowed;
	}
	
	@Override
	public J setDeleteAllowed(@NotNull boolean deleteAllowed)
	{
		this.deleteAllowed = deleteAllowed;
		return (J) this;
	}
	
	@Override
	public @NotNull boolean isReadAllowed()
	{
		return readAllowed;
	}
	
	@Override
	public J setReadAllowed(@NotNull boolean readAllowed)
	{
		this.readAllowed = readAllowed;
		return (J) this;
	}
	
	@Override
	public SecurityToken getSecurityTokenID()
	{
		return securityTokenID;
	}
	
	@Override
	public J setSecurityTokenID(ISecurityToken<?, ?> securityTokenID)
	{
		this.securityTokenID = (SecurityToken) securityTokenID;
		return (J) this;
	}
	
	@Override
	public void configureSecurityEntity(J securityEntity)
	{
	
	}
	
	/*
	@Override
	public ActiveFlag getActiveFlagID()
	{
		return activeFlagID;
	}
	
	@Override
	public J setActiveFlagID(IActiveFlag<?, ?> activeFlagID)
	{
		this.activeFlagID = (ActiveFlag) activeFlagID;
		return (J) this;
	}
	
	@Override
	public Enterprise getEnterpriseID()
	{
		return enterpriseID;
	}
	
	@Override
	public J setEnterpriseID(IEnterprise<?, ?> enterpriseID)
	{
		this.enterpriseID = (Enterprise) enterpriseID;
		return (J) this;
	}
	
	@Override
	public Systems getSystemID()
	{
		return systemID;
	}
	
	@Override
	public J setSystemID(ISystems<?, ?> systemID)
	{
		this.systemID = (Systems) systemID;
		return (J) this;
	}
	
	@Override
	public @NotNull String getOriginalSourceSystemUniqueID()
	{
		return originalSourceSystemUniqueID;
	}
	
	@Override
	public J setOriginalSourceSystemUniqueID(@NotNull String originalSourceSystemUniqueID)
	{
		this.originalSourceSystemUniqueID = originalSourceSystemUniqueID;
		return (J) this;
	}
	
	@Override
	public Systems getOriginalSourceSystemID()
	{
		return originalSourceSystemID;
	}
	
	@Override
	public J setOriginalSourceSystemID(ISystems<?, ?> originalSourceSystemID)
	{
		this.originalSourceSystemID = (Systems) originalSourceSystemID;
		return (J) this;
	}*/
}
