package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationQueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.*;
import com.guicedee.guicedinjection.GuiceContext;



public class ClassificationQueryBuilder
		extends QueryBuilderTable<ClassificationQueryBuilder, Classification, java.lang.String>
		implements IClassificationQueryBuilder<ClassificationQueryBuilder, Classification>,
		           IQueryBuilderNamesAndDescriptions<ClassificationQueryBuilder, Classification, java.lang.String>

{
	public ClassificationQueryBuilder withConcept(EnterpriseClassificationDataConcepts concept, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		IClassificationDataConceptService<?> service = GuiceContext.get(IClassificationDataConceptService.class);
		ClassificationDataConcept dc = (ClassificationDataConcept) service.find(concept, system, identityToken);
		where(Classification_.concept, Operand.Equals, dc);
		return this;
	}
	
}
