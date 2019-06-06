package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;
import com.armineasy.activitymaster.activitymaster.services.classifications.classification.IClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.systems.ISystemsClassification;

import java.io.Serializable;

public interface ISystems<J extends ISystems<J>>
		extends Serializable,
				        IContainsClassifications<Systems, Classification, SystemXClassification, ISystemsClassification<?>>,
				        IActivityMasterEntity<J>,
				        INameAndDescription<J>,
				        IContainsEnterprise<J>
{
}
