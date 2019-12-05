package com.guicedee.activitymaster.core.services.types;

import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;

public enum OrganicPartyTypes
		implements ITypeValue<OrganicPartyTypes>
{
	OrganicCustomerType("Customer"),
	OrganicEmployeeType("Employee"),
	OrganicUserType("User"),
	OrganicAgentType("Agent"),
	OrganicClientType("Client"),
	OrganicUnknownType("Unknown"),

	;
	private String classificationValue;

	OrganicPartyTypes(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	@Override
	public String classificationValue()
	{
		return classificationValue;
	}
}
