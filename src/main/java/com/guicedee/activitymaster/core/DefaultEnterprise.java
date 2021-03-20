package com.guicedee.activitymaster.core;


import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;

public enum DefaultEnterprise
{
	TestEnterprise("The test enterprise")
	;

	private String description;
	private IEnterprise<?,?> enterprise;

	DefaultEnterprise(String description)
	{
		this.description = description;
	}
	
	public String classificationDescription()
	{
		return description;
	}

	public IEnterprise<?,?> getEnterprise()
	{
		return enterprise;
	}

	public void setEnterprise(IEnterprise<?,?> enterprise)
	{
		this.enterprise = enterprise;
	}
}
