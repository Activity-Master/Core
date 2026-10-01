package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.api.ColumnEncryption;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.dto.PartyAddressDTO;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.geography.Geography;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.*;

import static com.entityassist.enumerations.Operand.Equals;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification;

/** Component-only physical/postal address persistence on the caller's stateless transaction. */
public class StructuredPartyAddressService {
    @Inject private IClassificationService<?> classifications;
    @Inject private IClassificationDataConceptService<?> concepts;
    @Inject private IInvolvedPartyService<?> parties;
    @Inject private IActiveFlagService<?> activeFlags;
    @Inject private ISecurityTokenService<?> security;
    private static final String PURPOSE_CONCEPT = "PartyAddressPurposes";
    private static final String IDENTIFIER_PREFIX = "AddressIdentifier";

    public Uni<PartyAddressDTO> save(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party,
                                      PartyAddressDTO dto, ISystems<?, ?> system, UUID... tokens) {
        if (!dto.identifiers().isEmpty() && !ColumnEncryption.searchableEncryption())
            return Uni.createFrom().failure(new IllegalStateException("Authenticated encryption is required for address identifiers"));
        Address address = new Address();
        address.setId(dto.id() == null ? UUID.randomUUID() : dto.id());
        return requireParty(session, party, system, true, tokens)
                .chain(() -> validateGeographies(session, dto, system, tokens))
                .chain(() -> dto.id() == null ? createAnchor(session, address, system, tokens)
                        : ownedLink(session, party, address, system, tokens)
                            .chain(link -> requireWrite(session, link, system, tokens))
                            .chain(() -> clearParts(session, party, address, system, tokens)))
                .chain(() -> choice(session, dto.purpose(), PURPOSE_CONCEPT, system, tokens))
                .chain(purpose -> {
                    InvolvedPartyXAddress link = new InvolvedPartyXAddress();
                    link.setInvolvedPartyID((InvolvedParty) party);
                    link.setAddressID(address);
                    link.setClassificationID(purpose);
                    link.setValue("");
                    return insert(session, link, system, tokens);
                })
                .chain(() -> saveComponents(session, address, dto, system, tokens))
                .chain(() -> saveGeographies(session, address, dto, system, tokens))
                .chain(() -> saveIdentifiers(session, party, address, dto, system, tokens))
                .replaceWith(new PartyAddressDTO(address.getId(), dto.purpose(), nonempty(dto.components()), dto.geographies(), nonempty(dto.identifiers())));
    }

    private static Map<String, String> nonempty(Map<String, String> source) {
        var result = new LinkedHashMap<String, String>();
        source.forEach((key, value) -> { if (!value.isEmpty()) result.put(key, value); });
        return result;
    }

    private Uni<Void> createAnchor(Mutiny.StatelessSession session, Address address, ISystems<?, ?> system, UUID... tokens) {
        return addressType(session, "StructuredAddress", system, tokens).chain(type ->
                classifications.find(session, NoClassification.classificationValue(), system, tokens).chain(classification -> {
                    address.setAddressTypeID(type);
                    address.setClassificationID(classification);
                    address.setValue("");
                    return insert(session, address, system, tokens);
                }));
    }

    private String componentType(String role) {
        return switch (role) {
            case "StreetName" -> "Street";
            case "BuildingNumber" -> "StreetNumber";
            default -> role;
        };
    }

    private Uni<AddressType> addressType(Mutiny.StatelessSession session, String role, ISystems<?, ?> system, UUID... tokens) {
        var query = new AddressType().builder(session).withName(role).withEnterprise(system.getEnterprise()).inActiveRange().inDateRange();
        return query.getCount().chain(count -> {
            if (count > 0) return new AddressType().builder(session).withName(role).withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().get();
            var type = new AddressType();
            type.setName(role);
            type.setDescription("Canonical address component role");
            return insert(session, type, system, tokens).replaceWith(type);
        });
    }

    private Uni<Address> component(Mutiny.StatelessSession session, String role, String value, ISystems<?, ?> system, UUID... tokens) {
        return addressType(session, componentType(role), system, tokens).chain(type -> {
            var query = new Address().builder(session).withEnterprise(system.getEnterprise()).where("addressTypeID", Equals, type)
                    .where("value", Equals, value).inActiveRange().inDateRange();
            return query.getCount().chain(count -> {
                if (count > 0) return new Address().builder(session).withEnterprise(system.getEnterprise()).where("addressTypeID", Equals, type)
                        .where("value", Equals, value).inActiveRange().inDateRange().get();
                return classifications.find(session, NoClassification.classificationValue(), system, tokens).chain(classification -> {
                    var component = new Address();
                    component.setAddressTypeID(type);
                    component.setClassificationID(classification);
                    component.setComponentValue(value);
                    return insert(session, component, system, tokens).replaceWith(component);
                });
            });
        });
    }

