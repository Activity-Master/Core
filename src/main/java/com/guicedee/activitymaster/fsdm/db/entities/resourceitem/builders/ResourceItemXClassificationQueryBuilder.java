package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ResourceItemXClassificationQueryBuilder
        extends QueryBuilderRelationshipClassification<ResourceItem, Classification, ResourceItemXClassificationQueryBuilder,
        ResourceItemXClassification, UUID, ResourceItemXClassificationSecurityTokenQueryBuilder>
{
    @Override
    public SingularAttribute<ResourceItemXClassification, ResourceItem> getPrimaryAttribute()
    {
        return ResourceItemXClassification_.resourceItemID;
    }

    @Override
    public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
    {
        return ResourceItemXClassification_.classificationID;
    }
}
