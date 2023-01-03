package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;


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
public abstract class WarehouseRelationshipTable<P extends WarehouseBaseTable<P, ?, java.lang.String>,
		S extends WarehouseBaseTable<S, ?, java.lang.String>,
		J extends WarehouseRelationshipTable<P, S, J, Q, I>,
		Q extends QueryBuilderRelationship<P, S, Q, J, I>,
		I extends java.lang.String>
		extends WarehouseTable<J, Q, I>
		implements IWarehouseRelationshipTable<J, Q, P, S, I>
{
	
	@Serial
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
	public void createDefaultSecurity(ISystems<?, ?> system, java.util.UUID... identityToken)
	{
	
	}
	
	
	public WarehouseSecurityTable createDefaultGuestNoSecurityAccess(ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return null;
	}
	
	public void updateSecurity(J newCoreTable, Systems system)
	{
	
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
		setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
		                            .getDeletedFlag(getEnterpriseID(), identifyingToken));
		setEffectiveToDate(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		setWarehouseLastUpdatedTimestamp(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		update(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
		
		setId(null);
		setValue(newValue);
		setEffectiveFromDate(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		setEffectiveToDate(EndOfTime.atOffset(java.time.ZoneOffset.UTC));
		setWarehouseCreatedTimestamp(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		setWarehouseLastUpdatedTimestamp(com.entityassist.querybuilder.QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow()));
		setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
		                            .getActiveFlag(getEnterpriseID(), identifyingToken));
		persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
		createDefaultSecurity(getSystemID());
		
		return (J) this;
	}
}
