package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.rest.parties.PartyCreateDTO;
import com.guicedee.activitymaster.fsdm.client.services.rest.parties.PartyDTO;
import com.guicedee.activitymaster.fsdm.client.services.rest.parties.PartyDataIncludes;
import com.guicedee.activitymaster.fsdm.client.services.rest.parties.PartySearchByIdentificationDTO;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXInvolvedPartyIdentificationType;
import com.guicedee.activitymaster.fsdm.rest.party.PartyRestService;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Regression coverage for the identification-value round trip on
 * {@link InvolvedPartyXInvolvedPartyIdentificationType}.
 *
 * <p><b>The defect this pins:</b> the entity overrode {@code setValue} to encrypt but had no matching
 * {@code getValue}, so every read returned the raw ciphertext column value
 * ({@code 110|127|141|142|96|123|140|135|}) instead of the plaintext that was written
 * ({@code TestFarm}). Find-by-id looked healthy because the <em>name</em> link
 * ({@code InvolvedPartyXInvolvedPartyNameType}) is deliberately plaintext, but every
 * identification-search &rarr; match-by-name consumer saw ciphertext and concluded "no such record".</p>
 *
 * <p>The contract asserted here:</p>
 * <ul>
 *     <li>{@code setValue} takes plaintext and stores ciphertext;</li>
 *     <li>{@code getValue} returns plaintext;</li>
 *     <li>legacy/plaintext rows (pre-encryption, {@code -Dencrypt=false}, or hand-repaired) read back
 *     verbatim instead of throwing {@link NumberFormatException};</li>
 *     <li>{@code -Dencrypt=false} is a plaintext passthrough on both accessors;</li>
 *     <li>the REST include path ({@code IdentificationTypes}) surfaces plaintext;</li>
 *     <li>search-by-value still works, i.e. the query builder's search-term encryption and the new
 *     read accessor agree.</li>
 * </ul>
 *
 * <p>The exact ciphertext is pinned so any change to the frozen cipher / {@code ascii.offset} is caught
 * here rather than in a downstream consumer.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ResourceLock(Resources.SYSTEM_PROPERTIES)
public class TestInvolvedPartyIdentificationValueEncryption
{
    private static final String SYSTEM = ISystemsService.ActivityMasterSystemName;

    /** The identification type UWE registers farms under. */
    private static final String FARM_TYPE = "Farm";
    /** The plaintext farm name from the field report. */
    private static final String FARM_NAME = "TestFarm";
    /**
     * The exact ciphertext the frozen cipher produces for {@link #FARM_NAME} - each byte offset by 26
     * and pipe terminated. This is the literal string that leaked to callers before the fix.
     */
    private static final String FARM_CIPHER = "110|127|141|142|96|123|140|135|";

    /** The ciphertext shape - used to prove no pipe-form value escapes through the REST DTO. */
    private static final Pattern CIPHER_SHAPE = Pattern.compile("(-?\\d+\\|)+");

    private Mutiny.SessionFactory sessionFactory;
    private String originalEncrypt;

    // ──────────────────────────────────────────────────────────────────────────
    // System property guard - the cipher is gated on a global, restore it every test
    // ──────────────────────────────────────────────────────────────────────────

    @BeforeEach
    public void captureEncryptProperty()
    {
        originalEncrypt = System.getProperty("encrypt");
    }

    @AfterEach
    public void restoreEncryptProperty()
    {
        if (originalEncrypt == null)
        {
            System.clearProperty("encrypt");
        }
        else
        {
            System.setProperty("encrypt", originalEncrypt);
        }
    }

    private static InvolvedPartyXInvolvedPartyIdentificationType link()
    {
        return new InvolvedPartyXInvolvedPartyIdentificationType();
    }

