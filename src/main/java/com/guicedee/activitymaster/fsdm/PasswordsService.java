package com.guicedee.activitymaster.fsdm;

/**
 * Reactivity Migration Checklist:
 * <p>
 * [✓] One action per Mutiny.Session at a time
 * - All operations on a session are sequential
 * - No parallel operations on the same session
 * <p>
 * [✓] Pass Mutiny.Session through the chain
 * - All methods accept session as parameter
 * - Session is passed to all dependent operations
 * <p>
 * [✓] No await() usage
 * - Using reactive chains instead of blocking operations
 * <p>
 * [✓] Synchronous execution of reactive chains
 * - All reactive chains execute synchronously
 * <p>
 * [✓] No parallel operations on a session
 * - All operations on a session are sequential
 * - Parallel operations have been replaced with sequential chains
 * <p>
 * [✓] No session/transaction creation in libraries
 * - Sessions are passed in from the caller
 * - No sessionFactory.withTransaction() in methods
 * <p>
 * See ReactivityMigrationGuide.md for more details on these rules.
 */

import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.api.PasswordEncoder;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IPTypes;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.SecurityAccessException;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.NameTypes.*;
import static com.guicedee.client.IGuiceContext.*;

@Log4j2
@Singleton
public class PasswordsService implements IPasswordsService<PasswordsService> {

    @Inject
    private IInvolvedPartyService<?> involvedPartyService;

    /**
     * Modern, self-describing password hasher (PBKDF2-HMAC-SHA256, OWASP work factor). New and
     * upgraded credentials are stored using this encoder; legacy credentials are verified with
     * {@link #encrypt(String, byte[])} and transparently migrated on first successful login.
     */
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();

