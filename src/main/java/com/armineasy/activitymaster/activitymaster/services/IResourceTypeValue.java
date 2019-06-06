package com.armineasy.activitymaster.activitymaster.services;

import com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts;

/**
 * Service restricted to enumerations
 *
 * @param <J>
 * 		This enum type
 */
public interface IResourceTypeValue<J extends Enum & IResourceTypeValue<J>>
{
	/**
	 * Overrides the enum and string
	 *
	 * @return The string for the enum
	 */
	String name();

	/**
	 * The physical classification value
	 *
	 * @return
	 */
	String classificationName();

	/**
	 * The physical classification value
	 *
	 * @return
	 */
	String classificationValue();

	/**
	 * The description
	 * @return
	 */
	String classificationDescription();

	/**
	 * The Default Concept
	 *
	 * @return
	 */
	default IClassificationDataConceptValue<?> concept()
	{
		return EnterpriseClassificationDataConcepts.ResourceItem;
	}
}
