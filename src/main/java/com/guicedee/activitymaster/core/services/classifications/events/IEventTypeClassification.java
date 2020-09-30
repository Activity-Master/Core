package com.guicedee.activitymaster.core.services.classifications.events;

public interface IEventTypeClassification<J extends Enum<J> & IEventTypeClassification<J>>
		extends IEventClassification<J>
{

}
