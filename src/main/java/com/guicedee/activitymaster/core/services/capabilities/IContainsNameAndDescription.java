package com.guicedee.activitymaster.core.services.capabilities;

public interface IContainsNameAndDescription<J extends IContainsNameAndDescription<J>>
{
	J setName(String name);

	J setDescription(String description);

	String getName();

	String getDescription();
}
