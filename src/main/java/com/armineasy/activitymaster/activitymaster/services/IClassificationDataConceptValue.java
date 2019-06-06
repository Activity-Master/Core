package com.armineasy.activitymaster.activitymaster.services;

/**
 * Service restricted to enumerations
 *
 * @param <J>
 * 		This enum type
 */
@SuppressWarnings("unused")
public interface IClassificationDataConceptValue<J extends Enum & IClassificationDataConceptValue<J>>
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
	String classificationValue();
}
