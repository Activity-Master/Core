package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.EnterpriseXClassification;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseClassification;

import java.io.Serializable;

public interface IEnterprise<J extends IEnterprise<J>>
		extends Serializable, INameAndDescription<J>,
				        IContainsClassifications<Enterprise, Classification, EnterpriseXClassification, IEnterpriseClassification<?>>,
				        IActivityMasterEntity<J>
{
}
