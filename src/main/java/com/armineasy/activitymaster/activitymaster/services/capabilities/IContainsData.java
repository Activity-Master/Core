package com.armineasy.activitymaster.activitymaster.services.capabilities;

/**
 * Specifies that this object can return a byte[]
 *
 */
@FunctionalInterface
public interface IContainsData<J extends IContainsData<J>>
{
	/**
	 * Returns the data
	 * @return The byte[] of data
	 */
	byte[] getData();
}
