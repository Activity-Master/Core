package com.armineasy.activitymaster.activitymaster;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;

public enum DefaultEnterprise implements IEnterpriseName<DefaultEnterprise>
{
	TestEnterprise("The test enterprise")
	;

	private String description;
	private Enterprise enterprise;

	DefaultEnterprise(String description)
	{
		this.description = description;
	}

	@Override
	public String classificationDescription()
	{
		return description;
	}

	@Override
	public Enterprise getEnterprise()
	{
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise)
	{
		this.enterprise = enterprise;
	}
}
