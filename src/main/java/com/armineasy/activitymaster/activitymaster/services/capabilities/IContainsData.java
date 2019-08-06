package com.armineasy.activitymaster.activitymaster.services.capabilities;

import java.util.UUID;

/**
 * Specifies that this object can return a byte[]
 *
 */
public interface IContainsData<J extends IContainsData<J>>
{
	/**
	 * Returns the data
	 * @return The byte[] of data
	 */
	byte[] getData(UUID...identityToken);

	/**
	 * Updates the data for the given item
	 * @param data The data of the item
	 */
	void updateData(byte[] data,UUID...identityToken);
}
