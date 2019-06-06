package com.armineasy.activitymaster.activitymaster.services;

/**
 * Service restricted to enumerations
 *
 * @param <J>
 * 		This enum type
 */
public interface IArrangementType<J extends Enum & IArrangementType<J>> extends ITypeValue<J>
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
