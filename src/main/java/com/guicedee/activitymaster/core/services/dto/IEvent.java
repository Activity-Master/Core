package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.events.*;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IEvent<J extends IEvent<J>>
		extends IContainsClassifications<Event, Classification, EventXClassification, IEventClassification<?>, IEvent<?>, IClassification<?>, Event>,
		        IContainsGeographies<Event, Geography, EventXGeography>,
		        IContainsResourceItems<Event, ResourceItem, EventXResourceItem, IClassificationValue<?>, IEvent<?>, IResourceItem<?>, Event>,
		        IContainsInvolvedParties<Event, InvolvedParty, EventXInvolvedParty, IClassificationValue<?>, IEvent<?>, IInvolvedParty<?>, Event>,
		        IContainsAddresses<Event, Address, EventXAddress, IAddressClassification<?>, IEvent<?>, IAddress<?>, Event>,
		        IContainsEventTypes<Event, EventType, EventXEventType, IClassificationValue<?>, IEventClassification<?>, IEvent<?>, IEventType<?>, Event>,
		        IActivityMasterEntity<Event>,
		        IContainsRules<Event, Rules,EventXRules,IClassification<?>,IEvent<?>,IRules<?>>
{
}
