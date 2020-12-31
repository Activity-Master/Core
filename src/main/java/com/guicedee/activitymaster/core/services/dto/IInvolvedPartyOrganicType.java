package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyOrganicType;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.IContainsNameAndDescription;

public interface IInvolvedPartyOrganicType<J extends IInvolvedPartyOrganicType<J>>
		extends IContainsNameAndDescription<InvolvedPartyOrganicType>,
		        IContainsEnterprise<InvolvedPartyOrganicType>,
		        IActivityMasterEntity<InvolvedPartyOrganicType>,
		        IContainsActiveFlags<InvolvedPartyOrganicType>
{
}
