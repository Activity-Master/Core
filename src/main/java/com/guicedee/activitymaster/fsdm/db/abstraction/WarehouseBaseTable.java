package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderDefault;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDate;
import java.util.UUID;

@MappedSuperclass()
public abstract class WarehouseBaseTable<J extends WarehouseBaseTable<J, Q, I>,
                                            Q extends QueryBuilderDefault<Q, J, I>, I extends java.util.UUID>
    extends SCDEntity<J, Q, I>
    implements IWarehouseBaseTable<J, Q, I>
{
  @Getter
  @Setter
  @Transient
  private int yearRange = 10;

  @Column
  @Getter
  @Setter
  private LocalDate warehouseFromDate;

  @Serial
  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unchecked")
  public WarehouseBaseTable()
  {
    setId((I) UUID.randomUUID());
    setWarehouseFromDate(getEffectiveFromDate().toLocalDate());
  }

  @Override
  public @NotNull boolean isFake()
  {
    return getId() == null;
  }
}
