package com.guicedee.activitymaster.core.db.abstraction;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilder;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.systems.ActiveFlagSystem;
import com.guicedee.guicedinjection.GuiceContext;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

/**
 * @param <S>
 * @param <J>
 * @param <S>
 *
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@MappedSuperclass

public abstract class WarehouseTable<J extends WarehouseTable<J, Q, I, S>,
		                                    Q extends QueryBuilder<Q, J, I, S>,
		                                    I extends Serializable,
		                                    S extends WarehouseSecurityTable>
		extends WarehouseSCDTable<J, Q, I, S>
		implements IContainsActiveFlags<J>
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
			name = "OriginalSourceSystemUniqueID")
	private String originalSourceSystemUniqueID = STRING_EMPTY;

	@JoinColumn(name = "OriginalSourceSystemID",
			referencedColumnName = "SystemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Systems originalSourceSystemID;

	public WarehouseTable()
	{

	}

	@Override
	@NotNull
	@SuppressWarnings("unchecked")
	protected J configureDefaultsSystemValues(Systems requestingSystem)
	{
		super.configureDefaultsSystemValues(requestingSystem);
		setOriginalSourceSystemID(requestingSystem);
		setOriginalSourceSystemUniqueID("");
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	public J remove()
	{
		setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                         .getDeletedFlag(getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(getEnterpriseID())));
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return (J) this;
	}


	@SuppressWarnings("unchecked")
	public J archive()
	{
		setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                         .getArchivedFlag(getEnterpriseID(), get(ActiveFlagSystem.class)
				                                                                             .getSystemToken(getEnterpriseID())));
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return (J) this;
	}

	public @NotNull String getOriginalSourceSystemUniqueID()
	{
		return this.originalSourceSystemUniqueID;
	}

	public WarehouseTable<J, Q, I, S> setOriginalSourceSystemUniqueID(@NotNull String originalSourceSystemUniqueID)
	{
		this.originalSourceSystemUniqueID = originalSourceSystemUniqueID;
		return this;
	}

	public Systems getOriginalSourceSystemID()
	{
		return this.originalSourceSystemID;
	}

	public WarehouseTable<J, Q, I, S> setOriginalSourceSystemID(Systems originalSourceSystemID)
	{
		this.originalSourceSystemID = originalSourceSystemID;
		return this;
	}
}
