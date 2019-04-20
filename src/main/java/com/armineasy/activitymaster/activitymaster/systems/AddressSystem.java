package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressBoxClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressBuildingClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressEmailClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressExternalSystemClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressHomeCellClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressHomeFaxClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressHomePagerClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressHomeTelephoneClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressInternalSystemClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressLegalClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressOfficeCellClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressOfficeFaxClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressOfficePagerClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressOfficeTelepehoneClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressRemoteSystemClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressWebClassifications.*;

public class AddressSystem
		implements IActivityMasterSystem<AddressSystem>
{
	private static final Map<Enterprise, UUID> systemTokens = new HashMap<>();

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Address System", "Starting Address Checks", progressMonitor);
		createDefaultTelephones(enterprise, progressMonitor);
		createDefaultInternetAddresses(enterprise, progressMonitor);
		createDefaultPhysicalAddresses(enterprise, progressMonitor);
	}

	@SuppressWarnings("Duplicates")
	private void createDefaultTelephones(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems system = GuiceContext.get(SystemsService.class)
		                             .getActivityMaster(enterprise);
		logProgress("Address System", "Starting Address Checks", progressMonitor);

		IEnterpriseName<?> enterpriseName = GuiceContext.get(ActivityMasterConfiguration.class)
		                                                .getEnterpriseName();


		ClassificationService service = GuiceContext.get(ClassificationService.class);

		service.create(HomeTelephoneNumber, system);
		service.create(HomeTelephoneCountryCode, system,HomeTelephoneNumber);
		service.create(HomeTelephoneExtensionNumber, system,HomeTelephoneNumber);
		service.create(HomeTelephoneAreaCode, system,HomeTelephoneNumber);


		logProgress("Address System", "Done Home Telephones", 1, progressMonitor);

		service.create(HomeFaxNumber, system);
		service.create(HomeFaxCountryCode, system,HomeFaxNumber);
		service.create(HomeFaxExtensionNumber, system,HomeFaxNumber);
		service.create(HomeFaxAreaCode, system,HomeFaxNumber);

		logProgress("Address System", "Done Home Fax", 1, progressMonitor);

		service.create(HomeCellNumber, system);
		service.create(HomeCellCountryCode, system,HomeCellNumber);
		service.create(HomeCellExtensionNumber, system,HomeCellNumber);
		service.create(HomeCellAreaCode, system,HomeCellNumber);


		logProgress("Address System", "Done Home Cell", 1, progressMonitor);

		service.create(HomePagerNumber, system);
		service.create(HomePagerCountryCode, system,HomePagerNumber);
		service.create(HomePagerExtensionNumber, system,HomePagerNumber);
		service.create(HomePagerAreaCode, system,HomePagerNumber);


		logProgress("Address System", "Done Home Pager", 1, progressMonitor);

		service.create(OfficeTelephoneNumber, system);
		service.create(OfficeTelephoneCountryCode, system,OfficeTelephoneNumber);
		service.create(OfficeTelephoneExtensionNumber, system,OfficeTelephoneNumber);
		service.create(OfficeTelephoneAreaCode, system,OfficeTelephoneNumber);

		logProgress("Address System", "Done Office Telephone", 1, progressMonitor);

		service.create(OfficeFaxNumber, system);
		service.create(OfficeFaxCountryCode, system,OfficeFaxNumber);
		service.create(OfficeFaxExtensionNumber, system,OfficeFaxNumber);
		service.create(OfficeFaxAreaCode, system,OfficeFaxNumber);


		logProgress("Address System", "Done Office Fax", 1, progressMonitor);

		service.create(OfficeCellNumber, system);
		service.create(OfficeCellCountryCode, system,OfficeCellNumber);
		service.create(OfficeCellExtensionNumber, system,OfficeCellNumber);
		service.create(OfficeCellAreaCode, system,OfficeCellNumber);


		logProgress("Address System", "Done Office Cell", 1, progressMonitor);

		service.create(OfficePagerNumber, system);
		service.create(OfficePagerCountryCode, system,OfficePagerNumber);
		service.create(OfficePagerExtensionNumber, system,OfficePagerNumber);
		service.create(OfficePagerAreaCode, system,OfficePagerNumber);


		logProgress("Address System", "Done Office Pager", 1, progressMonitor);
	}

	@SuppressWarnings("Duplicates")
	private void createDefaultInternetAddresses(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Address System", "Starting Internet Address Checks", progressMonitor);

		Systems system = GuiceContext.get(SystemsService.class)
		                             .getActivityMaster(enterprise);

		ClassificationService service = GuiceContext.get(ClassificationService.class);

		service.create(InternalAddress, system);
		service.create(InternalAddressHostName, system,InternalAddress);
		service.create(InternalAddressIPAddress, system,InternalAddress);
		service.create(InternalAddressSubnet, system,InternalAddress);
		service.create(InternalAddressDns, system,InternalAddress);
		service.create(InternalAddressGateway, system,InternalAddress);


		service.create(RemoteAddress, system);
		service.create(RemoteAddressHostName, system,RemoteAddress);
		service.create(RemoteAddressIPAddress, system,RemoteAddress);

		logProgress("Address System", "Done Internal Addresses", 1, progressMonitor);

		service.create(ExternalAddress, system);
		service.create(ExternalAddressHostName, system,ExternalAddress);
		service.create(ExternalAddressIPAddress, system,ExternalAddress);


		logProgress("Address System", "Done External Addresses", 1, progressMonitor);

		service.create(EmailAddress, system);
		service.create(EmailAddressDomain, system,EmailAddress);
		service.create(EmailAddressHost, system,EmailAddress);
		service.create(EmailAddressUser, system,EmailAddress);


		logProgress("Address System", "Done Email Addresses", 1, progressMonitor);

		service.create(WebAddress, system);
		service.create(WebAddressProtocol, system,WebAddress);
		service.create(WebAddressPort, system,WebAddress);
		service.create(WebAddressSubDomain, system,WebAddress);
		service.create(WebAddressDomain, system,WebAddress);
		service.create(WebAddressUrl, system,WebAddress);
		service.create(WebAddressQueryParameters, system,WebAddress);
		service.create(WebAddressSite, system,WebAddress);

		logProgress("Address System", "Done Web Addresses", 1, progressMonitor);
	}

	@SuppressWarnings("Duplicates")
	private void createDefaultPhysicalAddresses(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Address System", "Starting Physical Address Checks", progressMonitor);
		Systems system = GuiceContext.get(SystemsService.class)
		                             .getActivityMaster(enterprise);

		ClassificationService service = GuiceContext.get(ClassificationService.class);

		service.create(BuildingAddress, system);
		service.create(BuildingDesk, system,BuildingAddress);
		service.create(BuildingIsle, system,BuildingAddress);
		service.create(BuildingFloor, system,BuildingAddress);
		service.create(BuildingWindow, system,BuildingAddress);
		service.create(BuildingIdentifer, system,BuildingAddress);
		service.create(BuildingNumber, system,BuildingAddress);
		service.create(BuildingStreet, system,BuildingAddress);
		service.create(BuildingStreetType, system,BuildingAddress);

		logProgress("Address System", "Done Building Addresses", 1, progressMonitor);

		service.create(BoxAddress, system);
		service.create(BoxNumber, system,BoxAddress);
		service.create(BoxIdentifier, system,BoxAddress);
		service.create(BoxCity, system,BoxAddress);
		service.create(BoxPostalCode, system,BoxAddress);

		logProgress("Address System", "Done Box Addresses", 1, progressMonitor);

		service.create(LegalAddress, system);
		service.create(LegalDistrictNumber, system,LegalAddress);
		service.create(LegalLotNumber, system,LegalAddress);
		service.create(LegalBlockNumber, system,LegalAddress);

		logProgress("Address System", "Done Legal Addresses", 1, progressMonitor);
	}

	@Override
	public int totalTasks()
	{
		return 15;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 6;
	}

	@Override
	public void postUpdate(Enterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems newSystem = GuiceContext.get(SystemsService.class)
		                                .create(enterprise, "Address System", "The system for the address management", "");
		UUID securityToken = GuiceContext.get(SystemsSystem.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
	}

	public static Map<Enterprise, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
