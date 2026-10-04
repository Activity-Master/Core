package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.api.ColumnEncryption;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.dto.PartyPhoneDTO;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.*;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import java.util.*;
import static com.entityassist.enumerations.Operand.Equals;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.Address;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification;

/** Protected telephone address persistence on the caller's stateless transaction. */
public class PartyPhoneService {
    @Inject private IClassificationService<?> classifications;
    @Inject private IInvolvedPartyService<?> parties;
    @Inject private IActiveFlagService<?> activeFlags;
    @Inject private ISecurityTokenService<?> security;
    private static final String ANCHOR_TYPE = "PartyTelephone";
    private static final String NUMBER_TYPE = "TelephoneNumber";
    private static final String EXTENSION_TYPE = "TelephoneExtensionNumber";

    public Uni<PartyPhoneDTO> save(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party,
                                  PartyPhoneDTO phone, ISystems<?, ?> system, UUID... tokens) {
        if (!ColumnEncryption.searchableEncryption())
            return Uni.createFrom().failure(new IllegalStateException("Telephone numbers require authenticated encryption"));
        com.guicedee.activitymaster.fsdm.db.entities.address.Address address = new com.guicedee.activitymaster.fsdm.db.entities.address.Address();
        address.setId(phone.id() == null ? UUID.randomUUID() : phone.id());
        return requireParty(session, party, system, true, tokens)
            .chain(() -> phone.id() == null ? createAnchor(session, address, system, tokens)
                : ownedLink(session, party, address, system).chain(link -> requireWrite(session, link, system, tokens))
                    .chain(() -> requireWrite(session, address, system, tokens))
                    .chain(() -> clear(session, party, address, system, tokens)))
            .chain(() -> classifications.find(session, phone.type(), Address, system, tokens))
            .chain(type -> {
                InvolvedPartyXAddress link = new InvolvedPartyXAddress();
                link.setInvolvedPartyID((InvolvedParty) party);
                link.setAddressID(address);
                link.setClassificationID(type);
                link.setValue("");
                return insert(session, link, system, tokens);
            })
            .chain(() -> saveProtected(session, party, address, NUMBER_TYPE, phone.number(), system, tokens))
            .chain(() -> phone.extension().isEmpty() ? Uni.createFrom().voidItem()
                : saveProtected(session, party, address, EXTENSION_TYPE, phone.extension(), system, tokens))
            .replaceWith(new PartyPhoneDTO(address.getId(), phone.type(), phone.number(), phone.extension()));
    }

    private Uni<Void> createAnchor(Mutiny.StatelessSession session, com.guicedee.activitymaster.fsdm.db.entities.address.Address address,
                                   ISystems<?, ?> system, UUID... tokens) {
        return new AddressType().builder(session).withName(ANCHOR_TYPE).withEnterprise(system.getEnterprise())
            .inActiveRange().inDateRange().getCount().chain(count -> {
                if (count > 0) return new AddressType().builder(session).withName(ANCHOR_TYPE).withEnterprise(system.getEnterprise())
                    .inActiveRange().inDateRange().get();
                var type = new AddressType();
                type.setName(ANCHOR_TYPE);
                type.setDescription("Owned protected telephone address");
                return insert(session, type, system, tokens).replaceWith(type);
            }).chain(type -> classifications.find(session, NoClassification.classificationValue(), system, tokens).chain(classification -> {
                address.setAddressTypeID(type);
                address.setClassificationID(classification);
                address.setValue("");
                return insert(session, address, system, tokens);
            }));
    }

    private Uni<Void> saveProtected(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party,
                                    com.guicedee.activitymaster.fsdm.db.entities.address.Address address,
                                    String name, String value, ISystems<?, ?> system, UUID... tokens) {
        return parties.createIdentificationType(session, system, name, "Protected telephone component", tokens)
            .chain(type -> classifications.find(session, NoClassification.classificationValue(), system, tokens).chain(classification -> {
                var link = new InvolvedPartyXInvolvedPartyIdentificationType();
                link.setEnterpriseID(system.getEnterprise());
                link.setInvolvedPartyID((InvolvedParty) party);
                link.setAddressID(address);
                link.setInvolvedPartyIdentificationTypeID((InvolvedPartyIdentificationType) type);
                link.setClassificationID(classification);
                link.setValue(value);
                return insert(session, link, system, tokens);
            }));
    }

