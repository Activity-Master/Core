package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.db.entities.systems.Systems;

public interface IContainsOriginalSystemIds<J extends IContainsOriginalSystemIds<J>>
{
	String getOriginalSourceSystemUniqueID();
	
	public J setOriginalSourceSystemID(Systems originalSourceSystemID);
}
