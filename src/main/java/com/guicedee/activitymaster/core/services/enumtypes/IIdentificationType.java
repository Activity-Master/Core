package com.guicedee.activitymaster.core.services.enumtypes;

/**
 * Service restricted to enumerations
 *
 * @param <J> This enum type
 */
public interface IIdentificationType<J extends Enum<J> & IIdentificationType<J>> extends ITypeValue<J>
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
	@Override
	String classificationValue();
	
	/**
	 * The physical classification value
	 *
	 * @return
	 */
	String classificationDescription();
	
	default IClassificationDataConceptValue<?> concept()
	{
		return null;
	}
}
