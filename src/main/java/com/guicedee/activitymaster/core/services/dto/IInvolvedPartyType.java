package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyType;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IHasActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;

public interface IInvolvedPartyType<J extends IInvolvedPartyType<J>>
		extends INameAndDescription<InvolvedPartyType>,
				        IContainsEnterprise<InvolvedPartyType>,
				        IActivityMasterEntity<InvolvedPartyType>,
				IHasActiveFlags<InvolvedPartyType>
{
}
