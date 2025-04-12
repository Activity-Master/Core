package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConceptQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept;

import java.util.UUID;

public class ClassificationDataConceptQueryBuilder
		extends QueryBuilderSCD<ClassificationDataConceptQueryBuilder, ClassificationDataConcept, UUID,ClassificationDataConceptSecurityTokenQueryBuilder>
		implements IClassificationDataConceptQueryBuilder<ClassificationDataConceptQueryBuilder, ClassificationDataConcept>,
		           IQueryBuilderNamesAndDescriptions<ClassificationDataConceptQueryBuilder,ClassificationDataConcept,UUID>
{
}
