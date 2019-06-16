package com.armineasy.activitymaster.activitymaster.services.enumtypes;

/**
 * ClassificationDataConcept has both itself and a classification
 *
 * @param <J>
 */
public interface IClassificationClassificationDataConceptTypes<J extends Enum & IClassificationClassificationDataConceptTypes<J>>
		extends IClassificationValue<J>, IClassificationDataConceptValue<J>
{
}
