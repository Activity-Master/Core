package com.guicedee.activitymaster.core.services.enumtypes;

/**
 * Service restricted to enumerations
 *
 * @param <J>
 * 		This enum type
 */
public interface INameType<J extends Enum<J> & INameType<J>> extends ITypeValue<J>
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

	/**
	 * The physical classification value
	 *
	 * @return
	 */
	String classificationDescription();
}
