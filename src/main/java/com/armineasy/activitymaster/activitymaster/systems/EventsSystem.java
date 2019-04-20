package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.classifications.events.EventAddressClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.events.EventArrangementClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.events.EventInvolvedPartiesClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.events.EventProductClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.events.EventResourceItemClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.events.EventTypeClassifications.*;

@SuppressWarnings("Duplicates")
public class EventsSystem
		implements IActivityMasterSystem<EventsSystem>
{

	private static final Map<Enterprise, UUID> systemTokens = new HashMap<>();

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		createEventInvolvedPartyDefaultClassifications(enterprise, progressMonitor);
		createEventAddressDefaultClassifications(enterprise, progressMonitor);
		createEventArrangementDefaultClassifications(enterprise, progressMonitor);
		createEventEventTypesClassifications(enterprise, progressMonitor);
		createEventProductsDefaultClassifications(enterprise, progressMonitor);
		createEventResourceItemDefaultClassifications(enterprise, progressMonitor);
	}

	@Override
	public int totalTasks()
	{
		return 0;
	}

	@SuppressWarnings("WeakerAccess")
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	void createEventInvolvedPartyDefaultClassifications(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		ClassificationService service = GuiceContext.get(ClassificationService.class);

		//Involved party relations with events
		service.create(InvolvedPartyEvents, activityMasterSystem);
		service.create(PerformedBy, activityMasterSystem,InvolvedPartyEvents);
		service.create(OnBehalfOf, activityMasterSystem,InvolvedPartyEvents);
		service.create(For, activityMasterSystem,InvolvedPartyEvents);
		service.create(OwnedBy, activityMasterSystem,InvolvedPartyEvents);
		service.create(CreatedBy, activityMasterSystem,InvolvedPartyEvents);
		service.create(UpdatedBy, activityMasterSystem,InvolvedPartyEvents);
		service.create(SecurityCredentialsOf, activityMasterSystem,InvolvedPartyEvents);
		service.create(Notifies, activityMasterSystem,InvolvedPartyEvents);
		service.create(MeantFor, activityMasterSystem,InvolvedPartyEvents);
		logProgress("Events System", "Loaded Event InvolvedParty Classifications...", 1, progressMonitor);

	}

	@SuppressWarnings("WeakerAccess")
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	void createEventAddressDefaultClassifications(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		ClassificationService service = GuiceContext.get(ClassificationService.class);
		service.create(AddressEvents, activityMasterSystem);
		service.create(SignedAt, activityMasterSystem,AddressEvents);
		service.create(OccurredAt,activityMasterSystem,AddressEvents);
		service.create(RemoteAddress,  activityMasterSystem,AddressEvents);
		service.create(LocalAddress,  activityMasterSystem,AddressEvents);
		service.create(PhonedNumber,  activityMasterSystem,AddressEvents);
		service.create(SentAFax,  activityMasterSystem,AddressEvents);
		service.create(Emailed,  activityMasterSystem,AddressEvents);
		service.create(SMSd,  activityMasterSystem,AddressEvents);
		service.create(MMSd,  activityMasterSystem,AddressEvents);
		service.create(Posted, activityMasterSystem,AddressEvents);
		service.create(RegisteredPost, activityMasterSystem,AddressEvents);

		logProgress("Events System", "Loaded Event Address Classifications...", 1, progressMonitor);
	}

	@SuppressWarnings("WeakerAccess")
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	void createEventArrangementDefaultClassifications(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);
		ClassificationService service = GuiceContext.get(ClassificationService.class);

		service.create(ArrangementEvents, activityMasterSystem);
		service.create(Started, activityMasterSystem,ArrangementEvents);
		service.create(Concluded, activityMasterSystem,ArrangementEvents);
		service.create(AffectedThe, activityMasterSystem,ArrangementEvents);
		service.create(RestartedThe, activityMasterSystem,ArrangementEvents);
		service.create(SkippedThe, activityMasterSystem,ArrangementEvents);
		service.create(AlteredRiskValue, activityMasterSystem,ArrangementEvents);
		logProgress("Events System", "Loaded Event Arrangements Classifications...", 1, progressMonitor);
	}

	@SuppressWarnings("WeakerAccess")
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	void createEventEventTypesClassifications(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);
		ClassificationService service = GuiceContext.get(ClassificationService.class);



		service.create(TypeOfEvents, activityMasterSystem);
		service.create(HasTheType, activityMasterSystem,TypeOfEvents);
		service.create(CanBeIdentifiedBy, activityMasterSystem,TypeOfEvents);

		logProgress("Events System", "Loaded Event Event Types Classifications...", 1, progressMonitor);
	}

	@SuppressWarnings("WeakerAccess")
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	void createEventProductsDefaultClassifications(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);
		ClassificationService service = GuiceContext.get(ClassificationService.class);

		service.create(ProductEvent, activityMasterSystem);
		service.create(ShowedInterestIn, activityMasterSystem,ShowedInterestIn);
		service.create(Bought, activityMasterSystem,ShowedInterestIn);
		service.create(Sold, activityMasterSystem,ShowedInterestIn);
		service.create(MadeBidFor, activityMasterSystem,ShowedInterestIn);
		service.create(ChangedBidFor, activityMasterSystem,ShowedInterestIn);
		service.create(RemovedBidFor, activityMasterSystem,ShowedInterestIn);
		service.create(Cancelled, activityMasterSystem,ShowedInterestIn);
		service.create(DontShowProduct, activityMasterSystem,ShowedInterestIn);
		service.create(RemindMeOfTheProduct, activityMasterSystem,ShowedInterestIn);
		service.create(ChangedTheCostOf, activityMasterSystem,ShowedInterestIn);
		service.create(AddedTheInterestOf, activityMasterSystem,ShowedInterestIn);
		service.create(ChangedTheInterestOf, activityMasterSystem,ShowedInterestIn);
		service.create(RatedTheProduct, activityMasterSystem,ShowedInterestIn);
		service.create(ChangedTheRatingOfTheProduct, activityMasterSystem,ShowedInterestIn);

		logProgress("Events System", "Loaded Event Product Default Classifications...", 1, progressMonitor);
	}

	@SuppressWarnings("WeakerAccess")
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	void createEventResourceItemDefaultClassifications(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);
		ClassificationService service = GuiceContext.get(ClassificationService.class);


		service.create(ResourceItemEvent,  activityMasterSystem);
		service.create(AddedResourceItem,  activityMasterSystem,ResourceItemEvent);
		service.create(ChangedTheResourceItem,  activityMasterSystem,ResourceItemEvent);
		service.create(UpdatedTheResourceItem,  activityMasterSystem,ResourceItemEvent);
		service.create(RemovedTheResourceItem,  activityMasterSystem,ResourceItemEvent);
		service.create(RegisteredTheResourceItem,  activityMasterSystem,ResourceItemEvent);
		service.create(RemovedTheResourceItemRegistration,  activityMasterSystem,ResourceItemEvent);
		service.create(LodgedTheResourceItemRegistration,  activityMasterSystem,ResourceItemEvent);
		service.create(DeliveredTheResourceItemRegistration,  activityMasterSystem,ResourceItemEvent);
		service.create(DestroyedTheResourceItemRegistration,  activityMasterSystem,ResourceItemEvent);

		service.create(JSONCallRequest,  activityMasterSystem,ResourceItemEvent);
		service.create(JSONCallResponse, activityMasterSystem,ResourceItemEvent);
		service.create(WebServiceCallResponse,  activityMasterSystem,ResourceItemEvent);
		service.create(WebServiceCallRequest,  activityMasterSystem,ResourceItemEvent);

		service.create(HttpCallRequest,  activityMasterSystem,ResourceItemEvent);
		service.create(HttpCallResponse,  activityMasterSystem,ResourceItemEvent);
		service.create(HttpSession,  activityMasterSystem,ResourceItemEvent);
		service.create(HttpSessionProperties,  activityMasterSystem,ResourceItemEvent);
		service.create(UserAgent,  activityMasterSystem,ResourceItemEvent);

		logProgress("Events System", "Loaded Event Resource Item Default Classifications...", 1, progressMonitor);
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 9;
	}


	@Override
	public void postUpdate(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems newSystem = GuiceContext.get(SystemsService.class)
		                                .create(enterprise, "Events System",
		                                        "The system for managing events", "");
		UUID securityToken = GuiceContext.get(SystemsSystem.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
	}

	public static Map<Enterprise, UUID> getSystemTokens()
	{
		return systemTokens;
	}

}
