package com.guicedee.activitymaster.fsdm;

import com.google.common.base.Strings;
import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
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

import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressBoxClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressRemoteSystemClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressTelephoneClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.address.AddressWebClassifications.*;

@SuppressWarnings("Duplicates")
@Log4j2
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
		Address addy = new Address();

		// First, get the classification using reactive pattern
		var enterprise = system.getEnterprise();
		return classificationServiceProvider.find(session, addressClassification, system, identifyingToken)
		        .chain(classification -> {
		            // Check if address exists
		            return addy.builder(session)
		                    .withClassification(addressClassification, system)
		                    .withValue(value)
		                    .withEnterprise(enterprise)
		                    .inDateRange()
		                    .inActiveRange()
		                    .getCount()
		                    .onFailure().invoke(error -> log.error("Error checking if address exists: {}", error.getMessage(), error))
		                    .chain(count -> {
		                        boolean found = count > 0;
		                        if (!found)
		                        {
		                            // Address doesn't exist, create it
		                            if(key != null)
		                                addy.setId(key);
		                            addy.setEnterpriseID(system.getEnterpriseID());
		                            addy.setClassificationID((Classification) classification);
		                            addy.setValue(value);
		                            addy.setSystemID(system);
		                            addy.setOriginalSourceSystemID(system.getId());

		                            // Get active flag service
		                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		                            // Get active flag using reactive pattern
		                            return acService.getActiveFlag(session, enterprise)
		                                    .chain(activeFlag -> {
		                                        addy.setActiveFlagID(activeFlag);
		                                        return session.persist(addy).replaceWith(Uni.createFrom().item(addy));
		                                    })
		                                    .chain(persisted -> {
		                                        // Start createDefaultSecurity in parallel without waiting for it
		                                        persisted.createDefaultSecurity(session, system, identifyingToken)
		                                            .subscribe().with(
		                                                result -> {
		                                                    // Security setup completed successfully
		                                                },
		                                                error -> {
		                                                    // Log error but don't fail the main operation
		                                                    log.warn("Error in createDefaultSecurity", error);
		                                                }
		                                            );

		                                        // Return the persisted address immediately
		                                        return Uni.createFrom().item((IAddress<?, ?>) addy);
		                                    });
		                        }
		                        else
		                        {
		                            // Address exists, find and return it
		                            return addy.builder(session)
		                                    .withClassification(addressClassification, system)
		                                    .withEnterprise(enterprise)
		                                    .withValue(value)
		                                    .inDateRange()
		                                    .withEnterprise(enterprise)
		                                    .get()
		                                    .onFailure().invoke(error -> log.error("Error finding existing address: {}", error.getMessage(), error))
		                                    .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + value))
		                                    .map(address -> (IAddress<?, ?>) address);
		                        }
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

		Address address = new Address();
		var enterprise = system.getEnterprise();
		// First, get the classification using reactive pattern
		return classificationServiceProvider.find(session, RemoteAddressIPAddress.name(), system, identityToken)
		        .chain(ipAddressClassification -> {
		            // Check if address exists
		            return address.builder(session)
		                    .withClassification((Classification) ipAddressClassification)
		                    .withValue(ipAddress)
		                    .withEnterprise(enterprise)
		                    .inDateRange()
		                    .inActiveRange()
		                    .getCount()
		                    .onFailure().invoke(error -> log.error("Error checking if IP address exists: {}", error.getMessage(), error))
		                    .chain(count -> {
		                        boolean found = count > 0;
		                        if (!found)
		                        {
		                            // Address doesn't exist, create it
		                            address.setValue(ipAddress);
		                            address.setClassificationID((Classification) ipAddressClassification);
		                            address.setEnterpriseID(enterprise);
		                            address.setSystemID(system);
		                            address.setOriginalSourceSystemID(system.getId());

		                            // Get active flag service
		                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		                            // Get active flag using reactive pattern
		                            return acService.getActiveFlag(session, enterprise)
		                                    .chain(activeFlag -> {
		                                        address.setActiveFlagID(activeFlag);
		                                        return session.persist(address).replaceWith(Uni.createFrom().item(address));
		                                    })
		                                    .chain(persisted -> {
		                                        // Start createDefaultSecurity in parallel without waiting for it
		                                        address.createDefaultSecurity(session, system, identityToken)
		                                            .subscribe().with(
		                                                result -> {
		                                                    // Security setup completed successfully
		                                                },
		                                                error -> {
		                                                    // Log error but don't fail the main operation
		                                                    log.warn("Error in createDefaultSecurity for IP address", error);
		                                                }
		                                            );

		                                        // Return the persisted address immediately
		                                        return Uni.createFrom().item((IAddress<?, ?>) address);
		                                    });
		                        }
		                        else
		                        {
		                            // Address exists, find and return it
		                            return address.builder(session)
		                                    .withClassification((Classification) ipAddressClassification)
		                                    .withValue(ipAddress)
		                                    .withEnterprise(enterprise)
		                                    .inDateRange()
		                                    .inActiveRange()
		                                    .get()
		                                    .onFailure().invoke(error -> log.error("Error finding existing IP address: {}", error.getMessage(), error))
		                                    .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + ipAddress))
		                                    .map(foundAddress -> (IAddress<?, ?>) foundAddress);
		                        }
		                    });
		        });
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindHostName(Mutiny.Session session, String hostName, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		Address address = new Address();
		var enterprise = system.getEnterprise();
		// First, get the classification using reactive pattern
		return classificationServiceProvider.find(session, RemoteAddressHostName.name(), system, identityToken)
		        .chain(hostNameClassification -> {
		            // Check if address exists
		            return address.builder(session)
		                    .withClassification((Classification) hostNameClassification)
		                    .withValue(hostName)
		                    .withEnterprise(enterprise)
		                    .inDateRange()
		                    .inActiveRange()
		                    .getCount()
		                    .onFailure().invoke(error -> log.error("Error checking if hostname exists: {}", error.getMessage(), error))
		                    .chain(count -> {
		                        boolean found = count > 0;
		                        if (!found)
		                        {
		                            // Address doesn't exist, create it
		                            address.setValue(hostName);
		                            address.setClassificationID((Classification) hostNameClassification);
		                            address.setEnterpriseID(enterprise);
		                            address.setSystemID(system);
		                            address.setOriginalSourceSystemID(system.getId());

		                            // Get active flag service
		                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		                            // Get active flag using reactive pattern
		                            return acService.getActiveFlag(session, enterprise)
		                                    .chain(activeFlag -> {
		                                        address.setActiveFlagID(activeFlag);
		                                        return session.persist(address).replaceWith(Uni.createFrom().item(address));
		                                    })
		                                    .chain(persisted -> {
		                                        // Start createDefaultSecurity in parallel without waiting for it
		                                        address.createDefaultSecurity(session, system, identityToken)
		                                            .subscribe().with(
		                                                result -> {
		                                                    // Security setup completed successfully
		                                                },
		                                                error -> {
		                                                    // Log error but don't fail the main operation
		                                                    log.warn("Error in createDefaultSecurity for hostname", error);
		                                                }
		                                            );

		                                        // Return the persisted address immediately
		                                        return Uni.createFrom().item((IAddress<?, ?>) address);
		                                    });
		                        }
		                        else
		                        {
		                            // Address exists, find and return it
		                            return address.builder(session)
		                                    .withClassification((Classification) hostNameClassification)
		                                    .withValue(hostName)
		                                    .withEnterprise(enterprise)
		                                    .inDateRange()
		                                    .inActiveRange()
		                                    .get()
		                                    .onFailure().invoke(error -> log.error("Error finding existing hostname: {}", error.getMessage(), error))
		                                    .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + hostName))
		                                    .map(foundAddress -> (IAddress<?, ?>) foundAddress);
		                        }
		                    });
		        });
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindWebAddress(Mutiny.Session session, String webAddress, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		Address address = new Address();
		var enterprise = system.getEnterprise();
		// First, get the classification using reactive pattern
		return classificationServiceProvider.find(session, WebAddress.name(), system, identityToken)
		        .chain(webAddressClassification -> {
		            // Check if address exists
		            return address.builder(session)
		                    .withClassification(WebAddress.name(), system)
		                    .withValue(webAddress)
		                    .withEnterprise(enterprise)
		                    .inDateRange()
		                    .inActiveRange()
		                    .getCount()
		                    .onFailure().invoke(error -> log.error("Error checking if web address exists: {}", error.getMessage(), error))
		                    .chain(count -> {
		                        boolean found = count > 0;
		                        if (!found)
		                        {
		                            // Address doesn't exist, create it
		                            address.setValue(webAddress);
		                            address.setClassificationID((Classification) webAddressClassification);
		                            address.setEnterpriseID(enterprise);
		                            address.setSystemID(system);
		                            address.setOriginalSourceSystemID(system.getId());

		                            // Get active flag service
		                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		                            // Get active flag using reactive pattern
		                            return acService.getActiveFlag(session, enterprise)
		                                    .chain(activeFlag -> {
		                                        address.setActiveFlagID(activeFlag);
		                                        return session.persist(address).replaceWith(Uni.createFrom().item(address));
		                                    })
		                                    .chain(persisted -> {
		                                        // Start createDefaultSecurity in parallel without waiting for it
		                                        address.createDefaultSecurity(session, system, identityToken)
		                                            .subscribe().with(
		                                                result -> {
		                                                    // Security setup completed successfully
		                                                },
		                                                error -> {
		                                                    // Log error but don't fail the main operation
		                                                    log.warn("Error in createDefaultSecurity for web address", error);
		                                                }
		                                            );

		                                        try {
		                                            URL url = new URL(webAddress);
		                                            Pattern pattern = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?(\\?.*)?");
		                                            Matcher matcher = pattern.matcher(webAddress);
		                                            matcher.find();

		                                            String protocol = matcher.group(1);
		                                            String domain = matcher.group(2);
		                                            String port = matcher.group(3);
		                                            String uri = matcher.group(4);

		                                            // Process web details in a simpler way
		                                            // We'll get the port classification first
		                                            return classificationServiceProvider.find(session, WebAddressPort.name(), system, identityToken)
		                                                .chain(webPortClassification -> {
		                                                    // Create port address
		                                                    Address webDetails = new Address();
		                                                    webDetails.setValue(url.getPort() + "");
		                                                    webDetails.setClassificationID((Classification) webPortClassification);
		                                                    webDetails.setOriginalSourceSystemID(system.getId());
		                                                    webDetails.setSystemID(system);
		                                                    webDetails.setEnterpriseID(enterprise);

		                                                    // We need to get the active flag again for this entity
		                                                    return acService.getActiveFlag(session, enterprise)
		                                                        .chain(portActiveFlag -> {
		                                                            webDetails.setActiveFlagID(portActiveFlag);
		                                                            return session.persist(webDetails).replaceWith(Uni.createFrom().item(webDetails));
		                                                        })
		                                                        .chain(portPersisted -> {
		                                                            // Now get the domain classification
		                                                            return classificationServiceProvider.find(session, WebAddressDomain.name(), system, identityToken);
		                                                        });
		                                                })
		                                                .chain(webDomainClassification -> {
		                                                    // Create domain address
		                                                    Address domainDetails = new Address();
		                                                    domainDetails.setValue(domain);
		                                                    domainDetails.setClassificationID((Classification) webDomainClassification);
		                                                    domainDetails.setOriginalSourceSystemID(system.getId());
		                                                    domainDetails.setSystemID(system);
		                                                    domainDetails.setEnterpriseID(enterprise);

		                                                    // We need to get the active flag again for this entity
		                                                    return acService.getActiveFlag(session, enterprise)
		                                                        .chain(domainActiveFlag -> {
		                                                            domainDetails.setActiveFlagID(domainActiveFlag);
		                                                            return session.persist(domainDetails).replaceWith(Uni.createFrom().item(domainDetails));
		                                                        })
		                                                        .chain(domainPersisted -> {
		                                                            // Now get the protocol classification
		                                                            return classificationServiceProvider.find(session, WebAddressProtocol.name(), system, identityToken);
		                                                        });
		                                                })
		                                                .chain(webProtocolClassification -> {
		                                                    // Create protocol address
		                                                    Address protocolDetails = new Address();
		                                                    protocolDetails.setValue(protocol);
		                                                    protocolDetails.setClassificationID((Classification) webProtocolClassification);
		                                                    protocolDetails.setOriginalSourceSystemID(system.getId());
		                                                    protocolDetails.setSystemID(system);
		                                                    protocolDetails.setEnterpriseID(enterprise);

		                                                    // We need to get the active flag again for this entity
		                                                    return acService.getActiveFlag(session, enterprise)
		                                                        .chain(protocolActiveFlag -> {
		                                                            protocolDetails.setActiveFlagID(protocolActiveFlag);
		                                                            return session.persist(protocolDetails).replaceWith(Uni.createFrom().item(protocolDetails));
		                                                        })
		                                                        .chain(protocolPersisted -> {
		                                                            // Now get the site classification
		                                                            return classificationServiceProvider.find(session, WebAddressSite.name(), system, identityToken);
		                                                        });
		                                                })
		                                                .chain(webSiteClassification -> {
		                                                    // Create site address
		                                                    Address siteDetails = new Address();
		                                                    siteDetails.setValue(uri);
		                                                    siteDetails.setClassificationID((Classification) webSiteClassification);
		                                                    siteDetails.setOriginalSourceSystemID(system.getId());
		                                                    siteDetails.setSystemID(system);
		                                                    siteDetails.setEnterpriseID(enterprise);

		                                                    // We need to get the active flag again for this entity
		                                                    return acService.getActiveFlag(session, enterprise)
		                                                        .chain(siteActiveFlag -> {
		                                                            siteDetails.setActiveFlagID(siteActiveFlag);
		                                                            return session.persist(siteDetails).replaceWith(Uni.createFrom().item(siteDetails));
		                                                        });
		                                                });
		                                        } catch (MalformedURLException e) {
		                                            log.error("Malformed URL: {}", webAddress, e);
		                                            // Return the original address even if URL parsing fails
		                                            return Uni.createFrom().item((IAddress<?, ?>) address);
		                                        }
		                                    });
		                        }
		                        else
		                        {
		                            // Address exists, find and return it
		                            return address.builder(session)
		                                    .withClassification((Classification) webAddressClassification)
		                                    .withValue(webAddress)
		                                    .withEnterprise(enterprise)
		                                    .inDateRange()
		                                    .inActiveRange()
		                                    .get()
		                                    .onFailure().invoke(error -> log.error("Error finding existing web address: {}", error.getMessage(), error))
		                                    .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + webAddress))
		                                    .map(foundAddress -> (IAddress<?, ?>) foundAddress);
		                        }
		                    });
		        });
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindPhoneContact(Mutiny.Session session, String phoneNumber, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(phoneNumber);
		Address streetAddress = new Address();
		var enterprise = system.getEnterprise();
		// First get the main phone classification and check if phone exists
		return classificationServiceProvider.find(session, TelephoneNumber.name(), system, identityToken)
		    .chain(homePhoneNumber -> {
		        // Check if the phone number exists
		        return streetAddress.builder(session)
		            .withClassification(TelephoneNumber.name(), system)
		            .withValue(phoneNumber)
		            .withEnterprise(enterprise)
		            .inDateRange()
		            .inActiveRange()
		            .getCount()
		            .onFailure().invoke(error -> log.error("Error checking if phone number exists: {}", error.getMessage(), error))
		            .chain(count -> {
		                boolean found = count > 0;
		                if (!found) {
		                    // Phone number doesn't exist, create it
		                    streetAddress.setValue(phoneNumber);
		                    streetAddress.setClassificationID((Classification) homePhoneNumber);
		                    streetAddress.setEnterpriseID(enterprise);
		                    streetAddress.setSystemID(system);
		                    streetAddress.setOriginalSourceSystemID(system.getId());

		                    // Get active flag service
		                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		                    // Get active flag using reactive pattern
		                    return acService.getActiveFlag(session, enterprise)
		                        .chain(activeFlag -> {
		                            streetAddress.setActiveFlagID(activeFlag);
		                            return session.persist(streetAddress).replaceWith(Uni.createFrom().item(streetAddress));
		                        })
		                        .chain(persisted -> {
		                            // Start createDefaultSecurity in parallel without waiting for it
		                            streetAddress.createDefaultSecurity(session, system, identityToken)
		                                .subscribe().with(
		                                    result -> {
		                                        // Security setup completed successfully
		                                    },
		                                    error -> {
		                                        // Log error but don't fail the main operation
		                                        log.warn("Error in createDefaultSecurity for phone number", error);
		                                    }
		                                );

		                            // Create a Uni for each find-and-add pair
		                            // Each Uni first finds the classification and then adds it
		                            Uni<?> countryCodePairUni = classificationServiceProvider.find(session, TelephoneCountryCode.name(), system, identityToken)
		                                .chain(homePhoneNumberCountryCodeClassification -> {
		                                    return streetAddress.addClassification(
													session, ((Classification) homePhoneNumberCountryCodeClassification).getName(),
		                                        phoneNumberDTO.getCountryCode(), 
		                                        system, 
		                                        identityToken);
		                                });

		                            Uni<?> extensionPairUni = classificationServiceProvider.find(session, TelephoneExtensionNumber.name(), system, identityToken)
		                                .chain(homePhoneExtensionNumberClassification -> {
		                                    return streetAddress.addClassification(
													session, ((Classification) homePhoneExtensionNumberClassification).getName(),
		                                        Strings.nullToEmpty(phoneNumberDTO.getExtension()), 
		                                        system, 
		                                        identityToken);
		                                });

		                            Uni<?> areaCodePairUni = classificationServiceProvider.find(session, TelephoneAreaCode.name(), system, identityToken)
		                                .chain(homePhoneAreaCodeClassification -> {
		                                    return streetAddress.addClassification(
													session, ((Classification) homePhoneAreaCodeClassification).getName(),
		                                        phoneNumberDTO.getAreaCode(), 
		                                        system, 
		                                        identityToken);
		                                });

		                            // Combine all parallel operations and wait for all to complete
		                            return Uni.combine().all().unis(countryCodePairUni, extensionPairUni, areaCodePairUni)
		                                .discardItems()
		                                .chain(() -> {
		                                    // Return the address after all classifications are added
		                                    return Uni.createFrom().item((IAddress<?, ?>) streetAddress);
		                                });
		                        });
		                } else {
		                    // Phone number exists, find and return it
		                    return streetAddress.builder(session)
		                        .withClassification(TelephoneNumber.name(), system)
		                        .withValue(phoneNumber)
		                        .withEnterprise(enterprise)
		                        .inDateRange()
		                        .inActiveRange()
		                        .get()
		                        .onFailure().invoke(error -> log.error("Error finding existing phone number: {}", error.getMessage(), error))
		                        .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + phoneNumber))
		                        .map(foundAddress -> (IAddress<?, ?>) foundAddress);
		                }
		            });
		    });
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindEmailContact(Mutiny.Session session, String emailAddressString, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		Address emailAddy = new Address();
		String host, user, domain;

		try {
			host = emailAddressString.substring(emailAddressString.indexOf('@') + 1, emailAddressString.indexOf('.'));
			user = emailAddressString.substring(0, emailAddressString.indexOf('@'));
			domain = emailAddressString.substring(emailAddressString.indexOf('.') + 1);
		} catch (Throwable T) {
			return Uni.createFrom().failure(new AddressException("Unable to create email address - invalid value", T));
		}
		var enterprise = system.getEnterprise();

		// First get the main email classification and check if email exists
		return classificationServiceProvider.find(session, AddressEmailClassifications.EmailAddress.name(), system, identityToken)
		    .chain(emailAddress -> {
		        // Check if the email address exists
		        return emailAddy.builder(session)
		            .withClassification(AddressEmailClassifications.EmailAddress.name(), system)
		            .withValue(emailAddressString)
		            .withEnterprise(enterprise)
		            .inDateRange()
		            .inActiveRange()
		            .getCount()
		            .onFailure().invoke(error -> log.error("Error checking if email address exists: {}", error.getMessage(), error))
		            .chain(count -> {
		                boolean found = count > 0;
		                if (!found) {
		                    // Email address doesn't exist, create it
		                    emailAddy.setValue(emailAddressString);
		                    emailAddy.setClassificationID((Classification) emailAddress);
		                    emailAddy.setEnterpriseID(enterprise);
		                    emailAddy.setSystemID(system);
		                    emailAddy.setOriginalSourceSystemID(system.getId());

		                    // Get active flag service
		                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		                    // Get active flag using reactive pattern
		                    return acService.getActiveFlag(session, enterprise)
		                        .chain(activeFlag -> {
		                            emailAddy.setActiveFlagID(activeFlag);
		                            return session.persist(emailAddy).replaceWith(Uni.createFrom().item(emailAddy));
		                        })
		                        .chain(persisted -> {
		                            // Start createDefaultSecurity in parallel without waiting for it
		                            emailAddy.createDefaultSecurity(session, system, identityToken)
		                                .subscribe().with(
		                                    result -> {
		                                        // Security setup completed successfully
		                                    },
		                                    error -> {
		                                        // Log error but don't fail the main operation
		                                        log.warn("Error in createDefaultSecurity for email address", error);
		                                    }
		                                );

		                            // Create a Uni for each find-and-add pair
		                            // Each Uni first finds the classification and then adds it
		                            Uni<?> hostPairUni = classificationServiceProvider.find(session, AddressEmailClassifications.EmailAddressHost.name(), system, identityToken)
		                                .chain(emailAddressHost -> {
		                                    return emailAddy.addOrReuseClassification(
													session, ((Classification) emailAddressHost).toString(),
		                                        host, 
		                                        system, 
		                                        identityToken);
		                                });

		                            Uni<?> domainPairUni = classificationServiceProvider.find(session, AddressEmailClassifications.EmailAddressDomain.name(), system, identityToken)
		                                .chain(emailAddressDomain -> {
		                                    return emailAddy.addOrReuseClassification(
													session, ((Classification) emailAddressDomain).toString(),
		                                        domain, 
		                                        system, 
		                                        identityToken);
		                                });

		                            Uni<?> userPairUni = classificationServiceProvider.find(session, AddressEmailClassifications.EmailAddressUser.name(), system, identityToken)
		                                .chain(emailAddressUser -> {
		                                    return emailAddy.addOrReuseClassification(
													session, ((Classification) emailAddressUser).toString(),
		                                        user, 
		                                        system, 
		                                        identityToken);
		                                });

		                            // Combine all parallel operations and wait for all to complete
		                            return Uni.combine().all().unis(hostPairUni, domainPairUni, userPairUni)
		                                .discardItems()
		                                .chain(() -> {
		                                    // Return the address after all classifications are added
		                                    return Uni.createFrom().item((IAddress<?, ?>) emailAddy);
		                                });
		                        });
		                } else {
		                    // Email address exists, find and return it
		                    return emailAddy.builder(session)
		                        .withClassification(AddressEmailClassifications.EmailAddress.name(), system)
		                        .withValue(emailAddressString)
		                        .withEnterprise(enterprise)
		                        .inDateRange()
		                        .inActiveRange()
		                        .get()
		                        .onFailure().invoke(error -> log.error("Error finding existing email address: {}", error.getMessage(), error))
		                        .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + emailAddressString))
		                        .map(foundAddress -> (IAddress<?, ?>) foundAddress);
		                }
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
		Address streetAddress = new Address();
		var enterprise = system.getEnterprise();
		// First get the building address classification and check if address exists
		return classificationServiceProvider.find(session, AddressBuildingClassifications.BuildingAddress.name(), system, identityToken)
		    .chain(buildingAddressClassification -> {
		        // Check if the street address exists
		        return streetAddress.builder(session)
		            .withClassification(AddressBuildingClassifications.BuildingAddress.name(), system)
		            .withValue(number + " " + street + " " + streetType)
		            .withEnterprise(enterprise)
		            .inDateRange()
		            .inActiveRange()
		            .getCount()
		            .onFailure().invoke(error -> log.error("Error checking if street address exists: {}", error.getMessage(), error))
		            .chain(count -> {
		                boolean found = count > 0;
		                if (!found) {
		                    // Street address doesn't exist, create it
		                    Address address = new Address();
		                    address.setValue(number + " " + street + " " + streetType);
		                    address.setClassificationID((Classification) buildingAddressClassification);
		                    address.setEnterpriseID(enterprise);
		                    address.setSystemID(system);
		                    address.setOriginalSourceSystemID(system.getId());

		                    // Get active flag service
		                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		                    // Get active flag using reactive pattern
		                    return acService.getActiveFlag(session, enterprise)
		                        .chain(activeFlag -> {
		                            address.setActiveFlagID(activeFlag);
		                            return session.persist(address).replaceWith(Uni.createFrom().item(address));
		                        })
		                        .chain(persisted -> {
		                            // Start createDefaultSecurity in parallel without waiting for it
		                            address.createDefaultSecurity(session, system, identityToken)
		                                .subscribe().with(
		                                    result -> {
		                                        // Security setup completed successfully
		                                    },
		                                    error -> {
		                                        // Log error but don't fail the main operation
		                                        log.warn("Error in createDefaultSecurity for street address", error);
		                                    }
		                                );

		                            // Create a Uni for each find-and-add pair
		                            // Each Uni first finds the classification and then adds it
		                            Uni<?> numberPairUni = classificationServiceProvider.find(session, AddressBuildingClassifications.BuildingNumber.name(), system, identityToken)
		                                .chain(buildingNumberClassification -> {
		                                    return address.addClassification(
													session, ((Classification) buildingNumberClassification).getName(),
		                                        number, 
		                                        system, 
		                                        identityToken);
		                                });

		                            Uni<?> streetPairUni = classificationServiceProvider.find(session, AddressBuildingClassifications.BuildingStreet.name(), system, identityToken)
		                                .chain(buildingStreetClassification -> {
		                                    return address.addClassification(
													session, ((Classification) buildingStreetClassification).getName(),
		                                        street, 
		                                        system, 
		                                        identityToken);
		                                });

		                            Uni<?> typePairUni = classificationServiceProvider.find(session, AddressBuildingClassifications.BuildingStreetType.name(), system, identityToken)
		                                .chain(buildingStreetTypeClassification -> {
		                                    return address.addClassification(
													session, ((Classification) buildingStreetTypeClassification).getName(),
		                                        streetType, 
		                                        system, 
		                                        identityToken);
		                                });

		                            // Combine all parallel operations and wait for all to complete
		                            return Uni.combine().all().unis(numberPairUni, streetPairUni, typePairUni)
		                                .discardItems()
		                                .chain(() -> {
		                                    // Return the address after all classifications are added
		                                    return Uni.createFrom().item((IAddress<?, ?>) address);
		                                });
		                        });
		                } else {
		                    // Street address exists, find and return it
		                    return streetAddress.builder(session)
		                        .withClassification(AddressBuildingClassifications.BuildingAddress.name(), system)
		                        .withValue(number + " " + street + " " + streetType)
		                        .withEnterprise(enterprise)
		                        .inDateRange()
		                        .inActiveRange()
		                        .get()
		                        .onFailure().invoke(error -> log.error("Error finding existing street address: {}", error.getMessage(), error))
		                        .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + number + " " + street + " " + Strings.nullToEmpty(streetType)))
		                        .map(foundAddress -> (IAddress<?, ?>) foundAddress);
		                }
		            });
		    });
	}
	//@Transactional()
	@Override
	////@Transactional()
	public Uni<IAddress<?, ?>> addOrFindPostalAddress(Mutiny.Session session, String boxIdentifier, String boxNumber, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		Address address = new Address();
		var enterprise = system.getEnterprise();
		// First get the box address classification and check if address exists
		return classificationServiceProvider.find(session, BoxAddress.name(), system, identityToken)
		    .chain(boxAddressClassification -> {
		        // Check if the postal address exists
		        return address.builder(session)
		            .withClassification(BoxAddress.name(), system)
		            .withValue(boxIdentifier + " " + boxNumber)
		            .withEnterprise(enterprise)
		            .inDateRange()
		            .inActiveRange()
		            .getCount()
		            .onFailure().invoke(error -> log.error("Error checking if postal address exists: {}", error.getMessage(), error))
		            .chain(count -> {
		                boolean found = count > 0;
		                if (!found) {
		                    // Postal address doesn't exist, create it
		                    Address postalAddress = new Address();
		                    postalAddress.setValue(boxIdentifier + " " + boxNumber);
		                    postalAddress.setClassificationID((Classification) boxAddressClassification);
		                    postalAddress.setEnterpriseID(enterprise);
		                    postalAddress.setSystemID(system);
		                    postalAddress.setOriginalSourceSystemID(system.getId());

		                    // Get active flag service
		                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		                    // Get active flag using reactive pattern
		                    return acService.getActiveFlag(session, enterprise)
		                        .chain(activeFlag -> {
		                            postalAddress.setActiveFlagID(activeFlag);
		                            return session.persist(postalAddress).replaceWith(Uni.createFrom().item(postalAddress));
		                        })
		                        .chain(persisted -> {
		                            // Start createDefaultSecurity in parallel without waiting for it
		                            postalAddress.createDefaultSecurity(session, system, identityToken)
		                                .subscribe().with(
		                                    result -> {
		                                        // Security setup completed successfully
		                                    },
		                                    error -> {
		                                        // Log error but don't fail the main operation
		                                        log.warn("Error in createDefaultSecurity for postal address", error);
		                                    }
		                                );

		                            // Create a Uni for each find-and-add pair
		                            // Each Uni first finds the classification and then adds it
		                            Uni<?> numberPairUni = classificationServiceProvider.find(session, BoxNumber.name(), system, identityToken)
		                                .chain(boxNumberClassification -> {
		                                    return postalAddress.addClassification(
													session, ((Classification) boxNumberClassification).getName(),
		                                        boxNumber, 
		                                        system, 
		                                        identityToken);
		                                });

		                            Uni<?> identifierPairUni = classificationServiceProvider.find(session, BoxIdentifier.name(), system, identityToken)
		                                .chain(boxidentifierClassification -> {
		                                    return postalAddress.addClassification(
													session, ((Classification) boxidentifierClassification).getName(),
		                                        boxIdentifier, 
		                                        system, 
		                                        identityToken);
		                                });

		                            // Combine all parallel operations and wait for all to complete
		                            return Uni.combine().all().unis(numberPairUni, identifierPairUni)
		                                .discardItems()
		                                .chain(() -> {
		                                    // Return the address after all classifications are added
		                                    return Uni.createFrom().item((IAddress<?, ?>) postalAddress);
		                                });
		                        });
		                } else {
		                    // Postal address exists, find and return it
		                    return address.builder(session)
		                        .withClassification(BoxAddress.name(), system)
		                        .withValue(boxIdentifier + " " + boxNumber)
		                        .withEnterprise(enterprise)
		                        .inDateRange()
		                        .inActiveRange()
		                        .get()
		                        .onFailure().invoke(error -> log.error("Error finding existing postal address: {}", error.getMessage(), error))
		                        .onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + boxIdentifier + " " + boxNumber))
		                        .map(foundAddress -> (IAddress<?, ?>) foundAddress);
		                }
		            });
		    });
	}

}
