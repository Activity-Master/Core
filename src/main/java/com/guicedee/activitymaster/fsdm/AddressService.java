package com.guicedee.activitymaster.fsdm;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressBuildingClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications;
import com.guicedee.activitymaster.fsdm.client.services.dto.PhoneNumberDTO;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.AddressException;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressBoxClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressRemoteSystemClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressTelephoneClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressWebClassifications.*;

@SuppressWarnings("Duplicates")

public class AddressService
		implements IAddressService<AddressService>
{
	private static final Pattern ipAddressPattern = Pattern.compile("((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(?:\\.|$)){4}");
	
	@Inject
	private IClassificationService<?> classificationServiceProvider;
	
	@Inject
	private IEnterprise<?, ?> enterprise;
	
	@Override
	public IAddress<?, ?> get()
	{
		return new Address();
	}
	
	@Override
	public IAddress<?, ?> create(String addressClassification, ISystems<?, ?> system, String value, java.util.UUID... identifyingToken)
	{
		return create(addressClassification, null, system, value, identifyingToken);
	}
	@Transactional()
	@Override
	//@Transactional()
	public IAddress<?, ?> create(String addressClassification, java.util.UUID key, ISystems<?, ?> system, String value, java.util.UUID... identifyingToken)
	{
		Address addy = new Address();
		
		Classification classification = (Classification) classificationServiceProvider.find(addressClassification,
				system, identifyingToken);
		
		boolean found = addy.builder()
		                    .withClassification(addressClassification, system)
		                    .withValue(value)
		                    .withEnterprise(enterprise)
		                    .inDateRange()
		                    .inActiveRange()
		                    .getCount() > 0;
		
		if (!found)
		{
			if(key != null)
			addy.setId(key);
			addy.setEnterpriseID(system.getEnterpriseID());
			addy.setClassificationID(classification);
			addy.setValue(value);
			addy.setSystemID(system);
			addy.setOriginalSourceSystemID(system);
			IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
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
	@Transactional()
	@Override
	//@Transactional()
	public IAddress<?, ?> addOrFindIPAddress(String ipAddress, ISystems<?, ?> system, java.util.UUID... identityToken) throws AddressException
	{
		if (!ipAddressPattern.matcher(ipAddress)
		                     .matches())
		{
			throw new AddressException("Invalid IP Address");
		}
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) classificationServiceProvider.find(RemoteAddressIPAddress.name(),
				system,
				identityToken);
		
		boolean found = address.builder()
		                       .withClassification(ipAddressClassification)
		                       .withValue(ipAddress)
		                       .withEnterprise(enterprise)
		                       .inDateRange()
		                       .inActiveRange()
		                       .getCount() > 0;
		
		if (!found)
		{
			address.setValue(ipAddress);
			address.setClassificationID(ipAddressClassification);
			address.setEnterpriseID(enterprise);
			address.setSystemID(system);
			address.setOriginalSourceSystemID(system);
			IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
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
			                 .inActiveRange()
			                 .get()
			                 .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + ipAddress));
		}
		return address;
	}
	@Transactional()
	@Override
	//@Transactional()
	public IAddress<?, ?> addOrFindHostName(String hostName, ISystems<?, ?> system, java.util.UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) classificationServiceProvider.find(
				RemoteAddressHostName.name(),
				system,
				identityToken);
		
		boolean found = address.builder()
		                       .withClassification(ipAddressClassification)
		                       .withValue(hostName)
		                       .withEnterprise(enterprise)
		                       .inDateRange()
		                       .inActiveRange()
		                       .getCount() > 0;
		
		if (!found)
		{
			address.setValue(hostName);
			address.setClassificationID(ipAddressClassification);
			address.setEnterpriseID(enterprise);
			address.setSystemID(system);
			address.setOriginalSourceSystemID(system);
			IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
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
			                 .inActiveRange()
			                 .get()
			                 .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + hostName));
		}
		
		return address;
	}
	@Transactional()
	@Override
	//@Transactional()
	public IAddress<?, ?> addOrFindWebAddress(String webAddress, ISystems<?, ?> system, java.util.UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification ipAddressClassification = (Classification) classificationServiceProvider.find(
				WebAddress.name(),
				system,
				identityToken);
		
		
		boolean found = address.builder()
		                       .withClassification(WebAddress.name(), system)
		                       .withValue(webAddress)
		                       .withEnterprise(enterprise)
		                       .inDateRange()
		                       .inActiveRange()
		                       .getCount() > 0;
		if (!found)
		{
			address.setValue(webAddress);
			address.setClassificationID(ipAddressClassification);
			address.setEnterpriseID(enterprise);
			address.setSystemID(system);
			address.setOriginalSourceSystemID(system);
			IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
			address.setActiveFlagID(activeFlag);
			address.persist();
			address.createDefaultSecurity(system, identityToken);
			
			try
			{
				Classification webPortAddressClassification = (Classification) classificationServiceProvider.find(
						WebAddressPort.name(),
						system,
						identityToken);
				Classification webDomainAddressClassification = (Classification) classificationServiceProvider.find(
						WebAddressDomain.name(),
						system,
						identityToken);
				Classification webSubDomainAddressClassification = (Classification) classificationServiceProvider.find(
						WebAddressSubDomain.name(),
						system,
						identityToken);
				Classification webProtocolAddressClassification = (Classification) classificationServiceProvider.find(
						WebAddressProtocol.name(),
						system,
						identityToken);
				Classification webQueryParametersAddressClassification = (Classification) classificationServiceProvider.find(
						WebAddressQueryParameters.name(),
						system,
						identityToken);
				Classification webSiteAddressClassification = (Classification) classificationServiceProvider.find(
						WebAddressSite.name(),
						system,
						identityToken);
				Classification webUrlAddressClassification = (Classification) classificationServiceProvider.find(
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
			                 .inActiveRange()
			                 .get()
			                 .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + webAddress));
		}
		
		return address;
	}
	@Transactional()
	@Override
	//@Transactional()
	public IAddress<?, ?> addOrFindPhoneContact(String phoneNumber, ISystems<?, ?> system, java.util.UUID... identityToken) throws AddressException
	{
		Classification homePhoneNumber = (Classification) classificationServiceProvider.find(
				TelephoneNumber.name(),
				system,
				identityToken);
		
		Classification homePhoneNumberCountryCodeClassification = (Classification) classificationServiceProvider.find(
				TelephoneCountryCode.name(),
				system,
				identityToken);
		Classification homePhoneExtensionNumberClassification = (Classification) classificationServiceProvider.find(
				TelephoneExtensionNumber.name(),
				system,
				identityToken);
		Classification homePhoneAreaCodeClassification = (Classification) classificationServiceProvider.find(
				TelephoneAreaCode.name(),
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
		IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
		streetAddress.setActiveFlagID(activeFlag);
		streetAddress.persist();
		streetAddress.createDefaultSecurity(system, identityToken);
		
		
		streetAddress.addClassification(homePhoneNumberCountryCodeClassification.getName(), phoneNumberDTO.getCountryCode(), system, identityToken);
		streetAddress.addClassification(homePhoneExtensionNumberClassification.getName(), Strings.nullToEmpty(phoneNumberDTO.getExtension()), system, identityToken);
		streetAddress.addClassification(homePhoneAreaCodeClassification.getName(), phoneNumberDTO.getAreaCode(), system, identityToken);
		
		return streetAddress;
	}
	@Transactional()
	@Override
	//@Transactional()
	public IAddress<?, ?> addOrFindEmailContact(String emailAddressString, ISystems<?, ?> system, java.util.UUID... identityToken) throws AddressException
	{
		Classification emailAddress = (Classification) classificationServiceProvider.find(
				AddressEmailClassifications.EmailAddress.name(),
				system,
				identityToken);
		
		Classification emailAddressHost = (Classification) classificationServiceProvider.find(
				AddressEmailClassifications.EmailAddressHost.name(),
				system,
				identityToken);
		
		Classification emailAddressDomain = (Classification) classificationServiceProvider.find(
				AddressEmailClassifications.EmailAddressDomain.name(),
				system,
				identityToken);
		
		Classification emailAddressUser = (Classification) classificationServiceProvider.find(
				AddressEmailClassifications.EmailAddressUser.name(),
				system,
				identityToken);
		
		Address emailAddy = new Address();
		String host = null;
		String user = null;
		String domain = null;
		try
		{
			host = emailAddressString.substring(emailAddressString.indexOf('@') + 1, emailAddressString.indexOf('.'));
			user = emailAddressString.substring(0, emailAddressString.indexOf('@'));
			domain = emailAddressString.substring(emailAddressString.indexOf('.') + 1);
			
			if (emailAddy.builder()
			             .hasClassification(emailAddressHost, host)
			             .hasClassification(emailAddressDomain, domain)
			             .withClassification(emailAddress)
			             .withValue(emailAddressString)
			             .getCount() > 0)
			{
				return emailAddy.builder()
				                .hasClassification(emailAddressHost, host)
				                .hasClassification(emailAddressDomain, domain)
				                .withClassification(emailAddress)
				                .withValue(emailAddressString)
				                .get(true)
				                .orElseThrow(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + emailAddress));
			}
			
			emailAddy.setValue(emailAddressString);
			
			emailAddy.setClassificationID(emailAddress);
			emailAddy.setEnterpriseID(enterprise);
			emailAddy.setSystemID(system);
			emailAddy.setOriginalSourceSystemID(system);
			IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
			emailAddy.setActiveFlagID(activeFlag);
			emailAddy.persist();
			emailAddy.createDefaultSecurity(system, identityToken);
			
		}
		catch (Throwable T)
		{
			throw new AddressException("Unable to create email address - invalid value", T);
		}
		
		emailAddy.addOrReuseClassification(emailAddressHost.toString(), host, system, identityToken);
		emailAddy.addOrReuseClassification(emailAddressDomain.toString(), domain, system, identityToken);
		emailAddy.addOrReuseClassification(emailAddressUser.toString(), user, system, identityToken);
		
		return emailAddy;
	}
	@Transactional()
	@Override
	public Optional<? extends IRelationshipValue<?, IAddress<?, ?>, ?>> findCellPhoneContact(IInvolvedParty<?, ?> involvedParty, ISystems<?, ?> system, java.util.UUID... identityToken) throws AddressException
	{
		return involvedParty.findAddress(HomeCellNumber.name(), null, system, true, true, identityToken);
	}
	@Transactional()
	@Override
	//@Transactional()
	public IAddress<?, ?> addOrFindStreetAddress(String number, String street, String streetType, ISystems<?, ?> system, java.util.UUID... identityToken) throws AddressException
	{
		Address streetAddress = new Address();
		Classification buildingNumberClassification = (Classification) classificationServiceProvider.find(
				AddressBuildingClassifications.BuildingNumber.name(),
				system,
				identityToken);
		Classification buildingStreetClassification = (Classification) classificationServiceProvider.find(
				AddressBuildingClassifications.BuildingStreet.name(),
				system,
				identityToken);
		Classification buildingStreetTypeClassification = (Classification) classificationServiceProvider.find(
				AddressBuildingClassifications.BuildingStreetType.name(),
				system,
				identityToken);
		
		Classification buildingAddressClassification = (Classification) classificationServiceProvider.find(
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
		IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
		address.setActiveFlagID(activeFlag);
		address.persist();
		address.createDefaultSecurity(system, identityToken);
		
		address.addClassification(buildingNumberClassification.getName(), number, system, identityToken);
		address.addClassification(buildingStreetClassification.getName(), street, system, identityToken);
		address.addClassification(buildingStreetTypeClassification.getName(), streetType, system, identityToken);
		
		return address;
	}
	@Transactional()
	@Override
	//@Transactional()
	public IAddress<?, ?> addOrFindPostalAddress(String boxIdentifier, String boxNumber, ISystems<?, ?> system, java.util.UUID... identityToken) throws AddressException
	{
		
		Address address = new Address();
		Classification boxNumberClassification = (Classification) classificationServiceProvider.find(
				BoxNumber.name(),
				system,
				identityToken);
		Classification boxidentifierClassification = (Classification) classificationServiceProvider.find(
				BoxIdentifier.name(),
				system,
				identityToken);
		
		Classification boxAddressClassification = (Classification) classificationServiceProvider.find(
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
		IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
		address.setActiveFlagID(activeFlag);
		address.persist();
		address.createDefaultSecurity(system, identityToken);
		
		address.addClassification(boxNumberClassification.getName(), boxNumber, system, identityToken);
		address.addClassification(boxidentifierClassification.getName(), boxIdentifier, system, identityToken);
		
		return address;
	}
	
}
