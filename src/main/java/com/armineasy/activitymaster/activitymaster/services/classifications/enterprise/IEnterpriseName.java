package com.armineasy.activitymaster.activitymaster.services.classifications.enterprise;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.ISecurityTokenClassification;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public interface IEnterpriseName<J extends Enum & IEnterpriseName<J>>
		extends ISecurityTokenClassification<J>
{
	@Override
	default IDataConceptValue<?> concept()
	{
		return Enterprise;
	}

	com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise getEnterprise();

	void setEnterprise(com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise enterprise);
}