    private Uni<Void> saveComponents(Mutiny.StatelessSession session, Address address, PartyAddressDTO dto, ISystems<?, ?> system, UUID... tokens) {
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (var entry : nonempty(dto.components()).entrySet()) {
            chain = chain.chain(() -> component(session, entry.getKey(), entry.getValue(), system, tokens).chain(component ->
                    classifications.find(session, NoClassification.classificationValue(), system, tokens).chain(classification -> {
                        var link = new AddressXAddress();
                        link.setAddressID(address);
                        link.setComponentAddressID(component);
                        link.setClassificationID(classification);
                        link.setValue(entry.getKey());
                        return insert(session, link, system, tokens);
                    })));
        }
        return chain;
    }

    private Uni<Void> saveGeographies(Mutiny.StatelessSession session, Address address, PartyAddressDTO dto, ISystems<?, ?> system, UUID... tokens) {
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (var entry : dto.geographies().entrySet()) {
            chain = chain.chain(() -> new Geography().builder(session).find(entry.getValue())
                    .withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().get()
                    .onItem().ifNull().failWith(() -> new IllegalArgumentException("Address geography is unavailable"))
                    .chain(geography -> geography.canRead(session, system, tokens).chain(readable -> {
                        if (!readable) return Uni.createFrom().failure(new SecurityException("Address geography is unavailable"));
                        return classifications.find(session, NoClassification.classificationValue(), system, tokens).chain(type -> {
                            AddressXGeography link = new AddressXGeography();
                            link.setAddressID(address);
                            link.setGeographyID(geography);
                            link.setClassificationID(type);
                            link.setValue(entry.getKey());
                            return insert(session, link, system, tokens);
                        });
                    })));
        }
        return chain;
    }

    private Uni<Void> validateGeographies(Mutiny.StatelessSession session, PartyAddressDTO dto, ISystems<?, ?> system, UUID... tokens) {
        if (dto.geographies().isEmpty()) return Uni.createFrom().voidItem();
        UUID country = dto.geographies().get("Country");
        if (country == null) return Uni.createFrom().failure(new IllegalArgumentException("Choose the address country before its other geography levels"));
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (var entry : dto.geographies().entrySet()) {
            Set<String> kinds = switch (entry.getKey()) {
                case "Country" -> Set.of("Country");
                case "Province" -> Set.of("Province");
                case "District" -> Set.of("Municipalities");
                case "Locality" -> Set.of("Town", "City");
                default -> Set.of("PostalCode", "PostalCodeSuburb");
            };
            chain = chain.chain(() -> new Geography().builder(session).find(entry.getValue()).withEnterprise(system.getEnterprise())
                    .inActiveRange().inDateRange().get().chain(geo -> geo.canRead(session, system, tokens).chain(readable -> {
                        if (!readable) return Uni.createFrom().failure(new SecurityException("Address geography is unavailable"));
                        return session.fetch(geo.getClassificationID()).chain(kind -> kinds.contains(kind.getName())
                                ? Uni.createFrom().voidItem() : Uni.createFrom().failure(new IllegalArgumentException("Geography does not match the address level")));
                    })));
            if (entry.getKey().equals("Country")) continue;
            UUID parent = entry.getKey().equals("District") ? dto.geographies().getOrDefault("Province", country)
                    : entry.getKey().equals("Locality") ? dto.geographies().getOrDefault("District", dto.geographies().getOrDefault("Province", country))
                    : country;
            chain = chain.chain(() -> session.createNativeQuery("""
                WITH RECURSIVE tree(id, depth) AS (
                  SELECT geographyid, 0 FROM geography.geography WHERE geographyid = :parent AND enterpriseid = :enterprise
                  UNION ALL
                  SELECT x.childgeographyid, t.depth + 1 FROM geography.geographyxgeography x
                  JOIN tree t ON x.parentgeographyid = t.id
                  JOIN dbo.activeflag f ON f.activeflagid = x.activeflagid AND f.allowaccess = 1
                  WHERE t.depth < 8 AND x.enterpriseid = :enterprise
                    AND x.effectivefromdate <= statement_timestamp() AND x.effectivetodate > statement_timestamp())
                SELECT COUNT(*) FROM tree WHERE id = :child AND depth > 0
                """).setParameter("parent", parent).setParameter("child", entry.getValue())
                    .setParameter("enterprise", system.getEnterprise().getId()).getSingleResult()
                    .chain(count -> ((Number) count).longValue() > 0 ? Uni.createFrom().voidItem()
                            : Uni.createFrom().failure(new IllegalArgumentException("Address geography levels do not belong together"))));
        }
        return chain;
    }

