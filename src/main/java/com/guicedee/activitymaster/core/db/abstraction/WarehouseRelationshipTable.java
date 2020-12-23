package com.guicedee.activitymaster.core.db.abstraction;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IRelationshipValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.guicedinjection.GuiceContext;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @param <S>
 * @param <J>
 * @param <P>
 * @param <Q>
 * @author Marc Magon
 * @version 1.0
 * @since 09 Dec 2016
 */
@MappedSuperclass
public abstract class WarehouseRelationshipTable<P extends WarehouseBaseTable,
		S extends WarehouseBaseTable,
		J extends WarehouseRelationshipTable<P, S, J, Q, I, ST, L, R>,
		Q extends QueryBuilderRelationship<P, S, Q, J, I, ST>,
		I extends Serializable,
		ST extends WarehouseSecurityTable,
		L, R>
		extends WarehouseTable<J, Q, I, ST>
		implements IRelationshipValue<L, R, J>
{
	
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "Value")
	private String value;
	
	public WarehouseRelationshipTable()
	{
		//No configuration needed
	}
	
	@Override
	public Integer getValueAsNumber()
	{
		return Integer.parseInt(value);
	}
	
	@Override
	public Long getValueAsLong()
	{
		return Long.parseLong(value);
	}
	
	@Override
	public Boolean getValueAsBoolean()
	{
		return Boolean.parseBoolean(value);
	}
	
	@Override
	public BigDecimal getValueAsBigDecimal()
	{
		return BigDecimal.valueOf(getValueAsDouble());
	}
	
	@Override
	public Double getValueAsDouble()
	{
		return Double.parseDouble(value);
	}
	
	public String getValue()
	{
		return value;
	}
	
	@Override
	public J setValue(String value)
	{
		this.value = Strings.nullToEmpty(value);
		return (J) this;
	}
	
	@Override
	@NotNull
	@SuppressWarnings("unchecked")
	protected Class<Q> getClassQueryBuilderClass()
	{
		return (Class<Q>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
	}
	
	/**
	 * Returns this classes associated id class type
	 *
	 * @return
	 */
	@Override
	@NotNull
	@SuppressWarnings("unchecked")
	public Class<I> getClassIDType()
	{
		return (Class<I>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[4];
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected @NotNull Class<ST> findPersistentSecurityClass()
	{
		return (Class<ST>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[5];
	}
	
	
	@SuppressWarnings("unchecked")
	public @NotNull J update(String newValue, UUID... identifyingToken)
	{
		setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                         .getDeletedFlag(getEnterpriseID(), identifyingToken));
		setEffectiveToDate(LocalDateTime.now());
		setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
		update();
		
		setId(null);
		setValue(newValue);
		setEffectiveFromDate(LocalDateTime.now());
		setEffectiveToDate(EndOfTime);
		setWarehouseCreatedTimestamp(LocalDateTime.now());
		setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
		setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
		                                         .getActiveFlag(getEnterpriseID(), identifyingToken));
		persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			createDefaultSecurity(getSystemID());
		}
		return (J) this;
	}
}
