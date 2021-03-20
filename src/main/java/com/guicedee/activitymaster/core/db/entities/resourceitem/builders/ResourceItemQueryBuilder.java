package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.client.services.builders.IQueryBuilderClassifications;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItemQueryBuilder;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.resourceitem.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class ResourceItemQueryBuilder
		extends QueryBuilderTable<ResourceItemQueryBuilder, ResourceItem, java.util.UUID>
		implements IQueryBuilderClassifications<ResourceItemQueryBuilder, ResourceItem, UUID>,
		           IResourceItemQueryBuilder<ResourceItemQueryBuilder, ResourceItem>
{

	public @NotNull ResourceItemQueryBuilder withValue(String value)
	{
		where(ResourceItem_.resourceItemDataType, Equals, value);
		return this;
	}
	
	public ResourceItemQueryBuilder withType(String type,String typeValue, ISystems<?,?> system, UUID...identityToken)
	{
		JoinExpression joinExpression = new JoinExpression();
		
		ResourceItemXResourceItemTypeQueryBuilder table = new ResourceItemXResourceItemType().builder();
		table.inActiveRange(system.getEnterprise(), identityToken);
		table.inDateRange();
		table.withEnterprise(system.getEnterprise());
		table.withValue(typeValue);
		
		table.withType(type, system, identityToken);
		
		join(ResourceItem_.types, table, JoinType.INNER, joinExpression);
		
		return this;
	}
}
