package com.guicedee.activitymaster.core.db.abstraction.builders.handlers;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;

import static com.entityassist.enumerations.Operand.*;
import static com.entityassist.enumerations.Operand.InList;

public interface IContainsNameDescriptionQueryBuilder<J extends QueryBuilderDefault<J, E, I>, E extends WarehouseBaseTable<E, J, I>, I extends Serializable>
		extends IQueryBuilderDefault<J,E,I>
{
	
	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	default J withName(String name)
	{
		where(this.<E,String>getAttribute("name"), Equals, name);
		return (J) this;
	}
	
	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	default J withName(IClassificationValue<?> name)
	{
		where(this.<E,String>getAttribute("name"), Equals, name.classificationName());
		return (J) this;
	}
	
	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	default J withDescription(String name)
	{
		where(this.<E,String>getAttribute("description"), Equals, name);
		return (J) this;
	}
	
	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	default J withDescriptionLike(String name)
	{
		where(this.<E,String>getAttribute("description"), Like, "%" + name + "%");
		return (J) this;
	}
	
	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	default J withName(String... name)
	{
		where(this.<E,String>getAttribute("name"), InList, name);
		return (J) this;
	}
	
	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	default J withName(Collection<String> name)
	{
		where(this.<E,String>getAttribute("name"), InList, name);
		return (J) this;
	}
}
