package com.guicedee.activitymaster.core.db.abstraction;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.client.implementations.Passwords;
import com.guicedee.activitymaster.client.services.IActiveFlagService;
import com.guicedee.activitymaster.client.services.builders.warehouse.IWarehouseRelationshipTable;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
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
public abstract class WarehouseRelationshipTable<P extends WarehouseBaseTable<P,?,UUID>,
		S extends WarehouseBaseTable<S,?,UUID>,
		J extends WarehouseRelationshipTable<P, S, J, Q, I>,
		Q extends QueryBuilderRelationship<P, S, Q, J, I>,
		I extends java.util.UUID>
		extends WarehouseTable<J, Q, I>
		implements IWarehouseRelationshipTable<J,Q,P,S,I>
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
	public void createDefaultSecurity(ISystems<?,?> system, UUID... identity)
	{
	
	}
	

	public WarehouseSecurityTable createDefaultGuestNoSecurityAccess(ISystems<?,?> system, UUID... identity)
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
		setValue(value,false);
	}
	
	@Override
	public void setValue(String value,boolean encrypted)
	{
		this.value = Strings.nullToEmpty(value);
		if(!Strings.isNullOrEmpty(this.value) && encrypted)
		{
			this.value = new Passwords().integerEncrypt(this.value.getBytes());
		}
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
	public @NotNull J update(String newValue, UUID... identifyingToken)
	{
		setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
		                                         .getDeletedFlag(getEnterpriseID(), identifyingToken));
		setEffectiveToDate(LocalDateTime.now());
		setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
		update();
		
		setId(null);
		setValue(newValue);
		setEffectiveFromDate(StartOfTime);
		setEffectiveToDate(EndOfTime);
		setWarehouseCreatedTimestamp(LocalDateTime.now());
		setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
		setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
		                                         .getActiveFlag(getEnterpriseID(), identifyingToken));
		persist();
			createDefaultSecurity(getSystemID());
		
		return (J) this;
	}
}
