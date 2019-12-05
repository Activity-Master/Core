package com.guicedee.activitymaster.core.services.capabilities;

public interface INameAndDescription<J extends INameAndDescription<J>>
{
	J setName(String name);

	J setDescription(String description);

	String getName();

	String getDescription();
}
