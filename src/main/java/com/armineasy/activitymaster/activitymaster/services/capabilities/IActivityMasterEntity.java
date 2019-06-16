package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.jwebmp.guicedinjection.GuiceContext.*;

public interface IActivityMasterEntity<J extends IActivityMasterEntity<J>>
{
	J setId(Long id);
	Long getId();

	void createDefaultSecurity(ISystems<?> system, UUID... identity);

	J archive();
	J remove();
}
