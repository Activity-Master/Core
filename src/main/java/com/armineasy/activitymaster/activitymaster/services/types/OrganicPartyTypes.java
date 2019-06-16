package com.armineasy.activitymaster.activitymaster.services.types;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.ITypeValue;

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
