package com.guicedee.activitymaster.core.db.abstraction.builders.handlers;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;

import java.io.Serializable;

import static com.entityassist.enumerations.Operand.*;

public interface IHasClassificationQueryBuilder<J extends QueryBuilderDefault<J, E, I>, E extends WarehouseBaseTable<E, J, I>, I extends Serializable
		>
		extends IQueryBuilderDefault<J, E, I>,
		        IHasValueQueryBuilder<J,E,I>
{
	
	default J withClassification(Classification classification)
	{
		if (classification != null)
		{ where(this.<E, Classification>getAttribute("classificationID"), Equals, classification); }
		//noinspection unchecked
		return (J) this;
	}
	default J withClassification(Classification classification,String value)
	{
		if (classification != null)
		{ where(this.<E, Classification>getAttribute("classificationID"), Equals, classification); }
		withValue(value);
		//noinspection unchecked
		return (J) this;
	}
	
	default J withClassification(IClassification<?> classification)
	{
		if (classification != null)
		{ where(this.<E, Classification>getAttribute("classificationID"), Equals, (Classification) classification); }
		//noinspection unchecked
		return (J) this;
	}
	
	default J withClassification(IClassificationValue<?> classification, IEnterprise<?> enterprise)
	{
		IClassificationService<?> service = GuiceContext.get(IClassificationService.class);
		IClassification<?> classy = service.find(classification, enterprise);
		if (classification != null)
		{ where(this.<E, Classification>getAttribute("classificationID"), Equals, (Classification) classy); }
		//noinspection unchecked
		return (J) this;
	}
	
	default J withClassification(String classification, IEnterprise<?> enterprise)
	{
		IClassificationService<?> service = GuiceContext.get(IClassificationService.class);
		IClassification<?> classy = service.find(classification, enterprise);
		if (classification != null)
		{ where(this.<E, Classification>getAttribute("classificationID"), Equals, (Classification) classy); }
		//noinspection unchecked
		return (J) this;
	}
}
