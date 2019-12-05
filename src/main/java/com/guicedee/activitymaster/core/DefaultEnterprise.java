package com.guicedee.activitymaster.core;

import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

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
