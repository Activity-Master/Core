package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyNameType;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IHasActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.enumtypes.INameType;

public interface IInvolvedPartyNameType<J extends IInvolvedPartyNameType<J>>
		extends INameAndDescription<InvolvedPartyNameType>,
		        IContainsEnterprise<InvolvedPartyNameType>,
		        IActivityMasterEntity<InvolvedPartyNameType>,
		        IHasActiveFlags<InvolvedPartyNameType>,
		        INameType
{
}
