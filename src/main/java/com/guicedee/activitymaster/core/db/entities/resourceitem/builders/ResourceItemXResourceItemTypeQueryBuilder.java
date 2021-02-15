package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.resourceitem.*;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.system.IResourceItemService;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ResourceItemXResourceItemTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<ResourceItem,
		ResourceItemType,
		ResourceItemXResourceItemTypeQueryBuilder,
		ResourceItemXResourceItemType,
		IResourceType<?>,
		java.util.UUID,
		ResourceItemXResourceItemTypeSecurityToken>
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
	public ResourceItemXResourceItemTypeQueryBuilder withType(String typeValue, ISystems<?> system, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IResourceItemService<?> service = GuiceContext.get(IResourceItemService.class);
			ResourceItemType at = (ResourceItemType) service.findResourceItemType(typeValue, system, identityToken);
			where(ResourceItemXResourceItemType_.resourceItemTypeID, Operand.Equals, at);
		}
		return this;
	}
}
