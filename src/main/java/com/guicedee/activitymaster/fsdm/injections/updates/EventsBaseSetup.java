package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;

@SortedUpdate(sortOrder = -100, taskCount = 3)
@Log4j2
public class EventsBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;

	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;

	@Override
	public Uni<Boolean> update(IEnterprise<?,?> enterprise)
	{
		log.info("Starting parallel creation of event classifications");
		logProgress("Events System", "Loading Base Events...", 1);

		// Chain all the classification creation operations
		return createEventInvolvedPartyDefaultClassifications(enterprise)
			.chain(() -> createEventAddressDefaultClassifications(enterprise))
			.chain(() -> createEventArrangementDefaultClassifications(enterprise))
			.chain(() -> createEventEventTypesClassifications(enterprise))
			.chain(() -> createEventProductsDefaultClassifications(enterprise))
			.chain(() -> createEventResourceItemDefaultClassifications(enterprise))
			.map(result -> true); // Return Boolean
	}

	@SuppressWarnings("WeakerAccess")
	Uni<Void> createEventInvolvedPartyDefaultClassifications(IEnterprise<?,?> enterprise)
	{
		log.info("Creating involved party event classifications");

		// Create the base InvolvedPartyEvents classification first
		return service.create(EventInvolvedPartiesClassifications.InvolvedPartyEvents, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for involved party event classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all involved party event classification creation operations to the list
				operations.add(service.create(EventInvolvedPartiesClassifications.PerformedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.OnBehalfOf, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.For, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.OwnedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.Created, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.Added, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.Updated, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.CreatedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.CompletedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.UpdatedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.SecurityCredentialsOf, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.Notifies, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventInvolvedPartiesClassifications.MeantFor, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventClassifications.NotifiesInvolvedParty, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventClassifications.UpdatedPassword, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(EventClassifications.UpdatedUsername, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));

				log.info("Running {} involved party event classification creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating involved party event classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Events System", "Loaded Event InvolvedParty Classifications...", 1))
					.map(result -> null); // Return Void
			});
	}

	@SuppressWarnings("WeakerAccess")
	Uni<Void> createEventAddressDefaultClassifications(IEnterprise<?,?> enterprise)
	{
		log.info("Creating address event classifications");

		// Create the base AddressEvents classification first
		return service.create(EventAddressClassifications.AddressEvents, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for address event classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all address event classification creation operations to the list
				operations.add(service.create(EventAddressClassifications.SignedAt, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(EventAddressClassifications.OccurredAt, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(EventAddressClassifications.RemoteAddress, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(EventAddressClassifications.LocalAddress, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(EventAddressClassifications.PhonedNumber, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(EventAddressClassifications.SentAFax, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(EventAddressClassifications.Emailed, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(EventAddressClassifications.SMSd, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(EventAddressClassifications.MMSd, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(EventAddressClassifications.Posted, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(EventAddressClassifications.RegisteredPost, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(EventAddressClassifications.AddedAddress, activityMasterSystem, EventAddressClassifications.AddressEvents));

				log.info("Running {} address event classification creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating address event classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Events System", "Loaded Event Address Classifications...", 1))
					.map(result -> null); // Return Void
			});
	}

	@SuppressWarnings("WeakerAccess")
	Uni<Void> createEventArrangementDefaultClassifications(IEnterprise<?,?> enterprise)
	{
		log.info("Creating arrangement event classifications");

		// Create the base ArrangementEvents classification first
		return service.create(EventArrangementClassifications.ArrangementEvents, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for arrangement event classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all arrangement event classification creation operations to the list
				operations.add(service.create(EventArrangementClassifications.Started, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));
				operations.add(service.create(EventArrangementClassifications.Concluded, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));
				operations.add(service.create(EventArrangementClassifications.AffectedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));
				operations.add(service.create(EventArrangementClassifications.RestartedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));
				operations.add(service.create(EventArrangementClassifications.SkippedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));
				operations.add(service.create(EventArrangementClassifications.AlteredRiskValue, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));

				log.info("Running {} arrangement event classification creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating arrangement event classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Events System", "Loaded Event Arrangements Classifications...", 1))
					.map(result -> null); // Return Void
			});
	}

	@SuppressWarnings("WeakerAccess")
	Uni<Void> createEventEventTypesClassifications(IEnterprise<?,?> enterprise)
	{
		log.info("Creating event type classifications");

		// Create the base TypeOfEvents classification first
		return service.create(EventTypeClassifications.TypeOfEvents, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for event type classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all event type classification creation operations to the list
				operations.add(service.create(EventTypeClassifications.HasTheType, activityMasterSystem, EventTypeClassifications.TypeOfEvents));
				operations.add(service.create(EventTypeClassifications.CanBeIdentifiedBy, activityMasterSystem, EventTypeClassifications.TypeOfEvents));

				log.info("Running {} event type classification creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating event type classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Events System", "Loaded Event Event Types Classifications...", 1))
					.map(result -> null); // Return Void
			});
	}

	@SuppressWarnings("WeakerAccess")
	Uni<Void> createEventProductsDefaultClassifications(IEnterprise<?,?> enterprise)
	{
		log.info("Creating product event classifications");

		// Create the base ProductEvent classification first
		return service.create(EventProductClassifications.ProductEvent, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for product event classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all product event classification creation operations to the list
				operations.add(service.create(EventProductClassifications.ShowedInterestIn, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.Bought, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.Sold, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.MadeBidFor, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.ChangedBidFor, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.RemovedBidFor, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.Cancelled, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.DontShowProduct, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.RemindMeOfTheProduct, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.ChangedTheCostOf, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.AddedTheInterestOf, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.ChangedTheInterestOf, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.RatedTheProduct, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(EventProductClassifications.ChangedTheRatingOfTheProduct, activityMasterSystem, EventProductClassifications.ProductEvent));

				log.info("Running {} product event classification creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating product event classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Events System", "Loaded Event Product Default Classifications...", 1))
					.map(result -> null); // Return Void
			});
	}

	@SuppressWarnings("WeakerAccess")
	Uni<Void> createEventResourceItemDefaultClassifications(IEnterprise<?,?> enterprise)
	{
		log.info("Creating resource item event classifications");

		// Create the base ResourceItemEvent classification first
		return service.create(EventResourceItemClassifications.ResourceItemEvent, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for resource item event classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all resource item event classification creation operations to the list
				operations.add(service.create(EventResourceItemClassifications.AddedResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.ChangedTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.UpdatedTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.RemovedTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.RegisteredTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.RemovedTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.LodgedTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.DeliveredTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.DestroyedTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.JSONCallRequest, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.JSONCallResponse, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.WebServiceCallResponse, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.WebServiceCallRequest, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.HttpCallRequest, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.HttpCallResponse, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.HttpSession, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.HttpSessionProperties, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(EventResourceItemClassifications.UserAgent, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));

				log.info("Running {} resource item event classification creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating resource item event classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Events System", "Loaded Event Resource Item Default Classifications...", 1))
					.map(result -> null); // Return Void
			});
	}

}
