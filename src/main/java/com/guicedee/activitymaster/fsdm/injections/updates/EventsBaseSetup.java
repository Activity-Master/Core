package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;

@SortedUpdate(sortOrder = -100, taskCount = 3)
public class EventsBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;
	
	@Override
	public void update(IEnterprise<?,?> enterprise)
	{
		logProgress("Events System", "Loading Base Events...", 1);
		createEventInvolvedPartyDefaultClassifications(enterprise);
		createEventAddressDefaultClassifications(enterprise);
		createEventArrangementDefaultClassifications(enterprise);
		createEventEventTypesClassifications(enterprise);
		createEventProductsDefaultClassifications(enterprise);
		createEventResourceItemDefaultClassifications(enterprise);
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventInvolvedPartyDefaultClassifications(IEnterprise<?,?> enterprise)
	{
		//Involved party relations with events
		service.create(EventInvolvedPartiesClassifications.InvolvedPartyEvents, activityMasterSystem);
		service.create(EventInvolvedPartiesClassifications.PerformedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.OnBehalfOf, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.For, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.OwnedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.Created, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.Added, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.Updated, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.CreatedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.CompletedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.UpdatedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.SecurityCredentialsOf, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.Notifies, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventInvolvedPartiesClassifications.MeantFor, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventClassifications.NotifiesInvolvedParty, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventClassifications.UpdatedPassword, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		service.create(EventClassifications.UpdatedUsername, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents);
		
		logProgress("Events System", "Loaded Event InvolvedParty Classifications...", 1);
		
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventAddressDefaultClassifications(IEnterprise<?,?> enterprise)
	{
		service.create(EventAddressClassifications.AddressEvents, activityMasterSystem);
		service.create(EventAddressClassifications.SignedAt, activityMasterSystem, EventAddressClassifications.AddressEvents);
		service.create(EventAddressClassifications.OccurredAt, activityMasterSystem, EventAddressClassifications.AddressEvents);
		service.create(EventAddressClassifications.RemoteAddress, activityMasterSystem, EventAddressClassifications.AddressEvents);
		service.create(EventAddressClassifications.LocalAddress, activityMasterSystem, EventAddressClassifications.AddressEvents);
		service.create(EventAddressClassifications.PhonedNumber, activityMasterSystem, EventAddressClassifications.AddressEvents);
		service.create(EventAddressClassifications.SentAFax, activityMasterSystem, EventAddressClassifications.AddressEvents);
		service.create(EventAddressClassifications.Emailed, activityMasterSystem, EventAddressClassifications.AddressEvents);
		service.create(EventAddressClassifications.SMSd, activityMasterSystem, EventAddressClassifications.AddressEvents);
		service.create(EventAddressClassifications.MMSd, activityMasterSystem, EventAddressClassifications.AddressEvents);
		service.create(EventAddressClassifications.Posted, activityMasterSystem, EventAddressClassifications.AddressEvents);
		service.create(EventAddressClassifications.RegisteredPost, activityMasterSystem, EventAddressClassifications.AddressEvents);
		
		service.create(EventAddressClassifications.AddedAddress, activityMasterSystem, EventAddressClassifications.AddressEvents);
		
		logProgress("Events System", "Loaded Event Address Classifications...", 1);
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventArrangementDefaultClassifications(IEnterprise<?,?> enterprise)
	{
		service.create(EventArrangementClassifications.ArrangementEvents, activityMasterSystem);
		service.create(EventArrangementClassifications.Started, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		service.create(EventArrangementClassifications.Concluded, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		service.create(EventArrangementClassifications.AffectedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		service.create(EventArrangementClassifications.RestartedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		service.create(EventArrangementClassifications.SkippedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		service.create(EventArrangementClassifications.AlteredRiskValue, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		logProgress("Events System", "Loaded Event Arrangements Classifications...", 1);
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventEventTypesClassifications(IEnterprise<?,?> enterprise)
	{
		service.create(EventTypeClassifications.TypeOfEvents, activityMasterSystem);
		service.create(EventTypeClassifications.HasTheType, activityMasterSystem, EventTypeClassifications.TypeOfEvents);
		service.create(EventTypeClassifications.CanBeIdentifiedBy, activityMasterSystem, EventTypeClassifications.TypeOfEvents);
		
		logProgress("Events System", "Loaded Event Event Types Classifications...", 1);
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventProductsDefaultClassifications(IEnterprise<?,?> enterprise)
	{
		service.create(EventProductClassifications.ProductEvent, activityMasterSystem);
		service.create(EventProductClassifications.ShowedInterestIn, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.Bought, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.Sold, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.MadeBidFor, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.ChangedBidFor, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.RemovedBidFor, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.Cancelled, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.DontShowProduct, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.RemindMeOfTheProduct, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.ChangedTheCostOf, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.AddedTheInterestOf, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.ChangedTheInterestOf, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.RatedTheProduct, activityMasterSystem, EventProductClassifications.ProductEvent);
		service.create(EventProductClassifications.ChangedTheRatingOfTheProduct, activityMasterSystem, EventProductClassifications.ProductEvent);
		
		logProgress("Events System", "Loaded Event Product Default Classifications...", 1);
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventResourceItemDefaultClassifications(IEnterprise<?,?> enterprise)
	{
		service.create(EventResourceItemClassifications.ResourceItemEvent, activityMasterSystem);
		service.create(EventResourceItemClassifications.AddedResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.ChangedTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.UpdatedTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.RemovedTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.RegisteredTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.RemovedTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.LodgedTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.DeliveredTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.DestroyedTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		
		service.create(EventResourceItemClassifications.JSONCallRequest, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.JSONCallResponse, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.WebServiceCallResponse, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.WebServiceCallRequest, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		
		service.create(EventResourceItemClassifications.HttpCallRequest, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.HttpCallResponse, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.HttpSession, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.HttpSessionProperties, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		service.create(EventResourceItemClassifications.UserAgent, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent);
		
		logProgress("Events System", "Loaded Event Resource Item Default Classifications...", 1);
	}
	
}
