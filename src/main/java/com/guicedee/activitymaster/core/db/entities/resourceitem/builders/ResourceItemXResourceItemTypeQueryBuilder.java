package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationship;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementType;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXArrangementType_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.*;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.IResourceItemType;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IResourceType;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
import com.guicedee.activitymaster.core.services.system.IArrangementsService;
import com.guicedee.activitymaster.core.services.system.IResourceItemService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.persistence.metamodel.Attribute;
import java.util.UUID;

import static com.guicedee.guicedinjection.GuiceContext.*;

public class ResourceItemXResourceItemTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<ResourceItem,
		ResourceItemType,
		ResourceItemXResourceItemTypeQueryBuilder,
		ResourceItemXResourceItemType,
		IResourceType<?>,
		Long,
		ResourceItemXResourceItemTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ResourceItemXResourceItemType_.resourceItemID;
	}
	
	@Override
	public Attribute getSecondaryAttribute()
	{
		return ResourceItemXResourceItemType_.resourceItemTypeID;
	}
	
	@Override
	public ResourceItemXResourceItemTypeQueryBuilder withType(String typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IResourceItemService<?> service = GuiceContext.get(IResourceItemService.class);
			ResourceItemType at = (ResourceItemType) service.findResourceItemType(typeValue, enterprise, identityToken);
			where(ResourceItemXResourceItemType_.resourceItemTypeID, Operand.Equals, at);
		}
		return this;
	}
}
