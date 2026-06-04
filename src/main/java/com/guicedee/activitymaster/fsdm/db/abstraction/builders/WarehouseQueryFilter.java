package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.entityassist.enumerations.Operand;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * A single dynamic filter predicate.
 *
 * <p>{@link #path} is an EntityAssist attribute path (dot notation supported for relationship
 * traversal, e.g. {@code "activeFlagID.name"}). {@link #operand} is the comparison operator.
 * Use {@link #value} for single-valued operands and {@link #values} for list operands
 * ({@code InList} / {@code NotInList}).</p>
 */
@Getter
@Setter
@Accessors(chain = true)
public class WarehouseQueryFilter
{
    private String path;
    private Operand operand = Operand.Equals;
    private String value;
    private List<String> values;

    public WarehouseQueryFilter()
    {
    }

    public WarehouseQueryFilter(String path, Operand operand, String value)
    {
        this.path = path;
        this.operand = operand;
        this.value = value;
    }
}

