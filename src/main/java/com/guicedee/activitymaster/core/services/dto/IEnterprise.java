package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.enterprise.EnterpriseXClassification;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseClassification;

import java.io.Serializable;

public interface IEnterprise<J extends IEnterprise<J>>
		extends Serializable, INameAndDescription<J>,
				        IContainsClassifications<Enterprise, Classification, EnterpriseXClassification, IEnterpriseClassification<?>,IEnterprise<?>, IClassification<?>, Enterprise>,
				        IActivityMasterEntity<J>
{
}
