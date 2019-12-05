package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.services.dto.ISystems;

import java.util.UUID;

public interface IActivityMasterEntity<J extends IActivityMasterEntity<J>>
{
	J setId(Long id);
	Long getId();

	void createDefaultSecurity(ISystems<?> system, UUID... identity);

	J archive();
	J remove();
}
