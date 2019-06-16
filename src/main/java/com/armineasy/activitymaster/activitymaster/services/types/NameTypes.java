package com.armineasy.activitymaster.activitymaster.services.types;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.INameType;

public enum NameTypes
		implements INameType<NameTypes>
{
	FirstNameType("First Name"),
	FullNameType("Full Name"),
	PreferredNameType("Preferred Name"),
	BirthNameType("Birth Name"),
	LegalNameType("Legal Name"),
	CommonNameType("Common Name"),
	SalutationType("Salutation Name"),
	MiddleNameType("Middle Name"),
	InitialsType("Initials"),
	SurnameType("Surname"),
	QualificationType("Qualification"),
	SuffixType("Suffix"),
	Username("Username"),
	EmailAddress("Email Address");
	private String classificationValue;

	NameTypes(String classificationValue)
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
