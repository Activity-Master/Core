package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.classifications.address.*;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

@Singleton
public class AddressSystem
		extends ActivityMasterDefaultSystem<AddressSystem>
		implements IActivityMasterSystem<AddressSystem>
{
	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Address System", "Starting Address Checks", progressMonitor);
		createDefaultTelephones(enterprise, progressMonitor);
		createDefaultInternetAddresses(enterprise, progressMonitor);
		createDefaultPhysicalAddresses(enterprise, progressMonitor);
	}

	@SuppressWarnings("Duplicates")
	private void createDefaultTelephones(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> system = GuiceContext.get(SystemsService.class)
		                                 .getActivityMaster(enterprise);

		ClassificationService service = GuiceContext.get(ClassificationService.class);

		service.create(AddressHomeTelephoneClassifications.HomeTelephoneNumber, system);
		service.create(AddressHomeTelephoneClassifications.HomeTelephoneCountryCode, system, AddressHomeTelephoneClassifications.HomeTelephoneNumber);
		service.create(AddressHomeTelephoneClassifications.HomeTelephoneExtensionNumber, system, AddressHomeTelephoneClassifications.HomeTelephoneNumber);
		service.create(AddressHomeTelephoneClassifications.HomeTelephoneAreaCode, system, AddressHomeTelephoneClassifications.HomeTelephoneNumber);

		logProgress("Address System", "Done Home Telephones", 1, progressMonitor);

		service.create(AddressHomeFaxClassifications.HomeFaxNumber, system);
		service.create(AddressHomeFaxClassifications.HomeFaxCountryCode, system, AddressHomeFaxClassifications.HomeFaxNumber);
		service.create(AddressHomeFaxClassifications.HomeFaxExtensionNumber, system, AddressHomeFaxClassifications.HomeFaxNumber);
		service.create(AddressHomeFaxClassifications.HomeFaxAreaCode, system, AddressHomeFaxClassifications.HomeFaxNumber);

		logProgress("Address System", "Done Home Fax", 1, progressMonitor);

		service.create(AddressHomeCellClassifications.HomeCellNumber, system);
		service.create(AddressHomeCellClassifications.HomeCellCountryCode, system, AddressHomeCellClassifications.HomeCellNumber);
		service.create(AddressHomeCellClassifications.HomeCellExtensionNumber, system, AddressHomeCellClassifications.HomeCellNumber);
		service.create(AddressHomeCellClassifications.HomeCellAreaCode, system, AddressHomeCellClassifications.HomeCellNumber);

		logProgress("Address System", "Done Home Cell", 1, progressMonitor);

		service.create(AddressHomePagerClassifications.HomePagerNumber, system);
		service.create(AddressHomePagerClassifications.HomePagerCountryCode, system, AddressHomePagerClassifications.HomePagerNumber);
		service.create(AddressHomePagerClassifications.HomePagerExtensionNumber, system, AddressHomePagerClassifications.HomePagerNumber);
		service.create(AddressHomePagerClassifications.HomePagerAreaCode, system, AddressHomePagerClassifications.HomePagerNumber);

		logProgress("Address System", "Done Home Pager", 1, progressMonitor);

		service.create(AddressOfficeTelepehoneClassifications.OfficeTelephoneNumber, system);
		service.create(AddressOfficeTelepehoneClassifications.OfficeTelephoneCountryCode, system, AddressOfficeTelepehoneClassifications.OfficeTelephoneNumber);
		service.create(AddressOfficeTelepehoneClassifications.OfficeTelephoneExtensionNumber, system, AddressOfficeTelepehoneClassifications.OfficeTelephoneNumber);
		service.create(AddressOfficeTelepehoneClassifications.OfficeTelephoneAreaCode, system, AddressOfficeTelepehoneClassifications.OfficeTelephoneNumber);

		logProgress("Address System", "Done Office Telephone", 1, progressMonitor);

		service.create(AddressOfficeFaxClassifications.OfficeFaxNumber, system);
		service.create(AddressOfficeFaxClassifications.OfficeFaxCountryCode, system, AddressOfficeFaxClassifications.OfficeFaxNumber);
		service.create(AddressOfficeFaxClassifications.OfficeFaxExtensionNumber, system, AddressOfficeFaxClassifications.OfficeFaxNumber);
		service.create(AddressOfficeFaxClassifications.OfficeFaxAreaCode, system, AddressOfficeFaxClassifications.OfficeFaxNumber);

		logProgress("Address System", "Done Office Fax", 1, progressMonitor);

		service.create(AddressOfficeCellClassifications.OfficeCellNumber, system);
		service.create(AddressOfficeCellClassifications.OfficeCellCountryCode, system, AddressOfficeCellClassifications.OfficeCellNumber);
		service.create(AddressOfficeCellClassifications.OfficeCellExtensionNumber, system, AddressOfficeCellClassifications.OfficeCellNumber);
		service.create(AddressOfficeCellClassifications.OfficeCellAreaCode, system, AddressOfficeCellClassifications.OfficeCellNumber);

		logProgress("Address System", "Done Office Cell", 1, progressMonitor);

		service.create(AddressOfficePagerClassifications.OfficePagerNumber, system);
		service.create(AddressOfficePagerClassifications.OfficePagerCountryCode, system, AddressOfficePagerClassifications.OfficePagerNumber);
		service.create(AddressOfficePagerClassifications.OfficePagerExtensionNumber, system, AddressOfficePagerClassifications.OfficePagerNumber);
		service.create(AddressOfficePagerClassifications.OfficePagerAreaCode, system, AddressOfficePagerClassifications.OfficePagerNumber);

		logProgress("Address System", "Done Office Pager", 1, progressMonitor);
	}

	@SuppressWarnings("Duplicates")
	private void createDefaultInternetAddresses(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Address System", "Starting Internet Address Checks", progressMonitor);

		ISystems<?> system = GuiceContext.get(SystemsService.class)
		                                 .getActivityMaster(enterprise);

		ClassificationService service = GuiceContext.get(ClassificationService.class);

		service.create(AddressInternalSystemClassifications.InternalAddress, system);
		service.create(AddressInternalSystemClassifications.InternalAddressHostName, system, AddressInternalSystemClassifications.InternalAddress);
		service.create(AddressInternalSystemClassifications.InternalAddressIPAddress, system, AddressInternalSystemClassifications.InternalAddress);
		service.create(AddressInternalSystemClassifications.InternalAddressSubnet, system, AddressInternalSystemClassifications.InternalAddress);
		service.create(AddressInternalSystemClassifications.InternalAddressDns, system, AddressInternalSystemClassifications.InternalAddress);
		service.create(AddressInternalSystemClassifications.InternalAddressGateway, system, AddressInternalSystemClassifications.InternalAddress);

		service.create(AddressRemoteSystemClassifications.RemoteAddress, system);
		service.create(AddressRemoteSystemClassifications.RemoteAddressHostName, system, AddressRemoteSystemClassifications.RemoteAddress);
		service.create(AddressRemoteSystemClassifications.RemoteAddressIPAddress, system, AddressRemoteSystemClassifications.RemoteAddress);

		service.create(AddressLocalSystemClassifications.LocalAddress, system);
		service.create(AddressLocalSystemClassifications.LocalAddressHostName, system, AddressLocalSystemClassifications.LocalAddress);
		service.create(AddressLocalSystemClassifications.LocalAddressIPAddress, system, AddressLocalSystemClassifications.LocalAddress);

		logProgress("Address System", "Done Internal Addresses", 1, progressMonitor);

		service.create(AddressExternalSystemClassifications.ExternalAddress, system);
		service.create(AddressExternalSystemClassifications.ExternalAddressHostName, system, AddressExternalSystemClassifications.ExternalAddress);
		service.create(AddressExternalSystemClassifications.ExternalAddressIPAddress, system, AddressExternalSystemClassifications.ExternalAddress);

		logProgress("Address System", "Done External Addresses", 1, progressMonitor);

		service.create(AddressEmailClassifications.EmailAddress, system);
		service.create(AddressEmailClassifications.EmailAddressDomain, system, AddressEmailClassifications.EmailAddress);
		service.create(AddressEmailClassifications.EmailAddressHost, system, AddressEmailClassifications.EmailAddress);
		service.create(AddressEmailClassifications.EmailAddressUser, system, AddressEmailClassifications.EmailAddress);

		logProgress("Address System", "Done Email Addresses", 1, progressMonitor);

		service.create(AddressWebClassifications.WebAddress, system);
		service.create(AddressWebClassifications.WebAddressProtocol, system, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressPort, system, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressSubDomain, system, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressDomain, system, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressUrl, system, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressQueryParameters, system, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressSite, system, AddressWebClassifications.WebAddress);

		logProgress("Address System", "Done Web Addresses", 1, progressMonitor);
	}

	@SuppressWarnings("Duplicates")
	private void createDefaultPhysicalAddresses(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Address System", "Starting Physical Address Checks", progressMonitor);
		ISystems<?> system = GuiceContext.get(SystemsService.class)
		                                 .getActivityMaster(enterprise);

		ClassificationService service = GuiceContext.get(ClassificationService.class);

		service.create(AddressBuildingClassifications.BuildingAddress, system);
		service.create(AddressBuildingClassifications.BuildingDesk, system, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingIsle, system, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingFloor, system, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingWindow, system, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingIdentifer, system, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingNumber, system, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingStreet, system, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingStreetType, system, AddressBuildingClassifications.BuildingAddress);

		logProgress("Address System", "Done Building Addresses", 1, progressMonitor);

		service.create(AddressBoxClassifications.BoxAddress, system);
		service.create(AddressBoxClassifications.BoxNumber, system, AddressBoxClassifications.BoxAddress);
		service.create(AddressBoxClassifications.BoxIdentifier, system, AddressBoxClassifications.BoxAddress);
		service.create(AddressBoxClassifications.BoxCity, system, AddressBoxClassifications.BoxAddress);
		service.create(AddressBoxClassifications.BoxPostalCode, system, AddressBoxClassifications.BoxAddress);

		logProgress("Address System", "Done Box Addresses", 1, progressMonitor);

		service.create(AddressLegalClassifications.LegalAddress, system);
		service.create(AddressLegalClassifications.LegalDistrictNumber, system, AddressLegalClassifications.LegalAddress);
		service.create(AddressLegalClassifications.LegalLotNumber, system, AddressLegalClassifications.LegalAddress);
		service.create(AddressLegalClassifications.LegalBlockNumber, system, AddressLegalClassifications.LegalAddress);

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
	public String getSystemName()
	{
		return "Address System";
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for the address management";
	}
}
