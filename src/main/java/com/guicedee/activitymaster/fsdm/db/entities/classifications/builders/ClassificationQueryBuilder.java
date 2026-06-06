package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.entityassist.enumerations.Operand;
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
	/**
	 * Restricts the query to classifications whose data concept matches the supplied concept.
	 *
	 * <p>The concept is filtered by its name through the {@code concept.name} join path rather than
	 * by resolving the {@link ClassificationDataConcept} entity. Resolving the entity required a
	 * blocking call against the reactive {@code IClassificationDataConceptService} (which returns a
	 * {@code Uni}), so the previous implementation cast the {@code Uni} straight to the entity and
	 * threw {@link ClassCastException} at runtime. The concept's persisted {@code name} equals
	 * {@link EnterpriseClassificationDataConcepts#classificationValue()} (see
	 * {@code ClassificationsDataConceptService.find}), so the name-based join filter is equivalent
	 * and stays fully non-blocking.</p>
	 *
	 * @param concept       the data concept to match
	 * @param system        the requesting system (retained for API compatibility)
	 * @param identityToken optional security identity token(s) (retained for API compatibility)
	 * @return this builder for chaining
	 */
	public ClassificationQueryBuilder withConcept(EnterpriseClassificationDataConcepts concept, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		where("concept.name", Operand.Equals, concept.classificationValue());
		return this;
	}

}
