package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderClassifications;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItemQueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.validation.constraints.NotNull;

import static com.entityassist.enumerations.Operand.*;

public class ResourceItemQueryBuilder
		extends QueryBuilderSCD<ResourceItemQueryBuilder, ResourceItem, String,ResourceItemSecurityTokenQueryBuilder>
		implements IQueryBuilderClassifications<ResourceItemQueryBuilder, ResourceItem, java.lang.String>,
		           IResourceItemQueryBuilder<ResourceItemQueryBuilder, ResourceItem>
{
	
	public @NotNull ResourceItemQueryBuilder withValue(String value)
	{
		where(ResourceItem_.resourceItemDataType, Equals, value);
		return this;
	}
	
	public ResourceItemQueryBuilder withType(String type, String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		JoinExpression joinExpression = new JoinExpression();
		
		ResourceItemXResourceItemTypeQueryBuilder table = new ResourceItemXResourceItemType().builder();
		table.inActiveRange();
		table.inDateRange();
		table.withEnterprise(system.getEnterprise());
		table.withValue(typeValue);
		
		table.withType(type, system, identityToken);
		
		join(ResourceItem_.types, table, JoinType.INNER, joinExpression);
		
		return this;
	}
}
