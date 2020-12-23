package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsNameDescriptionQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification_;
import com.entityassist.enumerations.Operand;

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
}
