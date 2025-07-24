package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;


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
			IResourceItemService<?> service = com.guicedee.client.IGuiceContext.get(IResourceItemService.class);
			ResourceItemType at = (ResourceItemType) service.findResourceItemType(getEntityManager(), typeValue, system, identityToken);
			where(ResourceItemXResourceItemType_.resourceItemTypeID, Operand.Equals, at);
		}
		return this;
	}
}