    private Uni<Void> saveIdentifiers(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party, Address address,
                                      PartyAddressDTO dto, ISystems<?, ?> system, UUID... tokens) {
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (var entry : nonempty(dto.identifiers()).entrySet()) {
            chain = chain.chain(() -> addressType(session, componentType(entry.getKey()), system, tokens).chain(addressType -> parties.createIdentificationType(session, system,
                    IDENTIFIER_PREFIX + entry.getKey(), "Protected address identifier", tokens)
                    .chain(type -> classifications.find(session, NoClassification.classificationValue(), system, tokens)
                        .chain(classification -> {
                            InvolvedPartyXInvolvedPartyIdentificationType link = new InvolvedPartyXInvolvedPartyIdentificationType();
                            link.setEnterpriseID(system.getEnterprise());
                            link.setInvolvedPartyID((InvolvedParty) party);
                            link.setAddressID(address);
                            link.setAddressTypeID(addressType);
                            link.setInvolvedPartyIdentificationTypeID((InvolvedPartyIdentificationType) type);
                            link.setClassificationID(classification);
                            link.setValue(entry.getValue());
                            return insert(session, link, system, tokens);
                        }))));
        }
        return chain;
    }

    public Uni<List<PartyAddressDTO>> find(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party, ISystems<?, ?> system, UUID... tokens) {
        return requireParty(session, party, system, false, tokens)
                .chain(() -> new InvolvedPartyXAddress().builder(session).findLink((InvolvedParty) party, null, null)
                        .where("addressID.addressTypeID.name", Equals, "StructuredAddress")
                        .withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().getAll())
                .chain(links -> {
                    Uni<List<PartyAddressDTO>> chain = Uni.createFrom().item(new ArrayList<>());
                    for (var link : links) {
                        chain = chain.chain(result -> link.canRead(session, system, tokens).chain(readable -> !readable
                                ? Uni.createFrom().item(result)
                                : session.fetch(link.getClassificationID()).chain(purpose ->
                                    PartyAddressDTO.PURPOSES.contains(purpose.getName())
                                        ? readParts(session, party, link.getAddressID(), purpose.getName(), system, tokens)
                                            .invoke(result::add).replaceWith(result)
                                        : Uni.createFrom().item(result))));
                    }
                    return chain;
                });
    }

