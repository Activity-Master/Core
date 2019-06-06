package com.armineasy.activitymaster.activitymaster.services.types;

import com.armineasy.activitymaster.activitymaster.services.IArrangementType;

public enum ArrangementTypes
		implements IArrangementType<ArrangementTypes>
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
