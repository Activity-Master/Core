package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassification;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsNameAndDescription;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseClassification;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;

import java.io.Serializable;

public interface IEnterprise<J extends IEnterprise<J>>
		extends Serializable,
		        IContainsNameAndDescription<J>,
		        IContainsClassifications<Enterprise, Classification, EnterpriseXClassification, IEnterpriseClassification<?>,IEnterprise<?>, IClassification<?>, Enterprise>,
		        IActivityMasterEntity<J>,
		        IEnterpriseName
{
	IEnterpriseName<?> getIEnterprise();
}
