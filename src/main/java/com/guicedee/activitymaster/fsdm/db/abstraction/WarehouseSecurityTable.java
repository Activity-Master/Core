package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.systems.ActiveFlagSystem;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Types;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.convertToUTCDateTime;

import static com.guicedee.client.IGuiceContext.*;

/**
 * @author Marc Magon
 * @since 08 Dec 2016
 */
@SuppressWarnings("unchecked")
@MappedSuperclass

public abstract class WarehouseSecurityTable<J extends WarehouseSecurityTable<J, Q, I>,
		Q extends QueryBuilderSecurities<Q, J, I>,
		I extends java.util.UUID>
		extends WarehouseSCDTable<J, Q, I, J>
		implements IWarehouseSecurityTable<J, Q, I>
{

	@Serial
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "CreateAllowed")
	private boolean createAllowed;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "UpdateAllowed")
	private boolean updateAllowed;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "DeleteAllowed")
	private boolean deleteAllowed;
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "ReadAllowed")
	private boolean readAllowed;

	@JoinColumn(name = "SecurityTokenID",
	            referencedColumnName = "SecurityTokenID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private SecurityToken securityTokenID;

	//===========================================================================================================================


	public WarehouseSecurityTable()
	{

	}

	/**
	 * Marks the row as removed active flag, does not delete the record from the db.
	 * This method performs actions and returns a Uni that completes when the removal is done.
	 * It returns the result of calling update().
	 *
	 * @return A Uni that completes when the removal is done
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Uni<J> remove()
	{
		// Get the enterprise ID
		IEnterprise<?,?> enterprise = getEnterpriseID();

		// Get the system token
		ActiveFlagSystem activeSystem = get(ActiveFlagSystem.class);
		UUID systemToken = activeSystem.getSystemToken(enterprise);

		// Set the active flag to deleted
		setActiveFlagID(get(IActiveFlagService.class).getDeletedFlag(enterprise, systemToken));

		// Set the effective to date to now
		setEffectiveToDate(convertToUTCDateTime(com.entityassist.RootEntity.getNow()));

		// Update the entity and return the result
		return update();
	}

	/**
	 * Checks if create operations are allowed.
	 * This method is non-reactive as it simply returns a property value.
	 * 
	 * @return True if create operations are allowed, false otherwise
	 */
	@Override
	public @NotNull boolean isCreateAllowed()
	{
		return createAllowed;
	}

	/**
	 * Sets whether create operations are allowed.
	 * This method is non-reactive as it simply sets a property value.
	 * 
	 * @param createAllowed True to allow create operations, false to disallow
	 * @return This instance for method chaining
	 */
	@Override
	@SuppressWarnings("unchecked")
	public J setCreateAllowed(@NotNull boolean createAllowed)
	{
		this.createAllowed = createAllowed;
		return (J) this;
	}

	/**
	 * Checks if update operations are allowed.
	 * This method is non-reactive as it simply returns a property value.
	 * 
	 * @return True if update operations are allowed, false otherwise
	 */
	@Override
	public @NotNull boolean isUpdateAllowed()
	{
		return updateAllowed;
	}

	/**
	 * Sets whether update operations are allowed.
	 * This method is non-reactive as it simply sets a property value.
	 * 
	 * @param updateAllowed True to allow update operations, false to disallow
	 * @return This instance for method chaining
	 */
	@Override
	@SuppressWarnings("unchecked")
	public J setUpdateAllowed(@NotNull boolean updateAllowed)
	{
		this.updateAllowed = updateAllowed;
		return (J) this;
	}

	/**
	 * Checks if delete operations are allowed.
	 * This method is non-reactive as it simply returns a property value.
	 * 
	 * @return True if delete operations are allowed, false otherwise
	 */
	@Override
	public @NotNull boolean isDeleteAllowed()
	{
		return deleteAllowed;
	}

	/**
	 * Sets whether delete operations are allowed.
	 * This method is non-reactive as it simply sets a property value.
	 * 
	 * @param deleteAllowed True to allow delete operations, false to disallow
	 * @return This instance for method chaining
	 */
	@Override
	@SuppressWarnings("unchecked")
	public J setDeleteAllowed(@NotNull boolean deleteAllowed)
	{
		this.deleteAllowed = deleteAllowed;
		return (J) this;
	}

	/**
	 * Checks if read operations are allowed.
	 * This method is non-reactive as it simply returns a property value.
	 * 
	 * @return True if read operations are allowed, false otherwise
	 */
	@Override
	public @NotNull boolean isReadAllowed()
	{
		return readAllowed;
	}

	/**
	 * Sets whether read operations are allowed.
	 * This method is non-reactive as it simply sets a property value.
	 * 
	 * @param readAllowed True to allow read operations, false to disallow
	 * @return This instance for method chaining
	 */
	@Override
	@SuppressWarnings("unchecked")
	public J setReadAllowed(@NotNull boolean readAllowed)
	{
		this.readAllowed = readAllowed;
		return (J) this;
	}

	/**
	 * Gets the security token associated with this entity.
	 * This method is non-reactive as it simply returns a property value.
	 * 
	 * @return The security token
	 */
	@Override
	public ISecurityToken<?, ?> getSecurityTokenID()
	{
		return securityTokenID;
	}

	/**
	 * Sets the security token for this entity.
	 * This method is non-reactive as it simply sets a property value.
	 * 
	 * @param securityTokenID The security token to set
	 * @return This instance for method chaining
	 */
	@Override
	@SuppressWarnings("unchecked")
	public J setSecurityTokenID(ISecurityToken<?, ?> securityTokenID)
	{
		this.securityTokenID = (SecurityToken) securityTokenID;
		return (J) this;
	}

	@Override
	public void configureSecurityEntity(J securityEntity)
	{

	}

}
