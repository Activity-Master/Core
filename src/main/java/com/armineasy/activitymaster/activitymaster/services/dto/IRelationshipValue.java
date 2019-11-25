package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseBaseTable;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.system.IInvolvedPartyService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.guicedee.guicedinjection.GuiceContext.*;

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

	default IRelationshipValue<P,S,?> expire(Duration duration, ISystems<?> originatingSystem, UUID... identityToken)
	{
		WarehouseBaseTable tableForClassification = (WarehouseBaseTable) this;
		tableForClassification.setEffectiveToDate(LocalDateTime.now()
		                                                       .plus(duration));
		tableForClassification.updateNow();
		return this;
	}


	default IRelationshipValue<P,S,?> update(ISystems<?> originatingSystem, UUID... identityToken)
	{
		WarehouseBaseTable tableForClassification = (WarehouseBaseTable) this;
		tableForClassification.updateNow();
		return this;
	}
}