    /**
     * Legacy hash routine (PBKDF2-HMAC-SHA1 + integer encoding) retained only to verify and migrate
     * pre-existing credentials created before {@link PasswordEncoder} was introduced.
     */
    private String encrypt(String toEncrypt, byte[] salt) {
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

    /**
     * Constant-time comparison of two stored credential strings, guarding the legacy verification
     * path against timing attacks.
     */
    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return MessageDigest.isEqual(a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByUsername(Mutiny.Session session, String username, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Finding involved party by username: {}", username);
        var enterprise = system.getEnterprise();
        return new InvolvedParty().builder(session)
                .withEnterprise(enterprise)
                .findByIdentificationType(IdentificationTypeUserName, username, system, identityToken)
                .get()
                .map(party -> (IInvolvedParty<?, ?>) party);
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByUsernameAndPassword(Mutiny.Session session, String username, String password, ISystems<?, ?> system, boolean throwForNoUser, UUID... identityToken) {
        log.debug("Finding involved party by username and password: {}", username);
        return doesUsernameExist(session, username, system, identityToken)
                .chain(exists -> {
                    if (!exists) {
                        if (throwForNoUser) {
                            return Uni.createFrom()
                                    .failure(new SecurityAccessException("Invalid Username"));
                        } else {
                            return Uni.createFrom()
                                    .nullItem();
                        }
                    }

                    // Find involved party by username - identityToken already resolved by withActivityMaster
                    return new InvolvedParty().builder(session)
                            .findByIdentificationType(IdentificationTypeUserName, username, system, identityToken)
                            .get()
                            .onItem()
                            .ifNull()
                            .failWith(() -> new SecurityAccessException("Unable to find any Involved Party with that username"))
                            .chain(foundPart -> {
                                // Resolve the stored password credential (required for either format)
                                return foundPart.findClassification(session, SecurityPassword, system, identityToken)
                                        .onItem()
                                        .ifNull()
                                        .failWith(() -> new SecurityAccessException("Involved Party does not have password credentials"))
                                        .chain(passEntity -> {
                                            String stored = passEntity.getValue();

                                            // Modern, self-describing PBKDF2-HMAC-SHA256 credential
                                            if (passwordEncoder.isEncoded(stored)) {
                                                if (!passwordEncoder.matches(password, stored)) {
                                                    return Uni.createFrom()
                                                            .failure(new SecurityAccessException("Password Incorrect"));
                                                }
                                                // Upgrade the work factor transparently if the stored hash is weaker than current policy
                                                if (passwordEncoder.needsRehash(stored)) {
                                                    log.info("Upgrading password work factor for involved party: {}", foundPart.getId());
                                                    return addUpdateUsernamePassword(session, username, password,
                                                            (IInvolvedParty<?, ?>) foundPart, system, identityToken)
                                                            .replaceWith((IInvolvedParty<?, ?>) foundPart);
                                                }
                                                return Uni.createFrom()
                                                        .item((IInvolvedParty<?, ?>) foundPart);
                                            }

                                            // Legacy credential (PBKDF2-HMAC-SHA1 with separately stored salt) — verify then migrate
                                            return foundPart.findClassification(session, SecurityPasswordSalt, system, identityToken)
                                                    .onItem()
                                                    .ifNull()
                                                    .failWith(() -> new SecurityAccessException("Involved Party does not have salt credentials"))
                                                    .chain(saltEntity -> {
                                                        byte[] salt = new Passwords().integerDecrypt(saltEntity.getValue());
                                                        String legacyEncrypted = encrypt(password, salt);

                                                        if (!constantTimeEquals(legacyEncrypted, stored)) {
                                                            return Uni.createFrom()
                                                                    .failure(new SecurityAccessException("Password Incorrect"));
                                                        }

                                                        // Migration path: re-hash with the modern encoder on successful legacy login
                                                        log.info("Migrating legacy password hash to modern format for involved party: {}", foundPart.getId());
                                                        return addUpdateUsernamePassword(session, username, password,
                                                                (IInvolvedParty<?, ?>) foundPart, system, identityToken)
                                                                .replaceWith((IInvolvedParty<?, ?>) foundPart);
                                                    });
                                        });
                            });
                });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Uni<List<IInvolvedParty<?, ?>>> getAllUsers(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Getting all users for system: {}", system.getName());
        return new InvolvedParty().builder(session)
                .findByIdentificationType(IdentificationTypeUserName, null, system, identityToken)
                .getAll()
                .onFailure()
                .invoke(error -> log.error("Error getting all users: {}", error.getMessage(), error))
                .map(list -> {
                    List<IInvolvedParty<?, ?>> result = new ArrayList<>();
                    for (Object item : list) {
                        result.add((IInvolvedParty<?, ?>) item);
                    }
                    return result;
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Uni<IInvolvedParty<?, ?>> addUpdateUsernamePassword(Mutiny.Session session, String username, String password, IInvolvedParty<?, ?> involvedParty, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Adding/updating username and password for involved party: {}", involvedParty.getId());

        // Hash with the modern, self-describing encoder. The salt and work factor are embedded in
        // the stored value, so a separate SecurityPasswordSalt classification is no longer required.
        String encoded = passwordEncoder.encode(password);

        // Store the modern password credential
        return (Uni) involvedParty.addOrUpdateClassification(session, SecurityPassword, null, encoded, system, identityToken)
                .chain(() -> {
                    // Get identification type
                    return involvedPartyService.findInvolvedPartyIdentificationType(
                            session, IdentificationTypeUserName.toString(), system, identityToken);
                })
                .chain(identificationType -> {
                    // Add identification type
                    return involvedParty.addOrUpdateInvolvedPartyIdentificationType(
                            session, NoClassification.toString(), identificationType,
                            null, username, system, identityToken);
                })
                .map(result -> involvedParty)
                .onFailure()
                .invoke(error -> log.error("Error adding/updating username and password: {}", error.getMessage(), error));
    }

    @Override
    public Uni<Boolean> doesUsernameExist(Mutiny.Session session, String username, ISystems<?, ?> system, UUID... identityToken) {
        log.debug("Checking if username exists: {}", username);
        var enterprise = system.getEnterprise();
        return new InvolvedParty().builder(session)
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .findByIdentificationType(IdentificationTypeUserName, username, system, identityToken)
                .getCount()
                .onFailure()
                .invoke(error -> log.error("Error checking if username exists: {}", error.getMessage(), error))
                .map(count -> count > 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Uni<IInvolvedParty<?, ?>> createAdminAndCreatorUserForEnterprise(Mutiny.Session session, ISystems<?, ?> system, String adminUserName,
                                                                            @NotNull String adminPassword, UUID existingLocalKey) {
        log.debug("Creating admin and creator user for enterprise: {}", system.getEnterpriseID());
        logProgress("Checking base administrator user", "The default user is being checked for compliance", 1);

        // Resolve bootstrap context: identity token + administrators group (sequentially to avoid parallel session usage)
        ISystemsService<?> systemsService = get(ISystemsService.class);
        return (Uni) systemsService.getSecurityIdentityToken(session, system)
                .chain(identityToken -> get(SecurityTokenService.class).getAdministratorsFolder(session, system)
                        .chain(administratorsGroup -> {
                            SecurityToken adminsGroup = (SecurityToken) administratorsGroup;

                            // Check if user already exists
                            return (Uni) new InvolvedParty().builder(session)
                                    .findByIdentificationType(
                                            IdentificationTypes.IdentificationTypeEnterpriseCreatorRole,
                                            adminUserName, system)
                                    .get()
                                    .onItem()
                                    .ifNotNull()
                                    .transform(existingUser -> (IInvolvedParty<?, ?>) existingUser)
                                    .onFailure()
                                    .recoverWithUni(failure -> {
                                        // Create new user
                                        Pair<String, String> pair = new Pair<>(
                                                IdentificationTypes.IdentificationTypeEnterpriseCreatorRole.toString(), adminUserName);
                                        IInvolvedPartyService<?> service = get(IInvolvedPartyService.class);
                                        return (Uni) service.create(session, system, pair, true)
                                                .chain(adminUser -> adminUser.addOrReuseInvolvedPartyIdentificationType(
                                                                session, NoClassification.toString(),
                                                                IdentificationTypeUserName.toString(),
                                                                adminUserName, system, identityToken)
                                                        .replaceWith(adminUser))
                                                .chain(adminUser -> {
                                                    log.trace("Added username identification type");
                                                    return adminUser.addOrReuseInvolvedPartyType(
                                                                    session, NoClassification.toString(),
                                                                    IPTypes.TypeIndividual.toString(),
                                                                    "Creator Individual", system, identityToken)
                                                            .replaceWith(adminUser);
                                                })
                                                .chain(adminUser -> {
                                                    log.trace("Added party type");
                                                    return adminUser.addOrReuseInvolvedPartyNameType(
                                                                    session, NoClassification.toString(),
                                                                    PreferredNameType.toString(),
                                                                    "Enterprise Creator", system, identityToken)
                                                            .replaceWith(adminUser);
                                                })
                                                .chain(adminUser -> {
                                                    log.trace("Added preferred name type");
                                                    return adminUser.addOrReuseInvolvedPartyNameType(
                                                                    session, NoClassification.toString(),
                                                                    CommonNameType.toString(),
                                                                    "Enterprise Creator", system, identityToken)
                                                            .replaceWith(adminUser);
                                                })
                                                .chain(adminUser -> {
                                                    log.trace("Added common name type");
                                                    return adminUser.addOrReuseInvolvedPartyNameType(
                                                                    session, NoClassification.toString(),
                                                                    FullNameType.toString(),
                                                                    "Enterprise Creator", system, identityToken)
                                                            .replaceWith(adminUser);
                                                })
                                                .chain(adminUser -> {
                                                    log.trace("Added full name type");
                                                    return adminUser.addOrReuseInvolvedPartyNameType(
                                                                    session, NoClassification.toString(),
                                                                    FirstNameType.toString(),
                                                                    "Administrator", system, identityToken)
                                                            .replaceWith(adminUser);
                                                })
                                                .chain(adminUser -> {
                                                    log.trace("Added first name type");
                                                    return get(SecurityTokenService.class).create(
                                                                    session, SecurityTokenClassifications.Identity.toString(),
                                                                    adminUserName,
                                                                    "The creator of the enterprise",
                                                                    system,
                                                                    adminsGroup,
                                                                    identityToken)
                                                            .replaceWith(adminUser);
                                                })
                                                .chain(adminUser -> {
                                                    log.trace("Created security token");
                                                    return adminUser.addOrReuseInvolvedPartyIdentificationType(
                                                                    session, NoClassification.toString(),
                                                                    IdentificationTypeEnterpriseCreatorRole.toString(),
                                                                    adminUserName, system, identityToken)
                                                            .replaceWith(adminUser);
                                                })
                                                .chain(adminUser -> {
                                                    log.trace("Added creator role identification type");
                                                    return addUpdateUsernamePassword(
                                                            session, adminUserName, adminPassword, adminUser, system, identityToken);
                                                })
                                                .chain(adminUser -> {
                                                    log.trace("Added username and password");
                                                    return ((InvolvedParty) adminUser).createDefaultSecurity(session, system, identityToken)
                                                            .replaceWith(adminUser);
                                                })
                                                .map(result -> {
                                                    log.trace("Created default security, admin user setup complete");
                                                    return result;
                                                });
                                    });
                        }));
    }
}

