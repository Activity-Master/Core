package com.armineasy.activitymaster.activitymaster.db.abstraction;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.jwebmp.guicedinjection.GuiceContext;
import lombok.experimental.Accessors;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @param <S>
 * @param <J>
 * @param <S>
 *
 * @author GedMarc
 * @version 1.0
 * @since 06 Dec 2016
 */
@MappedSuperclass
@Accessors(chain = true)
public abstract class WarehouseSCDTable<J extends WarehouseSCDTable<J, Q, I, S>, Q extends QueryBuilder<Q, J, I, S>,
		                                       I extends Serializable, S extends WarehouseSecurityTable>
		extends WarehouseCoreTable<J, Q, I, S>
{
	private static final long serialVersionUID = 1L;

	@JoinColumn(name = "ActiveFlagID",
			referencedColumnName = "ActiveFlagID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ActiveFlag activeFlagID;

	@JoinColumn(name = "EnterpriseID",
			referencedColumnName = "EnterpriseID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Enterprise enterpriseID;

	@JoinColumn(name = "SystemID",
			referencedColumnName = "SystemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Systems systemID;

	public WarehouseSCDTable(IEnterprise<?> enterprise)
	{
		setEnterpriseID((Enterprise) enterprise);
	}

	public WarehouseSCDTable()
	{
	}

	protected J configureDefaultsSystemValues(Systems requestingSystem)
	{
		setSystemID(requestingSystem);
		setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
		                            .getActiveFlag(requestingSystem.getEnterpriseID()));
		setEnterpriseID(requestingSystem.getEnterpriseID());
		return (J) this;
	}

	public ActiveFlag getActiveFlagID()
	{
		return this.activeFlagID;
	}

	public Enterprise getEnterpriseID()
	{
		return this.enterpriseID;
	}

	public Systems getSystemID()
	{
		return this.systemID;
	}

	public WarehouseSCDTable<J, Q, I, S> setActiveFlagID(ActiveFlag activeFlagID)
	{
		this.activeFlagID = activeFlagID;
		return this;
	}

	public WarehouseSCDTable<J, Q, I, S> setEnterpriseID(Enterprise enterpriseID)
	{
		this.enterpriseID = enterpriseID;
		return this;
	}

	public WarehouseSCDTable<J, Q, I, S> setSystemID(Systems systemID)
	{
		this.systemID = systemID;
		return this;
	}
}
