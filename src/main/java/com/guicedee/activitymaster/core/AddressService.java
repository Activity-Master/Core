package com.guicedee.activitymaster.core;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.address.*;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.exceptions.AddressException;
import com.guicedee.activitymaster.core.services.security.Passwords;
import com.guicedee.activitymaster.core.services.system.IAddressService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.guicedee.activitymaster.core.SystemsService.*;
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
	
	@Inject
	private IClassificationService<?> classificationService;
	
	@Inject
	@Named("Active")
	private IActiveFlag<?> activeFlag;
	
	@Inject
	private IEnterprise<?> enterprise;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?> activityMasterSystem;
	
	@Override
	public IAddress<?> create(IAddressClassification<?> addressClassification, ISystems<?> system, String value, UUID... identifyingToken)
	{
		Address addy = new Address();
		
		Classification classification = (Classification) classificationService.find(addressClassification,
				system, identifyingToken);
		
		boolean found = addy.builder()
		                    .withClassification(classification)
		                    .withValue(value)
		                    .withEnterprise(enterprise)
		                    .inDateRange()
		                    .inActiveRange(enterprise, identifyingToken)
		                    .getCount() > 0;
		
		if (!found)
		{
			addy.setEnterpriseID(enterprise);
			addy.setClassificationID(classification);
			addy.setValue(value);
			addy.setSystemID((Systems) system);
			addy.setOriginalSourceSystemID((Systems) system);
			addy.setActiveFlagID(activeFlag);
			addy.persist();
			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				addy.createDefaultSecurity(system, identifyingToken);
			}
			if (EventThread.event.get() != null)
			{
				EventThread.event.get()
				                 .add(addy, Created, value, system, identifyingToken);
			}
		}
		else
		{
			addy = addy.builder()
			           .withClassification(classification)
			           .withEnterprise(enterprise)
			           .withValue(value)
			           .inDateRange()
			           .withEnterprise(enterprise)
			           .get()
			           .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + value));
		}
		return addy;
	}
	
	@Override
	public IAddress<?> addOrFindIPAddress(String ipAddress, ISystems<?> system, UUID... identityToken) throws AddressException
	{
		if (!ipAddressPattern.matcher(ipAddress)
		                     .matches())
		{
			throw new AddressException("Invalid IP Address");
		}
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) classificationService.find(RemoteAddressIPAddress,
				system,
				identityToken);
		
		boolean found = address.builder()
		                       .withClassification(ipAddressClassification)
		                       .withValue(ipAddress)
		                       .withEnterprise(enterprise)
		                       .inDateRange()
		                       .inActiveRange(enterprise, identityToken)
		                       .getCount() > 0;
		
		if (!found)
		{
			address.setValue(ipAddress);
			address.setClassificationID(ipAddressClassification);
			address.setEnterpriseID((Enterprise) enterprise);
			address.setSystemID((Systems) system);
			address.setOriginalSourceSystemID((Systems) system);
			address.setActiveFlagID(activeFlag);
			address.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				address.createDefaultSecurity(system, identityToken);
			}
			if (EventThread.event.get() != null)
			{
				EventThread.event.get()
				                 .add(address, Created, ipAddress, system, identityToken);
			}
		}
		else
		{
			address = address.builder()
			                 .withClassification(ipAddressClassification)
			                 .withValue(ipAddress)
			                 .withEnterprise(enterprise)
			                 .inDateRange()
			                 .inActiveRange(enterprise, identityToken)
			                 .get()
			                 .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + ipAddress));
		}
		return address;
	}
	
	@Override
	public IAddress<?> addOrFindHostName(String hostName, ISystems<?> system, UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) classificationService.find(
				RemoteAddressHostName,
				system,
				identityToken);
		
		boolean found = address.builder()
		                       .withClassification(ipAddressClassification)
		                       .withValue(hostName)
		                       .withEnterprise(enterprise)
		                       .inDateRange()
		                       .inActiveRange(enterprise, identityToken)
		                       .getCount() > 0;
		
		if (!found)
		{
			address.setValue(hostName);
			address.setClassificationID(ipAddressClassification);
			address.setEnterpriseID((Enterprise) enterprise);
			address.setSystemID((Systems) system);
			address.setOriginalSourceSystemID((Systems) system);
			address.setActiveFlagID(activeFlag);
			address.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				address.createDefaultSecurity(system, identityToken);
			}
			if (EventThread.event.get() != null)
			{
				EventThread.event.get()
				                 .add(address, Created, hostName, system, identityToken);
			}
		}
		else
		{
			address = address.builder()
			                 .withClassification(ipAddressClassification)
			                 .withValue(hostName)
			                 .withEnterprise(enterprise)
			                 .inDateRange()
			                 .inActiveRange(enterprise, identityToken)
			                 .get()
			                 .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + hostName));
		}
		
		return address;
	}
	
	@Override
	public IAddress<?> addOrFindWebAddress(String webAddress, ISystems<?> system, UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) classificationService.find(
				WebAddress,
				system,
				identityToken);
		
		
		boolean found = address.builder()
		                       .withClassification(ipAddressClassification)
		                       .withValue(webAddress)
		                       .withEnterprise(enterprise)
		                       .inDateRange()
		                       .inActiveRange(enterprise, identityToken)
		                       .getCount() > 0;
		if (!found)
		{
			address.setValue(webAddress);
			address.setClassificationID(ipAddressClassification);
			address.setEnterpriseID((Enterprise) enterprise);
			address.setSystemID((Systems) system);
			address.setOriginalSourceSystemID((Systems) system);
			address.setActiveFlagID(activeFlag);
			address.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				address.createDefaultSecurity(system, identityToken);
			}
			
			if (EventThread.event.get() != null)
			{
				EventThread.event.get()
				                 .add(address, Created, webAddress, system, identityToken);
			}
			
			try
			{
				Classification webPortAddressClassification = (Classification) classificationService.find(
						WebAddressPort,
						system,
						identityToken);
				Classification webDomainAddressClassification = (Classification) classificationService.find(
						WebAddressDomain,
						system,
						identityToken);
				Classification webSubDomainAddressClassification = (Classification) classificationService.find(
						WebAddressSubDomain,
						system,
						identityToken);
				Classification webProtocolAddressClassification = (Classification) classificationService.find(
						WebAddressProtocol,
						system,
						identityToken);
				Classification webQueryParametersAddressClassification = (Classification) classificationService.find(
						WebAddressQueryParameters,
						system,
						identityToken);
				Classification webSiteAddressClassification = (Classification) classificationService.find(
						WebAddressSite,
						system,
						identityToken);
				Classification webUrlAddressClassification = (Classification) classificationService.find(
						WebAddressUrl,
						system,
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
				webDetails.setEnterpriseID((Enterprise) enterprise);
				webDetails.setSystemID((Systems) system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setOriginalSourceSystemID((Systems) system);
				webDetails.setActiveFlagID(activeFlag);
				
				//
				webDetails = new Address();
				webDetails.setValue(url.getPort() + "");
				webDetails.setClassificationID(webPortAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) system);
				webDetails.setSystemID((Systems) system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setActiveFlagID(activeFlag);
				webDetails.persist();
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{
					webDetails.createDefaultSecurity(system, identityToken);
				}
				
				webDetails = new Address();
				webDetails.setValue(domain);
				webDetails.setClassificationID(webDomainAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) system);
				webDetails.setSystemID((Systems) system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setActiveFlagID(activeFlag);
				webDetails.persist();
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{
					webDetails.createDefaultSecurity(system, identityToken);
				}
				
				webDetails = new Address();
				webDetails.setValue(domain);
				webDetails.setClassificationID(webDomainAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) system);
				webDetails.setSystemID((Systems) system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setActiveFlagID(activeFlag);
				webDetails.persist();
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{
					webDetails.createDefaultSecurity(system, identityToken);
				}
				
				webDetails = new Address();
				webDetails.setValue(protocol);
				webDetails.setClassificationID(webProtocolAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) system);
				webDetails.setSystemID((Systems) system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setActiveFlagID(activeFlag);
				webDetails.persist();
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{
					webDetails.createDefaultSecurity(system, identityToken);
				}
				
				webDetails = new Address();
				webDetails.setValue(uri);
				webDetails.setOriginalSourceSystemID((Systems) system);
				webDetails.setClassificationID(webSiteAddressClassification);
				webDetails.setSystemID((Systems) system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setActiveFlagID(activeFlag);
				webDetails.persist();
				if (GuiceContext.get(ActivityMasterConfiguration.class)
				                .isSecurityEnabled())
				{
					webDetails.createDefaultSecurity(system, identityToken);
				}
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
			                 .withEnterprise(enterprise)
			                 .inDateRange()
			                 .inActiveRange(enterprise, identityToken)
			                 .get()
			                 .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + webAddress));
		}
		
		return address;
	}
	
	@Override
	public IAddress<?> addOrFindHomePhoneContact(String phoneNumber, ISystems<?> system, UUID... identityToken) throws AddressException
	{
		Classification homePhoneNumber = (Classification) classificationService.find(
				HomeTelephoneNumber,
				system,
				identityToken);
		
		Classification homePhoneNumberCountryCodeClassification = (Classification) classificationService.find(
				HomeTelephoneCountryCode,
				system,
				identityToken);
		Classification homePhoneExtensionNumberClassification = (Classification) classificationService.find(
				HomeTelephoneExtensionNumber,
				system,
				identityToken);
		Classification homePhoneAreaCodeClassification = (Classification) classificationService.find(
				HomeTelephoneAreaCode,
				system,
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
		streetAddress.setEnterpriseID((Enterprise) enterprise);
		streetAddress.setSystemID((Systems) system);
		streetAddress.setOriginalSourceSystemID((Systems) system);
		streetAddress.setActiveFlagID(activeFlag);
		streetAddress.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			streetAddress.createDefaultSecurity(system, identityToken);
		}
		
		streetAddress.add(homePhoneNumberCountryCodeClassification, phoneNumberDTO.getCountryCode(), system, identityToken);
		streetAddress.add(homePhoneExtensionNumberClassification, Strings.nullToEmpty(phoneNumberDTO.getExtension()), system, identityToken);
		streetAddress.add(homePhoneAreaCodeClassification, phoneNumberDTO.getAreaCode(), system, identityToken);
		
		return streetAddress;
	}
	
	@Override
	public IAddress<?> addOrFindCellPhoneContact(String phoneNumber, ISystems<?> originatingSystem, UUID... identityToken) throws AddressException
	{
		Classification homePhoneNumber = (Classification) classificationService.find(
				HomeCellNumber,
				originatingSystem,
				identityToken);
		
		Classification homePhoneNumberCountryCodeClassification = (Classification) classificationService.find(
				HomeCellCountryCode,
				originatingSystem,
				identityToken);
		
		Classification homePhoneAreaCodeClassification = (Classification) classificationService.find(
				HomeCellAreaCode,
				originatingSystem,
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
		streetAddress.setEnterpriseID((Enterprise) enterprise);
		streetAddress.setSystemID((Systems) originatingSystem);
		streetAddress.setOriginalSourceSystemID((Systems) originatingSystem);
		streetAddress.setActiveFlagID(activeFlag);
		streetAddress.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			streetAddress.createDefaultSecurity(originatingSystem, identityToken);
		}
		if (EventThread.event.get() != null)
		{
			EventThread.event.get()
			                 .add(streetAddress, Created, streetAddress.getValue(), activityMasterSystem, identityToken);
		}
		
		streetAddress.add(homePhoneNumberCountryCodeClassification, phoneNumberDTO.getCountryCode(), originatingSystem, identityToken);
		streetAddress.add(homePhoneAreaCodeClassification, phoneNumberDTO.getAreaCode(), originatingSystem, identityToken);
		
		return streetAddress;
	}
	
	@Override
	public IAddress<?> addOrFindStreetAddress(String number, String street, String streetType, ISystems<?> system, UUID... identityToken) throws AddressException
	{
		Address streetAddress = new Address();
		Classification buildingNumberClassification = (Classification) classificationService.find(
				AddressBuildingClassifications.BuildingNumber,
				system,
				identityToken);
		Classification buildingStreetClassification = (Classification) classificationService.find(
				AddressBuildingClassifications.BuildingStreet,
				system,
				identityToken);
		Classification buildingStreetTypeClassification = (Classification) classificationService.find(
				AddressBuildingClassifications.BuildingStreetType,
				system,
				identityToken);
		
		Classification buildingAddressClassification = (Classification) classificationService.find(
				AddressBuildingClassifications.BuildingAddress,
				system,
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
		address.setEnterpriseID((Enterprise) enterprise);
		address.setSystemID((Systems) system);
		address.setOriginalSourceSystemID((Systems) system);
		address.setActiveFlagID(activeFlag);
		address.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			address.createDefaultSecurity(system, identityToken);
		}
		if (EventThread.event.get() != null)
		{
			EventThread.event.get()
			                 .add(address, Created, address.getValue(), system, identityToken);
		}
		
		address.add(buildingNumberClassification, number, system, identityToken);
		address.add(buildingStreetClassification, street, system, identityToken);
		address.add(buildingStreetTypeClassification, streetType, system, identityToken);
		
		return address;
	}
	
	@Override
	public IAddress<?> addOrFindPostalAddress(String boxIdentifier, String boxNumber, ISystems<?> system, UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification boxNumberClassification = (Classification) classificationService.find(
				BoxNumber,
				system,
				identityToken);
		Classification boxidentifierClassification = (Classification) classificationService.find(
				BoxIdentifier,
				system,
				identityToken);
		
		Classification boxAddressClassification = (Classification) classificationService.find(
				BoxAddress,
				system,
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
		address.setEnterpriseID((Enterprise) enterprise);
		address.setSystemID((Systems) system);
		address.setOriginalSourceSystemID((Systems) system);
		address.setActiveFlagID(activeFlag);
		address.persist();
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			address.createDefaultSecurity(system, identityToken);
		}
		if (EventThread.event.get() != null)
		{
			EventThread.event.get()
			                 .add(address, Created, address.getValue(), system, identityToken);
		}
		
		address.add(boxNumberClassification, boxNumber, system, identityToken);
		address.add(boxidentifierClassification, boxIdentifier, system, identityToken);
		
		return address;
	}
	
}
