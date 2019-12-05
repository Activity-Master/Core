package com.guicedee.activitymaster.core.services.types;

import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;

public enum IdentificationTypes
		implements IIdentificationType<IdentificationTypes>
{
	IdentificationTypeUUID("UUID"),
	IdentificationTypeDriversLicense("Drivers License"),
	IdentificationTypePassportNumber("Passport Number"),
	IdentificationTypeTaxNumber("Tax Number"),
	IdentificationTypeVATNumber("Vat Registration Number"),
	IdentificationTypeIdentityNumber("Identity Number"),
	IdentificationTypeEmailAddress("Email Address"),
	IdentificationTypeCellPhoneNumber("Cell Phone Number"),
	IdentificationTypeSocialSecurityNumber("SocialSecurityNumber"),
	IdentificationTypeUserName("User Name"),
	IdentificationTypePassword("User Password"),
	IdentificationTypeSessionID("Session ID"),
	IdentificationTypeSystemID("System ID"),

	IdentificationTypeEnterpriseCreatorRole("Enterprise Creator"),
	IdentificationTypeUnassigned("Unassigned Involved Party"),

	;
	private String classificationValue;

	IdentificationTypes(String classificationValue)
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
