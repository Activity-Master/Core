package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyIdentificationType;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IHasActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;

public interface IInvolvedPartyIdentificationType<J extends IInvolvedPartyIdentificationType<J>>
		extends INameAndDescription<InvolvedPartyIdentificationType>,
				        IContainsEnterprise<InvolvedPartyIdentificationType>,
				        IActivityMasterEntity<InvolvedPartyIdentificationType>,
				IHasActiveFlags<InvolvedPartyIdentificationType>
{

}
