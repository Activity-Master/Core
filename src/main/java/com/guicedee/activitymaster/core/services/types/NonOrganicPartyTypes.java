package com.guicedee.activitymaster.core.services.types;

import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;

public enum NonOrganicPartyTypes
		implements ITypeValue<NonOrganicPartyTypes>
{
	Organisation("Organisation"),
	Partner("Partner"),
	FinancialOrganisation("FinancialOrganisation"),
	
	;
	private String classificationValue;
	
	NonOrganicPartyTypes(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}
	
	@Override
	public String classificationValue()
	{
		return classificationValue;
	}
}
