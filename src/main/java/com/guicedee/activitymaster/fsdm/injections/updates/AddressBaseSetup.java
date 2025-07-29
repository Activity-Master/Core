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
import org.hibernate.reactive.mutiny.Mutiny;

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
	public Uni<Boolean> update(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("📋 Starting sequential creation of address classifications with session: {}", session.hashCode());

		// Chain all the classification creation operations sequentially
		return createDefaultTelephones(session, enterprise)
			.chain(() -> createDefaultInternetAddresses(session, enterprise))
			.chain(() -> createDefaultPhysicalAddresses(session, enterprise))
			.onItem().invoke(() -> log.info("✅ Successfully completed all address classification operations"))
			.onFailure().invoke(error -> log.error("❌ Error creating address classifications: {}", error.getMessage(), error))
			.map(result -> true); // Return Boolean
	}

	@SuppressWarnings("Duplicates")
	private Uni<Void> createDefaultTelephones(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Creating telephone address classifications");

		// Create the base Address classification first
		return service.create(session, AddressClassifications.Address, activityMasterSystem)
			.chain(baseAddress -> {
				log.info("📋 Creating address type classifications sequentially");
				
				// Chain address type classification creation operations sequentially
				return service.create(session, ContactAddress, activityMasterSystem, AddressClassifications.Address)
					.chain(() -> service.create(session, PostalAddress, activityMasterSystem, AddressClassifications.Address))
					.chain(() -> service.create(session, CallAddress, activityMasterSystem, AddressClassifications.Address))
					.chain(() -> service.create(session, InternetAddress, activityMasterSystem, AddressClassifications.Address))
					.chain(() -> service.create(session, LocationAddress, activityMasterSystem, AddressClassifications.Address))
					.onItem().invoke(() -> log.info("✅ Successfully created all address type classifications"))
					.onFailure().invoke(error -> log.error("❌ Error creating address type classifications: {}", error.getMessage(), error))
					.chain(() -> {
						// Create the TelephoneNumber classification
						return service.create(session, TelephoneNumber, activityMasterSystem, CallAddress)
							.chain(telephoneNumber -> {
								log.info("📋 Creating telephone number classifications sequentially");
								
								// Chain telephone number classification creation operations sequentially
								// Start with home telephone numbers
								return service.create(session, AddressTelephoneClassifications.HomeTelephoneNumber, activityMasterSystem, TelephoneNumber)
									.chain(() -> service.create(session, AddressTelephoneClassifications.HomeCellNumber, activityMasterSystem, TelephoneNumber))
									.chain(() -> service.create(session, AddressTelephoneClassifications.HomePagerNumber, activityMasterSystem, TelephoneNumber))
									.chain(() -> service.create(session, AddressTelephoneClassifications.HomeFaxNumber, activityMasterSystem, TelephoneNumber))
									
									// Business telephone numbers
									.chain(() -> service.create(session, AddressTelephoneClassifications.BusinessTelephoneNumber, activityMasterSystem, TelephoneNumber))
									.chain(() -> service.create(session, AddressTelephoneClassifications.BusinessCellNumber, activityMasterSystem, TelephoneNumber))
									.chain(() -> service.create(session, AddressTelephoneClassifications.BusinessPagerNumber, activityMasterSystem, TelephoneNumber))
									.chain(() -> service.create(session, AddressTelephoneClassifications.BusinessFaxNumber, activityMasterSystem, TelephoneNumber))
									
									// Legal telephone numbers
									.chain(() -> service.create(session, AddressTelephoneClassifications.LegalTelephoneNumber, activityMasterSystem, TelephoneNumber))
									.chain(() -> service.create(session, AddressTelephoneClassifications.LegalCellNumber, activityMasterSystem, TelephoneNumber))
									.chain(() -> service.create(session, AddressTelephoneClassifications.LegalPagerNumber, activityMasterSystem, TelephoneNumber))
									.chain(() -> service.create(session, AddressTelephoneClassifications.LegalFaxNumber, activityMasterSystem, TelephoneNumber))
									
									// Telephone details
									.chain(() -> service.create(session, AddressTelephoneClassifications.TelephoneCountryCode, activityMasterSystem, TelephoneNumber))
									.chain(() -> service.create(session, AddressTelephoneClassifications.TelephoneExtensionNumber, activityMasterSystem, TelephoneNumber))
									.chain(() -> service.create(session, AddressTelephoneClassifications.TelephoneAreaCode, activityMasterSystem, TelephoneNumber))
									.onItem().invoke(() -> {
										log.info("✅ Successfully created all telephone number classifications");
										logProgress("Address System", "Done Telephones", 1);
									})
									.onFailure().invoke(error -> log.error("❌ Error creating telephone number classifications: {}", error.getMessage(), error))
									.replaceWithVoid();
							});
					});
			});
	}

	@SuppressWarnings("Duplicates")
	private Uni<Void> createDefaultInternetAddresses(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Creating internet address classifications");
		logProgress("Address System", "Starting Internet Address Checks");

		log.info("📋 Creating internet address classifications sequentially");

		// Create all address types and their children sequentially
		// Start with InternalAddress classification and its children
		return service.create(session, AddressInternalSystemClassifications.InternalAddress, activityMasterSystem, InternetAddress)
			.chain(internalAddress -> {
				log.info("📋 Creating internal address classifications sequentially");
				
				// Chain internal address classification creation operations sequentially
				return service.create(session, AddressInternalSystemClassifications.InternalAddressHostName, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress)
					.chain(() -> service.create(session, AddressInternalSystemClassifications.InternalAddressIPAddress, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress))
					.chain(() -> service.create(session, AddressInternalSystemClassifications.InternalAddressSubnet, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress))
					.chain(() -> service.create(session, AddressInternalSystemClassifications.InternalAddressDns, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress))
					.chain(() -> service.create(session, AddressInternalSystemClassifications.InternalAddressGateway, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress))
					.onItem().invoke(() -> log.info("✅ Successfully created all internal address classifications"))
					.onFailure().invoke(error -> log.error("❌ Error creating internal address classifications: {}", error.getMessage(), error));
			})
			
			// Then create RemoteAddress classification and its children
			.chain(() -> service.create(session, AddressRemoteSystemClassifications.RemoteAddress, activityMasterSystem, InternetAddress))
			.chain(remoteAddress -> {
				log.info("📋 Creating remote address classifications sequentially");
				
				// Chain remote address classification creation operations sequentially
				return service.create(session, AddressRemoteSystemClassifications.RemoteAddressHostName, activityMasterSystem, AddressRemoteSystemClassifications.RemoteAddress)
					.chain(() -> service.create(session, AddressRemoteSystemClassifications.RemoteAddressIPAddress, activityMasterSystem, AddressRemoteSystemClassifications.RemoteAddress))
					.onItem().invoke(() -> log.info("✅ Successfully created all remote address classifications"))
					.onFailure().invoke(error -> log.error("❌ Error creating remote address classifications: {}", error.getMessage(), error));
			})
			
			// Then create LocalAddress classification and its children
			.chain(() -> service.create(session, AddressLocalSystemClassifications.LocalAddress, activityMasterSystem, InternetAddress))
			.chain(localAddress -> {
				log.info("📋 Creating local address classifications sequentially");
				
				// Chain local address classification creation operations sequentially
				return service.create(session, AddressLocalSystemClassifications.LocalAddressHostName, activityMasterSystem, AddressLocalSystemClassifications.LocalAddress)
					.chain(() -> service.create(session, AddressLocalSystemClassifications.LocalAddressIPAddress, activityMasterSystem, AddressLocalSystemClassifications.LocalAddress))
					.onItem().invoke(() -> {
						log.info("✅ Successfully created all local address classifications");
						logProgress("Address System", "Done Internal Addresses", 1);
					})
					.onFailure().invoke(error -> log.error("❌ Error creating local address classifications: {}", error.getMessage(), error));
			})
			
			// Then create ExternalAddress classification and its children
			.chain(() -> service.create(session, AddressExternalSystemClassifications.ExternalAddress, activityMasterSystem, InternetAddress))
			.chain(externalAddress -> {
				log.info("📋 Creating external address classifications sequentially");
				
				// Chain external address classification creation operations sequentially
				return service.create(session, AddressExternalSystemClassifications.ExternalAddressHostName, activityMasterSystem, AddressExternalSystemClassifications.ExternalAddress)
					.chain(() -> service.create(session, AddressExternalSystemClassifications.ExternalAddressIPAddress, activityMasterSystem, AddressExternalSystemClassifications.ExternalAddress))
					.onItem().invoke(() -> {
						log.info("✅ Successfully created all external address classifications");
						logProgress("Address System", "Done External Addresses", 1);
					})
					.onFailure().invoke(error -> log.error("❌ Error creating external address classifications: {}", error.getMessage(), error));
			})
			
			// Then create EmailAddress classification and its children
			.chain(() -> service.create(session, AddressEmailClassifications.EmailAddress, activityMasterSystem, ContactAddress))
			.chain(emailAddress -> {
				log.info("📋 Creating email address classifications sequentially");
				
				// Chain email address classification creation operations sequentially
				return service.create(session, AddressEmailClassifications.PersonalEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress)
					.chain(() -> service.create(session, AddressEmailClassifications.BusinessEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress))
					.chain(() -> service.create(session, AddressEmailClassifications.LegalEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress))
					.chain(() -> service.create(session, AddressEmailClassifications.EmailAddressDomain, activityMasterSystem, AddressEmailClassifications.EmailAddress))
					.chain(() -> service.create(session, AddressEmailClassifications.EmailAddressHost, activityMasterSystem, AddressEmailClassifications.EmailAddress))
					.chain(() -> service.create(session, AddressEmailClassifications.EmailAddressUser, activityMasterSystem, AddressEmailClassifications.EmailAddress))
					.onItem().invoke(() -> {
						log.info("✅ Successfully created all email address classifications");
						logProgress("Address System", "Done Email Addresses", 1);
					})
					.onFailure().invoke(error -> log.error("❌ Error creating email address classifications: {}", error.getMessage(), error));
			})
			
			// Finally create WebAddress classification and its children
			.chain(() -> service.create(session, AddressWebClassifications.WebAddress, activityMasterSystem, InternetAddress))
			.chain(webAddress -> {
				log.info("📋 Creating web address classifications sequentially");
				
				// Chain web address classification creation operations sequentially
				return service.create(session, AddressWebClassifications.WebAddressProtocol, activityMasterSystem, AddressWebClassifications.WebAddress)
					.chain(() -> service.create(session, AddressWebClassifications.WebAddressPort, activityMasterSystem, AddressWebClassifications.WebAddress))
					.chain(() -> service.create(session, AddressWebClassifications.WebAddressSubDomain, activityMasterSystem, AddressWebClassifications.WebAddress))
					.chain(() -> service.create(session, AddressWebClassifications.WebAddressDomain, activityMasterSystem, AddressWebClassifications.WebAddress))
					.chain(() -> service.create(session, AddressWebClassifications.WebAddressUrl, activityMasterSystem, AddressWebClassifications.WebAddress))
					.chain(() -> service.create(session, AddressWebClassifications.WebAddressQueryParameters, activityMasterSystem, AddressWebClassifications.WebAddress))
					.chain(() -> service.create(session, AddressWebClassifications.WebAddressSite, activityMasterSystem, AddressWebClassifications.WebAddress))
					.onItem().invoke(() -> {
						log.info("✅ Successfully created all web address classifications");
						logProgress("Address System", "Done Web Addresses", 1);
					})
					.onFailure().invoke(error -> log.error("❌ Error creating web address classifications: {}", error.getMessage(), error));
			})
			
			// Complete the chain
			.onItem().invoke(() -> log.info("✅ Successfully created all internet address classifications"))
			.onFailure().invoke(error -> log.error("❌ Error creating internet address classifications: {}", error.getMessage(), error))
			.replaceWithVoid();
	}

	@SuppressWarnings("Duplicates")
	private Uni<Void> createDefaultPhysicalAddresses(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Creating physical address classifications");
		logProgress("Address System", "Starting Physical Address Checks");

		log.info("📋 Creating physical address classifications sequentially");

		// Create all address types and their children sequentially
		// Start with BuildingAddress classification and its children
		return service.create(session, AddressBuildingClassifications.BuildingAddress, activityMasterSystem, LocationAddress)
			.chain(buildingAddress -> {
				log.info("📋 Creating building address classifications sequentially");
				
				// Chain building address classification creation operations sequentially
				return service.create(session, AddressBuildingClassifications.BuildingDesk, activityMasterSystem, AddressBuildingClassifications.BuildingAddress)
					.chain(() -> service.create(session, AddressBuildingClassifications.BuildingIsle, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
					.chain(() -> service.create(session, AddressBuildingClassifications.BuildingFloor, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
					.chain(() -> service.create(session, AddressBuildingClassifications.BuildingWindow, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
					.chain(() -> service.create(session, AddressBuildingClassifications.BuildingIdentifer, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
					.chain(() -> service.create(session, AddressBuildingClassifications.BuildingNumber, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
					.chain(() -> service.create(session, AddressBuildingClassifications.BuildingStreet, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
					.chain(() -> service.create(session, AddressBuildingClassifications.BuildingStreetType, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
					.onItem().invoke(() -> {
						log.info("✅ Successfully created all building address classifications");
						logProgress("Address System", "Done Building Addresses", 1);
					})
					.onFailure().invoke(error -> log.error("❌ Error creating building address classifications: {}", error.getMessage(), error));
			})
			
			// Then create BoxAddress classification and its children
			.chain(() -> service.create(session, AddressBoxClassifications.BoxAddress, activityMasterSystem, PostalAddress))
			.chain(boxAddress -> {
				log.info("📋 Creating box address classifications sequentially");
				
				// Chain box address classification creation operations sequentially
				return service.create(session, AddressBoxClassifications.BoxNumber, activityMasterSystem, AddressBoxClassifications.BoxAddress)
					.chain(() -> service.create(session, AddressBoxClassifications.BoxIdentifier, activityMasterSystem, AddressBoxClassifications.BoxAddress))
					.chain(() -> service.create(session, AddressBoxClassifications.BoxCity, activityMasterSystem, AddressBoxClassifications.BoxAddress))
					.chain(() -> service.create(session, AddressBoxClassifications.BoxPostalCode, activityMasterSystem, AddressBoxClassifications.BoxAddress))
					.onItem().invoke(() -> {
						log.info("✅ Successfully created all box address classifications");
						logProgress("Address System", "Done Box Addresses", 1);
					})
					.onFailure().invoke(error -> log.error("❌ Error creating box address classifications: {}", error.getMessage(), error));
			})
			
			// Finally create LegalAddress classification and its children
			.chain(() -> service.create(session, AddressLegalClassifications.LegalAddress, activityMasterSystem, PostalAddress))
			.chain(legalAddress -> {
				log.info("📋 Creating legal address classifications sequentially");
				
				// Chain legal address classification creation operations sequentially
				return service.create(session, AddressLegalClassifications.LegalDistrictNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress)
					.chain(() -> service.create(session, AddressLegalClassifications.LegalLotNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress))
					.chain(() -> service.create(session, AddressLegalClassifications.LegalBlockNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress))
					.onItem().invoke(() -> {
						log.info("✅ Successfully created all legal address classifications");
						logProgress("Address System", "Done Legal Addresses", 1);
					})
					.onFailure().invoke(error -> log.error("❌ Error creating legal address classifications: {}", error.getMessage(), error));
			})
			
			// Complete the chain
			.onItem().invoke(() -> log.info("✅ Successfully created all physical address classifications"))
			.onFailure().invoke(error -> log.error("❌ Error creating physical address classifications: {}", error.getMessage(), error))
			.replaceWithVoid();
	}

}
