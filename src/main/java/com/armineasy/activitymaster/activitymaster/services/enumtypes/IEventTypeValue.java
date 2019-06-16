package com.armineasy.activitymaster.activitymaster.services.enumtypes;

/**
 * Service restricted to enumerations
 *
 * @param <J>
 * 		This enum type
 */
public interface IEventTypeValue<J extends Enum & IEventTypeValue<J>>
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
