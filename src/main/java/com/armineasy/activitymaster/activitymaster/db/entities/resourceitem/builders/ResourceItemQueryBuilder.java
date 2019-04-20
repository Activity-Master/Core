package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;

import static com.jwebmp.entityassist.enumerations.Operand.*;

public class ResourceItemQueryBuilder
		extends QueryBuilder<ResourceItemQueryBuilder, ResourceItem, Long, ResourceItemSecurityToken>
{

}
