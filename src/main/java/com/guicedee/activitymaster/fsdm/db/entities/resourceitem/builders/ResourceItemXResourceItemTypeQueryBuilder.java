package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class ResourceItemXResourceItemTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<ResourceItem,
		ResourceItemType,
		ResourceItemXResourceItemTypeQueryBuilder,
		ResourceItemXResourceItemType,
		UUID,
		ResourceItemXResourceItemTypeSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<ResourceItemXResourceItemType, ResourceItem> getPrimaryAttribute()
	{
		return ResourceItemXResourceItemType_.resourceItemID;
	}
	
	@Override
	public SingularAttribute<ResourceItemXResourceItemType, ResourceItemType> getSecondaryAttribute()
	{
		return ResourceItemXResourceItemType_.resourceItemTypeID;
	}
	
	@Override
	public ResourceItemXResourceItemTypeQueryBuilder withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (typeValue != null)
		{
			JoinExpression<?, ?, ?> joinExpression = new JoinExpression<>();
			join(getAttribute(ResourceItemXResourceItemType_.RESOURCE_ITEM_TYPE_ID), JoinType.INNER, joinExpression);
			var nameFilter = joinExpression.getFilter(ResourceItemType_.NAME, Equals, typeValue);
			getFilters().add(nameFilter);
		}
		return this;
	}
}
