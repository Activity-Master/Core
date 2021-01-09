package com.guicedee.activitymaster.core.services.enumtypes;

/**
 * Service restricted to enumerations
 *
 * @param <J>
 * 		This enum type
 */
public interface IClassificationValue<J extends Enum<J> & IClassificationValue<J>>
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
	default String classificationName() {
		return name();
	}

	/**
	 * The physical classification value
	 *
	 * @return
	 */
	String classificationDescription();

	/**
	 * Return the concept that this classification value is applied to
	 *
	 * @return
	 */
	IClassificationDataConceptValue<?> concept();
	
	/**
	 * If this value is encrypted or not
	 *
	 * @return default false
	 */
	default boolean encrypted()
	{
		return false;
	}
}
