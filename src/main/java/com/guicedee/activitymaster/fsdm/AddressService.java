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
		// Public create — world-readable (public/default security matrix).
		return createWithSecurity(session, addressClassification, key, system, value,
				addy -> addy.createDefaultSecurity(session, system, identifyingToken), identifyingToken);
	}

	/**
	 * Opt-in <strong>scope-restricted</strong> address create. Identical to
	 * {@link #create(Mutiny.Session, String, UUID, ISystems, String, UUID...)} except the address is secured
	 * with the restricted matrix: only Administrators / Systems / Applications / Plugins retain access, plus a
	 * <em>read</em> grant for {@code scopeToken}. Only identity tokens at that scope node or below it may read
	 * the address.
	 */
	@Override
	public Uni<IAddress<?, ?>> createScopeRestricted(Mutiny.Session session, String addressClassification, UUID key, ISystems<?, ?> system,
													 String value,
													 com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
													 UUID... identifyingToken)
	{
		return createWithSecurity(session, addressClassification, key, system, value,
				addy -> addy.createScopeRestrictedSecurity(session, system, scopeToken, identifyingToken), identifyingToken);
	}

	private Uni<IAddress<?, ?>> createWithSecurity(Mutiny.Session session, String addressClassification, UUID key, ISystems<?, ?> system, String value,
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
			                                        return session.persist(addy).replaceWith(Uni.createFrom().item(addy));
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
	@SuppressWarnings({"unchecked", "rawtypes"})
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

	@SuppressWarnings({"unchecked", "rawtypes"})
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

		var enterprise = system.getEnterprise();
			Address address = new Address();
			return classificationServiceProvider.find(session, RemoteAddressIPAddress.name(), system, identityToken)
			        .chain(ipAddressClassification -> {
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
			                            address.setValue(ipAddress);
			                            address.setClassificationID((Classification) ipAddressClassification);
			                            address.setEnterpriseID(enterprise);
			                            address.setSystemID(system);
			                            address.setOriginalSourceSystemID(system.getId());

			                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                            return acService.getActiveFlag(session, enterprise, identityToken)
			                                    .chain(activeFlag -> {
			                                        address.setActiveFlagID(activeFlag);
			                                        return session.persist(address).replaceWith(Uni.createFrom().item(address));
			                                    })
			                                    .chain(persisted -> {
			                                        return address.createDefaultSecurity(session, system, identityToken)
			                                            .onItem().invoke(() -> log.debug("Security setup completed successfully for IP address"))
			                                            .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for IP address", error))
			                                            .onFailure().recoverWithItem(() -> null)
			                                            .map(result -> (IAddress<?, ?>) address);
			                                    });
			                        }
			                        else
			                        {
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
		var enterprise = system.getEnterprise();
			Address address = new Address();
			return classificationServiceProvider.find(session, RemoteAddressHostName.name(), system, identityToken)
			        .chain(hostNameClassification -> {
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
			                            address.setValue(hostName);
			                            address.setClassificationID((Classification) hostNameClassification);
			                            address.setEnterpriseID(enterprise);
			                            address.setSystemID(system);
			                            address.setOriginalSourceSystemID(system.getId());

			                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                            return acService.getActiveFlag(session, enterprise, identityToken)
			                                    .chain(activeFlag -> {
			                                        address.setActiveFlagID(activeFlag);
			                                        return session.persist(address).replaceWith(Uni.createFrom().item(address));
			                                    })
			                                    .chain(persisted -> {
			                                        return address.createDefaultSecurity(session, system, identityToken)
			                                            .onItem().invoke(() -> log.debug("Security setup completed successfully for hostname"))
			                                            .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for hostname", error))
			                                            .onFailure().recoverWithItem(() -> null)
			                                            .map(result -> (IAddress<?, ?>) address);
			                                    });
			                        }
			                        else
			                        {
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
		var enterprise = system.getEnterprise();
			Address address = new Address();
			return classificationServiceProvider.find(session, WebAddress.name(), system, identityToken)
			        .chain(webAddressClassification -> {
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
			                            address.setValue(webAddress);
			                            address.setClassificationID((Classification) webAddressClassification);
			                            address.setEnterpriseID(enterprise);
			                            address.setSystemID(system);
			                            address.setOriginalSourceSystemID(system.getId());

			                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                            return acService.getActiveFlag(session, enterprise, identityToken)
			                                    .chain(activeFlag -> {
			                                        address.setActiveFlagID(activeFlag);
			                                        return session.persist(address).replaceWith(Uni.createFrom().item(address));
			                                    })
			                                    .chain(persisted -> {
			                                        return address.createDefaultSecurity(session, system, identityToken)
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

			                                                    return classificationServiceProvider.find(session, WebAddressPort.name(), system, identityToken)
			                                                        .chain(webPortClassification -> {
			                                                            Address webDetails = new Address();
			                                                            webDetails.setValue(url.getPort() + "");
			                                                            webDetails.setClassificationID((Classification) webPortClassification);
			                                                            webDetails.setOriginalSourceSystemID(system.getId());
			                                                            webDetails.setSystemID(system);
			                                                            webDetails.setEnterpriseID(enterprise);

			                                                            return acService.getActiveFlag(session, enterprise, identityToken)
			                                                                .chain(portActiveFlag -> {
			                                                                    webDetails.setActiveFlagID(portActiveFlag);
			                                                                    return session.persist(webDetails).replaceWith(Uni.createFrom().item(webDetails));
			                                                                })
			                                                                .chain(portPersisted -> {
			                                                                    return classificationServiceProvider.find(session, WebAddressDomain.name(), system, identityToken);
			                                                                });
			                                                        })
			                                                        .chain(webDomainClassification -> {
			                                                            Address domainDetails = new Address();
			                                                            domainDetails.setValue(domain);
			                                                            domainDetails.setClassificationID((Classification) webDomainClassification);
			                                                            domainDetails.setOriginalSourceSystemID(system.getId());
			                                                            domainDetails.setSystemID(system);
			                                                            domainDetails.setEnterpriseID(enterprise);

			                                                            return acService.getActiveFlag(session, enterprise, identityToken)
			                                                                .chain(domainActiveFlag -> {
			                                                                    domainDetails.setActiveFlagID(domainActiveFlag);
			                                                                    return session.persist(domainDetails).replaceWith(Uni.createFrom().item(domainDetails));
			                                                                })
			                                                                .chain(domainPersisted -> {
			                                                                    return classificationServiceProvider.find(session, WebAddressProtocol.name(), system, identityToken);
			                                                                });
			                                                        })
			                                                        .chain(webProtocolClassification -> {
			                                                            Address protocolDetails = new Address();
			                                                            protocolDetails.setValue(protocol);
			                                                            protocolDetails.setClassificationID((Classification) webProtocolClassification);
			                                                            protocolDetails.setOriginalSourceSystemID(system.getId());
			                                                            protocolDetails.setSystemID(system);
			                                                            protocolDetails.setEnterpriseID(enterprise);

			                                                            return acService.getActiveFlag(session, enterprise, identityToken)
			                                                                .chain(protocolActiveFlag -> {
			                                                                    protocolDetails.setActiveFlagID(protocolActiveFlag);
			                                                                    return session.persist(protocolDetails).replaceWith(Uni.createFrom().item(protocolDetails));
			                                                                })
			                                                                .chain(protocolPersisted -> {
			                                                                    return classificationServiceProvider.find(session, WebAddressSite.name(), system, identityToken);
			                                                                });
			                                                        })
			                                                        .chain(webSiteClassification -> {
			                                                            Address siteDetails = new Address();
			                                                            siteDetails.setValue(uri);
			                                                            siteDetails.setClassificationID((Classification) webSiteClassification);
			                                                            siteDetails.setOriginalSourceSystemID(system.getId());
			                                                            siteDetails.setSystemID(system);
			                                                            siteDetails.setEnterpriseID(enterprise);

			                                                            return acService.getActiveFlag(session, enterprise, identityToken)
			                                                                .chain(siteActiveFlag -> {
			                                                                    siteDetails.setActiveFlagID(siteActiveFlag);
			                                                                    return session.persist(siteDetails).replaceWith(Uni.createFrom().item(siteDetails));
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

		var enterprise = system.getEnterprise();
			Address streetAddress = new Address();
			return classificationServiceProvider.find(session, TelephoneNumber.name(), system, identityToken)
			    .chain(homePhoneNumber -> {
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
			                    streetAddress.setValue(phoneNumber);
			                    streetAddress.setClassificationID((Classification) homePhoneNumber);
			                    streetAddress.setEnterpriseID(enterprise);
			                    streetAddress.setSystemID(system);
			                    streetAddress.setOriginalSourceSystemID(system.getId());

			                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                    return acService.getActiveFlag(session, enterprise, identityToken)
			                        .chain(activeFlag -> {
			                            streetAddress.setActiveFlagID(activeFlag);
			                            return session.persist(streetAddress).replaceWith(Uni.createFrom().item(streetAddress));
			                        })
			                        .chain(persisted -> {
			                            return streetAddress.createDefaultSecurity(session, system, identityToken)
			                                .onItem().invoke(() -> log.debug("Security setup completed successfully for phone number"))
			                                .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for phone number", error))
			                                .onFailure().recoverWithItem(() -> null)
			                                .chain(securityResult -> {
			                                    return classificationServiceProvider.find(session, TelephoneCountryCode.name(), system, identityToken)
			                                        .chain(homePhoneNumberCountryCodeClassification -> {
			                                            return streetAddress.addClassification(
			                                                    session,
			                                                    ((Classification) homePhoneNumberCountryCodeClassification).getName(),
			                                                    phoneNumberDTO.getCountryCode(),
			                                                    system,
			                                                    identityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(session, TelephoneExtensionNumber.name(), system, identityToken);
			                                        })
			                                        .chain(homePhoneExtensionNumberClassification -> {
			                                            return streetAddress.addClassification(
			                                                    session,
			                                                    ((Classification) homePhoneExtensionNumberClassification).getName(),
			                                                    Strings.nullToEmpty(phoneNumberDTO.getExtension()),
			                                                    system,
			                                                    identityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(session, TelephoneAreaCode.name(), system, identityToken);
			                                        })
			                                        .chain(homePhoneAreaCodeClassification -> {
			                                            return streetAddress.addClassification(
			                                                    session,
			                                                    ((Classification) homePhoneAreaCodeClassification).getName(),
			                                                    phoneNumberDTO.getAreaCode(),
			                                                    system,
			                                                    identityToken);
			                                        })
			                                        .map(result -> (IAddress<?, ?>) streetAddress);
			                                });
			                        });
			                } else {
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
		String host, user, domain;

		try {
			host = emailAddressString.substring(emailAddressString.indexOf('@') + 1, emailAddressString.indexOf('.'));
			user = emailAddressString.substring(0, emailAddressString.indexOf('@'));
			domain = emailAddressString.substring(emailAddressString.indexOf('.') + 1);
		} catch (Throwable T) {
			return Uni.createFrom().failure(new AddressException("Unable to create email address - invalid value", T));
		}

		var enterprise = system.getEnterprise();
			Address emailAddy = new Address();
			return classificationServiceProvider.find(session, AddressEmailClassifications.EmailAddress.name(), system, identityToken)
			    .chain(emailAddress -> {
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
			                    emailAddy.setValue(emailAddressString);
			                    emailAddy.setClassificationID((Classification) emailAddress);
			                    emailAddy.setEnterpriseID(enterprise);
			                    emailAddy.setSystemID(system);
			                    emailAddy.setOriginalSourceSystemID(system.getId());

			                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                    return acService.getActiveFlag(session, enterprise, identityToken)
			                        .chain(activeFlag -> {
			                            emailAddy.setActiveFlagID(activeFlag);
			                            return session.persist(emailAddy).replaceWith(Uni.createFrom().item(emailAddy));
			                        })
			                        .chain(persisted -> {
			                            return emailAddy.createDefaultSecurity(session, system, identityToken)
			                                .onItem().invoke(() -> log.debug("Security setup completed successfully for email address"))
			                                .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for email address", error))
			                                .onFailure().recoverWithItem(() -> null)
			                                .chain(securityResult -> {
			                                    return classificationServiceProvider.find(session, AddressEmailClassifications.EmailAddressHost.name(), system, identityToken)
			                                        .chain(emailAddressHost -> {
			                                            return emailAddy.addOrReuseClassification(
			                                                    session,
			                                                    ((Classification) emailAddressHost).toString(),
			                                                    host,
			                                                    system,
			                                                    identityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(session, AddressEmailClassifications.EmailAddressDomain.name(), system, identityToken);
			                                        })
			                                        .chain(emailAddressDomain -> {
			                                            return emailAddy.addOrReuseClassification(
			                                                    session,
			                                                    ((Classification) emailAddressDomain).toString(),
			                                                    domain,
			                                                    system,
			                                                    identityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(session, AddressEmailClassifications.EmailAddressUser.name(), system, identityToken);
			                                        })
			                                        .chain(emailAddressUser -> {
			                                            return emailAddy.addOrReuseClassification(
			                                                    session,
			                                                    ((Classification) emailAddressUser).toString(),
			                                                    user,
			                                                    system,
			                                                    identityToken);
			                                        })
			                                        .map(result -> (IAddress<?, ?>) emailAddy);
			                                });
			                        });
			                } else {
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
		var enterprise = system.getEnterprise();
			Address streetAddress = new Address();
			return classificationServiceProvider.find(session, AddressBuildingClassifications.BuildingAddress.name(), system, identityToken)
			    .chain(buildingAddressClassification -> {
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
			                    Address address = new Address();
			                    address.setValue(number + " " + street + " " + streetType);
			                    address.setClassificationID((Classification) buildingAddressClassification);
			                    address.setEnterpriseID(enterprise);
			                    address.setSystemID(system);
			                    address.setOriginalSourceSystemID(system.getId());

			                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                    return acService.getActiveFlag(session, enterprise, identityToken)
			                        .chain(activeFlag -> {
			                            address.setActiveFlagID(activeFlag);
			                            return session.persist(address).replaceWith(Uni.createFrom().item(address));
			                        })
			                        .chain(persisted -> {
			                            return address.createDefaultSecurity(session, system, identityToken)
			                                .onItem().invoke(() -> log.debug("Security setup completed successfully for street address"))
			                                .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for street address", error))
			                                .onFailure().recoverWithItem(() -> null)
			                                .chain(securityResult -> {
			                                    return classificationServiceProvider.find(session, AddressBuildingClassifications.BuildingNumber.name(), system, identityToken)
			                                        .chain(buildingNumberClassification -> {
			                                            return address.addClassification(
			                                                    session,
			                                                    ((Classification) buildingNumberClassification).getName(),
			                                                    number,
			                                                    system,
			                                                    identityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(session, AddressBuildingClassifications.BuildingStreet.name(), system, identityToken);
			                                        })
			                                        .chain(buildingStreetClassification -> {
			                                            return address.addClassification(
			                                                    session,
			                                                    ((Classification) buildingStreetClassification).getName(),
			                                                    street,
			                                                    system,
			                                                    identityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(session, AddressBuildingClassifications.BuildingStreetType.name(), system, identityToken);
			                                        })
			                                        .chain(buildingStreetTypeClassification -> {
			                                            return address.addClassification(
			                                                    session,
			                                                    ((Classification) buildingStreetTypeClassification).getName(),
			                                                    streetType,
			                                                    system,
			                                                    identityToken);
			                                        })
			                                        .map(result -> (IAddress<?, ?>) address);
			                                });
			                        });
			                } else {
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
		var enterprise = system.getEnterprise();
			Address address = new Address();
			return classificationServiceProvider.find(session, BoxAddress.name(), system, identityToken)
			    .chain(boxAddressClassification -> {
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
			                    Address postalAddress = new Address();
			                    postalAddress.setValue(boxIdentifier + " " + boxNumber);
			                    postalAddress.setClassificationID((Classification) boxAddressClassification);
			                    postalAddress.setEnterpriseID(enterprise);
			                    postalAddress.setSystemID(system);
			                    postalAddress.setOriginalSourceSystemID(system.getId());

			                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

			                    return acService.getActiveFlag(session, enterprise, identityToken)
			                        .chain(activeFlag -> {
			                            postalAddress.setActiveFlagID(activeFlag);
			                            return session.persist(postalAddress).replaceWith(Uni.createFrom().item(postalAddress));
			                        })
			                        .chain(persisted -> {
			                            return postalAddress.createDefaultSecurity(session, system, identityToken)
			                                .onItem().invoke(() -> log.debug("Security setup completed successfully for postal address"))
			                                .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for postal address", error))
			                                .onFailure().recoverWithItem(() -> null)
			                                .chain(securityResult -> {
			                                    return classificationServiceProvider.find(session, BoxNumber.name(), system, identityToken)
			                                        .chain(boxNumberClassification -> {
			                                            return postalAddress.addClassification(
			                                                    session,
			                                                    ((Classification) boxNumberClassification).getName(),
			                                                    boxNumber,
			                                                    system,
			                                                    identityToken);
			                                        })
			                                        .chain(() -> {
			                                            return classificationServiceProvider.find(session, BoxIdentifier.name(), system, identityToken);
			                                        })
			                                        .chain(boxidentifierClassification -> {
			                                            return postalAddress.addClassification(
			                                                    session,
			                                                    ((Classification) boxidentifierClassification).getName(),
			                                                    boxIdentifier,
			                                                    system,
			                                                    identityToken);
			                                        })
			                                        .map(result -> (IAddress<?, ?>) postalAddress);
			                                });
			                        });
			                } else {
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

