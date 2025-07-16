package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.address.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressClassifications.*;
import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressTelephoneClassifications.*;

@SortedUpdate(sortOrder = -400, taskCount = 15)
@Log4j2
public class AddressBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;

	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;

	@Override
	public Uni<Boolean> update(IEnterprise<?,?> enterprise)
	{
		log.info("Starting parallel creation of address classifications");

		// Chain all the classification creation operations
		return createDefaultTelephones(enterprise)
			.chain(() -> createDefaultInternetAddresses(enterprise))
			.chain(() -> createDefaultPhysicalAddresses(enterprise))
			.map(result -> true); // Return Boolean
	}

	@SuppressWarnings("Duplicates")
	private Uni<Void> createDefaultTelephones(IEnterprise<?,?> enterprise)
	{
		log.info("Creating telephone address classifications");

		// Create the base Address classification first
		return service.create(AddressClassifications.Address, activityMasterSystem)
			.chain(baseAddress -> {
				// Create a list of operations to run in parallel for address type classifications
				List<Uni<?>> addressTypeOperations = new ArrayList<>();

				// Add all address type classification creation operations to the list
				addressTypeOperations.add(service.create(ContactAddress, activityMasterSystem, AddressClassifications.Address));
				addressTypeOperations.add(service.create(PostalAddress, activityMasterSystem, AddressClassifications.Address));
				addressTypeOperations.add(service.create(CallAddress, activityMasterSystem, AddressClassifications.Address));
				addressTypeOperations.add(service.create(InternetAddress, activityMasterSystem, AddressClassifications.Address));
				addressTypeOperations.add(service.create(LocationAddress, activityMasterSystem, AddressClassifications.Address));

				log.info("Running {} address type classification creation operations in parallel", addressTypeOperations.size());

				// Run all address type operations in parallel
				return Uni.combine().all().unis(addressTypeOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating address type classifications: {}", error.getMessage(), error))
					.chain(() -> {
						// Create the TelephoneNumber classification
						return service.create(TelephoneNumber, activityMasterSystem, CallAddress)
							.chain(telephoneNumber -> {
								// Create a list of operations to run in parallel for telephone number classifications
								List<Uni<?>> telephoneOperations = new ArrayList<>();

								// Add all telephone number classification creation operations to the list
								// Home telephone numbers
								telephoneOperations.add(service.create(AddressTelephoneClassifications.HomeTelephoneNumber, activityMasterSystem, TelephoneNumber));
								telephoneOperations.add(service.create(AddressTelephoneClassifications.HomeCellNumber, activityMasterSystem, TelephoneNumber));
								telephoneOperations.add(service.create(AddressTelephoneClassifications.HomePagerNumber, activityMasterSystem, TelephoneNumber));
								telephoneOperations.add(service.create(AddressTelephoneClassifications.HomeFaxNumber, activityMasterSystem, TelephoneNumber));

								// Business telephone numbers
								telephoneOperations.add(service.create(AddressTelephoneClassifications.BusinessTelephoneNumber, activityMasterSystem, TelephoneNumber));
								telephoneOperations.add(service.create(AddressTelephoneClassifications.BusinessCellNumber, activityMasterSystem, TelephoneNumber));
								telephoneOperations.add(service.create(AddressTelephoneClassifications.BusinessPagerNumber, activityMasterSystem, TelephoneNumber));
								telephoneOperations.add(service.create(AddressTelephoneClassifications.BusinessFaxNumber, activityMasterSystem, TelephoneNumber));

								// Legal telephone numbers
								telephoneOperations.add(service.create(AddressTelephoneClassifications.LegalTelephoneNumber, activityMasterSystem, TelephoneNumber));
								telephoneOperations.add(service.create(AddressTelephoneClassifications.LegalCellNumber, activityMasterSystem, TelephoneNumber));
								telephoneOperations.add(service.create(AddressTelephoneClassifications.LegalPagerNumber, activityMasterSystem, TelephoneNumber));
								telephoneOperations.add(service.create(AddressTelephoneClassifications.LegalFaxNumber, activityMasterSystem, TelephoneNumber));

								// Telephone details
								telephoneOperations.add(service.create(AddressTelephoneClassifications.TelephoneCountryCode, activityMasterSystem, TelephoneNumber));
								telephoneOperations.add(service.create(AddressTelephoneClassifications.TelephoneExtensionNumber, activityMasterSystem, TelephoneNumber));
								telephoneOperations.add(service.create(AddressTelephoneClassifications.TelephoneAreaCode, activityMasterSystem, TelephoneNumber));

								log.info("Running {} telephone number classification creation operations in parallel", telephoneOperations.size());

								// Run all telephone operations in parallel
								return Uni.combine().all().unis(telephoneOperations)
									.discardItems()
									.onFailure().invoke(error -> log.error("Error creating telephone number classifications: {}", error.getMessage(), error))
									.invoke(() -> logProgress("Address System", "Done Telephones", 1))
									.map(result -> null); // Return Void
							});
					});
			});
	}

	@SuppressWarnings("Duplicates")
	private Uni<Void> createDefaultInternetAddresses(IEnterprise<?,?> enterprise)
	{
		log.info("Creating internet address classifications");
		logProgress("Address System", "Starting Internet Address Checks");

		log.info("Creating InternalAddress, RemoteAddress, LocalAddress, ExternalAddress, EmailAddress, and WebAddress classifications in parallel");

		// Create InternalAddress classification and its children
		Uni<Void> internalAddressUni = service.create(AddressInternalSystemClassifications.InternalAddress, activityMasterSystem, InternetAddress)
			.chain(internalAddress -> {
				// Create a list of operations to run in parallel for internal address classifications
				List<Uni<?>> internalAddressOperations = new ArrayList<>();

				// Add all internal address classification creation operations to the list
				internalAddressOperations.add(service.create(AddressInternalSystemClassifications.InternalAddressHostName, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress));
				internalAddressOperations.add(service.create(AddressInternalSystemClassifications.InternalAddressIPAddress, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress));
				internalAddressOperations.add(service.create(AddressInternalSystemClassifications.InternalAddressSubnet, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress));
				internalAddressOperations.add(service.create(AddressInternalSystemClassifications.InternalAddressDns, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress));
				internalAddressOperations.add(service.create(AddressInternalSystemClassifications.InternalAddressGateway, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress));

				log.info("Running {} internal address classification creation operations in parallel", internalAddressOperations.size());

				// Run all internal address operations in parallel
				return Uni.combine().all().unis(internalAddressOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating internal address classifications: {}", error.getMessage(), error))
					.map(result -> null); // Return Void
			});

		// Create RemoteAddress classification and its children
		Uni<Void> remoteAddressUni = service.create(AddressRemoteSystemClassifications.RemoteAddress, activityMasterSystem, InternetAddress)
			.chain(remoteAddress -> {
				// Create a list of operations to run in parallel for remote address classifications
				List<Uni<?>> remoteAddressOperations = new ArrayList<>();

				// Add all remote address classification creation operations to the list
				remoteAddressOperations.add(service.create(AddressRemoteSystemClassifications.RemoteAddressHostName, activityMasterSystem, AddressRemoteSystemClassifications.RemoteAddress));
				remoteAddressOperations.add(service.create(AddressRemoteSystemClassifications.RemoteAddressIPAddress, activityMasterSystem, AddressRemoteSystemClassifications.RemoteAddress));

				log.info("Running {} remote address classification creation operations in parallel", remoteAddressOperations.size());

				// Run all remote address operations in parallel
				return Uni.combine().all().unis(remoteAddressOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating remote address classifications: {}", error.getMessage(), error))
					.map(result -> null); // Return Void
			});

		// Create LocalAddress classification and its children
		Uni<Void> localAddressUni = service.create(AddressLocalSystemClassifications.LocalAddress, activityMasterSystem, InternetAddress)
			.chain(localAddress -> {
				// Create a list of operations to run in parallel for local address classifications
				List<Uni<?>> localAddressOperations = new ArrayList<>();

				// Add all local address classification creation operations to the list
				localAddressOperations.add(service.create(AddressLocalSystemClassifications.LocalAddressHostName, activityMasterSystem, AddressLocalSystemClassifications.LocalAddress));
				localAddressOperations.add(service.create(AddressLocalSystemClassifications.LocalAddressIPAddress, activityMasterSystem, AddressLocalSystemClassifications.LocalAddress));

				log.info("Running {} local address classification creation operations in parallel", localAddressOperations.size());

				// Run all local address operations in parallel
				return Uni.combine().all().unis(localAddressOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating local address classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Address System", "Done Internal Addresses", 1))
					.map(result -> null); // Return Void
			});

		// Create ExternalAddress classification and its children
		Uni<Void> externalAddressUni = service.create(AddressExternalSystemClassifications.ExternalAddress, activityMasterSystem, InternetAddress)
			.chain(externalAddress -> {
				// Create a list of operations to run in parallel for external address classifications
				List<Uni<?>> externalAddressOperations = new ArrayList<>();

				// Add all external address classification creation operations to the list
				externalAddressOperations.add(service.create(AddressExternalSystemClassifications.ExternalAddressHostName, activityMasterSystem, AddressExternalSystemClassifications.ExternalAddress));
				externalAddressOperations.add(service.create(AddressExternalSystemClassifications.ExternalAddressIPAddress, activityMasterSystem, AddressExternalSystemClassifications.ExternalAddress));

				log.info("Running {} external address classification creation operations in parallel", externalAddressOperations.size());

				// Run all external address operations in parallel
				return Uni.combine().all().unis(externalAddressOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating external address classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Address System", "Done External Addresses", 1))
					.map(result -> null); // Return Void
			});

		// Create EmailAddress classification and its children
		Uni<Void> emailAddressUni = service.create(AddressEmailClassifications.EmailAddress, activityMasterSystem, ContactAddress)
			.chain(emailAddress -> {
				// Create a list of operations to run in parallel for email address classifications
				List<Uni<?>> emailAddressOperations = new ArrayList<>();

				// Add all email address classification creation operations to the list
				emailAddressOperations.add(service.create(AddressEmailClassifications.PersonalEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress));
				emailAddressOperations.add(service.create(AddressEmailClassifications.BusinessEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress));
				emailAddressOperations.add(service.create(AddressEmailClassifications.LegalEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress));
				emailAddressOperations.add(service.create(AddressEmailClassifications.EmailAddressDomain, activityMasterSystem, AddressEmailClassifications.EmailAddress));
				emailAddressOperations.add(service.create(AddressEmailClassifications.EmailAddressHost, activityMasterSystem, AddressEmailClassifications.EmailAddress));
				emailAddressOperations.add(service.create(AddressEmailClassifications.EmailAddressUser, activityMasterSystem, AddressEmailClassifications.EmailAddress));

				log.info("Running {} email address classification creation operations in parallel", emailAddressOperations.size());

				// Run all email address operations in parallel
				return Uni.combine().all().unis(emailAddressOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating email address classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Address System", "Done Email Addresses", 1))
					.map(result -> null); // Return Void
			});

		// Create WebAddress classification and its children
		Uni<Void> webAddressUni = service.create(AddressWebClassifications.WebAddress, activityMasterSystem, InternetAddress)
			.chain(webAddress -> {
				// Create a list of operations to run in parallel for web address classifications
				List<Uni<?>> webAddressOperations = new ArrayList<>();

				// Add all web address classification creation operations to the list
				webAddressOperations.add(service.create(AddressWebClassifications.WebAddressProtocol, activityMasterSystem, AddressWebClassifications.WebAddress));
				webAddressOperations.add(service.create(AddressWebClassifications.WebAddressPort, activityMasterSystem, AddressWebClassifications.WebAddress));
				webAddressOperations.add(service.create(AddressWebClassifications.WebAddressSubDomain, activityMasterSystem, AddressWebClassifications.WebAddress));
				webAddressOperations.add(service.create(AddressWebClassifications.WebAddressDomain, activityMasterSystem, AddressWebClassifications.WebAddress));
				webAddressOperations.add(service.create(AddressWebClassifications.WebAddressUrl, activityMasterSystem, AddressWebClassifications.WebAddress));
				webAddressOperations.add(service.create(AddressWebClassifications.WebAddressQueryParameters, activityMasterSystem, AddressWebClassifications.WebAddress));
				webAddressOperations.add(service.create(AddressWebClassifications.WebAddressSite, activityMasterSystem, AddressWebClassifications.WebAddress));

				log.info("Running {} web address classification creation operations in parallel", webAddressOperations.size());

				// Run all web address operations in parallel
				return Uni.combine().all().unis(webAddressOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating web address classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Address System", "Done Web Addresses", 1))
					.map(result -> null); // Return Void
			});

		// Run all address classification creations in parallel
		return Uni.combine().all().unis(internalAddressUni, remoteAddressUni, localAddressUni, externalAddressUni, emailAddressUni, webAddressUni)
			.discardItems()
			.onFailure().invoke(error -> log.error("Error creating internet address classifications: {}", error.getMessage(), error))
			.map(result -> null); // Return Void
	}

	@SuppressWarnings("Duplicates")
	private Uni<Void> createDefaultPhysicalAddresses(IEnterprise<?,?> enterprise)
	{
		log.info("Creating physical address classifications");
		logProgress("Address System", "Starting Physical Address Checks");

		log.info("Creating BuildingAddress, BoxAddress, and LegalAddress classifications in parallel");

		// Create BuildingAddress classification and its children
		Uni<Void> buildingAddressUni = service.create(AddressBuildingClassifications.BuildingAddress, activityMasterSystem, LocationAddress)
			.chain(buildingAddress -> {
				// Create a list of operations to run in parallel for building address classifications
				List<Uni<?>> buildingAddressOperations = new ArrayList<>();

				// Add all building address classification creation operations to the list
				buildingAddressOperations.add(service.create(AddressBuildingClassifications.BuildingDesk, activityMasterSystem, AddressBuildingClassifications.BuildingAddress));
				buildingAddressOperations.add(service.create(AddressBuildingClassifications.BuildingIsle, activityMasterSystem, AddressBuildingClassifications.BuildingAddress));
				buildingAddressOperations.add(service.create(AddressBuildingClassifications.BuildingFloor, activityMasterSystem, AddressBuildingClassifications.BuildingAddress));
				buildingAddressOperations.add(service.create(AddressBuildingClassifications.BuildingWindow, activityMasterSystem, AddressBuildingClassifications.BuildingAddress));
				buildingAddressOperations.add(service.create(AddressBuildingClassifications.BuildingIdentifer, activityMasterSystem, AddressBuildingClassifications.BuildingAddress));
				buildingAddressOperations.add(service.create(AddressBuildingClassifications.BuildingNumber, activityMasterSystem, AddressBuildingClassifications.BuildingAddress));
				buildingAddressOperations.add(service.create(AddressBuildingClassifications.BuildingStreet, activityMasterSystem, AddressBuildingClassifications.BuildingAddress));
				buildingAddressOperations.add(service.create(AddressBuildingClassifications.BuildingStreetType, activityMasterSystem, AddressBuildingClassifications.BuildingAddress));

				log.info("Running {} building address classification creation operations in parallel", buildingAddressOperations.size());

				// Run all building address operations in parallel
				return Uni.combine().all().unis(buildingAddressOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating building address classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Address System", "Done Building Addresses", 1))
					.map(result -> null); // Return Void
			});

		// Create BoxAddress classification and its children
		Uni<Void> boxAddressUni = service.create(AddressBoxClassifications.BoxAddress, activityMasterSystem, PostalAddress)
			.chain(boxAddress -> {
				// Create a list of operations to run in parallel for box address classifications
				List<Uni<?>> boxAddressOperations = new ArrayList<>();

				// Add all box address classification creation operations to the list
				boxAddressOperations.add(service.create(AddressBoxClassifications.BoxNumber, activityMasterSystem, AddressBoxClassifications.BoxAddress));
				boxAddressOperations.add(service.create(AddressBoxClassifications.BoxIdentifier, activityMasterSystem, AddressBoxClassifications.BoxAddress));
				boxAddressOperations.add(service.create(AddressBoxClassifications.BoxCity, activityMasterSystem, AddressBoxClassifications.BoxAddress));
				boxAddressOperations.add(service.create(AddressBoxClassifications.BoxPostalCode, activityMasterSystem, AddressBoxClassifications.BoxAddress));

				log.info("Running {} box address classification creation operations in parallel", boxAddressOperations.size());

				// Run all box address operations in parallel
				return Uni.combine().all().unis(boxAddressOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating box address classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Address System", "Done Box Addresses", 1))
					.map(result -> null); // Return Void
			});

		// Create LegalAddress classification and its children
		Uni<Void> legalAddressUni = service.create(AddressLegalClassifications.LegalAddress, activityMasterSystem, PostalAddress)
			.chain(legalAddress -> {
				// Create a list of operations to run in parallel for legal address classifications
				List<Uni<?>> legalAddressOperations = new ArrayList<>();

				// Add all legal address classification creation operations to the list
				legalAddressOperations.add(service.create(AddressLegalClassifications.LegalDistrictNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress));
				legalAddressOperations.add(service.create(AddressLegalClassifications.LegalLotNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress));
				legalAddressOperations.add(service.create(AddressLegalClassifications.LegalBlockNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress));

				log.info("Running {} legal address classification creation operations in parallel", legalAddressOperations.size());

				// Run all legal address operations in parallel
				return Uni.combine().all().unis(legalAddressOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating legal address classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Address System", "Done Legal Addresses", 1))
					.map(result -> null); // Return Void
			});

		// Run BuildingAddress, BoxAddress, and LegalAddress creation in parallel
		return Uni.combine().all().unis(buildingAddressUni, boxAddressUni, legalAddressUni)
			.discardItems()
			.onFailure().invoke(error -> log.error("Error creating physical address classifications: {}", error.getMessage(), error))
			.map(result -> null); // Return Void
	}

}
