package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.reactive.mutiny.Mutiny;
import org.hibernate.type.NumericBooleanConverter;

import java.io.Serial;

import static com.guicedee.client.IGuiceContext.*;

/**
 * @author Marc Magon
 * @since 08 Dec 2016
 */
@SuppressWarnings("unchecked")
@MappedSuperclass

public abstract class WarehouseSecurityTable<J extends WarehouseSecurityTable<J, Q, I>,
                                                Q extends QueryBuilderSecurities<Q, J, I>,
                                                I extends java.util.UUID>
    extends WarehouseSCDTable<J, Q, I, J>
    implements IWarehouseSecurityTable<J, Q, I>
{

  @Serial
  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(nullable = false,
      name = "CreateAllowed")
  @Convert(converter = NumericBooleanConverter.class)
  private boolean createAllowed;
  @Basic(optional = false)
  @NotNull
  @Column(nullable = false,
      name = "UpdateAllowed")
  @Convert(converter = NumericBooleanConverter.class)
  private boolean updateAllowed;
  @Basic(optional = false)
  @NotNull
  @Column(nullable = false,
      name = "DeleteAllowed")
  @Convert(converter = NumericBooleanConverter.class)
  private boolean deleteAllowed;
  @Basic(optional = false)
  @NotNull
  @Column(nullable = false,
      name = "ReadAllowed")
  @Convert(converter = NumericBooleanConverter.class)
  private boolean readAllowed;

  @JoinColumn(name = "SecurityTokenID",
      referencedColumnName = "SecurityTokenID",
      nullable = false)
  @ManyToOne(optional = false,
      fetch = FetchType.EAGER)
  private SecurityToken securityTokenID;

  //===========================================================================================================================


  public WarehouseSecurityTable()
  {

  }

  /**
   * Marks the row as removed active flag, does not delete the record from the db
   *
   * @return
   */
  @SuppressWarnings("UnusedReturnValue")
  public Uni<J> remove(Mutiny.Session session)
  {
    IActiveFlagService<?> service = get(IActiveFlagService.class);
    return (Uni) service.getDeletedFlag(session, getEnterpriseID())
                     .chain(flag -> {
                       setActiveFlagID(flag);
                       return session.merge(this);
                     });
  }

  @Override
  public @NotNull boolean isCreateAllowed()
  {
    return createAllowed;
  }

  @Override
  public @org.jspecify.annotations.NonNull J setCreateAllowed(@NotNull boolean createAllowed)
  {
    this.createAllowed = createAllowed;
    return (J) this;
  }

  @Override
  public @NotNull boolean isUpdateAllowed()
  {
    return updateAllowed;
  }

  @Override
  public @org.jspecify.annotations.NonNull J setUpdateAllowed(@NotNull boolean updateAllowed)
  {
    this.updateAllowed = updateAllowed;
    return (J) this;
  }

  @Override
  public @NotNull boolean isDeleteAllowed()
  {
    return deleteAllowed;
  }

  @Override
  public @org.jspecify.annotations.NonNull J setDeleteAllowed(@NotNull boolean deleteAllowed)
  {
    this.deleteAllowed = deleteAllowed;
    return (J) this;
  }

  @Override
  public @NotNull boolean isReadAllowed()
  {
    return readAllowed;
  }

  @Override
  public @org.jspecify.annotations.NonNull J setReadAllowed(@NotNull boolean readAllowed)
  {
    this.readAllowed = readAllowed;
    return (J) this;
  }

  @Override
  public SecurityToken getSecurityTokenID()
  {
    return securityTokenID;
  }

  @Override
  public @org.jspecify.annotations.NonNull J setSecurityTokenID(ISecurityToken<?, ?> securityTokenID)
  {
    this.securityTokenID = (SecurityToken) securityTokenID;
    return (J) this;
  }

  @Override
  public void configureSecurityEntity(J securityEntity)
  {

  }

}
