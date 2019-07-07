package com.armineasy.activitymaster.activitymaster.services.dto;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("unused")
public interface IRelationshipValue<P,S,J extends IRelationshipValue<P,S,J>>
{
	J setValue(String value);

	String getValue();

	default Integer getValueAsNumber()
	{
		return Integer.parseInt(getValue());
	}

	default Long getValueAsLong(){
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

	P getPrimary();

	S getSecondary();

}
