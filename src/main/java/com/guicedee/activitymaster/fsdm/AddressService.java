package com.guicedee.activitymaster.fsdm;

/**
 * Reactivity Migration Checklist:
 * 
 * [✓] One action per Mutiny.Session at a time
 *     - All operations on a session are sequential
 *     - No parallel operations on the same session
 * 
 * [!] Pass Mutiny.Session through the chain
 *     - Most methods accept session as parameter
 *     - The createDefaultSecurity method is called without passing the session parameter
 * 
 * [✓] No await() usage
 *     - Using reactive chains instead of blocking operations
 * 
 * [!] Synchronous execution of reactive chains
 *     - Most reactive chains execute synchronously
 *     - The create method uses fire-and-forget operations with subscribe().with()
 *       for createDefaultSecurity, which should be chained properly
 * 
 * [✓] No parallel operations on a session
 *     - Not using Uni.combine().all().unis() with operations that share the same session
 * 
 * [✓] No session/transaction creation in libraries
 *     - Sessions are passed in from the caller
 *     - No sessionFactory.withTransaction() in methods
 * 
 * See ReactivityMigrationGuide.md for more details on these rules.
 */

import com.google.common.base.Strings;
import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddress;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressBuildingClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressEmailClassifications;
import com.guicedee.activitymaster.fsdm.client.services.dto.PhoneNumberDTO;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.AddressException;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.applicationEnterpriseName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressBoxClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressRemoteSystemClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressTelephoneClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressWebClassifications.*;