    /**
     * Reveals what is actually held in the {@code Value} column.
     * <p>
     * With the {@code encrypt} gate off, {@code getValue()} is a raw passthrough of the persisted field,
     * so flipping the gate after the write exposes the stored representation without needing reflection
     * into the (non-open) mapped superclass.
     */
    private static String persistedColumnValue(InvolvedPartyXInvolvedPartyIdentificationType link)
    {
        String previous = System.getProperty("encrypt");
        System.setProperty("encrypt", "false");
        try
        {
            return link.getValue();
        }
        finally
        {
            if (previous == null)
            {
                System.clearProperty("encrypt");
            }
            else
            {
                System.setProperty("encrypt", previous);
            }
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // 1. Entity round trip
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    public void entityValueRoundTripsThroughTheCipher()
    {
        System.setProperty("encrypt", "true");

        // Pin the frozen cipher itself so an ascii.offset / algorithm change fails here first.
        assertEquals(FARM_CIPHER, new Passwords().integerEncrypt(FARM_NAME.getBytes()),
                "The frozen cipher must still encode '" + FARM_NAME + "' as '" + FARM_CIPHER + "'");

        InvolvedPartyXInvolvedPartyIdentificationType link = link();
        link.setValue(FARM_NAME);

        assertEquals(FARM_NAME, link.getValue(),
                "getValue() must return the PLAINTEXT that setValue() was given - this is the defect that shipped ciphertext to callers");
        assertEquals(FARM_CIPHER, persistedColumnValue(link),
                "the persisted Value column must still hold the ciphertext (the write path is unchanged)");
        assertNotEquals(FARM_CIPHER, link.getValue(),
                "getValue() must never return the pipe form");
    }

    @Test
    @Order(2)
    public void entityRoundTripSurvivesRepeatedWrites()
    {
        System.setProperty("encrypt", "true");

        InvolvedPartyXInvolvedPartyIdentificationType link = link();
        link.setValue(FARM_NAME);
        // Re-writing what was just read must not double-encrypt (it did before getValue() existed).
        link.setValue(link.getValue());

        assertEquals(FARM_NAME, link.getValue(), "set(get()) must be idempotent");
        assertEquals(FARM_CIPHER, persistedColumnValue(link), "set(get()) must not double-encrypt the column");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // 2. Legacy / plaintext row tolerance
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @Order(3)
    public void legacyPlaintextRowReadsBackVerbatim()
    {
        // Simulate a row written before the setValue() encryption existed (or with -Dencrypt=false,
        // or repaired by hand): the column holds plaintext.
        System.setProperty("encrypt", "false");
        InvolvedPartyXInvolvedPartyIdentificationType link = link();
        link.setValue(FARM_NAME);
        assertEquals(FARM_NAME, persistedColumnValue(link), "precondition - the column holds plaintext");

        // Now read it back with encryption ON, exactly as a mixed-vintage database would be read.
        System.setProperty("encrypt", "true");
        assertDoesNotThrow(link::getValue,
                "a legacy plaintext row must not blow up in Passwords.integerDecrypt / Integer.parseInt");
        assertEquals(FARM_NAME, link.getValue(),
                "a legacy plaintext row must read back verbatim - never decrypted, never blanked");
    }

    @Test
    @Order(4)
    public void legacyPlaintextVariantsAreAllTolerated()
    {
        System.setProperty("encrypt", "false");

        InvolvedPartyXInvolvedPartyIdentificationType digits = link();
        digits.setValue("8001015009087");           // a legacy national identification number
        InvolvedPartyXInvolvedPartyIdentificationType email = link();
        email.setValue("user@example.com");         // a legacy email identification
        InvolvedPartyXInvolvedPartyIdentificationType piped = link();
        piped.setValue("Farm|North");               // pipes, but not the cipher shape
        InvolvedPartyXInvolvedPartyIdentificationType empty = link();
        empty.setValue("");

        System.setProperty("encrypt", "true");

        assertEquals("8001015009087", digits.getValue(), "a bare numeric legacy value must not be decoded");
        assertEquals("user@example.com", email.getValue(), "a legacy email value must read back verbatim");
        assertEquals("Farm|North", piped.getValue(), "a piped but non-numeric value must read back verbatim");
        assertEquals("", empty.getValue(), "an empty value must stay empty, never fail");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // 3. -Dencrypt=false escape hatch
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @Order(5)
    public void encryptDisabledIsAPlaintextPassthroughOnBothAccessors()
    {
        System.setProperty("encrypt", "false");

        InvolvedPartyXInvolvedPartyIdentificationType link = link();
        link.setValue(FARM_NAME);

        assertEquals(FARM_NAME, link.getValue(), "-Dencrypt=false must read plaintext");
        assertEquals(FARM_NAME, persistedColumnValue(link), "-Dencrypt=false must store plaintext - no cipher applied");
    }

    @Test
    @Order(6)
    public void encryptDisabledLeavesCiphertextRowsUntouched()
    {
        // A row written while encryption was on, read with the escape hatch: raw passthrough, as Address does.
        System.setProperty("encrypt", "true");
        InvolvedPartyXInvolvedPartyIdentificationType link = link();
        link.setValue(FARM_NAME);

        System.setProperty("encrypt", "false");
        assertEquals(FARM_CIPHER, link.getValue(),
                "-Dencrypt=false must not attempt to decrypt - it is a pure passthrough");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Database backed - the regression that actually shipped
    // ──────────────────────────────────────────────────────────────────────────

    @BeforeAll
    public void setup()
    {
        ActivityMasterConfiguration.get()
                                   .setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        // Provision the enterprise on the stateless pipeline. Idempotent: create only when absent, always start.
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        sessionFactory.openStatelessSession()
                      .chain(ss -> enterpriseService.getEnterprise(ss, TestEnterprise.name())
                                                    .onFailure()
                                                    .recoverWithUni(t -> {
                                                        var ent = enterpriseService.get();
                                                        ent.setName(TestEnterprise.name());
                                                        ent.setDescription("Enterprise for identification-value encryption tests");
                                                        return enterpriseService.createNewEnterprise(ss, ent);
                                                    })
                                                    .chain(ent -> enterpriseService.startNewEnterprise(ss, TestEnterprise.name(), "admin", "adminadmin!@"))
                                                    .onFailure()
                                                    .recoverWithItem(e -> null)
                                                    .eventually(ss::close))
                      .await()
                      .atMost(Duration.ofMinutes(3));

        // Register the 'Farm' identification type (the one UWE registers farms under).
        IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);
        sessionFactory.withStatelessTransaction(session -> activityMaster(session)
                              .chain(sys -> partyService.createIdentificationType(session, sys, FARM_TYPE, "Farm registration")))
                      .await()
                      .atMost(Duration.ofMinutes(2));
    }

    private Uni<ISystems<?, ?>> activityMaster(Mutiny.StatelessSession session)
    {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        return es.getEnterprise(session, TestEnterprise.name())
                 .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                 .map(sys -> (ISystems<?, ?>) sys);
    }

    private static <T> T await(Uni<T> uni)
    {
        return uni.await()
                  .atMost(Duration.ofMinutes(2));
    }

    /**
     * The exact call UWE makes: create a Farm-identified party through the REST service, then search
     * for it by identification type and hydrate the {@code IdentificationTypes} include. Before the fix
     * the returned map held {@code 110|127|141|142|96|123|140|135|}, so every match-by-name consumer
     * (UWE's {@code FarmService#farmIdToNameMap}) saw no usable farms.
     */
    @Test
    @Order(10)
    public void restIdentificationSearchReturnsPlaintextValue()
    {
        System.setProperty("encrypt", "true");
        PartyRestService rest = IGuiceContext.get(PartyRestService.class);
        assertNotNull(rest, "PartyRestService should be injectable");

        PartyCreateDTO create = new PartyCreateDTO();
        create.identificationType = FARM_TYPE;
        create.identificationValue = FARM_NAME;
        create.organic = false;

        PartyDTO created = await(rest.create(TestEnterprise.name(), SYSTEM, create));
        assertNotNull(created, "create should echo a DTO");
        assertNotNull(created.partyId, "create should return a generated party id");

        PartySearchByIdentificationDTO search = new PartySearchByIdentificationDTO();
        search.identificationType = FARM_TYPE;
        search.identificationValue = FARM_NAME;
        search.includes = List.of(PartyDataIncludes.IdentificationTypes);

        List<PartyDTO> results = await(rest.searchByIdentification(TestEnterprise.name(), SYSTEM, search));
        assertNotNull(results, "search/identification should not return null");
        assertFalse(results.isEmpty(), "search/identification should find the farm party that was just created");

        boolean sawPlaintext = false;
        for (PartyDTO dto : results)
        {
            Map<String, String> identificationTypes = dto.identificationTypes;
            assertNotNull(identificationTypes, "the IdentificationTypes include must be hydrated");
            for (Map.Entry<String, String> entry : identificationTypes.entrySet())
            {
                assertFalse(CIPHER_SHAPE.matcher(entry.getValue())
                                        .matches(),
                        "the REST DTO must never surface the ciphertext pipe form - got '" + entry.getValue()
                                + "' for classification '" + entry.getKey() + "'");
                if (FARM_NAME.equals(entry.getValue()))
                {
                    sawPlaintext = true;
                }
            }
        }
        assertTrue(sawPlaintext,
                "the identificationTypes map must contain the plaintext '" + FARM_NAME + "', not '" + FARM_CIPHER + "'");
    }

    /**
     * Proves the query builder's search-term encryption and the new read accessor agree: the search still
     * matches (the term is encrypted before comparison) and the hydrated link now reads back as plaintext.
     */
    @Test
    @Order(11)
    public void searchByValueStillMatchesAndHydratesPlaintext()
    {
        System.setProperty("encrypt", "true");
        IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);

        List<?> found = await(sessionFactory.withStatelessTransaction(session ->
                partyService.findAllByIdentificationType(session, FARM_TYPE, FARM_NAME)
                            .map(list -> (List<?>) list)));

        assertNotNull(found, "findAllByIdentificationType should not return null");
        assertFalse(found.isEmpty(),
                "findAllByIdentificationType must still match on the encrypted column (the query builder encrypts the search term)");

        for (Object row : found)
        {
            InvolvedPartyXInvolvedPartyIdentificationType link = (InvolvedPartyXInvolvedPartyIdentificationType) row;
            assertEquals(FARM_NAME, link.getValue(),
                    "the hydrated link must read back as plaintext");
        }
    }
}

