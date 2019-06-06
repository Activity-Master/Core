package com.armineasy.activitymaster.activitymaster.services.classifications.enterprise;

import com.armineasy.activitymaster.activitymaster.services.IClassificationDataConceptValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.ISecurityTokenClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public interface IEnterpriseName<J extends Enum & IEnterpriseName<J>>
		extends ISecurityTokenClassification<J>
{
	@Override
	default IClassificationDataConceptValue<?> concept()
	{
		return Enterprise;
	}

	IEnterprise<?> getEnterprise();

	void setEnterprise(IEnterprise<?> enterprise);
}
