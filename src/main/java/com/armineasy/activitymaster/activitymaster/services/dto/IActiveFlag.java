package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;
import com.armineasy.activitymaster.activitymaster.services.classifications.activeflag.IActiveFlagClassification;

import java.io.Serializable;

public interface IActiveFlag<J extends IActiveFlag<J>>
		extends Serializable,
				        INameAndDescription<J>,
				        IContainsClassifications<ActiveFlag, Classification, ActiveFlagXClassification, IActiveFlagClassification<?>,IActiveFlag<?>, IClassification<?>,  IActiveFlag<ActiveFlag>>,
				        IActivityMasterEntity<J>,
				        IContainsEnterprise<J>
{
}
