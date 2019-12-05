package com.guicedee.activitymaster.core.services.classifications.enterprise;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;
import com.guicedee.activitymaster.core.services.classifications.securitytokens.ISecurityTokenClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public interface IEnterpriseName<J extends Enum<J> & IEnterpriseName<J>>
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
