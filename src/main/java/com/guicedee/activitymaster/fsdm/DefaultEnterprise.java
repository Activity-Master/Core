package com.guicedee.activitymaster.fsdm;


import com.guicedee.activitymaster.fsdm.client.IEnterpriseNames;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;

public enum DefaultEnterprise implements IEnterpriseNames<DefaultEnterprise>
{
	TestEnterprise("The test enterprise")
	;

	private String description;

	DefaultEnterprise(String description)
	{
		this.description = description;
	}
	
	public String classificationDescription()
	{
		return description;
	}

}
