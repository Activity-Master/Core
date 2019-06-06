package com.armineasy.activitymaster.activitymaster;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

public enum DefaultEnterprise implements IEnterpriseName<DefaultEnterprise>
{
	TestEnterprise("The test enterprise")
	;

	private String description;
	private IEnterprise<?> enterprise;

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
	public IEnterprise<?> getEnterprise()
	{
		return enterprise;
	}

	public void setEnterprise(IEnterprise<?> enterprise)
	{
		this.enterprise = enterprise;
	}
}
