package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.entityassist.enumerations.Operand;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsNameDescriptionQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification_;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IClassificationDataConceptService;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.UUID;

public class ClassificationQueryBuilder
		extends QueryBuilderTable<ClassificationQueryBuilder, Classification, java.util.UUID, ClassificationSecurityToken>
		implements IContainsNameDescriptionQueryBuilder<ClassificationQueryBuilder, Classification, java.util.UUID>
	
{
	public ClassificationQueryBuilder findByNameAndConcept(String name, ClassificationDataConcept concept)
	{
		withName(name);
		where(Classification_.concept, Operand.Equals, concept);
		return this;
	}
	
	public ClassificationQueryBuilder findByNameAndConcept(String name, String concept, ISystems<?> system, UUID...identityToken)
	{
		withName(name);
		if (!Strings.isNullOrEmpty(concept))
		{
			IClassificationDataConceptService<?> service = GuiceContext.get(IClassificationDataConceptService.class);
			ClassificationDataConcept classificationDataConcept = service.find(concept, system, identityToken);
			where(Classification_.concept, Operand.Equals, classificationDataConcept);
		}
		return this;
	}
}
