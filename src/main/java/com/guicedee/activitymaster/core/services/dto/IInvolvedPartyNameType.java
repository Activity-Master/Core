package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyNameType;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;

public interface IInvolvedPartyNameType<J extends IInvolvedPartyNameType<J>>
		extends INameAndDescription<InvolvedPartyNameType>,
				        IContainsEnterprise<InvolvedPartyNameType>,
				        IActivityMasterEntity<InvolvedPartyNameType>,
				        IContainsActiveFlags<InvolvedPartyNameType>
{
}
