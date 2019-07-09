package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.classifications.address.IAddressClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IAddress;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.exceptions.AddressException;

import java.util.UUID;

public interface IAddressService<J extends IAddressService<?>>
{
	IAddress<?> create(IAddressClassification<?> addressClassification, ISystems<?> originatingSystem, String value, UUID... identifyingToken);

	IAddress<?> addOrFindIPAddress(String ipAddress, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException;

	IAddress<?> addOrFindHostName(String hostName, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException;

	IAddress<?> addOrFindWebAddress(String webAddress, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException;
}
