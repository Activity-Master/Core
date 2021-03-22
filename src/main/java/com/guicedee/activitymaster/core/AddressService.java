package com.guicedee.activitymaster.core;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.IAddressService;
import com.guicedee.activitymaster.client.services.IClassificationService;
import com.guicedee.activitymaster.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.classifications.address.AddressBuildingClassifications;
import com.guicedee.activitymaster.client.services.dto.PhoneNumberDTO;
import com.guicedee.activitymaster.client.services.exceptions.AddressException;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.guicedee.activitymaster.client.services.classifications.address.AddressBoxClassifications.*;
import static com.guicedee.activitymaster.client.services.classifications.address.AddressHomeCellClassifications.*;
import static com.guicedee.activitymaster.client.services.classifications.address.AddressHomeTelephoneClassifications.*;
import static com.guicedee.activitymaster.client.services.classifications.address.AddressRemoteSystemClassifications.*;
import static com.guicedee.activitymaster.client.services.classifications.address.AddressWebClassifications.*;
import static com.guicedee.activitymaster.core.SystemsService.*;

@SuppressWarnings("Duplicates")

public class AddressService
		implements IAddressService<AddressService>
{
	private static final Pattern ipAddressPattern = Pattern.compile("((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(?:\\.|$)){4}");
	
	@Inject
	private IClassificationService<?> classificationService;
	
	@Inject
	@Named("Active")
	private IActiveFlag<?,?> activeFlag;
	
	@Inject
	private IEnterprise<?,?> enterprise;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;
	
	@Override
	public IAddress<?, ?> create(String addressClassification, ISystems<?,?> system, String value, UUID... identifyingToken)
	{
		Address addy = new Address();
		
		Classification classification = (Classification) classificationService.find(addressClassification,
				system, identifyingToken);
		
		boolean found = addy.builder()
		                    .withClassification(addressClassification, system)
		                    .withValue(value)
		                    .withEnterprise(enterprise)
		                    .inDateRange()
		                    .inActiveRange(enterprise, identifyingToken)
		                    .getCount() > 0;
		
		if (!found)
		{
			addy.setEnterpriseID(system.getEnterpriseID());
			addy.setClassificationID(classification);
			addy.setValue(value);
			addy.setSystemID(system);
			addy.setOriginalSourceSystemID(system);
			addy.setActiveFlagID(activeFlag);
			addy.persist();
			
			addy.createDefaultSecurity(system, identifyingToken);
		}
		else
		{
			addy = addy.builder()
			           .withClassification(addressClassification, system)
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
	public IAddress<?, ?> addOrFindIPAddress(String ipAddress, ISystems<?,?> system, UUID... identityToken) throws AddressException
	{
		if (!ipAddressPattern.matcher(ipAddress)
		                     .matches())
		{
			throw new AddressException("Invalid IP Address");
		}
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) classificationService.find(RemoteAddressIPAddress.name(),
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
			address.setEnterpriseID(enterprise);
			address.setSystemID(system);
			address.setOriginalSourceSystemID(system);
			address.setActiveFlagID(activeFlag);
			address.persist();
			address.createDefaultSecurity(system, identityToken);
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
	public IAddress<?, ?> addOrFindHostName(String hostName, ISystems<?,?> system, UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) classificationService.find(
				RemoteAddressHostName.name(),
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
			address.setEnterpriseID(enterprise);
			address.setSystemID(system);
			address.setOriginalSourceSystemID(system);
			address.setActiveFlagID(activeFlag);
			address.persist();
			address.createDefaultSecurity(system, identityToken);
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
	public IAddress<?, ?> addOrFindWebAddress(String webAddress, ISystems<?,?> system, UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) classificationService.find(
				WebAddress.name(),
				system,
				identityToken);
		
		
		boolean found = address.builder()
		                       .withClassification(WebAddress.name(), system)
		                       .withValue(webAddress)
		                       .withEnterprise(enterprise)
		                       .inDateRange()
		                       .inActiveRange(enterprise, identityToken)
		                       .getCount() > 0;
		if (!found)
		{
			address.setValue(webAddress);
			address.setClassificationID(ipAddressClassification);
			address.setEnterpriseID(enterprise);
			address.setSystemID(system);
			address.setOriginalSourceSystemID(system);
			address.setActiveFlagID(activeFlag);
			address.persist();
			address.createDefaultSecurity(system, identityToken);
			
			try
			{
				Classification webPortAddressClassification = (Classification) classificationService.find(
						WebAddressPort.name(),
						system,
						identityToken);
				Classification webDomainAddressClassification = (Classification) classificationService.find(
						WebAddressDomain.name(),
						system,
						identityToken);
				Classification webSubDomainAddressClassification = (Classification) classificationService.find(
						WebAddressSubDomain.name(),
						system,
						identityToken);
				Classification webProtocolAddressClassification = (Classification) classificationService.find(
						WebAddressProtocol.name(),
						system,
						identityToken);
				Classification webQueryParametersAddressClassification = (Classification) classificationService.find(
						WebAddressQueryParameters.name(),
						system,
						identityToken);
				Classification webSiteAddressClassification = (Classification) classificationService.find(
						WebAddressSite.name(),
						system,
						identityToken);
				Classification webUrlAddressClassification = (Classification) classificationService.find(
						WebAddressUrl.name(),
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
				webDetails.setEnterpriseID(enterprise);
				webDetails.setSystemID(system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setOriginalSourceSystemID(system);
				webDetails.setActiveFlagID(activeFlag);
				
				//
				webDetails = new Address();
				webDetails.setValue(url.getPort() + "");
				webDetails.setClassificationID(webPortAddressClassification);
				webDetails.setOriginalSourceSystemID(system);
				webDetails.setSystemID(system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setActiveFlagID(activeFlag);
				webDetails.persist();
				webDetails.createDefaultSecurity(system, identityToken);
				
				
				webDetails = new Address();
				webDetails.setValue(domain);
				webDetails.setClassificationID(webDomainAddressClassification);
				webDetails.setOriginalSourceSystemID(system);
				webDetails.setSystemID(system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setActiveFlagID(activeFlag);
				webDetails.persist();
				webDetails.createDefaultSecurity(system, identityToken);
				
				
				webDetails = new Address();
				webDetails.setValue(domain);
				webDetails.setClassificationID(webDomainAddressClassification);
				webDetails.setOriginalSourceSystemID(system);
				webDetails.setSystemID(system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setActiveFlagID(activeFlag);
				webDetails.persist();
				webDetails.createDefaultSecurity(system, identityToken);
				
				
				webDetails = new Address();
				webDetails.setValue(protocol);
				webDetails.setClassificationID(webProtocolAddressClassification);
				webDetails.setOriginalSourceSystemID(system);
				webDetails.setSystemID(system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setActiveFlagID(activeFlag);
				webDetails.persist();
				webDetails.createDefaultSecurity(system, identityToken);
				
				
				webDetails = new Address();
				webDetails.setValue(uri);
				webDetails.setOriginalSourceSystemID(system);
				webDetails.setClassificationID(webSiteAddressClassification);
				webDetails.setSystemID(system);
				webDetails.setEnterpriseID(((Systems) system).getEnterpriseID());
				webDetails.setActiveFlagID(activeFlag);
				webDetails.persist();
				webDetails.createDefaultSecurity(system, identityToken);
				
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
	public IAddress<?, ?> addOrFindHomePhoneContact(String phoneNumber, ISystems<?,?> system, UUID... identityToken) throws AddressException
	{
		Classification homePhoneNumber = (Classification) classificationService.find(
				HomeTelephoneNumber.name(),
				system,
				identityToken);
		
		Classification homePhoneNumberCountryCodeClassification = (Classification) classificationService.find(
				HomeTelephoneCountryCode.name(),
				system,
				identityToken);
		Classification homePhoneExtensionNumberClassification = (Classification) classificationService.find(
				HomeTelephoneExtensionNumber.name(),
				system,
				identityToken);
		Classification homePhoneAreaCodeClassification = (Classification) classificationService.find(
				HomeTelephoneAreaCode.name(),
				system,
				identityToken);
		
		PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(phoneNumber);
		
		Address streetAddress = new Address();
		if (streetAddress.builder()
		                 .hasClassification(homePhoneNumberCountryCodeClassification, phoneNumberDTO.getCountryCode())
		                 .hasClassification(homePhoneExtensionNumberClassification, Strings.nullToEmpty(phoneNumberDTO.getExtension()))
		                 .hasClassification(homePhoneAreaCodeClassification, phoneNumberDTO.getAreaCode())
		                 .withClassification(homePhoneNumber)
		                 .withValue(phoneNumber)
		                 .getCount() > 0)
		{
			return streetAddress.builder()
			                    .hasClassification(homePhoneNumberCountryCodeClassification, phoneNumberDTO.getCountryCode())
			                    .hasClassification(homePhoneExtensionNumberClassification, Strings.nullToEmpty(phoneNumberDTO.getExtension()))
			                    .hasClassification(homePhoneAreaCodeClassification, phoneNumberDTO.getAreaCode())
			                    .withClassification(homePhoneNumber)
			                    .withValue(phoneNumber)
			                    .get(true)
			                    .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + homePhoneNumber));
		}
		
		streetAddress.setValue(phoneNumber);
		streetAddress.setClassificationID(homePhoneNumber);
		streetAddress.setEnterpriseID(enterprise);
		streetAddress.setSystemID(system);
		streetAddress.setOriginalSourceSystemID(system);
		streetAddress.setActiveFlagID(activeFlag);
		streetAddress.persist();
		streetAddress.createDefaultSecurity(system, identityToken);
		
		
		streetAddress.addClassification(homePhoneNumberCountryCodeClassification.getName(), phoneNumberDTO.getCountryCode(), system, identityToken);
		streetAddress.addClassification(homePhoneExtensionNumberClassification.getName(), Strings.nullToEmpty(phoneNumberDTO.getExtension()), system, identityToken);
		streetAddress.addClassification(homePhoneAreaCodeClassification.getName(), phoneNumberDTO.getAreaCode(), system, identityToken);
		
		return streetAddress;
	}
	
	@Override
	public IAddress<?, ?> addOrFindCellPhoneContact(String phoneNumber, ISystems<?,?> originatingSystem, UUID... identityToken) throws AddressException
	{
		Classification homePhoneNumber = (Classification) classificationService.find(
				HomeCellNumber.name(),
				originatingSystem,
				identityToken);
		
		Classification homePhoneNumberCountryCodeClassification = (Classification) classificationService.find(
				HomeCellCountryCode.name(),
				originatingSystem,
				identityToken);
		
		Classification homePhoneAreaCodeClassification = (Classification) classificationService.find(
				HomeCellAreaCode.name(),
				originatingSystem,
				identityToken);
		
		PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(phoneNumber);
		
		Address streetAddress = new Address();
		
		String contactEncrypted = phoneNumberDTO.getCompleteNumber();
		
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
		streetAddress.setEnterpriseID(enterprise);
		streetAddress.setSystemID(originatingSystem);
		streetAddress.setOriginalSourceSystemID(originatingSystem);
		streetAddress.setActiveFlagID(activeFlag);
		streetAddress.persist();
		streetAddress.createDefaultSecurity(originatingSystem, identityToken);
		
		streetAddress.addClassification(homePhoneNumberCountryCodeClassification.getName(), phoneNumberDTO.getCountryCode(), originatingSystem, identityToken);
		streetAddress.addClassification(homePhoneAreaCodeClassification.getName(), phoneNumberDTO.getAreaCode(), originatingSystem, identityToken);
		
		return streetAddress;
	}
	
	@Override
	public IAddress<?, ?> addOrFindStreetAddress(String number, String street, String streetType, ISystems<?,?> system, UUID... identityToken) throws AddressException
	{
		Address streetAddress = new Address();
		Classification buildingNumberClassification = (Classification) classificationService.find(
				AddressBuildingClassifications.BuildingNumber.name(),
				system,
				identityToken);
		Classification buildingStreetClassification = (Classification) classificationService.find(
				AddressBuildingClassifications.BuildingStreet.name(),
				system,
				identityToken);
		Classification buildingStreetTypeClassification = (Classification) classificationService.find(
				AddressBuildingClassifications.BuildingStreetType.name(),
				system,
				identityToken);
		
		Classification buildingAddressClassification = (Classification) classificationService.find(
				AddressBuildingClassifications.BuildingAddress.name(),
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
		address.setEnterpriseID(enterprise);
		address.setSystemID(system);
		address.setOriginalSourceSystemID(system);
		address.setActiveFlagID(activeFlag);
		address.persist();
		address.createDefaultSecurity(system, identityToken);
		
		address.addClassification(buildingNumberClassification.getName(), number, system, identityToken);
		address.addClassification(buildingStreetClassification.getName(), street, system, identityToken);
		address.addClassification(buildingStreetTypeClassification.getName(), streetType, system, identityToken);
		
		return address;
	}
	
	@Override
	public IAddress<?, ?> addOrFindPostalAddress(String boxIdentifier, String boxNumber, ISystems<?,?> system, UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification boxNumberClassification = (Classification) classificationService.find(
				BoxNumber.name(),
				system,
				identityToken);
		Classification boxidentifierClassification = (Classification) classificationService.find(
				BoxIdentifier.name(),
				system,
				identityToken);
		
		Classification boxAddressClassification = (Classification) classificationService.find(
				BoxAddress.name(),
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
		address.setEnterpriseID(enterprise);
		address.setSystemID(system);
		address.setOriginalSourceSystemID(system);
		address.setActiveFlagID(activeFlag);
		address.persist();
		address.createDefaultSecurity(system, identityToken);
		
		address.addClassification(boxNumberClassification.getName(), boxNumber, system, identityToken);
		address.addClassification(boxidentifierClassification.getName(), boxIdentifier, system, identityToken);
		
		return address;
	}
	
}
