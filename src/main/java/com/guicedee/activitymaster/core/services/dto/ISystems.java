package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassification;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.classifications.systems.ISystemsClassification;

import java.io.Serializable;

public interface ISystems<J extends ISystems<J>>
		extends Serializable,
				        IContainsClassifications<Systems, Classification, SystemXClassification, ISystemsClassification<?>,ISystems<?>, IClassification<?>, Systems>,
				        IActivityMasterEntity<J>,
				        INameAndDescription<J>,
				        IContainsEnterprise<J>
{
}
