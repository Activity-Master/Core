package com.guicedee.activitymaster.core.services.enumtypes;

/**
 * Service restricted to enumerations
 *
 * @param <J> This enum type
 */
public interface IRulesTypeValue<J extends Enum<J> & IRulesTypeValue<J>>
		extends IClassificationValue<J>
{
	/**
	 * Overrides the enum and string
	 *
	 * @return The string for the enum
	 */
	@Override
	String name();
	
	/**
	 * The physical classification value
	 *
	 * @return
	 */
	String classificationValue();
	
	@Override
	default IClassificationDataConceptValue<?> concept()
	{
		return null;
	}
	
	@Override
	default String classificationDescription()
	{
		return classificationValue();
	}
}
