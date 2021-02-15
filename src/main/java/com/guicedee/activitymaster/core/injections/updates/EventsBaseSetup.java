package com.guicedee.activitymaster.core.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.classifications.events.*;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.updates.DatedUpdate;
import com.guicedee.activitymaster.core.updates.ISystemUpdate;

import static com.guicedee.activitymaster.core.SystemsService.*;

@DatedUpdate(date = "2016/02/05", taskCount = 3)
public class EventsBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?> activityMasterSystem;
	
	@Override
	public void update(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Events System", "Loading Base Events...", 1, progressMonitor);
		createEventInvolvedPartyDefaultClassifications(enterprise, progressMonitor);
		createEventAddressDefaultClassifications(enterprise, progressMonitor);
		createEventArrangementDefaultClassifications(enterprise, progressMonitor);
		createEventEventTypesClassifications(enterprise, progressMonitor);
		createEventProductsDefaultClassifications(enterprise, progressMonitor);
		createEventResourceItemDefaultClassifications(enterprise, progressMonitor);
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventInvolvedPartyDefaultClassifications(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
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
		
		logProgress("Events System", "Loaded Event InvolvedParty Classifications...", 1, progressMonitor);
		
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventAddressDefaultClassifications(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
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
		
		logProgress("Events System", "Loaded Event Address Classifications...", 1, progressMonitor);
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventArrangementDefaultClassifications(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		service.create(EventArrangementClassifications.ArrangementEvents, activityMasterSystem);
		service.create(EventArrangementClassifications.Started, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		service.create(EventArrangementClassifications.Concluded, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		service.create(EventArrangementClassifications.AffectedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		service.create(EventArrangementClassifications.RestartedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		service.create(EventArrangementClassifications.SkippedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		service.create(EventArrangementClassifications.AlteredRiskValue, activityMasterSystem, EventArrangementClassifications.ArrangementEvents);
		logProgress("Events System", "Loaded Event Arrangements Classifications...", 1, progressMonitor);
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventEventTypesClassifications(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		service.create(EventTypeClassifications.TypeOfEvents, activityMasterSystem);
		service.create(EventTypeClassifications.HasTheType, activityMasterSystem, EventTypeClassifications.TypeOfEvents);
		service.create(EventTypeClassifications.CanBeIdentifiedBy, activityMasterSystem, EventTypeClassifications.TypeOfEvents);
		
		logProgress("Events System", "Loaded Event Event Types Classifications...", 1, progressMonitor);
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventProductsDefaultClassifications(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
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
		
		logProgress("Events System", "Loaded Event Product Default Classifications...", 1, progressMonitor);
	}
	
	@SuppressWarnings("WeakerAccess")
	void createEventResourceItemDefaultClassifications(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
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
		
		logProgress("Events System", "Loaded Event Resource Item Default Classifications...", 1, progressMonitor);
	}
	
}
