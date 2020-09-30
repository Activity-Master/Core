package com.guicedee.activitymaster.core.implementations;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.address.AddressBuildingClassifications;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.classifications.address.PhoneNumberDTO;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.dto.IAddress;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.exceptions.AddressException;
import com.guicedee.activitymaster.core.services.security.Passwords;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IAddressService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.guicedinjection.GuiceContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.guicedee.activitymaster.core.services.classifications.address.AddressBoxClassifications.*;
import static com.guicedee.activitymaster.core.services.classifications.address.AddressHomeCellClassifications.*;
import static com.guicedee.activitymaster.core.services.classifications.address.AddressHomeTelephoneClassifications.*;
import static com.guicedee.activitymaster.core.services.classifications.address.AddressRemoteSystemClassifications.*;
import static com.guicedee.activitymaster.core.services.classifications.address.AddressWebClassifications.*;
import static com.guicedee.activitymaster.core.services.classifications.events.EventInvolvedPartiesClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")

public class AddressService
		implements IAddressService<AddressService>
{
	private static final Pattern ipAddressPattern = Pattern.compile("((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(?:\\.|$)){4}");
	
	@Override
	public IAddress<?> create(IAddressClassification<?> addressClassification, ISystems<?> originatingSystem, String value, UUID... identifyingToken)
	{
		Address addy = new Address();
		ISystems<?> activityMasterSystem = get(ISystemsService.class)
				.getActivityMaster(originatingSystem.getEnterpriseID());
		Classification classification = (Classification) get(ClassificationService.class).find(addressClassification,
		                                                                                       originatingSystem.getEnterpriseID(), identifyingToken);
		
		boolean found = addy.builder()
		                    .withClassification(classification)
		                    .withValue(value)
		                    .withEnterprise(originatingSystem.getEnterpriseID())
		                    .inDateRange()
		                    .inActiveRange(activityMasterSystem.getEnterprise(), identifyingToken)
		                    .getCount() > 0;
		
		if (!found)
		{
			addy.setEnterpriseID(classification.getEnterpriseID());
			addy.setClassificationID(classification);
			addy.setValue(value);
			addy.setSystemID((Systems) activityMasterSystem);
			addy.setOriginalSourceSystemID((Systems) activityMasterSystem);
			addy.setActiveFlagID(classification.getActiveFlagID());
			addy.persist();
			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				addy.createDefaultSecurity(activityMasterSystem, identifyingToken);
			}
			if (EventThread.event.get() != null)
			{
				EventThread.event.get()
				                 .add(addy, Created, value, activityMasterSystem, identifyingToken);
			}
		}
		else
		{
			addy = addy.builder()
			           .withClassification(classification)
			           .withEnterprise(originatingSystem.getEnterpriseID())
			           .withValue(value)
			           .inDateRange()
			           .withEnterprise(activityMasterSystem.getEnterprise())
			           .get()
			           .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + value));
		}
		return addy;
	}
	
	@Override
	public IAddress<?> addOrFindIPAddress(String ipAddress, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException
	{
		if (!ipAddressPattern.matcher(ipAddress)
		                     .matches())
		{
			throw new AddressException("Invalid IP Address");
		}
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) get(ClassificationService.class).find(RemoteAddressIPAddress,
		                                                                                                originatingSystem.getEnterpriseID(),
		                                                                                                identityToken);
		
		boolean found = address.builder()
		                       .withClassification(ipAddressClassification)
		                       .withValue(ipAddress)
		                       .withEnterprise(originatingSystem.getEnterpriseID())
		                       .inDateRange()
		                       .inActiveRange(originatingSystem.getEnterprise(), identityToken)
		                       .getCount() > 0;
		
		if (!found)
		{
			address.setValue(ipAddress);
			address.setClassificationID(ipAddressClassification);
			address.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			address.setSystemID((Systems) originatingSystem);
			address.setOriginalSourceSystemID((Systems) originatingSystem);
			address.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
					.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			address.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{ address.createDefaultSecurity(originatingSystem, identityToken); }
			if (EventThread.event.get() != null)
			{
				ISystems<?> activityMasterSystem = get(ISystemsService.class)
						.getActivityMaster(originatingSystem.getEnterpriseID());
				EventThread.event.get()
				                 .add(address, Created, ipAddress, activityMasterSystem, identityToken);
			}
		}
		else
		{
			address = address.builder()
			                 .withClassification(ipAddressClassification)
			                 .withValue(ipAddress)
			                 .withEnterprise(originatingSystem.getEnterpriseID())
			                 .inDateRange()
			                 .inActiveRange(originatingSystem.getEnterprise(), identityToken)
			                 .get()
			                 .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + ipAddress));
		}
		return address;
	}
	
	@Override
	public IAddress<?> addOrFindHostName(String hostName, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) get(ClassificationService.class).find(
				RemoteAddressHostName,
				originatingSystem.getEnterpriseID(),
				identityToken);
		
		boolean found = address.builder()
		                       .withClassification(ipAddressClassification)
		                       .withValue(hostName)
		                       .withEnterprise(originatingSystem.getEnterpriseID())
		                       .inDateRange()
		                       .inActiveRange(originatingSystem.getEnterprise(), identityToken)
		                       .getCount() > 0;
		
		if (!found)
		{
			address.setValue(hostName);
			address.setClassificationID(ipAddressClassification);
			address.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			address.setSystemID((Systems) originatingSystem);
			address.setOriginalSourceSystemID((Systems) originatingSystem);
			address.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
					.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			address.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{ address.createDefaultSecurity(originatingSystem, identityToken); }
			if (EventThread.event.get() != null)
			{
				ISystems<?> activityMasterSystem = get(ISystemsService.class)
						.getActivityMaster(originatingSystem.getEnterpriseID());
				EventThread.event.get()
				                 .add(address, Created, hostName, activityMasterSystem, identityToken);
			}
		}
		else
		{
			address = address.builder()
			                 .withClassification(ipAddressClassification)
			                 .withValue(hostName)
			                 .withEnterprise(originatingSystem.getEnterpriseID())
			                 .inDateRange()
			                 .inActiveRange(originatingSystem.getEnterprise(), identityToken)
			                 .get()
			                 .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + hostName));
		}
		
		return address;
	}
	
	@Override
	public IAddress<?> addOrFindWebAddress(String webAddress, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) get(ClassificationService.class).find(
				WebAddress,
				originatingSystem.getEnterpriseID(),
				identityToken);
		
		
		boolean found = address.builder()
		                       .withClassification(ipAddressClassification)
		                       .withValue(webAddress)
		                       .withEnterprise(originatingSystem.getEnterpriseID())
		                       .inDateRange()
		                       .inActiveRange(originatingSystem.getEnterprise(), identityToken)
		                       .getCount() > 0;
		if (!found)
		{
			address.setValue(webAddress);
			address.setClassificationID(ipAddressClassification);
			address.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			address.setSystemID((Systems) originatingSystem);
			address.setOriginalSourceSystemID((Systems) originatingSystem);
			address.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
					.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			address.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{ address.createDefaultSecurity(originatingSystem, identityToken); }
			
			if (EventThread.event.get() != null)
			{
				ISystems<?> activityMasterSystem = get(ISystemsService.class)
						.getActivityMaster(originatingSystem.getEnterpriseID());
				EventThread.event.get()
				                 .add(address, Created, webAddress, activityMasterSystem, identityToken);
			}
			
			try
			{
				Classification webPortAddressClassification = (Classification) get(ClassificationService.class).find(
						WebAddressPort,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webDomainAddressClassification = (Classification) get(ClassificationService.class).find(
						WebAddressDomain,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webSubDomainAddressClassification = (Classification) get(ClassificationService.class).find(
						WebAddressSubDomain,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webProtocolAddressClassification = (Classification) get(ClassificationService.class).find(
						WebAddressProtocol,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webQueryParametersAddressClassification = (Classification) get(ClassificationService.class).find(
						WebAddressQueryParameters,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webSiteAddressClassification = (Classification) get(ClassificationService.class).find(
						WebAddressSite,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webUrlAddressClassification = (Classification) get(ClassificationService.class).find(
						WebAddressUrl,
						originatingSystem.getEnterpriseID(),
						identityToken);
				URL url = new URL(webAddress);
				Pattern pattern = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?(\\?.*)?");
				Matcher matcher = pattern.matcher(webAddress);
				matcher.find();
				
				String protocol = matcher.group(1);
				String domain = matcher.group(2);
				String port = matcher.group(3);
				String uri = matcher.group(4);
				Address webDetails = new Address();
				webDetails.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
				webDetails.setSystemID((Systems) originatingSystem);
				webDetails.setEnterpriseID(((Systems) originatingSystem).getEnterpriseID());
				webDetails.setOriginalSourceSystemID((Systems) originatingSystem);
				webDetails.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
						.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				
				//
				webDetails = new Address();
				webDetails.setValue(url.getPort() + "");
				webDetails.setClassificationID(webPortAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) originatingSystem);
				webDetails.setSystemID((Systems) originatingSystem);
				webDetails.setEnterpriseID(((Systems) originatingSystem).getEnterpriseID());
				webDetails.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
						.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				webDetails.persist();
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{ webDetails.createDefaultSecurity(originatingSystem, identityToken); }
				
				webDetails = new Address();
				webDetails.setValue(domain);
				webDetails.setClassificationID(webDomainAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) originatingSystem);
				webDetails.setSystemID((Systems) originatingSystem);
				webDetails.setEnterpriseID(((Systems) originatingSystem).getEnterpriseID());
				webDetails.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
						.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				webDetails.persist();
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{ webDetails.createDefaultSecurity(originatingSystem, identityToken); }
				
				webDetails = new Address();
				webDetails.setValue(domain);
				webDetails.setClassificationID(webDomainAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) originatingSystem);
				webDetails.setSystemID((Systems) originatingSystem);
				webDetails.setEnterpriseID(((Systems) originatingSystem).getEnterpriseID());
				webDetails.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
						.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				webDetails.persist();
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{ webDetails.createDefaultSecurity(originatingSystem, identityToken); }
				
				webDetails = new Address();
				webDetails.setValue(protocol);
				webDetails.setClassificationID(webProtocolAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) originatingSystem);
				webDetails.setSystemID((Systems) originatingSystem);
				webDetails.setEnterpriseID(((Systems) originatingSystem).getEnterpriseID());
				webDetails.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
						.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				webDetails.persist();
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{ webDetails.createDefaultSecurity(originatingSystem, identityToken); }
				
				webDetails = new Address();
				webDetails.setValue(uri);
				webDetails.setOriginalSourceSystemID((Systems) originatingSystem);
				webDetails.setClassificationID(webSiteAddressClassification);
				webDetails.setSystemID((Systems) originatingSystem);
				webDetails.setEnterpriseID(((Systems) originatingSystem).getEnterpriseID());
				webDetails.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
						.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				webDetails.persist();
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{ webDetails.createDefaultSecurity(originatingSystem, identityToken); }
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			address = address.builder()
			                 .withClassification(ipAddressClassification)
			                 .withValue(webAddress)
			                 .withEnterprise(originatingSystem.getEnterpriseID())
			                 .inDateRange()
			                 .inActiveRange(originatingSystem.getEnterprise(), identityToken)
			                 .get()
			                 .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + webAddress));
		}
		
		return address;
	}
	
	@Override
	public IAddress<?> addOrFindHomePhoneContact(String phoneNumber, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException
	{
		Classification homePhoneNumber = (Classification) get(ClassificationService.class).find(
				HomeTelephoneNumber,
				originatingSystem.getEnterpriseID(),
				identityToken);
		
		Classification homePhoneNumberCountryCodeClassification = (Classification) get(ClassificationService.class).find(
				HomeTelephoneCountryCode,
				originatingSystem.getEnterpriseID(),
				identityToken);
		Classification homePhoneExtensionNumberClassification = (Classification) get(ClassificationService.class).find(
				HomeTelephoneExtensionNumber,
				originatingSystem.getEnterpriseID(),
				identityToken);
		Classification homePhoneAreaCodeClassification = (Classification) get(ClassificationService.class).find(
				HomeTelephoneAreaCode,
				originatingSystem.getEnterpriseID(),
				identityToken);
		
		PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(phoneNumber);
		
		String contactEncrypted = new Passwords().integerEncrypt(phoneNumberDTO.getCompleteNumber()
		                                                                       .getBytes());
		Address streetAddress = new Address();
		if (streetAddress.builder()
		                 .hasClassification(homePhoneNumberCountryCodeClassification, phoneNumberDTO.getCountryCode())
		                 .hasClassification(homePhoneExtensionNumberClassification, Strings.nullToEmpty(phoneNumberDTO.getExtension()))
		                 .hasClassification(homePhoneAreaCodeClassification, phoneNumberDTO.getAreaCode())
		                 .withClassification(homePhoneNumber)
		                 .withValue(contactEncrypted)
		                 .getCount() > 0)
		{
			return streetAddress.builder()
			                    .hasClassification(homePhoneNumberCountryCodeClassification, phoneNumberDTO.getCountryCode())
			                    .hasClassification(homePhoneExtensionNumberClassification, Strings.nullToEmpty(phoneNumberDTO.getExtension()))
			                    .hasClassification(homePhoneAreaCodeClassification, phoneNumberDTO.getAreaCode())
			                    .withClassification(homePhoneNumber)
			                    .withValue(contactEncrypted)
			                    .get(true)
			                    .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + homePhoneNumber));
		}
		
		streetAddress.setValue(contactEncrypted);
		streetAddress.setClassificationID(homePhoneNumber);
		streetAddress.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		streetAddress.setSystemID((Systems) originatingSystem);
		streetAddress.setOriginalSourceSystemID((Systems) originatingSystem);
		streetAddress.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
				.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
		streetAddress.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{ streetAddress.createDefaultSecurity(originatingSystem, identityToken); }
		
		if (EventThread.event.get() != null)
		{
			ISystems<?> activityMasterSystem = get(ISystemsService.class)
					.getActivityMaster(originatingSystem.getEnterpriseID());
			EventThread.event.get()
			                 .add(streetAddress, Created, streetAddress.getValue(), activityMasterSystem, identityToken);
		}
		
		streetAddress.add(homePhoneNumberCountryCodeClassification, phoneNumberDTO.getCountryCode(), originatingSystem, identityToken);
		streetAddress.add(homePhoneExtensionNumberClassification, Strings.nullToEmpty(phoneNumberDTO.getExtension()), originatingSystem, identityToken);
		streetAddress.add(homePhoneAreaCodeClassification, phoneNumberDTO.getAreaCode(), originatingSystem, identityToken);
		
		return streetAddress;
	}
	
	@Override
	public IAddress<?> addOrFindCellPhoneContact(String phoneNumber, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException
	{
		Classification homePhoneNumber = (Classification) get(ClassificationService.class).find(
				HomeCellNumber,
				originatingSystem.getEnterpriseID(),
				identityToken);
		
		Classification homePhoneNumberCountryCodeClassification = (Classification) get(ClassificationService.class).find(
				HomeCellCountryCode,
				originatingSystem.getEnterpriseID(),
				identityToken);
		
		Classification homePhoneAreaCodeClassification = (Classification) get(ClassificationService.class).find(
				HomeCellAreaCode,
				originatingSystem.getEnterpriseID(),
				identityToken);
		
		PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(phoneNumber);
		
		Address streetAddress = new Address();
		
		String contactEncrypted = new Passwords().integerEncrypt(phoneNumberDTO.getCompleteNumber()
		                                                                       .getBytes());
		
		if (streetAddress.builder()
		                 .hasClassification(homePhoneNumberCountryCodeClassification, phoneNumberDTO.getCountryCode())
		                 .hasClassification(homePhoneAreaCodeClassification, phoneNumberDTO.getAreaCode())
		                 .withClassification(homePhoneNumber)
		                 .withValue(contactEncrypted)
		                 .getCount() > 0)
		{
			return streetAddress.builder()
			                    .hasClassification(homePhoneNumberCountryCodeClassification, phoneNumberDTO.getCountryCode())
			                    .hasClassification(homePhoneAreaCodeClassification, phoneNumberDTO.getAreaCode())
			                    .withClassification(homePhoneNumber)
			                    .withValue(contactEncrypted)
			                    .get(true)
			                    .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + homePhoneNumber));
		}
		
		streetAddress.setValue(contactEncrypted);
		streetAddress.setClassificationID(homePhoneNumber);
		streetAddress.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		streetAddress.setSystemID((Systems) originatingSystem);
		streetAddress.setOriginalSourceSystemID((Systems) originatingSystem);
		streetAddress.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
				.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
		streetAddress.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{ streetAddress.createDefaultSecurity(originatingSystem, identityToken); }
		if (EventThread.event.get() != null)
		{
			ISystems<?> activityMasterSystem = get(ISystemsService.class)
					.getActivityMaster(originatingSystem.getEnterpriseID());
			EventThread.event.get()
			                 .add(streetAddress, Created, streetAddress.getValue(), activityMasterSystem, identityToken);
		}
		
		streetAddress.add(homePhoneNumberCountryCodeClassification, phoneNumberDTO.getCountryCode(), originatingSystem, identityToken);
		streetAddress.add(homePhoneAreaCodeClassification, phoneNumberDTO.getAreaCode(), originatingSystem, identityToken);
		
		return streetAddress;
	}
	
	@Override
	public IAddress<?> addOrFindStreetAddress(String number, String street, String streetType, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException
	{
		Address streetAddress = new Address();
		Classification buildingNumberClassification = (Classification) get(ClassificationService.class).find(
				AddressBuildingClassifications.BuildingNumber,
				originatingSystem.getEnterpriseID(),
				identityToken);
		Classification buildingStreetClassification = (Classification) get(ClassificationService.class).find(
				AddressBuildingClassifications.BuildingStreet,
				originatingSystem.getEnterpriseID(),
				identityToken);
		Classification buildingStreetTypeClassification = (Classification) get(ClassificationService.class).find(
				AddressBuildingClassifications.BuildingStreetType,
				originatingSystem.getEnterpriseID(),
				identityToken);
		
		Classification buildingAddressClassification = (Classification) get(ClassificationService.class).find(
				AddressBuildingClassifications.BuildingAddress,
				originatingSystem.getEnterpriseID(),
				identityToken);
		
		if (streetAddress.builder()
		                 .hasClassification(buildingStreetClassification, street)
		                 .hasClassification(buildingNumberClassification, number)
		                 .hasClassification(buildingStreetTypeClassification, streetType)
		                 .withClassification(buildingAddressClassification)
		                 .getCount() > 0)
		{
			return streetAddress.builder()
			                    .hasClassification(buildingStreetClassification, street)
			                    .hasClassification(buildingNumberClassification, number)
			                    .hasClassification(buildingStreetTypeClassification, streetType)
			                    .withClassification(buildingAddressClassification)
			                    .get(true)
			                    .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + number + " " + street + " " + Strings.nullToEmpty(streetType)));
		}
		
		Address address = new Address();
		address.setValue(number + " " + street + " " + streetType);
		address.setClassificationID(buildingAddressClassification);
		address.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		address.setSystemID((Systems) originatingSystem);
		address.setOriginalSourceSystemID((Systems) originatingSystem);
		address.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
				.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
		address.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{ address.createDefaultSecurity(originatingSystem, identityToken); }
		if (EventThread.event.get() != null)
		{
			ISystems<?> activityMasterSystem = get(ISystemsService.class)
					.getActivityMaster(originatingSystem.getEnterpriseID());
			EventThread.event.get()
			                 .add(address, Created, address.getValue(), activityMasterSystem, identityToken);
		}
		
		address.add(buildingNumberClassification, number, originatingSystem, identityToken);
		address.add(buildingStreetClassification, street, originatingSystem, identityToken);
		address.add(buildingStreetTypeClassification, streetType, originatingSystem, identityToken);
		
		return address;
	}
	
	@Override
	public IAddress<?> addOrFindPostalAddress(String boxIdentifier, String boxNumber, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification boxNumberClassification = (Classification) get(ClassificationService.class).find(
				BoxNumber,
				originatingSystem.getEnterpriseID(),
				identityToken);
		Classification boxidentifierClassification = (Classification) get(ClassificationService.class).find(
				BoxIdentifier,
				originatingSystem.getEnterpriseID(),
				identityToken);
		
		Classification boxAddressClassification = (Classification) get(ClassificationService.class).find(
				BoxAddress,
				originatingSystem.getEnterpriseID(),
				identityToken);
		
		if (address.builder()
		           .hasClassification(boxidentifierClassification, boxIdentifier)
		           .hasClassification(boxNumberClassification, boxNumber)
		           .withClassification(boxAddressClassification)
		           .getCount() > 0)
		{
			return address.builder()
			              .hasClassification(boxidentifierClassification, boxIdentifier)
			              .hasClassification(boxNumberClassification, boxNumber)
			              .withClassification(boxAddressClassification)
			              .get(true)
			              .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + boxIdentifier + " " + boxNumber));
		}
		
		address.setValue(boxIdentifier + " " + boxNumber);
		address.setClassificationID(boxAddressClassification);
		address.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		address.setSystemID((Systems) originatingSystem);
		address.setOriginalSourceSystemID((Systems) originatingSystem);
		address.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
				.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
		address.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{ address.createDefaultSecurity(originatingSystem, identityToken); }
		if (EventThread.event.get() != null)
		{
			ISystems<?> activityMasterSystem = get(ISystemsService.class)
					.getActivityMaster(originatingSystem.getEnterpriseID());
			EventThread.event.get()
			                 .add(address, Created, address.getValue(), activityMasterSystem, identityToken);
		}
		
		address.add(boxNumberClassification, boxNumber, originatingSystem, identityToken);
		address.add(boxidentifierClassification, boxIdentifier, originatingSystem, identityToken);
		
		return address;
	}
	
}
