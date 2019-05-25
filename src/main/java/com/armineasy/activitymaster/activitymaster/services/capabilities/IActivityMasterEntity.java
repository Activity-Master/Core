package com.armineasy.activitymaster.activitymaster.services.capabilities;

public interface IActivityMasterEntity<J extends IActivityMasterEntity<J>>
{
	J setId(Long id);
	Long getId();
}
