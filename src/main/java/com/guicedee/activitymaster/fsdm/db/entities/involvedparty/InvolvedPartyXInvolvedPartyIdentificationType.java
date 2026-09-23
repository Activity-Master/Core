package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.api.ColumnEncryption;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * Link between an {@link InvolvedParty} and an {@link InvolvedPartyIdentificationType}, carrying the
 * identification value (identity number, farm name, account reference, ...).
 *
 * <h2>Value round-trip contract</h2>
 * <ul>
 *     <li>{@link #setValue(String)} takes <b>plaintext</b> and stores <b>ciphertext</b>.</li>
 *     <li>{@link #getValue()} returns <b>plaintext</b>.</li>
 *     <li>The persisted {@code Value} column holds the configured storage representation
 *     (field access is used, so JPA reads/writes the raw column and never goes through these accessors).</li>
 *     <li>Any query builder that compares against this column must use the format-aware lookup -
 *     see {@link InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder#withValue}.</li>
 *     <li>Legacy writes honour {@code encrypt} (default {@code true}); opt-in AES-GCM writes
 *     override that gate. Strong envelopes are always authenticated and decrypted.</li>
 *     <li>Legacy rows holding plaintext in the column are returned verbatim by {@link #getValue()} -
 *     they are never decrypted and never blanked.</li>
 * </ul>
 *
 * <h2>Do not "harmonise" with the name link</h2>
 * {@code InvolvedPartyXInvolvedPartyNameType} is deliberately <b>plaintext</b> - every find-by-name /
 * resolve-name consumer depends on it. Do not add encryption there, and do not remove encryption here.
 * The two links are intentionally asymmetric.
 *
 * <p>The default ASCII-offset format remains frozen for compatibility. {@link ColumnEncryption}
 * adds opt-in authenticated encryption without changing password hashing or party names.
 *
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Party",
        name = "InvolvedPartyXInvolvedPartyIdentificationType")
@XmlRootElement

@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvolvedPartyXInvolvedPartyIdentificationType
        extends WarehouseClassificationRelationshipTypesTable<InvolvedParty, InvolvedPartyIdentificationType,
                                InvolvedPartyXInvolvedPartyIdentificationType,
                                InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder,
                                UUID,
                                InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;

    @Id

    @Column(nullable = false,
            name = "InvolvedPartyXInvolvedPartyIdentificationTypeID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> securities;

    @JoinColumn(name = "InvolvedPartyID",
            referencedColumnName = "InvolvedPartyID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private InvolvedParty involvedPartyID;

    @JoinColumn(name = "InvolvedPartyIdentificationTypeID",
            referencedColumnName = "InvolvedPartyIdentificationTypeID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private InvolvedPartyIdentificationType involvedPartyIdentificationTypeID;

    public InvolvedPartyXInvolvedPartyIdentificationType(UUID involvedPartyXInvolvedPartyIdentificationTypeID, String value)
    {
        this.id = involvedPartyXInvolvedPartyIdentificationTypeID;
        setValue(value);
    }

    @Override
    public void configureSecurityEntity(InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public InvolvedPartyXInvolvedPartyIdentificationType setSecurities(List<InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public InvolvedParty getInvolvedPartyID()
    {
        return this.involvedPartyID;
    }

    public InvolvedPartyXInvolvedPartyIdentificationType setInvolvedPartyID(InvolvedParty involvedPartyID)
    {
        this.involvedPartyID = involvedPartyID;
        return this;
    }

    public InvolvedPartyIdentificationType getInvolvedPartyIdentificationTypeID()
    {
        return this.involvedPartyIdentificationTypeID;
    }

    public InvolvedPartyXInvolvedPartyIdentificationType setInvolvedPartyIdentificationTypeID(InvolvedPartyIdentificationType involvedPartyIdentificationTypeID)
    {
        this.involvedPartyIdentificationTypeID = involvedPartyIdentificationTypeID;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        InvolvedPartyXInvolvedPartyIdentificationType that = (InvolvedPartyXInvolvedPartyIdentificationType) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public InvolvedParty getPrimary()
    {
        return getInvolvedPartyID();
    }

    @Override
    public InvolvedPartyIdentificationType getSecondary()
    {
        return getInvolvedPartyIdentificationTypeID();
    }

    /**
     * Stores the identification value in the configured protection format.
     * <p>
     * Accepts <b>plaintext</b> and writes <b>ciphertext</b> to the {@code Value} column. When the
     * legacy mode is active and {@code encrypt} is {@code false}, the value is stored verbatim.
     *
     * @param value the plaintext identification value
     *
     * @see #getValue()
     */
    @Override
    public void setValue(String value)
    {
        super.setValue(ColumnEncryption.encrypt(value, ColumnEncryption.IDENTIFICATION,
                ColumnEncryption.enterpriseWrites() && getEnterpriseID() != null ? getEnterpriseID().getId() : null));
    }

    /**
     * Returns the identification value as <b>plaintext</b>, reversing {@link #setValue(String)}.
     * <p>
     * Accepts legacy plaintext, legacy obfuscation and authenticated AES-GCM envelopes.
     * Strong-envelope failures propagate; they never fall back to plaintext or obfuscation.
     *
     * @return the plaintext identification value, or the stored value verbatim when it is not ciphertext
     */
    @Override
    public String getValue()
    {
        String stored = super.getValue();
        return ColumnEncryption.decrypt(stored, ColumnEncryption.IDENTIFICATION,
                stored != null && stored.startsWith("amenc:2:") && getEnterpriseID() != null
                        ? getEnterpriseID().getId() : null);
    }
}
