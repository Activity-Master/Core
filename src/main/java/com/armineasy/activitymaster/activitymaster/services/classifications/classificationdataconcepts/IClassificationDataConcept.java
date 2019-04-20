package com.armineasy.activitymaster.activitymaster.services.classifications.classificationdataconcepts;

import com.armineasy.activitymaster.activitymaster.services.IClassificationValue;

public interface IClassificationDataConcept<J extends Enum & IClassificationDataConcept<J>> extends IClassificationValue<J>
{
}
