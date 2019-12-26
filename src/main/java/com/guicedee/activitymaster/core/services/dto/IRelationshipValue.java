package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * An actual value for a relationship
 *
 * @param <P>
 * 		The primary source
 * @param <S>
 * 		The secondary source
 * @param <J>
 * 		This object
 */
@SuppressWarnings("unused")
public interface IRelationshipValue<P, S, J extends IRelationshipValue<P, S, J>>
{
	J setValue(String value);

	String getValue();

	default Integer getValueAsNumber()
	{
		return Integer.parseInt(getValue());
	}

	default Long getValueAsLong()
	{
		return Long.parseLong(getValue());
	}

	default Boolean getValueAsBoolean()
	{
		return Boolean.parseBoolean(getValue());
	}

	default BigDecimal getValueAsBigDecimal()
	{
		return BigDecimal.valueOf(getValueAsDouble());
	}

	default Double getValueAsDouble()
	{
		return Double.parseDouble(getValue());
	}

	default UUID getValueAsUUID()
	{
		return UUID.fromString(getValue());
	}

	/**
	 * The left hand side of the relationship
	 *
	 * @return
	 */
	P getPrimary();

	/**
	 * The right hand side of the relationship
	 *
	 * @return
	 */
	S getSecondary();

	default IRelationshipValue<P, S, ?> expire(Duration duration, ISystems<?> originatingSystem, UUID... identityToken)
	{
		WarehouseBaseTable<?, ?, ?> tableForClassification = (WarehouseBaseTable) this;
		tableForClassification.setEffectiveToDate(LocalDateTime.now()
		                                                       .plus(duration));
		tableForClassification.updateNow();
		return this;
	}

	default IRelationshipValue<P, S, ?> expire()
	{
		WarehouseBaseTable<?, ?, ?> tableForClassification = (WarehouseBaseTable) this;
		tableForClassification.setEffectiveToDate(LocalDateTime.now());
		tableForClassification.updateNow();
		return this;
	}

	default IRelationshipValue<P, S, ?> update(ISystems<?> originatingSystem, UUID... identityToken)
	{
		WarehouseBaseTable<?, ?, ?> tableForClassification = (WarehouseBaseTable) this;
		tableForClassification.updateNow();
		return this;
	}
}
