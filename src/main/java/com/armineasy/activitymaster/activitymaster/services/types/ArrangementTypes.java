package com.armineasy.activitymaster.activitymaster.services.types;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IArrangementTypes;

public enum ArrangementTypes
		implements IArrangementTypes<ArrangementTypes>
{

	;
	private String classificationValue;

	ArrangementTypes(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	@Override
	public String classificationValue()
	{
		return name();
	}

	@Override
	public String classificationDescription()
	{
		return classificationValue;
	}

}
