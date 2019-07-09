package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.events.*;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;
import com.armineasy.activitymaster.activitymaster.services.classifications.events.IEventClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IEventTypeValue;

public interface IEvent<J extends IEvent<J>>
		extends IContainsClassifications<Event, Classification, EventXClassification, IEventClassification<?>,IEvent<?>,IClassification<?>, Event>,
				        IContainsGeographies<Event, Geography, EventXGeography>,
				        IContainsResourceItems<Event, ResourceItem, EventXResourceItem, IResourceItemClassification<?>, IEvent<?>, IResourceItem<?>, Event>,
				        IContainsInvolvedParties<Event, InvolvedParty, EventXInvolvedParty>,
				        IContainsAddresses<Event, Address, EventXAddress>,
				        IContainsEventTypes<Event, EventType, EventXEventType, IEventTypeValue<?>, Event>,
				        IActivityMasterEntity<Event>
{
}
