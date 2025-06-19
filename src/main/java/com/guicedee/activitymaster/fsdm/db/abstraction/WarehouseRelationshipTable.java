package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationship;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
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
public abstract class WarehouseRelationshipTable<
		P extends WarehouseBaseTable<P, ?, ?>,
		S extends WarehouseBaseTable<S, ?, ?>,
		J extends WarehouseRelationshipTable<P, S, J, Q, I,QS>,
		Q extends QueryBuilderRelationship<P, S, Q, J, I,?>,
		I extends java.util.UUID,
		QS extends WarehouseSecurityTable<QS,?,I>>
		extends WarehouseSCDTable<J, Q, I,QS>
		implements IWarehouseRelationshipTable<J, Q, P, S, I,QS>
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false,
	        name = "Value")
	private String value;
	
	
	@Override
	public Class<QS> findPersistentSecurityClass()
	{
		return (Class<QS>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[4];
	}
	
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
	public void setValue(String value)
	{
		this.value = Strings.nullToEmpty(value);
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
	
	@SuppressWarnings("unchecked")
	public @NotNull J update(String newValue, java.util.UUID... identifyingToken)
	{
		setActiveFlagID(com.guicedee.client.IGuiceContext.get(IActiveFlagService.class)
		                            .getDeletedFlag(getEnterpriseID(), identifyingToken));
		setEffectiveToDate(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		setWarehouseLastUpdatedTimestamp(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		update();
		
		setId(null);
		setValue(newValue);
		setEffectiveFromDate(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		setEffectiveToDate(EndOfTime.atOffset(java.time.ZoneOffset.UTC));
		setWarehouseCreatedTimestamp(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		setWarehouseLastUpdatedTimestamp(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		setActiveFlagID(com.guicedee.client.IGuiceContext.get(IActiveFlagService.class)
		                            .getActiveFlag(getEnterpriseID(), identifyingToken));
		persist();
		createDefaultSecurity(getSystemID());
		
		return (J) this;
	}
}
