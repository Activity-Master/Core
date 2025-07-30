package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;


public class RulesXRulesTypeQueryBuilder
        extends QueryBuilderRelationshipClassification<Rules, RulesType, RulesXRulesTypeQueryBuilder,
        RulesXRulesType, UUID, RulesXRulesTypeSecurityTokenQueryBuilder>
{
    @Override
    public SingularAttribute<RulesXRulesType, Rules> getPrimaryAttribute()
    {
        return RulesXRulesType_.rulesID;
    }

    @Override
    public SingularAttribute<RulesXRulesType, RulesType> getSecondaryAttribute()
    {
        return RulesXRulesType_.rulesTypeID;
    }


    
    public RulesXRulesTypeQueryBuilder withClassification(String classification, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        if (isStateless()) {
            // Use stateless entity manager
            withClassification(classification, system);
            withValue(value);
            inActiveRange();
            inDateRange();
            withEnterprise(system.getEnterpriseID());
        } else {
            // Use regular entity manager
            withClassification(classification, system);
            withValue(value);
            inActiveRange();
            inDateRange();
            withEnterprise(system.getEnterpriseID());
        }
        return this;
    }
}
