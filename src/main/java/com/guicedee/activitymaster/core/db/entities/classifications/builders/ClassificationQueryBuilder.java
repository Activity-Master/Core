package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassificationQueryBuilder;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.classifications.*;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.UUID;

public class ClassificationQueryBuilder
		extends QueryBuilderTable<ClassificationQueryBuilder, Classification, java.util.UUID>
		implements IClassificationQueryBuilder<ClassificationQueryBuilder, Classification>,
		           IQueryBuilderNamesAndDescriptions<ClassificationQueryBuilder, Classification, UUID>

{
	public ClassificationQueryBuilder findByNameAndConcept(String name, ClassificationDataConcept concept)
	{
		withName(name);
		where(Classification_.concept, Operand.Equals, concept);
		return this;
	}
	
	public ClassificationQueryBuilder findByNameAndConcept(String name, EnterpriseClassificationDataConcepts concept, ISystems<?,?> system, UUID... identityToken)
	{
		withName(name);
		if (concept == null)
		{
			concept = EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;
		}
		
		IClassificationDataConceptService<?> service = GuiceContext.get(IClassificationDataConceptService.class);
		ClassificationDataConcept classificationDataConcept = (ClassificationDataConcept) service.find(concept, system, identityToken);
		where(Classification_.concept, Operand.Equals, classificationDataConcept);
		
		return this;
	}
}