@SuppressWarnings("Duplicates")
@Log4j2
@Singleton
public class AddressService
		implements IAddressService<AddressService>
{
	private static final Pattern ipAddressPattern = Pattern.compile("((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(?:\\.|$)){4}");

	@Inject
	private IClassificationService<?> classificationServiceProvider;

	@Override
	public IAddress<?, ?> get()
	{
		return new Address();
	}

	@Override
	public Uni<IAddress<?, ?>> create(Mutiny.Session session, String addressClassification, ISystems<?, ?> system, String value, UUID... identifyingToken)
	{
		return create(session, addressClassification, null, system, value, identifyingToken);
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> create(Mutiny.Session session, String addressClassification, UUID key, ISystems<?, ?> system, String value, UUID... identifyingToken)
	{
		return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
			var createSession = tuple.getItem1();
			var createEnterprise = tuple.getItem2();
			var createSystem = tuple.getItem3();
			var createIdentityToken = tuple.getItem4();

			Address addy = new Address();

			return classificationServiceProvider.find(createSession, addressClassification, createSystem, createIdentityToken)
			        .chain(classification -> {
			            return addy.builder(createSession)
			                    .withClassification(addressClassification, createSystem)
			                    .withValue(value)
			                    .withEnterprise(createEnterprise)
			                    .inDateRange()
			                    .inActiveRange()
			                    .getCount()
			                    .onFailure().invoke(error -> log.error("Error checking if address exists: {}", error.getMessage(), error))
			                    .chain(count -> {
			                        boolean found = count > 0;
			                        if (!found)
			                        {
			                            if(key != null)
			                                addy.setId(key);
			                            addy.setEnterpriseID(createEnterprise);
			                            addy.setClassificationID((Classification) classification);
			                            addy.setValue(value);
			                            addy.setSystemID(createSystem);
			                            addy.setOriginalSourceSystemID(createSystem.getId());

			                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                            return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                                    .chain(activeFlag -> {
			                                        addy.setActiveFlagID(activeFlag);
			                                        return createSession.persist(addy).replaceWith(Uni.createFrom().item(addy));
			                                    })
			                                    .chain(persisted -> {
			                                        return persisted.createDefaultSecurity(createSession, createSystem, createIdentityToken)
			                                            .onItem().invoke(() -> log.debug("Security setup completed successfully"))
			                                            .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity", error))
			                                            .onFailure().recoverWithItem(() -> null)
			                                            .map(result -> (IAddress<?, ?>) addy);
			                                    });
			                        }
			                        else
			                        {
			                            return addy.builder(createSession)
			                                    .withClassification(addressClassification, createSystem)
			                                    .withEnterprise(createEnterprise)
			                                    .withValue(value)
			                                    .inDateRange()
			                                    .withEnterprise(createEnterprise)
			                                    .get()
			                                    .onFailure().invoke(error -> log.error("Error finding existing address: {}", error.getMessage(), error))
			                                    .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + value))
			                                    .map(address -> (IAddress<?, ?>) address);
			                        }
			                    });
			        });
		});
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindIPAddress(Mutiny.Session session, String ipAddress, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		if (!ipAddressPattern.matcher(ipAddress)
		                     .matches())
		{
			return Uni.createFrom().failure(new AddressException("Invalid IP Address"));
		}

		return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
			var createSession = tuple.getItem1();
			var createEnterprise = tuple.getItem2();
			var createSystem = tuple.getItem3();
			var createIdentityToken = tuple.getItem4();

			Address address = new Address();
			return classificationServiceProvider.find(createSession, RemoteAddressIPAddress.name(), createSystem, createIdentityToken)
			        .chain(ipAddressClassification -> {
			            return address.builder(createSession)
			                    .withClassification((Classification) ipAddressClassification)
			                    .withValue(ipAddress)
			                    .withEnterprise(createEnterprise)
			                    .inDateRange()
			                    .inActiveRange()
			                    .getCount()
			                    .onFailure().invoke(error -> log.error("Error checking if IP address exists: {}", error.getMessage(), error))
			                    .chain(count -> {
			                        boolean found = count > 0;
			                        if (!found)
			                        {
			                            address.setValue(ipAddress);
			                            address.setClassificationID((Classification) ipAddressClassification);
			                            address.setEnterpriseID(createEnterprise);
			                            address.setSystemID(createSystem);
			                            address.setOriginalSourceSystemID(createSystem.getId());

			                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                            return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                                    .chain(activeFlag -> {
			                                        address.setActiveFlagID(activeFlag);
			                                        return createSession.persist(address).replaceWith(Uni.createFrom().item(address));
			                                    })
			                                    .chain(persisted -> {
			                                        return address.createDefaultSecurity(createSession, createSystem, createIdentityToken)
			                                            .onItem().invoke(() -> log.debug("Security setup completed successfully for IP address"))
			                                            .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for IP address", error))
			                                            .onFailure().recoverWithItem(() -> null)
			                                            .map(result -> (IAddress<?, ?>) address);
			                                    });
			                        }
			                        else
			                        {
			                            return address.builder(createSession)
			                                    .withClassification((Classification) ipAddressClassification)
			                                    .withValue(ipAddress)
			                                    .withEnterprise(createEnterprise)
			                                    .inDateRange()
			                                    .inActiveRange()
			                                    .get()
			                                    .onFailure().invoke(error -> log.error("Error finding existing IP address: {}", error.getMessage(), error))
			                                    .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + ipAddress))
			                                    .map(foundAddress -> (IAddress<?, ?>) foundAddress);
			                        }
			                    });
			        });
		});
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindHostName(Mutiny.Session session, String hostName, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
			var createSession = tuple.getItem1();
			var createEnterprise = tuple.getItem2();
			var createSystem = tuple.getItem3();
			var createIdentityToken = tuple.getItem4();

			Address address = new Address();
			return classificationServiceProvider.find(createSession, RemoteAddressHostName.name(), createSystem, createIdentityToken)
			        .chain(hostNameClassification -> {
			            return address.builder(createSession)
			                    .withClassification((Classification) hostNameClassification)
			                    .withValue(hostName)
			                    .withEnterprise(createEnterprise)
			                    .inDateRange()
			                    .inActiveRange()
			                    .getCount()
			                    .onFailure().invoke(error -> log.error("Error checking if hostname exists: {}", error.getMessage(), error))
			                    .chain(count -> {
			                        boolean found = count > 0;
			                        if (!found)
			                        {
			                            address.setValue(hostName);
			                            address.setClassificationID((Classification) hostNameClassification);
			                            address.setEnterpriseID(createEnterprise);
			                            address.setSystemID(createSystem);
			                            address.setOriginalSourceSystemID(createSystem.getId());

			                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                            return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                                    .chain(activeFlag -> {
			                                        address.setActiveFlagID(activeFlag);
			                                        return createSession.persist(address).replaceWith(Uni.createFrom().item(address));
			                                    })
			                                    .chain(persisted -> {
			                                        return address.createDefaultSecurity(createSession, createSystem, createIdentityToken)
			                                            .onItem().invoke(() -> log.debug("Security setup completed successfully for hostname"))
			                                            .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for hostname", error))
			                                            .onFailure().recoverWithItem(() -> null)
			                                            .map(result -> (IAddress<?, ?>) address);
			                                    });
			                        }
			                        else
			                        {
			                            return address.builder(createSession)
			                                    .withClassification((Classification) hostNameClassification)
			                                    .withValue(hostName)
			                                    .withEnterprise(createEnterprise)
			                                    .inDateRange()
			                                    .inActiveRange()
			                                    .get()
			                                    .onFailure().invoke(error -> log.error("Error finding existing hostname: {}", error.getMessage(), error))
			                                    .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + hostName))
			                                    .map(foundAddress -> (IAddress<?, ?>) foundAddress);
			                        }
			                    });
			        });
		});
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindWebAddress(Mutiny.Session session, String webAddress, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
			var createSession = tuple.getItem1();
			var createEnterprise = tuple.getItem2();
			var createSystem = tuple.getItem3();
			var createIdentityToken = tuple.getItem4();

			Address address = new Address();
			return classificationServiceProvider.find(createSession, WebAddress.name(), createSystem, createIdentityToken)
			        .chain(webAddressClassification -> {
			            return address.builder(createSession)
			                    .withClassification(WebAddress.name(), createSystem)
			                    .withValue(webAddress)
			                    .withEnterprise(createEnterprise)
			                    .inDateRange()
			                    .inActiveRange()
			                    .getCount()
			                    .onFailure().invoke(error -> log.error("Error checking if web address exists: {}", error.getMessage(), error))
			                    .chain(count -> {
			                        boolean found = count > 0;
			                        if (!found)
			                        {
			                            address.setValue(webAddress);
			                            address.setClassificationID((Classification) webAddressClassification);
			                            address.setEnterpriseID(createEnterprise);
			                            address.setSystemID(createSystem);
			                            address.setOriginalSourceSystemID(createSystem.getId());

			                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                            return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                                    .chain(activeFlag -> {
			                                        address.setActiveFlagID(activeFlag);
			                                        return createSession.persist(address).replaceWith(Uni.createFrom().item(address));
			                                    })
			                                    .chain(persisted -> {
			                                        return address.createDefaultSecurity(createSession, createSystem, createIdentityToken)
			                                            .onItem().invoke(() -> log.debug("Security setup completed successfully for web address"))
			                                            .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for web address", error))
			                                            .onFailure().recoverWithItem(() -> null)
			                                            .chain(securityResult -> {
			                                                try {
			                                                    URL url = new URL(webAddress);
			                                                    Pattern pattern = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?(\\?.*)?");
			                                                    Matcher matcher = pattern.matcher(webAddress);
			                                                    matcher.find();

			                                                    String protocol = matcher.group(1);
			                                                    String domain = matcher.group(2);
			                                                    String port = matcher.group(3);
			                                                    String uri = matcher.group(4);

			                                                    return classificationServiceProvider.find(createSession, WebAddressPort.name(), createSystem, createIdentityToken)
			                                                        .chain(webPortClassification -> {
			                                                            Address webDetails = new Address();
			                                                            webDetails.setValue(url.getPort() + "");
			                                                            webDetails.setClassificationID((Classification) webPortClassification);
			                                                            webDetails.setOriginalSourceSystemID(createSystem.getId());
			                                                            webDetails.setSystemID(createSystem);
			                                                            webDetails.setEnterpriseID(createEnterprise);

			                                                            return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                                                                .chain(portActiveFlag -> {
			                                                                    webDetails.setActiveFlagID(portActiveFlag);
			                                                                    return createSession.persist(webDetails).replaceWith(Uni.createFrom().item(webDetails));
			                                                                })
			                                                                .chain(portPersisted -> {
			                                                                    return classificationServiceProvider.find(createSession, WebAddressDomain.name(), createSystem, createIdentityToken);
			                                                                });
			                                                        })
			                                                        .chain(webDomainClassification -> {
			                                                            Address domainDetails = new Address();
			                                                            domainDetails.setValue(domain);
			                                                            domainDetails.setClassificationID((Classification) webDomainClassification);
			                                                            domainDetails.setOriginalSourceSystemID(createSystem.getId());
			                                                            domainDetails.setSystemID(createSystem);
			                                                            domainDetails.setEnterpriseID(createEnterprise);

			                                                            return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                                                                .chain(domainActiveFlag -> {
			                                                                    domainDetails.setActiveFlagID(domainActiveFlag);
			                                                                    return createSession.persist(domainDetails).replaceWith(Uni.createFrom().item(domainDetails));
			                                                                })
			                                                                .chain(domainPersisted -> {
			                                                                    return classificationServiceProvider.find(createSession, WebAddressProtocol.name(), createSystem, createIdentityToken);
			                                                                });
			                                                        })
			                                                        .chain(webProtocolClassification -> {
			                                                            Address protocolDetails = new Address();
			                                                            protocolDetails.setValue(protocol);
			                                                            protocolDetails.setClassificationID((Classification) webProtocolClassification);
			                                                            protocolDetails.setOriginalSourceSystemID(createSystem.getId());
			                                                            protocolDetails.setSystemID(createSystem);
			                                                            protocolDetails.setEnterpriseID(createEnterprise);

			                                                            return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                                                                .chain(protocolActiveFlag -> {
			                                                                    protocolDetails.setActiveFlagID(protocolActiveFlag);
			                                                                    return createSession.persist(protocolDetails).replaceWith(Uni.createFrom().item(protocolDetails));
			                                                                })
			                                                                .chain(protocolPersisted -> {
			                                                                    return classificationServiceProvider.find(createSession, WebAddressSite.name(), createSystem, createIdentityToken);
			                                                                });
			                                                        })
			                                                        .chain(webSiteClassification -> {
			                                                            Address siteDetails = new Address();
			                                                            siteDetails.setValue(uri);
			                                                            siteDetails.setClassificationID((Classification) webSiteClassification);
			                                                            siteDetails.setOriginalSourceSystemID(createSystem.getId());
			                                                            siteDetails.setSystemID(createSystem);
			                                                            siteDetails.setEnterpriseID(createEnterprise);

			                                                            return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                                                                .chain(siteActiveFlag -> {
			                                                                    siteDetails.setActiveFlagID(siteActiveFlag);
			                                                                    return createSession.persist(siteDetails).replaceWith(Uni.createFrom().item(siteDetails));
			                                                                })
			                                                                .map(siteResult -> (IAddress<?, ?>) address);
			                                                        });
			                                                } catch (MalformedURLException e) {
			                                                    log.error("Malformed URL: {}", webAddress, e);
			                                                    return Uni.createFrom().item((IAddress<?, ?>) address);
			                                                }
			                                            });
			                                    });
			                        }
			                        else
			                        {
			                            return address.builder(createSession)
			                                    .withClassification((Classification) webAddressClassification)
			                                    .withValue(webAddress)
			                                    .withEnterprise(createEnterprise)
			                                    .inDateRange()
			                                    .inActiveRange()
			                                    .get()
			                                    .onFailure().invoke(error -> log.error("Error finding existing web address: {}", error.getMessage(), error))
			                                    .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + webAddress))
			                                    .map(foundAddress -> (IAddress<?, ?>) foundAddress);
			                        }
			                    });
			        });
		});
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindPhoneContact(Mutiny.Session session, String phoneNumber, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(phoneNumber);

		return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
			var createSession = tuple.getItem1();
			var createEnterprise = tuple.getItem2();
			var createSystem = tuple.getItem3();
			var createIdentityToken = tuple.getItem4();

			Address streetAddress = new Address();
			return classificationServiceProvider.find(createSession, TelephoneNumber.name(), createSystem, createIdentityToken)
			    .chain(homePhoneNumber -> {
			        return streetAddress.builder(createSession)
			            .withClassification(TelephoneNumber.name(), createSystem)
			            .withValue(phoneNumber)
			            .withEnterprise(createEnterprise)
			            .inDateRange()
			            .inActiveRange()
			            .getCount()
			            .onFailure().invoke(error -> log.error("Error checking if phone number exists: {}", error.getMessage(), error))
			            .chain(count -> {
			                boolean found = count > 0;
			                if (!found) {
			                    streetAddress.setValue(phoneNumber);
			                    streetAddress.setClassificationID((Classification) homePhoneNumber);
			                    streetAddress.setEnterpriseID(createEnterprise);
			                    streetAddress.setSystemID(createSystem);
			                    streetAddress.setOriginalSourceSystemID(createSystem.getId());

			                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                    return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                        .chain(activeFlag -> {
			                            streetAddress.setActiveFlagID(activeFlag);
			                            return createSession.persist(streetAddress).replaceWith(Uni.createFrom().item(streetAddress));
			                        })
			                        .chain(persisted -> {
			                            return streetAddress.createDefaultSecurity(createSession, createSystem, createIdentityToken)
			                                .onItem().invoke(() -> log.debug("Security setup completed successfully for phone number"))
			                                .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for phone number", error))
			                                .onFailure().recoverWithItem(() -> null)
			                                .chain(securityResult -> {
			                                    return classificationServiceProvider.find(createSession, TelephoneCountryCode.name(), createSystem, createIdentityToken)
			                                        .chain(homePhoneNumberCountryCodeClassification -> {
			                                            return streetAddress.addClassification(
			                                                    createSession,
			                                                    ((Classification) homePhoneNumberCountryCodeClassification).getName(),
			                                                    phoneNumberDTO.getCountryCode(),
			                                                    createSystem,
			                                                    createIdentityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(createSession, TelephoneExtensionNumber.name(), createSystem, createIdentityToken);
			                                        })
			                                        .chain(homePhoneExtensionNumberClassification -> {
			                                            return streetAddress.addClassification(
			                                                    createSession,
			                                                    ((Classification) homePhoneExtensionNumberClassification).getName(),
			                                                    Strings.nullToEmpty(phoneNumberDTO.getExtension()),
			                                                    createSystem,
			                                                    createIdentityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(createSession, TelephoneAreaCode.name(), createSystem, createIdentityToken);
			                                        })
			                                        .chain(homePhoneAreaCodeClassification -> {
			                                            return streetAddress.addClassification(
			                                                    createSession,
			                                                    ((Classification) homePhoneAreaCodeClassification).getName(),
			                                                    phoneNumberDTO.getAreaCode(),
			                                                    createSystem,
			                                                    createIdentityToken);
			                                        })
			                                        .map(result -> (IAddress<?, ?>) streetAddress);
			                                });
			                        });
			                } else {
			                    return streetAddress.builder(createSession)
			                        .withClassification(TelephoneNumber.name(), createSystem)
			                        .withValue(phoneNumber)
			                        .withEnterprise(createEnterprise)
			                        .inDateRange()
			                        .inActiveRange()
			                        .get()
			                        .onFailure().invoke(error -> log.error("Error finding existing phone number: {}", error.getMessage(), error))
			                        .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + phoneNumber))
			                        .map(foundAddress -> (IAddress<?, ?>) foundAddress);
			                }
			            });
			    });
		});
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindEmailContact(Mutiny.Session session, String emailAddressString, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		String host, user, domain;

		try {
			host = emailAddressString.substring(emailAddressString.indexOf('@') + 1, emailAddressString.indexOf('.'));
			user = emailAddressString.substring(0, emailAddressString.indexOf('@'));
			domain = emailAddressString.substring(emailAddressString.indexOf('.') + 1);
		} catch (Throwable T) {
			return Uni.createFrom().failure(new AddressException("Unable to create email address - invalid value", T));
		}

		return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
			var createSession = tuple.getItem1();
			var createEnterprise = tuple.getItem2();
			var createSystem = tuple.getItem3();
			var createIdentityToken = tuple.getItem4();

			Address emailAddy = new Address();
			return classificationServiceProvider.find(createSession, AddressEmailClassifications.EmailAddress.name(), createSystem, createIdentityToken)
			    .chain(emailAddress -> {
			        return emailAddy.builder(createSession)
			            .withClassification(AddressEmailClassifications.EmailAddress.name(), createSystem)
			            .withValue(emailAddressString)
			            .withEnterprise(createEnterprise)
			            .inDateRange()
			            .inActiveRange()
			            .getCount()
			            .onFailure().invoke(error -> log.error("Error checking if email address exists: {}", error.getMessage(), error))
			            .chain(count -> {
			                boolean found = count > 0;
			                if (!found) {
			                    emailAddy.setValue(emailAddressString);
			                    emailAddy.setClassificationID((Classification) emailAddress);
			                    emailAddy.setEnterpriseID(createEnterprise);
			                    emailAddy.setSystemID(createSystem);
			                    emailAddy.setOriginalSourceSystemID(createSystem.getId());

			                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                    return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                        .chain(activeFlag -> {
			                            emailAddy.setActiveFlagID(activeFlag);
			                            return createSession.persist(emailAddy).replaceWith(Uni.createFrom().item(emailAddy));
			                        })
			                        .chain(persisted -> {
			                            return emailAddy.createDefaultSecurity(createSession, createSystem, createIdentityToken)
			                                .onItem().invoke(() -> log.debug("Security setup completed successfully for email address"))
			                                .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for email address", error))
			                                .onFailure().recoverWithItem(() -> null)
			                                .chain(securityResult -> {
			                                    return classificationServiceProvider.find(createSession, AddressEmailClassifications.EmailAddressHost.name(), createSystem, createIdentityToken)
			                                        .chain(emailAddressHost -> {
			                                            return emailAddy.addOrReuseClassification(
			                                                    createSession,
			                                                    ((Classification) emailAddressHost).toString(),
			                                                    host,
			                                                    createSystem,
			                                                    createIdentityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(createSession, AddressEmailClassifications.EmailAddressDomain.name(), createSystem, createIdentityToken);
			                                        })
			                                        .chain(emailAddressDomain -> {
			                                            return emailAddy.addOrReuseClassification(
			                                                    createSession,
			                                                    ((Classification) emailAddressDomain).toString(),
			                                                    domain,
			                                                    createSystem,
			                                                    createIdentityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(createSession, AddressEmailClassifications.EmailAddressUser.name(), createSystem, createIdentityToken);
			                                        })
			                                        .chain(emailAddressUser -> {
			                                            return emailAddy.addOrReuseClassification(
			                                                    createSession,
			                                                    ((Classification) emailAddressUser).toString(),
			                                                    user,
			                                                    createSystem,
			                                                    createIdentityToken);
			                                        })
			                                        .map(result -> (IAddress<?, ?>) emailAddy);
			                                });
			                        });
			                } else {
			                    return emailAddy.builder(createSession)
			                        .withClassification(AddressEmailClassifications.EmailAddress.name(), createSystem)
			                        .withValue(emailAddressString)
			                        .withEnterprise(createEnterprise)
			                        .inDateRange()
			                        .inActiveRange()
			                        .get()
			                        .onFailure().invoke(error -> log.error("Error finding existing email address: {}", error.getMessage(), error))
			                        .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + emailAddressString))
			                        .map(foundAddress -> (IAddress<?, ?>) foundAddress);
			                }
			            });
			    });
		});
	}
	//@Transactional()
	@Override
	public Uni<IRelationshipValue<?, IAddress<?, ?>, ?>> findCellPhoneContact(Mutiny.Session session, IInvolvedParty<?, ?> involvedParty, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		// Check if the involvedParty is null
		if (involvedParty == null) {
			return Uni.createFrom().failure(new AddressException("Involved party cannot be null"));
		}
		return involvedParty.findAddress(session, HomeCellNumber.name(), null, system, true, true, identityToken)
					   .map(result->result);
	}

	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindStreetAddress(Mutiny.Session session, String number, String street, String streetType, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
			var createSession = tuple.getItem1();
			var createEnterprise = tuple.getItem2();
			var createSystem = tuple.getItem3();
			var createIdentityToken = tuple.getItem4();

			Address streetAddress = new Address();
			return classificationServiceProvider.find(createSession, AddressBuildingClassifications.BuildingAddress.name(), createSystem, createIdentityToken)
			    .chain(buildingAddressClassification -> {
			        return streetAddress.builder(createSession)
			            .withClassification(AddressBuildingClassifications.BuildingAddress.name(), createSystem)
			            .withValue(number + " " + street + " " + streetType)
			            .withEnterprise(createEnterprise)
			            .inDateRange()
			            .inActiveRange()
			            .getCount()
			            .onFailure().invoke(error -> log.error("Error checking if street address exists: {}", error.getMessage(), error))
			            .chain(count -> {
			                boolean found = count > 0;
			                if (!found) {
			                    Address address = new Address();
			                    address.setValue(number + " " + street + " " + streetType);
			                    address.setClassificationID((Classification) buildingAddressClassification);
			                    address.setEnterpriseID(createEnterprise);
			                    address.setSystemID(createSystem);
			                    address.setOriginalSourceSystemID(createSystem.getId());

			                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                    return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                        .chain(activeFlag -> {
			                            address.setActiveFlagID(activeFlag);
			                            return createSession.persist(address).replaceWith(Uni.createFrom().item(address));
			                        })
			                        .chain(persisted -> {
			                            return address.createDefaultSecurity(createSession, createSystem, createIdentityToken)
			                                .onItem().invoke(() -> log.debug("Security setup completed successfully for street address"))
			                                .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for street address", error))
			                                .onFailure().recoverWithItem(() -> null)
			                                .chain(securityResult -> {
			                                    return classificationServiceProvider.find(createSession, AddressBuildingClassifications.BuildingNumber.name(), createSystem, createIdentityToken)
			                                        .chain(buildingNumberClassification -> {
			                                            return address.addClassification(
			                                                    createSession,
			                                                    ((Classification) buildingNumberClassification).getName(),
			                                                    number,
			                                                    createSystem,
			                                                    createIdentityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(createSession, AddressBuildingClassifications.BuildingStreet.name(), createSystem, createIdentityToken);
			                                        })
			                                        .chain(buildingStreetClassification -> {
			                                            return address.addClassification(
			                                                    createSession,
			                                                    ((Classification) buildingStreetClassification).getName(),
			                                                    street,
			                                                    createSystem,
			                                                    createIdentityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(createSession, AddressBuildingClassifications.BuildingStreetType.name(), createSystem, createIdentityToken);
			                                        })
			                                        .chain(buildingStreetTypeClassification -> {
			                                            return address.addClassification(
			                                                    createSession,
			                                                    ((Classification) buildingStreetTypeClassification).getName(),
			                                                    streetType,
			                                                    createSystem,
			                                                    createIdentityToken);
			                                        })
			                                        .map(result -> (IAddress<?, ?>) address);
			                                });
			                        });
			                } else {
			                    return streetAddress.builder(createSession)
			                        .withClassification(AddressBuildingClassifications.BuildingAddress.name(), createSystem)
			                        .withValue(number + " " + street + " " + streetType)
			                        .withEnterprise(createEnterprise)
			                        .inDateRange()
			                        .inActiveRange()
			                        .get()
			                        .onFailure().invoke(error -> log.error("Error finding existing street address: {}", error.getMessage(), error))
			                        .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + number + " " + street + " " + Strings.nullToEmpty(streetType)))
			                        .map(foundAddress -> (IAddress<?, ?>) foundAddress);
			                }
			            });
			    });
		});
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindPostalAddress(Mutiny.Session session, String boxIdentifier, String boxNumber, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
			var createSession = tuple.getItem1();
			var createEnterprise = tuple.getItem2();
			var createSystem = tuple.getItem3();
			var createIdentityToken = tuple.getItem4();

			Address address = new Address();
			return classificationServiceProvider.find(createSession, BoxAddress.name(), createSystem, createIdentityToken)
			    .chain(boxAddressClassification -> {
			        return address.builder(createSession)
			            .withClassification(BoxAddress.name(), createSystem)
			            .withValue(boxIdentifier + " " + boxNumber)
			            .withEnterprise(createEnterprise)
			            .inDateRange()
			            .inActiveRange()
			            .getCount()
			            .onFailure().invoke(error -> log.error("Error checking if postal address exists: {}", error.getMessage(), error))
			            .chain(count -> {
			                boolean found = count > 0;
			                if (!found) {
			                    Address postalAddress = new Address();
			                    postalAddress.setValue(boxIdentifier + " " + boxNumber);
			                    postalAddress.setClassificationID((Classification) boxAddressClassification);
			                    postalAddress.setEnterpriseID(createEnterprise);
			                    postalAddress.setSystemID(createSystem);
			                    postalAddress.setOriginalSourceSystemID(createSystem.getId());

			                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                    return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
			                        .chain(activeFlag -> {
			                            postalAddress.setActiveFlagID(activeFlag);
			                            return createSession.persist(postalAddress).replaceWith(Uni.createFrom().item(postalAddress));
			                        })
			                        .chain(persisted -> {
			                            return postalAddress.createDefaultSecurity(createSession, createSystem, createIdentityToken)
			                                .onItem().invoke(() -> log.debug("Security setup completed successfully for postal address"))
			                                .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for postal address", error))
			                                .onFailure().recoverWithItem(() -> null)
			                                .chain(securityResult -> {
			                                    return classificationServiceProvider.find(createSession, BoxNumber.name(), createSystem, createIdentityToken)
			                                        .chain(boxNumberClassification -> {
			                                            return postalAddress.addClassification(
			                                                    createSession,
			                                                    ((Classification) boxNumberClassification).getName(),
			                                                    boxNumber,
			                                                    createSystem,
			                                                    createIdentityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(createSession, BoxIdentifier.name(), createSystem, createIdentityToken);
			                                        })
			                                        .chain(boxidentifierClassification -> {
			                                            return postalAddress.addClassification(
			                                                    createSession,
			                                                    ((Classification) boxidentifierClassification).getName(),
			                                                    boxIdentifier,
			                                                    createSystem,
			                                                    createIdentityToken);
			                                        })
			                                        .map(result -> (IAddress<?, ?>) postalAddress);
			                                });
			                        });
			                } else {
			                    return address.builder(createSession)
			                        .withClassification(BoxAddress.name(), createSystem)
			                        .withValue(boxIdentifier + " " + boxNumber)
			                        .withEnterprise(createEnterprise)
			                        .inDateRange()
			                        .inActiveRange()
			                        .get()
			                        .onFailure().invoke(error -> log.error("Error finding existing postal address: {}", error.getMessage(), error))
			                        .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + boxIdentifier + " " + boxNumber))
			                        .map(foundAddress -> (IAddress<?, ?>) foundAddress);
			                }
			            });
			    });
		});
	}

}

