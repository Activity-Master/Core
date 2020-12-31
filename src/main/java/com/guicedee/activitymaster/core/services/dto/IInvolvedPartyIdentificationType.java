package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedPartyIdentificationType;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.IContainsNameAndDescription;

public interface IInvolvedPartyIdentificationType<J extends IInvolvedPartyIdentificationType<J>>
		extends IContainsNameAndDescription<InvolvedPartyIdentificationType>,
		        IContainsEnterprise<InvolvedPartyIdentificationType>,
		        IActivityMasterEntity<InvolvedPartyIdentificationType>,
		        IContainsActiveFlags<InvolvedPartyIdentificationType>
{

}
