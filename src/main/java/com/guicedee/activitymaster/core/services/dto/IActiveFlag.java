package com.guicedee.activitymaster.core.services.dto;

import com.google.inject.ImplementedBy;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlagXClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.IContainsNameAndDescription;
import com.guicedee.activitymaster.core.services.classifications.activeflag.IActiveFlagClassification;

import java.io.Serializable;

public interface IActiveFlag<J extends IActiveFlag<J>>
		extends Serializable,
		        IContainsNameAndDescription<J>,
				        IContainsClassifications<ActiveFlag, Classification, ActiveFlagXClassification, IActiveFlagClassification<?>,IActiveFlag<?>, IClassification<?>,  IActiveFlag<ActiveFlag>>,
				        IActivityMasterEntity<J>,
				        IContainsEnterprise<J>
{
}
