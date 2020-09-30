package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganicType;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IHasActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;

public interface IInvolvedPartyOrganicType<J extends IInvolvedPartyOrganicType<J>>
		extends INameAndDescription<InvolvedPartyOrganicType>,
				        IContainsEnterprise<InvolvedPartyOrganicType>,
				        IActivityMasterEntity<InvolvedPartyOrganicType>,
				IHasActiveFlags<InvolvedPartyOrganicType>
{
}
