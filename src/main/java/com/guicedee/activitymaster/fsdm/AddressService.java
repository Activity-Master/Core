package com.guicedee.activitymaster.fsdm;

/**
 * Reactivity Migration Checklist:
 * 
 * [✓] One action per Mutiny.StatelessSession at a time
 *     - All operations on a session are sequential
 *     - No parallel operations on the same session
 * 
 * [!] Pass Mutiny.StatelessSession through the chain
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

	
	private Uni<IAddress<?, ?>> createWithSecurity(Mutiny.StatelessSession session, String addressClassification, UUID key, ISystems<?, ?> system, String value,
												   java.util.function.Function<Address, Uni<?>> securityFn, UUID... identifyingToken)
	{
		var enterprise = system.getEnterprise();
			Address addy = new Address();

			return classificationServiceProvider.find(session, addressClassification, system, identifyingToken)
			        .chain(classification -> {
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
			                            if(key != null)
			                                addy.setId(key);
			                            addy.setEnterpriseID(enterprise);
			                            addy.setClassificationID((Classification) classification);
			                            addy.setValue(value);
			                            addy.setSystemID(system);
			                            addy.setOriginalSourceSystemID(system.getId());

			                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                            return acService.getActiveFlag(session, enterprise, identifyingToken)
			                                    .chain(activeFlag -> {
			                                        addy.setActiveFlagID(activeFlag);
			                                        return session.insert(addy).replaceWith(Uni.createFrom().item(addy));
			                                    })
			                                    .chain(persisted -> {
			                                        return securityFn.apply(addy)
			                                            .onItem().invoke(() -> log.debug("Security setup completed successfully"))
			                                            .onFailure().invoke(error -> log.warn("Error in security setup", error))
			                                            .onFailure().recoverWithItem(() -> null)
			                                            .map(result -> (IAddress<?, ?>) addy);
			                                    });
			                        }
			                        else
			                        {
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

	// ----------------------------------------------------------------------------------------------------
	// Stateless (Mutiny.StatelessSession) twins. Address is NOT @Cacheable and all its @ManyToOne
	// associations are LAZY, so the existence getCount() and the existing-row .get() are stateless-safe;
	// inserts use session.insert + the stateless resolveDefaultGroupFolderTokens/createDefaultSecurity path.
	// ----------------------------------------------------------------------------------------------------

	@Override
	public Uni<IAddress<?, ?>> create(Mutiny.StatelessSession session, String addressClassification, ISystems<?, ?> system, String value, UUID... identifyingToken)
	{
		return create(session, addressClassification, null, system, value, identifyingToken);
	}

	@Override
	public Uni<IAddress<?, ?>> create(Mutiny.StatelessSession session, String addressClassification, UUID key, ISystems<?, ?> system, String value, UUID... identifyingToken)
	{
		return createWithSecurityStateless(session, addressClassification, key, system, value, false, null, null, identifyingToken);
	}

	@Override
	public Uni<IAddress<?, ?>> createScopeRestricted(Mutiny.StatelessSession session, String addressClassification, UUID key, ISystems<?, ?> system,
	                                                 String value,
	                                                 com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
	                                                 UUID... identifyingToken)
	{
		return createWithSecurityStateless(session, addressClassification, key, system, value, true, scopeToken, null, identifyingToken);
	}

	@Override
	public Uni<IAddress<?, ?>> addOrFindIPAddress(Mutiny.StatelessSession session, String ipAddress, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		if (!ipAddressPattern.matcher(ipAddress).matches())
		{
			return Uni.createFrom().failure(new AddressException("Invalid IP Address"));
		}
		return createWithSecurityStateless(session, RemoteAddressIPAddress.name(), null, system, ipAddress, false, null, null, identityToken);
	}

	@Override
	public Uni<IAddress<?, ?>> addOrFindHostName(Mutiny.StatelessSession session, String hostName, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		return createWithSecurityStateless(session, RemoteAddressHostName.name(), null, system, hostName, false, null, null, identityToken);
	}

	@Override
	public Uni<IAddress<?, ?>> addOrFindWebAddress(Mutiny.StatelessSession session, String webAddress, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		var enterprise = system.getEnterprise();
		return createWithSecurityStateless(session, WebAddress.name(), null, system, webAddress, false, null,
				addy -> {
					try
					{
						URL url = new URL(webAddress);
						Pattern pattern = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?(\\?.*)?");
						Matcher matcher = pattern.matcher(webAddress);
						matcher.find();
						String protocol = matcher.group(1);
						String domain = matcher.group(2);
						String uri = matcher.group(4);
						return insertSubAddressStateless(session, WebAddressPort.name(), url.getPort() + "", system, enterprise, identityToken)
								.chain(() -> insertSubAddressStateless(session, WebAddressDomain.name(), domain, system, enterprise, identityToken))
								.chain(() -> insertSubAddressStateless(session, WebAddressProtocol.name(), protocol, system, enterprise, identityToken))
								.chain(() -> insertSubAddressStateless(session, WebAddressSite.name(), uri, system, enterprise, identityToken));
					}
					catch (MalformedURLException e)
					{
						log.error("Malformed URL: {}", webAddress, e);
						return Uni.createFrom().voidItem();
					}
				}, identityToken);
	}

	@Override
	public Uni<IAddress<?, ?>> addOrFindPhoneContact(Mutiny.StatelessSession session, String phoneNumber, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		PhoneNumberDTO dto = new PhoneNumberDTO(phoneNumber);
		return createWithSecurityStateless(session, TelephoneNumber.name(), null, system, phoneNumber, false, null,
				addy -> addy.addClassification(session, TelephoneCountryCode.name(), dto.getCountryCode(), system, identityToken)
						.chain(() -> addy.addClassification(session, TelephoneExtensionNumber.name(), Strings.nullToEmpty(dto.getExtension()), system, identityToken))
						.chain(() -> addy.addClassification(session, TelephoneAreaCode.name(), dto.getAreaCode(), system, identityToken)),
				identityToken);
	}

	@Override
	public Uni<IAddress<?, ?>> addOrFindEmailContact(Mutiny.StatelessSession session, String emailAddressString, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		final String host, user, domain;
		try
		{
			host = emailAddressString.substring(emailAddressString.indexOf('@') + 1, emailAddressString.indexOf('.'));
			user = emailAddressString.substring(0, emailAddressString.indexOf('@'));
			domain = emailAddressString.substring(emailAddressString.indexOf('.') + 1);
		}
		catch (Throwable T)
		{
			return Uni.createFrom().failure(new AddressException("Unable to create email address - invalid value", T));
		}
		return createWithSecurityStateless(session, AddressEmailClassifications.EmailAddress.name(), null, system, emailAddressString, false, null,
				addy -> addy.addOrReuseClassification(session, AddressEmailClassifications.EmailAddressHost.name(), host, system, identityToken)
						.chain(() -> addy.addOrReuseClassification(session, AddressEmailClassifications.EmailAddressDomain.name(), domain, system, identityToken))
						.chain(() -> addy.addOrReuseClassification(session, AddressEmailClassifications.EmailAddressUser.name(), user, system, identityToken)),
				identityToken);
	}

	@Override
	public Uni<IAddress<?, ?>> addOrFindStreetAddress(Mutiny.StatelessSession session, String number, String street, String streetType, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		return createWithSecurityStateless(session, AddressBuildingClassifications.BuildingAddress.name(), null, system, number + " " + street + " " + streetType, false, null,
				addy -> addy.addClassification(session, AddressBuildingClassifications.BuildingNumber.name(), number, system, identityToken)
						.chain(() -> addy.addClassification(session, AddressBuildingClassifications.BuildingStreet.name(), street, system, identityToken))
						.chain(() -> addy.addClassification(session, AddressBuildingClassifications.BuildingStreetType.name(), streetType, system, identityToken)),
				identityToken);
	}

	@Override
	public Uni<IAddress<?, ?>> addOrFindPostalAddress(Mutiny.StatelessSession session, String boxIdentifier, String boxNumber, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		return createWithSecurityStateless(session, BoxAddress.name(), null, system, boxIdentifier + " " + boxNumber, false, null,
				addy -> addy.addClassification(session, BoxNumber.name(), boxNumber, system, identityToken)
						.chain(() -> addy.addClassification(session, BoxIdentifier.name(), boxIdentifier, system, identityToken)),
				identityToken);
	}

	@Override
	public Uni<IRelationshipValue<?, IAddress<?, ?>, ?>> findCellPhoneContact(Mutiny.StatelessSession session, IInvolvedParty<?, ?> involvedParty, ISystems<?, ?> system, UUID... identityToken) throws AddressException
	{
		if (involvedParty == null)
		{
			return Uni.createFrom().failure(new AddressException("Involved party cannot be null"));
		}
		return involvedParty.findAddress(session, HomeCellNumber.name(), null, system, true, true, identityToken)
				.map(result -> result);
	}

	/** Inserts a metadata sub-address (no default security, mirroring the managed web-detail persists). */
	private Uni<Void> insertSubAddressStateless(Mutiny.StatelessSession session, String classificationName, String value, ISystems<?, ?> system,
	                                            com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?> enterprise, UUID... identityToken)
	{
		Address sub = new Address();
		IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
		return classificationServiceProvider.find(session, classificationName, system, identityToken)
				.chain(cl -> acService.getActiveFlag(session, enterprise, identityToken)
						.chain(af -> {
							sub.setValue(value);
							sub.setClassificationID((Classification) cl);
							sub.setOriginalSourceSystemID(system.getId());
							sub.setSystemID(system);
							sub.setEnterpriseID(enterprise);
							sub.setActiveFlagID(af);
							return session.insert(sub).replaceWithVoid();
						}));
	}

	private Uni<IAddress<?, ?>> createWithSecurityStateless(Mutiny.StatelessSession session, String addressClassification, UUID key, ISystems<?, ?> system, String value,
	                                                        boolean scopeRestricted,
	                                                        com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
	                                                        java.util.function.Function<Address, Uni<?>> postCreate,
	                                                        UUID... identifyingToken)
	{
		var enterprise = system.getEnterprise();
		Address addy = new Address();
		ISecurityTokenService<?> sts = IGuiceContext.get(ISecurityTokenService.class);
		return classificationServiceProvider.find(session, addressClassification, system, identifyingToken)
				.chain(classification -> addy.builder(session)
						.withClassification(addressClassification, system)
						.withValue(value)
						.withEnterprise(enterprise)
						.inDateRange()
						.inActiveRange()
						.getCount()
						.chain(count -> {
							if (count == null || count == 0)
							{
								if (key != null)
									addy.setId(key);
								addy.setEnterpriseID(enterprise);
								addy.setClassificationID((Classification) classification);
								addy.setValue(value);
								addy.setSystemID(system);
								addy.setOriginalSourceSystemID(system.getId());
								IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
								return acService.getActiveFlag(session, enterprise, identifyingToken)
										.chain(activeFlag -> {
											addy.setActiveFlagID(activeFlag);
											com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable<?, ?, ?, ?> core =
													(com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable<?, ?, ?, ?>) addy;
											return session.insert(addy)
													.chain(() -> sts.resolveDefaultGroupFolderTokens(session, system, identifyingToken)
															.chain(tokens -> scopeRestricted
																	? core.createScopeRestrictedSecurity(session, system, enterprise, activeFlag, tokens, scopeToken, identifyingToken)
																	: core.createDefaultSecurity(session, system, enterprise, activeFlag, tokens, identifyingToken))
															.onFailure().recoverWithItem(0L))
													.chain(() -> postCreate == null ? Uni.createFrom().voidItem() : postCreate.apply(addy).replaceWithVoid())
													.map(r -> (IAddress<?, ?>) addy);
										});
							}
							return addy.builder(session)
									.withClassification(addressClassification, system)
									.withEnterprise(enterprise)
									.withValue(value)
									.inDateRange()
									.inActiveRange()
									.get()
									.onItem().ifNull().failWith(() -> new AddressException("Cannot find an address that was already confirmed to exist - " + value))
									.map(found -> (IAddress<?, ?>) found);
						}));
	}

	
	
	
	
	
	
	
}

