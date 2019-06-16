package com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum SecurityTokenClassifications
		implements ISecurityTokenClassification<SecurityTokenClassifications>
{
	Identity("Defines an identity classification relationship", SecurityTokenXSecurityToken),
	Guests("Defines an item as a user with no security applied", SecurityTokenXSecurityToken),
	Visitors("Defines an item as a user with no previous identification", SecurityTokenXSecurityToken),
	Registered("Defines an item as a user who has registered with guest access", SecurityTokenXSecurityToken),


	UserGroup("Defines an item as a user group with security applied", SecurityTokenXSecurityToken),
	User("Defines an item as a user with security applied", SecurityTokenXSecurityToken),
	Application("Defines an item as an application with security applied", SecurityTokenXSecurityToken),
	Plugin("Defines an item as a plugin with security applied", SecurityTokenXSecurityToken),


	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	SecurityTokenClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	SecurityTokenClassifications(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	@Override
	public String classificationDescription()
	{
		return classificationValue;
	}
	@Override
	public IClassificationDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
