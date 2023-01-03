package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.ISystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.systems.SortedUpdate;
import com.guicedee.activitymaster.fsdm.client.types.classifications.address.*;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.address.AddressClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.address.AddressTelephoneClassifications.*;

@SortedUpdate(sortOrder = -400, taskCount = 15)
public class AddressBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;
	
	@Override
	public void update(IEnterprise<?,?> enterprise)
	{
		createDefaultTelephones(enterprise);
		createDefaultInternetAddresses(enterprise);
		createDefaultPhysicalAddresses(enterprise);
	}
	
	@SuppressWarnings("Duplicates")
	private void createDefaultTelephones(IEnterprise<?,?> enterprise)
	{
		service.create(AddressClassifications.Address, activityMasterSystem);
		service.create(ContactAddress, activityMasterSystem,AddressClassifications.Address);
		service.create(PostalAddress, activityMasterSystem,AddressClassifications.Address);
		service.create(CallAddress, activityMasterSystem,AddressClassifications.Address);
		service.create(InternetAddress, activityMasterSystem,AddressClassifications.Address);
		service.create(LocationAddress, activityMasterSystem,AddressClassifications.Address);
		
		service.create(TelephoneNumber, activityMasterSystem,CallAddress);
		
		service.create(AddressTelephoneClassifications.HomeTelephoneNumber, activityMasterSystem,TelephoneNumber);
		service.create(AddressTelephoneClassifications.HomeCellNumber, activityMasterSystem,TelephoneNumber);
		service.create(AddressTelephoneClassifications.HomePagerNumber, activityMasterSystem,TelephoneNumber);
		service.create(AddressTelephoneClassifications.HomeFaxNumber, activityMasterSystem,TelephoneNumber);
		
		service.create(AddressTelephoneClassifications.BusinessTelephoneNumber, activityMasterSystem,TelephoneNumber);
		service.create(AddressTelephoneClassifications.BusinessCellNumber, activityMasterSystem,TelephoneNumber);
		service.create(AddressTelephoneClassifications.BusinessPagerNumber, activityMasterSystem,TelephoneNumber);
		service.create(AddressTelephoneClassifications.BusinessFaxNumber, activityMasterSystem,TelephoneNumber);
		
		service.create(AddressTelephoneClassifications.LegalTelephoneNumber, activityMasterSystem,TelephoneNumber);
		service.create(AddressTelephoneClassifications.LegalCellNumber, activityMasterSystem,TelephoneNumber);
		service.create(AddressTelephoneClassifications.LegalPagerNumber, activityMasterSystem,TelephoneNumber);
		service.create(AddressTelephoneClassifications.LegalFaxNumber, activityMasterSystem,TelephoneNumber);
		
		service.create(AddressTelephoneClassifications.TelephoneCountryCode, activityMasterSystem, TelephoneNumber);
		service.create(AddressTelephoneClassifications.TelephoneExtensionNumber, activityMasterSystem, TelephoneNumber);
		service.create(AddressTelephoneClassifications.TelephoneAreaCode, activityMasterSystem, TelephoneNumber);
		
		logProgress("Address System", "Done Telephones", 1);
	}
	
	@SuppressWarnings("Duplicates")
	private void createDefaultInternetAddresses(IEnterprise<?,?> enterprise)
	{
		logProgress("Address System", "Starting Internet Address Checks");
		
		service.create(AddressInternalSystemClassifications.InternalAddress, activityMasterSystem,InternetAddress);
		service.create(AddressInternalSystemClassifications.InternalAddressHostName, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress);
		service.create(AddressInternalSystemClassifications.InternalAddressIPAddress, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress);
		service.create(AddressInternalSystemClassifications.InternalAddressSubnet, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress);
		service.create(AddressInternalSystemClassifications.InternalAddressDns, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress);
		service.create(AddressInternalSystemClassifications.InternalAddressGateway, activityMasterSystem, AddressInternalSystemClassifications.InternalAddress);
		
		service.create(AddressRemoteSystemClassifications.RemoteAddress, activityMasterSystem,InternetAddress);
		service.create(AddressRemoteSystemClassifications.RemoteAddressHostName, activityMasterSystem, AddressRemoteSystemClassifications.RemoteAddress);
		service.create(AddressRemoteSystemClassifications.RemoteAddressIPAddress, activityMasterSystem, AddressRemoteSystemClassifications.RemoteAddress);
		
		service.create(AddressLocalSystemClassifications.LocalAddress, activityMasterSystem,InternetAddress);
		service.create(AddressLocalSystemClassifications.LocalAddressHostName, activityMasterSystem, AddressLocalSystemClassifications.LocalAddress);
		service.create(AddressLocalSystemClassifications.LocalAddressIPAddress, activityMasterSystem, AddressLocalSystemClassifications.LocalAddress);
		
		logProgress("Address System", "Done Internal Addresses", 1);
		
		service.create(AddressExternalSystemClassifications.ExternalAddress, activityMasterSystem,InternetAddress);
		service.create(AddressExternalSystemClassifications.ExternalAddressHostName, activityMasterSystem, AddressExternalSystemClassifications.ExternalAddress);
		service.create(AddressExternalSystemClassifications.ExternalAddressIPAddress, activityMasterSystem, AddressExternalSystemClassifications.ExternalAddress);
		
		logProgress("Address System", "Done External Addresses", 1);
		
		service.create(AddressEmailClassifications.EmailAddress, activityMasterSystem,ContactAddress);
		
		service.create(AddressEmailClassifications.PersonalEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress);
		service.create(AddressEmailClassifications.BusinessEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress);
		service.create(AddressEmailClassifications.LegalEmailAddress, activityMasterSystem, AddressEmailClassifications.EmailAddress);
		
		
		service.create(AddressEmailClassifications.EmailAddressDomain, activityMasterSystem, AddressEmailClassifications.EmailAddress);
		service.create(AddressEmailClassifications.EmailAddressHost, activityMasterSystem, AddressEmailClassifications.EmailAddress);
		service.create(AddressEmailClassifications.EmailAddressUser, activityMasterSystem, AddressEmailClassifications.EmailAddress);
		
		logProgress("Address System", "Done Email Addresses", 1);
		
		service.create(AddressWebClassifications.WebAddress, activityMasterSystem,InternetAddress);
		service.create(AddressWebClassifications.WebAddressProtocol, activityMasterSystem, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressPort, activityMasterSystem, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressSubDomain, activityMasterSystem, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressDomain, activityMasterSystem, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressUrl, activityMasterSystem, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressQueryParameters, activityMasterSystem, AddressWebClassifications.WebAddress);
		service.create(AddressWebClassifications.WebAddressSite, activityMasterSystem, AddressWebClassifications.WebAddress);
		
		logProgress("Address System", "Done Web Addresses", 1);
	}
	
	@SuppressWarnings("Duplicates")
	private void createDefaultPhysicalAddresses(IEnterprise<?,?> enterprise)
	{
		logProgress("Address System", "Starting Physical Address Checks");
		
		service.create(AddressBuildingClassifications.BuildingAddress, activityMasterSystem,LocationAddress);
		service.create(AddressBuildingClassifications.BuildingDesk, activityMasterSystem, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingIsle, activityMasterSystem, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingFloor, activityMasterSystem, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingWindow, activityMasterSystem, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingIdentifer, activityMasterSystem, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingNumber, activityMasterSystem, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingStreet, activityMasterSystem, AddressBuildingClassifications.BuildingAddress);
		service.create(AddressBuildingClassifications.BuildingStreetType, activityMasterSystem, AddressBuildingClassifications.BuildingAddress);
		
		logProgress("Address System", "Done Building Addresses", 1);
		
		service.create(AddressBoxClassifications.BoxAddress, activityMasterSystem,PostalAddress);
		service.create(AddressBoxClassifications.BoxNumber, activityMasterSystem, AddressBoxClassifications.BoxAddress);
		service.create(AddressBoxClassifications.BoxIdentifier, activityMasterSystem, AddressBoxClassifications.BoxAddress);
		service.create(AddressBoxClassifications.BoxCity, activityMasterSystem, AddressBoxClassifications.BoxAddress);
		service.create(AddressBoxClassifications.BoxPostalCode, activityMasterSystem, AddressBoxClassifications.BoxAddress);
		
		logProgress("Address System", "Done Box Addresses", 1);
		
		service.create(AddressLegalClassifications.LegalAddress, activityMasterSystem,PostalAddress);
		service.create(AddressLegalClassifications.LegalDistrictNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress);
		service.create(AddressLegalClassifications.LegalLotNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress);
		service.create(AddressLegalClassifications.LegalBlockNumber, activityMasterSystem, AddressLegalClassifications.LegalAddress);
		
		logProgress("Address System", "Done Legal Addresses", 1);
	}
	
}
