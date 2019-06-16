package com.armineasy.activitymaster.activitymaster.db.abstraction;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsActiveFlags;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.systems.ActiveFlagSystem;
import com.jwebmp.guicedinjection.GuiceContext;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.jwebmp.entityassist.querybuilder.EntityAssistStrings.*;

/**
 * @param <S>
 * @param <J>
 * @param <S>
 *
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@MappedSuperclass
@Accessors(chain = true)
@Getter
@Setter
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
		setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
		                            .getDeletedFlag(getEnterpriseID(), ActiveFlagSystem.getSystemTokens()
		                                                                                .get(getEnterpriseID())));
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return (J)this;
	}


	@SuppressWarnings("unchecked")
	public J archive()
	{
		setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
		                            .getArchivedFlag(getEnterpriseID(), ActiveFlagSystem.getSystemTokens()
		                                                                               .get(getEnterpriseID())));
		setEffectiveToDate(LocalDateTime.now());
		updateNow();
		return (J)this;
	}
}
