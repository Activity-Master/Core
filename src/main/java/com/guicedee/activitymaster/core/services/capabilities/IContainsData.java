package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.services.dto.ISystems;

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
	void updateData(byte[] data, ISystems<?> system,UUID...identityToken);
	
	void updateAndKeepHistoryData(byte[] data,ISystems<?> system, UUID... identityToken);
}
