package com.armineasy.activitymaster.activitymaster.db.abstraction;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationship;
import com.armineasy.activitymaster.activitymaster.services.dto.IRelationshipValue;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.jwebmp.entityassist.SCDEntity;
import com.jwebmp.guicedinjection.GuiceContext;
import lombok.experimental.Accessors;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
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
 *
 * @author GedMarc
 * @version 1.0
 * @since 09 Dec 2016
 */
@MappedSuperclass
@Accessors(chain = true)
public abstract class WarehouseRelationshipTable<P extends WarehouseCoreTable,
		                                                S extends WarehouseCoreTable,
		                                                J extends WarehouseRelationshipTable<P, S, J, Q, I, ST>,
		                                                Q extends QueryBuilderRelationship<P, S, Q, J, I, ST>,
		                                                I extends Serializable,
		                                                ST extends WarehouseSecurityTable>
		extends WarehouseTable<J, Q, I, ST>
		implements IRelationshipValue<J>
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
		this.value = value;
		return (J)this;
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
		setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
		                            .getDeletedFlag(getEnterpriseID(), identifyingToken));
		setEffectiveToDate(LocalDateTime.now());
		setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
		update();

		setId(null);
		setValue(newValue);
		setEffectiveFromDate(LocalDateTime.now());
		setEffectiveToDate(SCDEntity.EndOfTime);
		setWarehouseCreatedTimestamp(LocalDateTime.now());
		setWarehouseLastUpdatedTimestamp(LocalDateTime.now());
		setActiveFlagID(GuiceContext.get(IActiveFlagService.class)
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
