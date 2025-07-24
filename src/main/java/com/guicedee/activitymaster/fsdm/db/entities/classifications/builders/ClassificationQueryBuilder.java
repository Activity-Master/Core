package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationQueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.*;

import java.util.UUID;


public class ClassificationQueryBuilder
		extends QueryBuilderSCD<ClassificationQueryBuilder, Classification, UUID,ClassificationsSecurityTokenQueryBuilder>
		implements IClassificationQueryBuilder<ClassificationQueryBuilder, Classification>,
		           IQueryBuilderNamesAndDescriptions<ClassificationQueryBuilder, Classification, UUID>

{
	public ClassificationQueryBuilder withConcept(EnterpriseClassificationDataConcepts concept, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		IClassificationDataConceptService<?> service = com.guicedee.client.IGuiceContext.get(IClassificationDataConceptService.class);
		ClassificationDataConcept dc = (ClassificationDataConcept) service.find(getEntityManager(), concept, system, identityToken);
		where(Classification_.concept, Operand.Equals, dc);
		return this;
	}
	
}
