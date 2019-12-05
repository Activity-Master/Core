package com.guicedee.activitymaster.core.services.types;

import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;

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
