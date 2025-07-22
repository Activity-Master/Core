package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyIdentificationType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IPTypes;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.SecurityAccessException;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.systems.InvolvedPartySystem;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.NoSuchElementException;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.NameTypes.*;
import static com.guicedee.client.IGuiceContext.*;

@Log4j2
public class PasswordsService implements IPasswordsService<PasswordsService>
{
	@Inject
	private IEnterprise<?, ?> enterprise;

	@Inject
	private IInvolvedPartyService<?> involvedPartyService;

	private String encrypt(String toEncrypt, byte[] salt)
	{
		Passwords passwords = new Passwords();
		//byte[] salt = saltString.getBytes();
		//byte[] saltDecrypted = salt.getBytes();
		char[] pass = toEncrypt.toCharArray();
		byte[] passHashed = passwords.hash(pass, salt);
		//String saltEncrypted = passwords.integerEncrypt(salt);
		String passEncrypted = passwords.integerEncrypt(passHashed);
		//String passEncrypted = new String(passHashed);
		return passEncrypted;
	}

 @Override
	public Uni<IInvolvedParty<?, ?>> findByUsername(String username, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		log.debug("Finding involved party by username: {}", username);
		return ReactiveTransactionUtil.withTransaction(session -> {
			return new InvolvedParty().builder()
					.withEnterprise(enterprise)
					.findByIdentificationType(IdentificationTypeUserName, username, system, identityToken)
					.get()
					.onItem().ifNull().failWith(() -> new SecurityAccessException("Involved Party Does Not Exist"))
					.map(party -> (IInvolvedParty<?, ?>) party);
		});
	}

 @Override
	public Uni<IInvolvedParty<?, ?>> findByUsernameAndPassword(String username, String password, ISystems<?, ?> system, boolean throwForNoUser, java.util.UUID... identityToken)
	{
		log.debug("Finding involved party by username and password: {}", username);
		return ReactiveTransactionUtil.withTransaction(session -> {
			return doesUsernameExist(username, system, identityToken)
				.chain(exists -> {
					if (!exists) {
						if (throwForNoUser) {
							return Uni.createFrom().failure(new SecurityAccessException("Invalid Username"));
						} else {
							return Uni.createFrom().nullItem();
						}
					}

					// Get system token
					return Uni.createFrom().item(() -> get(InvolvedPartySystem.class).getSystemToken(system.getEnterpriseID()))
						.chain(systemToken -> {
							// Find involved party by username
							return new InvolvedParty().builder()
								.findByIdentificationType(IdentificationTypeUserName, username, system, systemToken)
								.get()
								.onItem().ifNull().failWith(() -> new SecurityAccessException("Unable to find any Involved Party with that username"))
								.chain(foundPart -> {
									// Get salt and password classifications
									Uni<IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?>> saltEntityUni = 
										foundPart.findClassification(SecurityPasswordSalt, system, systemToken)
											.onItem().ifNull().failWith(() -> new SecurityAccessException("Involved Party does not have salt credentials"));

									Uni<IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?>> passEntityUni = 
										foundPart.findClassification(SecurityPassword, system, systemToken)
											.onItem().ifNull().failWith(() -> new SecurityAccessException("Involved Party does not have password credentials"));

									// Combine salt and password entities
									return Uni.combine().all().unis(saltEntityUni, passEntityUni)
										.asTuple()
										.chain(tuple -> {
											IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?> saltEntity = tuple.getItem1();
											IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?> passEntity = tuple.getItem2();

											String saltString = saltEntity.getValue();
											byte[] salt = new Passwords().integerDecrypt(saltString);
											String passMatch = passEntity.getValue();
											String passEncrypted = encrypt(password, salt);

											if (!passEncrypted.equalsIgnoreCase(passMatch)) {
												return Uni.createFrom().failure(new SecurityAccessException("Password Incorrect"));
											}

											return Uni.createFrom().item((IInvolvedParty<?, ?>) foundPart);
										});
								});
						});
				});
		});
	}

 @SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Uni<List<IInvolvedParty<?, ?>>> getAllUsers(ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		log.debug("Getting all users for system: {}", system.getName());
		return ReactiveTransactionUtil.withTransaction(session -> {
			return new InvolvedParty().builder()
					.findByIdentificationType(IdentificationTypeUserName, null, system, identityToken)
					.getAll()
					.onFailure().invoke(error -> log.error("Error getting all users: {}", error.getMessage(), error))
					.map(list -> {
						List<IInvolvedParty<?, ?>> result = new ArrayList<>();
						for (Object item : list) {
							result.add((IInvolvedParty<?, ?>) item);
						}
						return result;
					});
		});
	}
 @SuppressWarnings("unchecked")
 @Override
	public Uni<IInvolvedParty<?, ?>> addUpdateUsernamePassword(String username, String password, IInvolvedParty<?, ?> involvedParty, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		log.debug("Adding/updating username and password for involved party: {}", involvedParty.getId());
		return (Uni) ReactiveTransactionUtil.withTransaction(session -> {
			// Generate salt and encrypt password
			byte[] salt = System.getProperty("systemSalt") != null ? 
				System.getProperty("systemSalt").getBytes() : 
				new Passwords().getNextSalt();

			String passEncrypted = encrypt(password, salt);
			String saltEncrypted = new Passwords().integerEncrypt(salt);

			// First add password classification
			return involvedParty.addOrUpdateClassification(SecurityPassword, null, passEncrypted, system, identityToken)
				.chain(() -> {
					// Then add salt classification
					return involvedParty.addOrUpdateClassification(SecurityPasswordSalt, null, saltEncrypted, system, identityToken);
				})
				.chain(() -> {
					// Get identification type
					return involvedPartyService.findInvolvedPartyIdentificationType(
						IdentificationTypeUserName.toString(), system, identityToken);
				})
				.chain(identificationType -> {
					// Add identification type
					return involvedParty.addOrUpdateInvolvedPartyIdentificationType(
						NoClassification.toString(), identificationType,
						null, username, system, identityToken);
				})
				.map(result -> involvedParty)
				.onFailure().invoke(error -> log.error("Error adding/updating username and password: {}", error.getMessage(), error));
		});
	}
 @Override
	public Uni<Boolean> doesUsernameExist(String username, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		log.debug("Checking if username exists: {}", username);
		return ReactiveTransactionUtil.withTransaction(session -> {
			return new InvolvedParty().builder()
					.withEnterprise(enterprise)
					.inActiveRange()
					.inDateRange()
					.findByIdentificationType(IdentificationTypeUserName, username, system, identityToken)
					.getCount()
					.onFailure().invoke(error -> log.error("Error checking if username exists: {}", error.getMessage(), error))
					.map(count -> count > 0);
		});
	}

