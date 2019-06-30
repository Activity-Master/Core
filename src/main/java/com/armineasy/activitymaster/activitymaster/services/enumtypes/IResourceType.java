package com.armineasy.activitymaster.activitymaster.services.enumtypes;

import com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts;

/**
 * Service restricted to enumerations
 *
 * @param <J>
 * 		This enum type
 */
public interface IResourceType<J extends Enum & IResourceType<J>>
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
