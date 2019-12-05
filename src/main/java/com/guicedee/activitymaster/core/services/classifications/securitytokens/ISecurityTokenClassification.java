package com.guicedee.activitymaster.core.services.classifications.securitytokens;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface ISecurityTokenClassification<J extends Enum<J> & ISecurityTokenClassification<J>> extends IClassificationValue<J>
{

}
