package com.guicedee.activitymaster.core.implementations;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.dto.IAddress;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.exceptions.AddressException;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IAddressService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.guicedee.activitymaster.core.services.classifications.address.AddressRemoteSystemClassifications.*;
import static com.guicedee.activitymaster.core.services.classifications.address.AddressWebClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")
@Singleton
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
		Optional<Address> addressExists = addy.builder()
		                                      .withClassification(classification)
		                                      .withEnterprise((Enterprise) originatingSystem.getEnterpriseID())
		                                      .withValue(value)
		                                      .inDateRange()
		                                      .withEnterprise(activityMasterSystem.getEnterprise())
		                                      .get();
		if (addressExists.isEmpty())
		{
			addy.setEnterpriseID(classification.getEnterpriseID());
			addy.setClassification(classification);
			addy.setValue(value);
			addy.setSystemID((Systems) activityMasterSystem);
			addy.setOriginalSourceSystemID((Systems) activityMasterSystem);
			addy.setActiveFlagID(classification.getActiveFlagID());
			addy.persist();
			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				addy.createDefaultSecurity(activityMasterSystem, identifyingToken);
			}
		}
		else
		{
			addy = addressExists.get();
		}

		addy.addOrReuse(addressClassification, value, originatingSystem, identifyingToken);
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

		Optional<Address> exists = address.builder()
		                                  .withClassification(ipAddressClassification)
		                                  .withValue(ipAddress)
		                                  .get();
		if (exists.isEmpty())
		{
			address.setValue(ipAddress);
			address.setClassification(ipAddressClassification);
			address.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			address.setSystemID((Systems) originatingSystem);
			address.setOriginalSourceSystemID((Systems) originatingSystem);
			address.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
					                                     .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			address.persist();
			address.createDefaultSecurity(originatingSystem, identityToken);
		}
		else
		{
			address = exists.get();
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

		Optional<Address> exists = address.builder()
		                                  .withClassification(ipAddressClassification)
		                                  .withValue(hostName)
		                                  .withEnterprise(originatingSystem.getEnterprise())
		                                  .get();
		if (exists.isEmpty())
		{
			address.setValue(hostName);
			address.setClassification(ipAddressClassification);
			address.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			address.setSystemID((Systems) originatingSystem);
			address.setOriginalSourceSystemID((Systems) originatingSystem);
			address.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
					                                     .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			address.persist();
			address.createDefaultSecurity(originatingSystem, identityToken);
		}
		else
		{
			address = exists.get();
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

		Optional<Address> exists = address.builder()
		                                  .withClassification(ipAddressClassification)
		                                  .withValue(webAddress)
		                                  .withEnterprise(originatingSystem.getEnterprise())
		                                  .get();
		if (exists.isEmpty())
		{
			address.setValue(webAddress);
			address.setClassification(ipAddressClassification);
			address.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			address.setSystemID((Systems) originatingSystem);
			address.setOriginalSourceSystemID((Systems) originatingSystem);
			address.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
					                                     .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			address.persist();
			address.createDefaultSecurity(originatingSystem, identityToken);

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
				webDetails.setClassification(webPortAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) originatingSystem);
				webDetails.setSystemID((Systems) originatingSystem);
				webDetails.setEnterpriseID(((Systems) originatingSystem).getEnterpriseID());
				webDetails.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
						                                        .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				webDetails.persist();
				webDetails.createDefaultSecurity(originatingSystem, identityToken);

				webDetails = new Address();
				webDetails.setValue(domain);
				webDetails.setClassification(webDomainAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) originatingSystem);
				webDetails.setSystemID((Systems) originatingSystem);
				webDetails.setEnterpriseID(((Systems) originatingSystem).getEnterpriseID());
				webDetails.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
						                                        .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				webDetails.persist();
				webDetails.createDefaultSecurity(originatingSystem, identityToken);

				webDetails = new Address();
				webDetails.setValue(domain);
				webDetails.setClassification(webDomainAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) originatingSystem);
				webDetails.setSystemID((Systems) originatingSystem);
				webDetails.setEnterpriseID(((Systems) originatingSystem).getEnterpriseID());
				webDetails.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
						                                        .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				webDetails.persist();
				webDetails.createDefaultSecurity(originatingSystem, identityToken);

				webDetails = new Address();
				webDetails.setValue(protocol);
				webDetails.setClassification(webProtocolAddressClassification);
				webDetails.setOriginalSourceSystemID((Systems) originatingSystem);
				webDetails.setSystemID((Systems) originatingSystem);
				webDetails.setEnterpriseID(((Systems) originatingSystem).getEnterpriseID());
				webDetails.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
						                                        .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				webDetails.persist();
				webDetails.createDefaultSecurity(originatingSystem, identityToken);

				webDetails = new Address();
				webDetails.setValue(protocol);
				webDetails.setOriginalSourceSystemID((Systems) originatingSystem);
				webDetails.setClassification(webSiteAddressClassification);
				webDetails.setSystemID((Systems) originatingSystem);
				webDetails.setEnterpriseID(((Systems) originatingSystem).getEnterpriseID());
				webDetails.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
						                                        .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
				webDetails.persist();
				webDetails.createDefaultSecurity(originatingSystem, identityToken);
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
