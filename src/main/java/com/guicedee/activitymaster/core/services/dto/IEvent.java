package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.*;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IEventTypeValue;
import com.guicedee.activitymaster.core.db.entities.events.*;
import com.guicedee.activitymaster.core.services.capabilities.*;

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
