package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.classifications.address.IAddressClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;

import java.util.UUID;

public interface IAddressService
{
	Address create(IAddressClassification<?> addressClassification, ISystems originatingSystem, String value, UUID... identifyingToken);
}
