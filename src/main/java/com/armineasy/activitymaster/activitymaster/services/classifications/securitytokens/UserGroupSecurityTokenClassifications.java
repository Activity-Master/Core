package com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts;

public enum UserGroupSecurityTokenClassifications
		implements ISecurityTokenClassification<UserGroupSecurityTokenClassifications>
{
	Administrators("This group defines all the administrators in the system", EnterpriseDataConcepts.SecurityToken),
	Applications("This group grants access to applications registered within the enterprise", EnterpriseDataConcepts.SecurityToken),
	Everyone("This groups defines everyone allowed to access the system", EnterpriseDataConcepts.SecurityToken),
	Everywhere("This groups defines everyone allowed to access the system", EnterpriseDataConcepts.SecurityToken),

	System("This group defines the systems associated with the enterprise", EnterpriseDataConcepts.SecurityToken),
	Plugins("This group allows plugins to register under specific security", EnterpriseDataConcepts.SecurityToken),

	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	UserGroupSecurityTokenClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
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
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}
