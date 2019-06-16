package com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts;

public enum UserGroupSecurityTokenClassifications
		implements ISecurityTokenClassification<UserGroupSecurityTokenClassifications>
{
	Administrators("This group defines all the administrators in the system", EnterpriseClassificationDataConcepts.SecurityToken),
	Applications("This group grants access to applications registered within the enterprise", EnterpriseClassificationDataConcepts.SecurityToken),
	Everyone("This groups defines everyone allowed to access the system", EnterpriseClassificationDataConcepts.SecurityToken),
	Everywhere("This groups defines everyone allowed to access the system", EnterpriseClassificationDataConcepts.SecurityToken),

	System("This group defines the systems associated with the enterprise", EnterpriseClassificationDataConcepts.SecurityToken),
	Plugins("This group allows plugins to register under specific security", EnterpriseClassificationDataConcepts.SecurityToken),

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	UserGroupSecurityTokenClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	UserGroupSecurityTokenClassifications(String classificationValue)
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
