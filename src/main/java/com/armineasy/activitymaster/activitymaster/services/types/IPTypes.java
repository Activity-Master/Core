package com.armineasy.activitymaster.activitymaster.services.types;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.ITypeValue;

public enum IPTypes
		implements ITypeValue<IPTypes>
{
	TypeIndividual("Individual"),
	TypeOrganisation("Organisation"),
	TypeSystem("System"),
	TypeApplication("Application"),
	TypeAbstraction("Abstraction"),
	TypeUnknown("Unknown"),
	;
	private String classificationValue;

	IPTypes(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	@Override
	public String classificationValue()
	{
		return classificationValue;
	}
}
