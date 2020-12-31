package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyType;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.IContainsNameAndDescription;

public interface IInvolvedPartyType<J extends IInvolvedPartyType<J>>
		extends IContainsNameAndDescription<InvolvedPartyType>,
		        IContainsEnterprise<InvolvedPartyType>,
		        IActivityMasterEntity<InvolvedPartyType>,
		        IContainsActiveFlags<InvolvedPartyType>
{
}
