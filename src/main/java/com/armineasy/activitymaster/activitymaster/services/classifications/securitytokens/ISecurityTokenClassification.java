package com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;

public interface ISecurityTokenClassification<J extends Enum & ISecurityTokenClassification<J>> extends IClassificationValue<J>
{

}
