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
import org.hibernate.reactive.mutiny.Mutiny;

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
	public Uni<Boolean> update(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Starting parallel creation of event classifications");
		logProgress("Events System", "Loading Base Events...", 1);

		// Chain all the classification creation operations
		return createEventInvolvedPartyDefaultClassifications(session, enterprise)
			.chain(() -> createEventAddressDefaultClassifications(session, enterprise))
			.chain(() -> createEventArrangementDefaultClassifications(session, enterprise))
			.chain(() -> createEventEventTypesClassifications(session, enterprise))
			.chain(() -> createEventProductsDefaultClassifications(session, enterprise))
			.chain(() -> createEventResourceItemDefaultClassifications(session, enterprise))
			.map(result -> true); // Return Boolean
	}

	@SuppressWarnings("WeakerAccess")
	Uni<Void> createEventInvolvedPartyDefaultClassifications(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Creating involved party event classifications");

		// Create the base InvolvedPartyEvents classification first
		return service.create(session, EventInvolvedPartiesClassifications.InvolvedPartyEvents, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for involved party event classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all involved party event classification creation operations to the list
				operations.add(service.create(session, EventInvolvedPartiesClassifications.PerformedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.OnBehalfOf, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.For, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.OwnedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.Created, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.Added, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.Updated, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.CreatedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.CompletedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.UpdatedBy, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.SecurityCredentialsOf, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.Notifies, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventInvolvedPartiesClassifications.MeantFor, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventClassifications.NotifiesInvolvedParty, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventClassifications.UpdatedPassword, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));
				operations.add(service.create(session, EventClassifications.UpdatedUsername, activityMasterSystem, EventInvolvedPartiesClassifications.InvolvedPartyEvents));

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
	Uni<Void> createEventAddressDefaultClassifications(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Creating address event classifications");

		// Create the base AddressEvents classification first
		return service.create(session, EventAddressClassifications.AddressEvents, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for address event classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all address event classification creation operations to the list
				operations.add(service.create(session, EventAddressClassifications.SignedAt, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(session, EventAddressClassifications.OccurredAt, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(session, EventAddressClassifications.RemoteAddress, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(session, EventAddressClassifications.LocalAddress, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(session, EventAddressClassifications.PhonedNumber, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(session, EventAddressClassifications.SentAFax, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(session, EventAddressClassifications.Emailed, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(session, EventAddressClassifications.SMSd, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(session, EventAddressClassifications.MMSd, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(session, EventAddressClassifications.Posted, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(session, EventAddressClassifications.RegisteredPost, activityMasterSystem, EventAddressClassifications.AddressEvents));
				operations.add(service.create(session, EventAddressClassifications.AddedAddress, activityMasterSystem, EventAddressClassifications.AddressEvents));

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
	Uni<Void> createEventArrangementDefaultClassifications(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Creating arrangement event classifications");

		// Create the base ArrangementEvents classification first
		return service.create(session, EventArrangementClassifications.ArrangementEvents, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for arrangement event classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all arrangement event classification creation operations to the list
				operations.add(service.create(session, EventArrangementClassifications.Started, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));
				operations.add(service.create(session, EventArrangementClassifications.Concluded, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));
				operations.add(service.create(session, EventArrangementClassifications.AffectedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));
				operations.add(service.create(session, EventArrangementClassifications.RestartedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));
				operations.add(service.create(session, EventArrangementClassifications.SkippedThe, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));
				operations.add(service.create(session, EventArrangementClassifications.AlteredRiskValue, activityMasterSystem, EventArrangementClassifications.ArrangementEvents));

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
	Uni<Void> createEventEventTypesClassifications(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Creating event type classifications");

		// Create the base TypeOfEvents classification first
		return service.create(session, EventTypeClassifications.TypeOfEvents, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for event type classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all event type classification creation operations to the list
				operations.add(service.create(session, EventTypeClassifications.HasTheType, activityMasterSystem, EventTypeClassifications.TypeOfEvents));
				operations.add(service.create(session, EventTypeClassifications.CanBeIdentifiedBy, activityMasterSystem, EventTypeClassifications.TypeOfEvents));

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
	Uni<Void> createEventProductsDefaultClassifications(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Creating product event classifications");

		// Create the base ProductEvent classification first
		return service.create(session, EventProductClassifications.ProductEvent, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for product event classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all product event classification creation operations to the list
				operations.add(service.create(session, EventProductClassifications.ShowedInterestIn, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.Bought, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.Sold, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.MadeBidFor, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.ChangedBidFor, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.RemovedBidFor, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.Cancelled, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.DontShowProduct, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.RemindMeOfTheProduct, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.ChangedTheCostOf, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.AddedTheInterestOf, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.ChangedTheInterestOf, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.RatedTheProduct, activityMasterSystem, EventProductClassifications.ProductEvent));
				operations.add(service.create(session, EventProductClassifications.ChangedTheRatingOfTheProduct, activityMasterSystem, EventProductClassifications.ProductEvent));

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
	Uni<Void> createEventResourceItemDefaultClassifications(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Creating resource item event classifications");

		// Create the base ResourceItemEvent classification first
		return service.create(session, EventResourceItemClassifications.ResourceItemEvent, activityMasterSystem)
			.chain(baseClassification -> {
				// Create a list of operations to run in parallel for resource item event classifications
				List<Uni<?>> operations = new ArrayList<>();

				// Add all resource item event classification creation operations to the list
				operations.add(service.create(session, EventResourceItemClassifications.AddedResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.ChangedTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.UpdatedTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.RemovedTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.RegisteredTheResourceItem, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.RemovedTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.LodgedTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.DeliveredTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.DestroyedTheResourceItemRegistration, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.JSONCallRequest, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.JSONCallResponse, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.WebServiceCallResponse, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.WebServiceCallRequest, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.HttpCallRequest, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.HttpCallResponse, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.HttpSession, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.HttpSessionProperties, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));
				operations.add(service.create(session, EventResourceItemClassifications.UserAgent, activityMasterSystem, EventResourceItemClassifications.ResourceItemEvent));

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