 @SuppressWarnings("unchecked")
 @Override
	public Uni<IInvolvedParty<?, ?>> createAdminAndCreatorUserForEnterprise(ISystems<?, ?> system, String adminUserName,
	                                                                   @NotNull String adminPassword, UUID existingLocalKey)
	{
		log.debug("Creating admin and creator user for enterprise: {}", system.getEnterpriseID());
		logProgress("Checking base administrator user", "The default user is being checked for compliance", 1);

		return (Uni) ReactiveTransactionUtil.withTransaction(session -> {
			// Get security identity token
			ISystemsService<?> systemsService =  get(ISystemsService.class);
			return systemsService.getSecurityIdentityToken(system)
				.chain(identityToken -> {
					// Get administrators folder
					return Uni.createFrom().item(() -> get(SecurityTokenService.class).getAdministratorsFolder(system))
						.chain(administratorsGroup -> {
							// Check if user already exists
							Pair<String, String> pair = new Pair<>(
								IdentificationTypes.IdentificationTypeEnterpriseCreatorRole.toString(), adminUserName);

							return new InvolvedParty().builder()
								.findByIdentificationType(
									IdentificationTypes.IdentificationTypeEnterpriseCreatorRole,
									adminUserName, system)
								.get()
								.onItem().ifNotNull().transform(existingUser -> (IInvolvedParty<?, ?>) existingUser)
								.onFailure().recoverWithItem(() -> {
									// Create new user
									IInvolvedPartyService<?> service = get(IInvolvedPartyService.class);
									return service.create(system, pair, true)
										.chain(adminUser -> {
											// Add identification types
											adminUser.addOrReuseInvolvedPartyIdentificationType(
												NoClassification.toString(), 
												IdentificationTypes.IdentificationTypeUserName.toString(),
												adminUserName,system, identityToken)
																.chain(() -> {
													// Add party type
													return adminUser.addOrReuseInvolvedPartyType(
														NoClassification.toString(), 
														IPTypes.TypeIndividual.toString(), 
														"Creator Individual", system, identityToken);
												})
												.chain(() -> {
													// Add name types in sequence
													return adminUser.addOrReuseInvolvedPartyNameType(
														NoClassification.toString(), 
														PreferredNameType.toString(), 
														"Enterprise Creator", system, identityToken);
												})
												.chain(() -> {
													return adminUser.addOrReuseInvolvedPartyNameType(
														NoClassification.toString(), 
														CommonNameType.toString(), 
														"Enterprise Creator", system, identityToken);
												})
												.chain(() -> {
													return adminUser.addOrReuseInvolvedPartyNameType(
														NoClassification.toString(), 
														FullNameType.toString(), 
														"Enterprise Creator", system, identityToken);
												})
												.chain(() -> {
													return adminUser.addOrReuseInvolvedPartyNameType(
														NoClassification.toString(), 
														FirstNameType.toString(), 
														"Administrator", system, identityToken);
												})
												.chain(() -> {
													// Create security token
													SecurityTokenService tokenService = get(SecurityTokenService.class);
													return tokenService.create(
														SecurityTokenClassifications.Identity.toString(),
														adminUserName,
														"The creator of the enterprise", 
														system, 
														(SecurityToken)administratorsGroup, 
														identityToken);
												})
												.chain(() -> {
													// Add enterprise creator role
													return adminUser.addOrReuseInvolvedPartyIdentificationType(
														NoClassification.toString(), 
														IdentificationTypes.IdentificationTypeEnterpriseCreatorRole.toString(),
														adminUserName, system, identityToken);
												})
												.chain(() -> {
													// Add username and password
													return addUpdateUsernamePassword(
														adminUserName, adminPassword, adminUser, system, identityToken);
												})
												.chain(() -> {
													// Create default security
													return ((InvolvedParty)adminUser).createDefaultSecurity(system, identityToken);
												});
													return Uni.createFrom().item((IInvolvedParty<?,?>)adminUser);
										})
												   .map(result->(IInvolvedParty)result)
												   .await()
												   .atMost(Duration.of(50L, ChronoUnit.SECONDS));
								});
						});
				})
				.onFailure().invoke(error -> log.error("Error creating admin user: {}",error));
		});
	}
}