    public Uni<List<PartyPhoneDTO>> find(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party, ISystems<?, ?> system, UUID... tokens) {
        return requireParty(session, party, system, false, tokens)
            .chain(() -> new InvolvedPartyXAddress().builder(session).findLink((InvolvedParty) party, null, null)
                .where("addressID.addressTypeID.name", Equals, ANCHOR_TYPE)
                .withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().getAll())
            .chain(links -> {
                Uni<List<PartyPhoneDTO>> chain = Uni.createFrom().item(new ArrayList<>());
                for (var link : links) chain = chain.chain(result -> link.canRead(session, system, tokens).chain(readable -> {
                    if (!readable) return Uni.createFrom().item(result);
                    return session.fetch(link.getAddressID()).chain(address -> address.canRead(session, system, tokens).chain(allowed -> {
                        if (!allowed) return Uni.createFrom().item(result);
                        return session.fetch(link.getClassificationID()).chain(type -> read(session, party, address, type.getName(), system, tokens))
                            .invoke(phone -> { if (phone != null) result.add(phone); }).replaceWith(result);
                    }));
                }));
                return chain;
            });
    }

    private Uni<PartyPhoneDTO> read(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party,
                                    com.guicedee.activitymaster.fsdm.db.entities.address.Address address, String type, ISystems<?, ?> system, UUID... tokens) {
        var values = new HashMap<String, String>();
        return protectedLinks(session, party, address, system).chain(links -> {
            Uni<Void> chain = Uni.createFrom().voidItem();
            for (var link : links) chain = chain.chain(() -> link.canRead(session, system, tokens).chain(allowed -> allowed
                ? session.fetch(link.getInvolvedPartyIdentificationTypeID()).invoke(kind -> values.put(kind.getName(), link.getValue())).replaceWithVoid()
                : Uni.createFrom().voidItem()));
            return chain;
        }).replaceWith(() -> values.containsKey(NUMBER_TYPE) && PartyPhoneDTO.TYPES.contains(type)
            ? new PartyPhoneDTO(address.getId(), type, values.get(NUMBER_TYPE), values.get(EXTENSION_TYPE)) : null);
    }

    public Uni<Void> end(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party, UUID id, ISystems<?, ?> system, UUID... tokens) {
        var address = new com.guicedee.activitymaster.fsdm.db.entities.address.Address();
        address.setId(id);
        return requireParty(session, party, system, true, tokens)
            .chain(() -> ownedLink(session, party, address, system))
            .chain(link -> requireWrite(session, link, system, tokens))
            .chain(() -> clear(session, party, address, system, tokens));
    }

    private Uni<InvolvedPartyXAddress> ownedLink(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party,
                                                com.guicedee.activitymaster.fsdm.db.entities.address.Address address, ISystems<?, ?> system) {
        return new InvolvedPartyXAddress().builder(session).findLink((InvolvedParty) party, address, null)
            .where("addressID.addressTypeID.name", Equals, ANCHOR_TYPE)
            .withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().get()
            .onFailure(jakarta.persistence.NoResultException.class).transform(error -> new SecurityException("Telephone address is unavailable"))
            .onItem().ifNull().failWith(() -> new SecurityException("Telephone address is unavailable"));
    }

    private Uni<List<InvolvedPartyXInvolvedPartyIdentificationType>> protectedLinks(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party,
                        com.guicedee.activitymaster.fsdm.db.entities.address.Address address, ISystems<?, ?> system) {
        return new InvolvedPartyXInvolvedPartyIdentificationType().builder(session).findLink((InvolvedParty) party, null, null)
            .where("addressID", Equals, address).withEnterprise(system.getEnterprise()).inActiveRange().inDateRange().getAll();
    }

    private Uni<Void> clear(Mutiny.StatelessSession session, IInvolvedParty<?, ?> party,
                            com.guicedee.activitymaster.fsdm.db.entities.address.Address address, ISystems<?, ?> system, UUID... tokens) {
        return protectedLinks(session, party, address, system).chain(links -> {
            Uni<Void> chain = Uni.createFrom().voidItem();
            for (var link : links) chain = chain.chain(() -> requireWrite(session, link, system, tokens))
                .chain(() -> link.archive(session, system, tokens).replaceWithVoid());
            return chain;
        }).chain(() -> ownedLink(session, party, address, system))
            .chain(link -> requireWrite(session, link, system, tokens).chain(() -> link.archive(session, system, tokens).replaceWithVoid()));
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
