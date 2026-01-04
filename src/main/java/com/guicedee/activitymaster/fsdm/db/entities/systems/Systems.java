package com.guicedee.activitymaster.fsdm.db.entities.systems;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseRelationshipClassificationTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.capabilities.contains.IContainsNameAndDescription;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.systems.builders.SystemsQueryBuilder;
import com.guicedee.activitymaster.fsdm.systems.ActiveFlagSystem;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.reactive.mutiny.Mutiny;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static com.guicedee.client.IGuiceContext.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "Systems",
        schema = "dbo")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
@Log4j2
public class Systems
        extends WarehouseCoreTable<Systems, SystemsQueryBuilder, UUID, SystemsSecurityToken>
        implements ISystems<Systems, SystemsQueryBuilder>,
                           IContainsNameAndDescription<Systems>
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(nullable = false,
            name = "SystemID")
    @JsonValue

    private java.util.UUID id;
    @Basic(optional = false,
            fetch = FetchType.EAGER)
    @NotNull
    @Size(min = 1,
            max = 150)
    @Column(nullable = false,
            length = 150,
            name = "SystemName")
    private String name;
    @Basic(optional = false,
            fetch = FetchType.EAGER)
    @NotNull
    @Size()
    @Column(nullable = false,
            length = 250,
            name = "SystemDesc")
    private String description;
    @Basic(optional = false,
            fetch = FetchType.EAGER)
    @NotNull
    @Size(min = 1,
            max = 250)
    @Column(nullable = false,
            length = 250,
            name = "SystemHistoryName")
    private String systemHistoryName;

    @JoinColumn(name = "EnterpriseID",
            referencedColumnName = "EnterpriseID",
            nullable = false)
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Enterprise enterpriseID;

    @JoinColumn(name = "ActiveFlagID",
            referencedColumnName = "ActiveFlagID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private ActiveFlag activeFlagID;

    @OneToMany(
            mappedBy = "systemID",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<SystemsSecurityToken> securities;

    public Systems(UUID systemID, String systemName, String systemDesc, String systemHistoryName)
    {
        id = systemID;
        name = systemName;
        description = systemDesc;
        this.systemHistoryName = systemHistoryName;
    }

    @Override
    public void configureSecurityEntity(SystemsSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    @Override
    public String toString()
    {
        return getName();
    }


    public void configureForClassification(SystemsXClassification classificationLink, ISystems<?, ?> system)
    {
        classificationLink.setSystemID(this);
    }

    /**
     * Marks the entity as removed by setting its active flag to deleted.
     * This method performs actions and returns a Uni that completes when the removal is done.
     * It returns the result of calling update().
     *
     * @return A Uni that completes when the removal is done
     */
    public Uni<Systems> remove(Mutiny.Session  session)
    {
        IActiveFlagService<?> service = IGuiceContext.get(IActiveFlagService.class);
        return service.getDeletedFlag(session, getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(session, getEnterpriseID()).await().atMost(java.time.Duration.ofSeconds(50)))
                       .chain(deletedFlag -> {
                           setActiveFlagID((ActiveFlag) deletedFlag);
                           return session.persist(this).replaceWith(Uni.createFrom().item(this));
                       })
                       .onItem()
                       .invoke(result -> {
                           System.out.println("✅ System " + getName() + " successfully marked as removed");
                       })
                       .onFailure()
                       .invoke(error -> {
                           System.err.println("❌ Failed to remove system " + getName() + ": " + error.getMessage());
                       });
    }


    public Uni<Systems> archive(Mutiny.Session session)
    {
        IActiveFlagService<?> service = IGuiceContext.get(IActiveFlagService.class);
        return service.getArchivedFlag(session, getEnterpriseID(), get(ActiveFlagSystem.class).getSystemToken(session, getEnterpriseID()).await().atMost(java.time.Duration.ofSeconds(50)))
                       .chain(archivedFlag -> {
                           setActiveFlagID((ActiveFlag) archivedFlag);
                           return session.merge(this);
                       })
                       .onItem()
                       .invoke(result -> {
                           log.debug("✅ System {} successfully archived", getName());
                       })
                       .onFailure()
                       .invoke(error -> {
                           log.error("❌ Failed to archive system {}: {}", getName(), error.getMessage(), error);
                       });
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
        Systems systems = (Systems) o;
        return Objects.equals(getName(), systems.getName());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getName());
    }


    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Systems setName(String name)
    {
        this.name = name;
        return this;
    }

    @Override
    public @NotNull @Size() String getDescription()
    {
        return description;
    }

    @Override
    public Systems setDescription(@NotNull @Size() String description)
    {
        this.description = description;
        return this;
    }

    public String getSystemHistoryName()
    {
        return systemHistoryName;
    }

    public Systems setSystemHistoryName(@NotNull String systemHistoryName)
    {
        this.systemHistoryName = systemHistoryName;
        return this;
    }

    @Override
    public Enterprise getEnterpriseID()
    {
        return enterpriseID;
    }

    public Systems setEnterpriseID(IEnterprise<?, ?> enterpriseID)
    {
        this.enterpriseID = (Enterprise) enterpriseID;
        return this;
    }

    public ActiveFlag getActiveFlagID()
    {
        return activeFlagID;
    }

    @Override
    public Systems setActiveFlagID(IActiveFlag<?, ?> activeFlagID)
    {
        return setActiveFlagID((ActiveFlag) activeFlagID);
    }

    public Systems setActiveFlagID(ActiveFlag activeFlagID)
    {
        this.activeFlagID = activeFlagID;
        return this;
    }

    @Override
    public void configureForClassification(Mutiny.Session session, IWarehouseRelationshipClassificationTable linkTable, IClassification<?, ?> classificationValue, ISystems<?, ?> system)
    {
        ((SystemsXClassification) linkTable)
                .setSystemID(this);
    }
}
