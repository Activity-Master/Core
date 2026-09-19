package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.address.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import com.guicedee.client.IGuiceContext;
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

	// =============================================================================================
	// Stateless twins — mirror the managed seeding on a Mutiny.StatelessSession for the stateless install loop.
	// =============================================================================================

	@Override
	public Uni<Boolean> update(Mutiny.StatelessSession session, IEnterprise<?,?> enterprise)
	{
		log.info("📋 Starting sequential creation of address classifications (stateless)");
		ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
		return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
			.chain(activityMasterSystem -> createDefaultTelephones(session, enterprise, activityMasterSystem)
				.chain(() -> createDefaultInternetAddresses(session, enterprise, activityMasterSystem))
				.chain(() -> createDefaultPhysicalAddresses(session, enterprise, activityMasterSystem))
				.onItem().invoke(() -> log.info("✅ Successfully completed all address classification operations (stateless)"))
				.onFailure().invoke(error -> log.error("❌ Error creating address classifications (stateless): {}", error.getMessage(), error))
				.map(result -> true));
	}

	private Uni<Void> createDefaultTelephones(Mutiny.StatelessSession session, IEnterprise<?,?> enterprise, ISystems<?,?> activityMasterSystem)
	{
		log.info("Creating telephone address classifications (stateless)");
		return service.create(session, AddressClassifications.Address, activityMasterSystem)
			.chain(baseAddress -> service.create(session, ContactAddress, activityMasterSystem, AddressClassifications.Address)
				.chain(() -> service.create(session, PostalAddress, activityMasterSystem, AddressClassifications.Address))
				.chain(() -> service.create(session, CallAddress, activityMasterSystem, AddressClassifications.Address))
				.chain(() -> service.create(session, InternetAddress, activityMasterSystem, AddressClassifications.Address))
				.chain(() -> service.create(session, LocationAddress, activityMasterSystem, AddressClassifications.Address))
				.onFailure().invoke(error -> log.error("❌ Error creating address type classifications (stateless): {}", error.getMessage(), error))
				.chain(() -> service.create(session, TelephoneNumber, activityMasterSystem, CallAddress)
					.chain(telephoneNumber -> service.create(session, AddressTelephoneClassifications.HomeTelephoneNumber, activityMasterSystem, TelephoneNumber)
						.chain(() -> service.create(session, AddressTelephoneClassifications.HomeCellNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.HomePagerNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.HomeFaxNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.BusinessTelephoneNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.BusinessCellNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.BusinessPagerNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.BusinessFaxNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.LegalTelephoneNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.LegalCellNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.LegalPagerNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.LegalFaxNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.TelephoneCountryCode, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.TelephoneExtensionNumber, activityMasterSystem, TelephoneNumber))
						.chain(() -> service.create(session, AddressTelephoneClassifications.TelephoneAreaCode, activityMasterSystem, TelephoneNumber))
						.invoke(() -> logProgress("Address System", "Done Telephones", 1))
						.onFailure().invoke(error -> log.error("❌ Error creating telephone number classifications (stateless): {}", error.getMessage(), error))
						.replaceWithVoid())));
	}

	private Uni<Void> createDefaultInternetAddresses(Mutiny.StatelessSession session, IEnterprise<?,?> enterprise, ISystems<?,?> activityMasterSystem)
	{
		log.info("Creating internet address classifications (stateless)");
		logProgress("Address System", "Starting Internet Address Checks");
		return service.create(session, AddressInternalSystemClassifications.InternalAddress, activityMasterSystem, InternetAddress)
			.chain(internalAddress -> service.create(session, AddressInternalSystemClassifications.InternalAddressHostName, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress)
				.chain(() -> service.create(session, AddressInternalSystemClassifications.InternalAddressIPAddress, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress))
				.chain(() -> service.create(session, AddressInternalSystemClassifications.InternalAddressSubnet, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress))
				.chain(() -> service.create(session, AddressInternalSystemClassifications.InternalAddressDns, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress))
				.chain(() -> service.create(session, AddressInternalSystemClassifications.InternalAddressGateway, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress))
				.onFailure().invoke(error -> log.error("❌ Error creating internal address classifications (stateless): {}", error.getMessage(), error)))
			.chain(() -> service.create(session, AddressRemoteSystemClassifications.RemoteAddress, activityMasterSystem, InternetAddress))
			.chain(remoteAddress -> service.create(session, AddressRemoteSystemClassifications.RemoteAddressHostName, activityMasterSystem, AddressRemoteSystemClassifications.RemoteAddress)
				.chain(() -> service.create(session, AddressRemoteSystemClassifications.RemoteAddressIPAddress, activityMasterSystem, AddressRemoteSystemClassifications.RemoteAddress))
				.onFailure().invoke(error -> log.error("❌ Error creating remote address classifications (stateless): {}", error.getMessage(), error)))
			.chain(() -> service.create(session, AddressLocalSystemClassifications.LocalAddress, activityMasterSystem, InternetAddress))
			.chain(localAddress -> service.create(session, AddressLocalSystemClassifications.LocalAddressHostName, activityMasterSystem, AddressLocalSystemClassifications.LocalAddress)
				.chain(() -> service.create(session, AddressLocalSystemClassifications.LocalAddressIPAddress, activityMasterSystem, AddressLocalSystemClassifications.LocalAddress))
				.invoke(() -> logProgress("Address System", "Done Internal Addresses", 1))
				.onFailure().invoke(error -> log.error("❌ Error creating local address classifications (stateless): {}", error.getMessage(), error)))
			.chain(() -> service.create(session, AddressExternalSystemClassifications.ExternalAddress, activityMasterSystem, InternetAddress))
			.chain(externalAddress -> service.create(session, AddressExternalSystemClassifications.ExternalAddressHostName, activityMasterSystem, AddressExternalSystemClassifications.ExternalAddress)
				.chain(() -> service.create(session, AddressExternalSystemClassifications.ExternalAddressIPAddress, activityMasterSystem, AddressExternalSystemClassifications.ExternalAddress))
				.invoke(() -> logProgress("Address System", "Done External Addresses", 1))
				.onFailure().invoke(error -> log.error("❌ Error creating external address classifications (stateless): {}", error.getMessage(), error)))
			.chain(() -> service.create(session, AddressEmailClassifications.EmailAddress, activityMasterSystem, ContactAddress))
			.chain(emailAddress -> service.create(session, AddressEmailClassifications.PersonalEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress)
				.chain(() -> service.create(session, AddressEmailClassifications.BusinessEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress))
				.chain(() -> service.create(session, AddressEmailClassifications.LegalEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress))
				.chain(() -> service.create(session, AddressEmailClassifications.EmailAddressDomain, activityMasterSystem, AddressEmailClassifications.EmailAddress))
				.chain(() -> service.create(session, AddressEmailClassifications.EmailAddressHost, activityMasterSystem, AddressEmailClassifications.EmailAddress))
				.chain(() -> service.create(session, AddressEmailClassifications.EmailAddressUser, activityMasterSystem, AddressEmailClassifications.EmailAddress))
				.invoke(() -> logProgress("Address System", "Done Email Addresses", 1))
				.onFailure().invoke(error -> log.error("❌ Error creating email address classifications (stateless): {}", error.getMessage(), error)))
			.chain(() -> service.create(session, AddressWebClassifications.WebAddress, activityMasterSystem, InternetAddress))
			.chain(webAddress -> service.create(session, AddressWebClassifications.WebAddressProtocol, activityMasterSystem, AddressWebClassifications.WebAddress)
				.chain(() -> service.create(session, AddressWebClassifications.WebAddressPort, activityMasterSystem, AddressWebClassifications.WebAddress))
				.chain(() -> service.create(session, AddressWebClassifications.WebAddressSubDomain, activityMasterSystem, AddressWebClassifications.WebAddress))
				.chain(() -> service.create(session, AddressWebClassifications.WebAddressDomain, activityMasterSystem, AddressWebClassifications.WebAddress))
				.chain(() -> service.create(session, AddressWebClassifications.WebAddressUrl, activityMasterSystem, AddressWebClassifications.WebAddress))
				.chain(() -> service.create(session, AddressWebClassifications.WebAddressQueryParameters, activityMasterSystem, AddressWebClassifications.WebAddress))
				.chain(() -> service.create(session, AddressWebClassifications.WebAddressSite, activityMasterSystem, AddressWebClassifications.WebAddress))
				.invoke(() -> logProgress("Address System", "Done Web Addresses", 1))
				.onFailure().invoke(error -> log.error("❌ Error creating web address classifications (stateless): {}", error.getMessage(), error)))
			.onFailure().invoke(error -> log.error("❌ Error creating internet address classifications (stateless): {}", error.getMessage(), error))
			.replaceWithVoid();
	}

	private Uni<Void> createDefaultPhysicalAddresses(Mutiny.StatelessSession session, IEnterprise<?,?> enterprise, ISystems<?,?> activityMasterSystem)
	{
		log.info("Creating physical address classifications (stateless)");
		logProgress("Address System", "Starting Physical Address Checks");
		return service.create(session, AddressBuildingClassifications.BuildingAddress, activityMasterSystem, LocationAddress)
			.chain(buildingAddress -> service.create(session, AddressBuildingClassifications.BuildingDesk, activityMasterSystem, AddressBuildingClassifications.BuildingAddress)
				.chain(() -> service.create(session, AddressBuildingClassifications.BuildingIsle, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
				.chain(() -> service.create(session, AddressBuildingClassifications.BuildingFloor, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
				.chain(() -> service.create(session, AddressBuildingClassifications.BuildingWindow, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
				.chain(() -> service.create(session, AddressBuildingClassifications.BuildingIdentifer, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
				.chain(() -> service.create(session, AddressBuildingClassifications.BuildingNumber, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
				.chain(() -> service.create(session, AddressBuildingClassifications.BuildingStreet, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
				.chain(() -> service.create(session, AddressBuildingClassifications.BuildingStreetType, activityMasterSystem, AddressBuildingClassifications.BuildingAddress))
				.invoke(() -> logProgress("Address System", "Done Building Addresses", 1))
				.onFailure().invoke(error -> log.error("❌ Error creating building address classifications (stateless): {}", error.getMessage(), error)))
			.chain(() -> service.create(session, AddressBoxClassifications.BoxAddress, activityMasterSystem, PostalAddress))
			.chain(boxAddress -> service.create(session, AddressBoxClassifications.BoxNumber, activityMasterSystem, AddressBoxClassifications.BoxAddress)
				.chain(() -> service.create(session, AddressBoxClassifications.BoxIdentifier, activityMasterSystem, AddressBoxClassifications.BoxAddress))
				.chain(() -> service.create(session, AddressBoxClassifications.BoxCity, activityMasterSystem, AddressBoxClassifications.BoxAddress))
				.chain(() -> service.create(session, AddressBoxClassifications.BoxPostalCode, activityMasterSystem, AddressBoxClassifications.BoxAddress))
				.invoke(() -> logProgress("Address System", "Done Box Addresses", 1))
				.onFailure().invoke(error -> log.error("❌ Error creating box address classifications (stateless): {}", error.getMessage(), error)))
			.chain(() -> service.create(session, AddressLegalClassifications.LegalAddress, activityMasterSystem, PostalAddress))
			.chain(legalAddress -> service.create(session, AddressLegalClassifications.LegalDistrictNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress)
				.chain(() -> service.create(session, AddressLegalClassifications.LegalLotNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress))
				.chain(() -> service.create(session, AddressLegalClassifications.LegalBlockNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress))
				.invoke(() -> logProgress("Address System", "Done Legal Addresses", 1))
				.onFailure().invoke(error -> log.error("❌ Error creating legal address classifications (stateless): {}", error.getMessage(), error)))
			.onFailure().invoke(error -> log.error("❌ Error creating physical address classifications (stateless): {}", error.getMessage(), error))
			.replaceWithVoid();
	}
}