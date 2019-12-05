package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.dto.IAddress;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.exceptions.AddressException;

import java.util.UUID;

public interface IAddressService<J extends IAddressService<?>>
{
	IAddress<?> create(IAddressClassification<?> addressClassification, ISystems<?> originatingSystem, String value, UUID... identifyingToken);

	IAddress<?> addOrFindIPAddress(String ipAddress, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException;

	IAddress<?> addOrFindHostName(String hostName, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException;

	IAddress<?> addOrFindWebAddress(String webAddress, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException;
}