    private Uni<PartyAddressDTO> readParts(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party, Address address,
                                          String purpose, ISystems<?, ?> system, UUID... tokens) {
        var components = new LinkedHashMap<String, String>();
        var geographies = new LinkedHashMap<String, UUID>();
        var identifiers = new LinkedHashMap<String, String>();
        var labels = new LinkedHashMap<String, String>();
        return new AddressXAddress().builder(session).findLink(address, null, null)
                .withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().getAll()
                .chain(links -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var link : links) chain = chain.chain(() -> link.canRead(session, system, tokens).chain(readable -> readable
                            ? session.fetch(link.getComponentAddressID()).chain(component -> component.canRead(session, system, tokens)
                                .invoke(allowed -> { if (allowed) components.put(link.getValue(), component.getValue()); }).replaceWithVoid())
                            : Uni.createFrom().voidItem()));
                    return chain;
                })
                .chain(() -> new AddressXGeography().builder(session).findLink(address, null, null)
                        .withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().getAll())
                .chain(links -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var link : links) chain = chain.chain(() -> link.canRead(session, system, tokens).chain(readable -> readable
                            ? session.fetch(link.getGeographyID()).invoke(geo -> {
                                geographies.put(link.getValue(), geo.getId());
                                labels.put(link.getValue(), link.getValue().equals("Country") && geo.getDescription() != null ? geo.getDescription() : geo.getName());
                              }).replaceWithVoid()
                            : Uni.createFrom().voidItem()));
                    return chain;
                })
                .chain(() -> identificationLinks(session, party, address, system))
                .chain(links -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var link : links) chain = chain.chain(() -> link.canRead(session, system, tokens).chain(readable -> readable
                            ? session.fetch(link.getInvolvedPartyIdentificationTypeID()).invoke(type -> {
                                if (type.getName().startsWith(IDENTIFIER_PREFIX)) identifiers.put(type.getName().substring(IDENTIFIER_PREFIX.length()), link.getValue());
                              }).replaceWithVoid()
                            : Uni.createFrom().voidItem()));
                    return chain;
                }).replaceWith(() -> new PartyAddressDTO(address.getId(), purpose, components, geographies, identifiers, labels));
    }

    public Uni<Void> end(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party, UUID addressId, ISystems<?, ?> system, UUID... tokens) {
        Address address = new Address();
        address.setId(addressId);
        return requireParty(session, party, system, true, tokens)
                .chain(() -> ownedLink(session, party, address, system, tokens))
                .chain(link -> requireWrite(session, link, system, tokens))
                .chain(() -> clearParts(session, party, address, system, tokens));
    }

    private Uni<InvolvedPartyXAddress> ownedLink(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party, Address address, ISystems<?, ?> system, UUID... tokens) {
        return new InvolvedPartyXAddress().builder(session).findLink((InvolvedParty) party, address, null)
                .where("addressID.addressTypeID.name", Equals, "StructuredAddress")
                .withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().get()
                .onFailure(jakarta.persistence.NoResultException.class).transform(failure -> new SecurityException("Address does not belong to this party"))
                .onItem().ifNull().failWith(() -> new SecurityException("Address does not belong to this party"));
    }

    private Uni<List<InvolvedPartyXInvolvedPartyIdentificationType>> identificationLinks(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party, Address address, ISystems<?, ?> system) {
        return new InvolvedPartyXInvolvedPartyIdentificationType().builder(session).findLink((InvolvedParty) party, null, null)
                .where("addressID", Equals, address).withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().getAll();
    }

    private Uni<Void> clearParts(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party, Address address, ISystems<?, ?> system, UUID... tokens) {
        return identificationLinks(session, party, address, system).chain(links -> {
            Uni<Void> chain = Uni.createFrom().voidItem();
            for (var link : links) chain = chain.chain(() -> link.archive(session, system, tokens).replaceWithVoid());
            return chain;
        }).chain(() -> new AddressXAddress().builder(session).findLink(address, null, null)
                .withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().getAll()).chain(links -> {
            Uni<Void> chain = Uni.createFrom().voidItem();
            for (var link : links) chain = chain.chain(() -> link.archive(session, system, tokens).replaceWithVoid());
            return chain;
        }).chain(() -> new AddressXGeography().builder(session).findLink(address, null, null)
                .withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().getAll()).chain(links -> {
            Uni<Void> chain = Uni.createFrom().voidItem();
            for (var link : links) chain = chain.chain(() -> link.archive(session, system, tokens).replaceWithVoid());
            return chain;
        }).chain(() -> ownedLink(session, party, address, system, tokens))
                .chain(link -> link.archive(session, system, tokens).replaceWithVoid());
    }

    private Uni<Void> requireParty(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party, ISystems<?, ?> system, boolean write, UUID... tokens) {
        return new InvolvedParty().builder(session).find(party.getId()).withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().get()
                .onFailure(jakarta.persistence.NoResultException.class).transform(failure -> new SecurityException("Party is unavailable"))
                .onItem().ifNull().failWith(() -> new SecurityException("Party is unavailable"))
                .chain(found -> write ? requireWrite(session, found, system, tokens)
                        : found.canRead(session, system, tokens).chain(allowed -> allowed ? Uni.createFrom().voidItem()
                            : Uni.createFrom().failure(new SecurityException("Party is unavailable"))));
    }

    private Uni<Void> requireWrite(Mutiny.StatelessSession session, WarehouseCoreTable<?, ?, ?, ?> row, ISystems<?, ?> system, UUID... tokens) {
        return row.canWrite(session, system, tokens).chain(allowed -> allowed ? Uni.createFrom().voidItem()
                : Uni.createFrom().failure(new SecurityException("Address write is not permitted")));
    }

    private Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification<?, ?>> choice(
            Mutiny.StatelessSession session, String value, String concept, ISystems<?, ?> system, UUID... tokens) {
        return concepts.createNamedDataConcept(session, concept, "Canonical address components", system, tokens)
                .chain(() -> classifications.createInConcept(session, value, value, concept, system, null, null, tokens));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Uni<Void> insert(Mutiny.StatelessSession session, WarehouseSCDTable row, ISystems<?, ?> system, UUID... tokens) {
        if (row.getId() == null) row.setId(UUID.randomUUID());
        row.setEnterpriseID(system.getEnterprise());
        row.setSystemID(system);
        row.setOriginalSourceSystemID(system.getId());
        return activeFlags.getActiveFlag(session, system.getEnterprise(), tokens).chain(active -> {
            row.setActiveFlagID(active);
            return session.insert(row).chain(() -> security.resolveDefaultGroupFolderTokens(session, system, tokens)
                    .chain(grants -> row.createScopeRestrictedSecurity(session, system, system.getEnterprise(), active, grants, null, tokens))).replaceWithVoid();
        });
    }
}
