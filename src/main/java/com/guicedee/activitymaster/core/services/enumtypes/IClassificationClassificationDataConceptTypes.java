package com.guicedee.activitymaster.core.services.enumtypes;

/**
 * ClassificationDataConcept has both itself and a classification
 *
 * @param <J>
 */
public interface IClassificationClassificationDataConceptTypes<J extends Enum<J> & IClassificationClassificationDataConceptTypes<J>>
		extends IClassificationValue<J>, IClassificationDataConceptValue<J>
{
}
