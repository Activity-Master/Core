package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.exceptions.AddressException;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressRemoteSystemClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.address.AddressWebClassifications.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")
@Singleton
public class AddressService
{
	private static final Pattern ipAddressPattern = Pattern.compile("((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(?:\\.|$)){4}");

	public Address addOrFindIPAddress(String ipAddress, Systems originatingSystem, UUID...identityToken) throws AddressException
	{
		if(!ipAddressPattern.matcher(ipAddress).matches())
		{
			throw new AddressException("Invalid IP Address");
		}

		Address address = new Address();
		Classification ipAddressClassification = get(ClassificationService.class).find(RemoteAddressIPAddress,
		                                                                                            originatingSystem.getEnterpriseID(),
		                                                                                            identityToken);

		Optional<Address> exists = address.builder()
		                                  .withClassification(ipAddressClassification)
		                                  .withValue(ipAddress)
		                                  .get();
		if (exists.isEmpty())
		{
			address.setValue(ipAddress);
			address.setClassification(ipAddressClassification);
			address.setEnterpriseID(originatingSystem.getEnterpriseID());
			address.setSystemID(originatingSystem);
			address.setOriginalSourceSystemID(originatingSystem);
			address.setActiveFlagID(get(IActiveFlagService.class)
			                                    .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			address.persist();
			address.createDefaultSecurity(originatingSystem,identityToken);
		}
		else
		{
			address = exists.get();
		}

		return address;
	}

	public Address addOrFindHostName(String hostName, Systems originatingSystem, UUID...identityToken) throws AddressException
	{

		Address address = new Address();
		Classification ipAddressClassification = get(ClassificationService.class).find(
				RemoteAddressHostName,
				originatingSystem.getEnterpriseID(),
				identityToken);

		Optional<Address> exists = address.builder()
		                                  .withClassification(ipAddressClassification)
		                                  .withValue(hostName)
		                                  .get();
		if (exists.isEmpty())
		{
			address.setValue(hostName);
			address.setClassification(ipAddressClassification);
			address.setEnterpriseID(originatingSystem.getEnterpriseID());
			address.setSystemID(originatingSystem);
			address.setOriginalSourceSystemID(originatingSystem);
			address.setActiveFlagID(get(IActiveFlagService.class)
			                                    .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			address.persist();
			address.createDefaultSecurity(originatingSystem,identityToken);
		}
		else
		{
			address = exists.get();
		}

		return address;
	}


	public Address addOrFindWebAddress(String webAddress, Systems originatingSystem, UUID...identityToken) throws AddressException
	{

		Address address = new Address();
		Classification ipAddressClassification = get(ClassificationService.class).find(
				WebAddress,
				originatingSystem.getEnterpriseID(),
				identityToken);

		Optional<Address> exists = address.builder()
		                                  .withClassification(ipAddressClassification)
		                                  .withValue(webAddress)
		                                  .get();
		if (exists.isEmpty())
		{
			address.setValue(webAddress);
			address.setClassification(ipAddressClassification);
			address.setEnterpriseID(originatingSystem.getEnterpriseID());
			address.setSystemID(originatingSystem);
			address.setOriginalSourceSystemID(originatingSystem);
			address.setActiveFlagID(get(IActiveFlagService.class)
			                                    .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			address.persist();
			address.createDefaultSecurity(originatingSystem,identityToken);

			try
			{
				Classification webPortAddressClassification = get(ClassificationService.class).find(
						WebAddressPort,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webDomainAddressClassification = get(ClassificationService.class).find(
						WebAddressDomain,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webSubDomainAddressClassification = get(ClassificationService.class).find(
						WebAddressSubDomain,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webProtocolAddressClassification = get(ClassificationService.class).find(
						WebAddressProtocol,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webQueryParametersAddressClassification = get(ClassificationService.class).find(
						WebAddressQueryParameters,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webSiteAddressClassification = get(ClassificationService.class).find(
						WebAddressSite,
						originatingSystem.getEnterpriseID(),
						identityToken);
				Classification webUrlAddressClassification = get(ClassificationService.class).find(
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
				webDetails.setEnterpriseID(originatingSystem.getEnterpriseID());
				webDetails.setSystemID(originatingSystem);
				webDetails.setOriginalSourceSystemID(originatingSystem);
				webDetails.setActiveFlagID(get(IActiveFlagService.class)
						                           .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				//
				webDetails.setValue(url.getPort() + "");
				webDetails.setClassification(webPortAddressClassification);
				webDetails.persist();
				webDetails.createDefaultSecurity(originatingSystem,identityToken);

				webDetails.setValue(domain);
				webDetails.setClassification(webDomainAddressClassification);
				webDetails.persist();
				webDetails.createDefaultSecurity(originatingSystem,identityToken);

				webDetails.setValue(domain);
				webDetails.setClassification(webDomainAddressClassification);
				webDetails.persist();
				webDetails.createDefaultSecurity(originatingSystem,identityToken);

				webDetails.setValue(protocol);
				webDetails.setClassification(webProtocolAddressClassification);
				webDetails.persist();
				webDetails.createDefaultSecurity(originatingSystem,identityToken);

				webDetails.setValue(protocol);
				webDetails.setClassification(webSiteAddressClassification);
				webDetails.persist();
				webDetails.createDefaultSecurity(originatingSystem,identityToken);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}

		}
		else
		{
			address = exists.get();
		}

		return address;
	}
}
