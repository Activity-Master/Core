package com.guicedee.activitymaster.core.db.abstraction.builders.handlers;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;

import java.io.Serializable;

import static com.entityassist.enumerations.Operand.*;

public interface IHasValueQueryBuilder<J extends QueryBuilderDefault<J, E, I>, E extends WarehouseBaseTable<E, J, I>, I extends Serializable>
		extends IQueryBuilderDefault<J, E, I>
{
	@jakarta.validation.constraints.NotNull
	default J withValue(String value)
	{
		if (value != null)
		{ where(this.<E, String>getAttribute("value"), Equals, value); }
		//noinspection unchecked
		return (J) this;
	}
	
	@jakarta.validation.constraints.NotNull
	default J withValueGT(String value)
	{
		if (value != null)
		{ where(this.<E, String>getAttribute("value"), GreaterThan, value); }
		//noinspection unchecked
		return (J) this;
	}
	
	@jakarta.validation.constraints.NotNull
	default J withValueGTE(String value)
	{
		if (value != null)
		{ where(this.<E, String>getAttribute("value"), GreaterThanEqualTo, value); }
		//noinspection unchecked
		return (J) this;
	}
	
	@jakarta.validation.constraints.NotNull
	default J withValueLT(String value)
	{
		if (value != null)
		{ where(this.<E, String>getAttribute("value"), LessThan, value); }
		//noinspection unchecked
		return (J) this;
	}
	
	@jakarta.validation.constraints.NotNull
	default J withValueLTE(String value)
	{
		if (value != null)
		{ where(this.<E, String>getAttribute("value"), LessThanEqualTo, value); }
		//noinspection unchecked
		return (J) this;
	}
	
}
