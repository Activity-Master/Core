package com.guicedee.activitymaster.fsdm.db.abstraction.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * A transport-neutral description of a warehouse query.
 *
 * <p>Built from any source (GraphQL, REST, internal) and applied to a
 * {@link QueryBuilderSCD} via {@code QueryBuilderSCD.applyQuerySpec(spec)} so that
 * the EntityAssist {@code QueryBuilder} is constructed dynamically and reused consistently across
 * all seven FSDM domains.</p>
 */
@Getter
@Setter
@Accessors(chain = true)
public class WarehouseQuerySpec
{
    /** Enterprise scope applied via {@code withEnterprise(...)}. */
    private IEnterprise<?, ?> enterprise;

    /** Dynamic filter predicates applied via dot-notation {@code where(path, operand, value)}. */
    private List<WarehouseQueryFilter> filters = new ArrayList<>();

    /** Optional attribute path to order by (top level attribute name). */
    private String orderBy;

    /** When {@code true} the {@link #orderBy} is descending, otherwise ascending. */
    private boolean descending;

    /** Restrict to rows whose {@code activeFlagID} is within the active range and up. */
    private boolean activeOnly = true;

    /** Restrict to rows whose effective date range contains "now". */
    private boolean inDateRange = true;

    /** Zero-based first result for pagination (nullable). */
    private Integer first;

    /** Maximum number of rows to return (nullable). */
    private Integer max;

    public WarehouseQuerySpec addFilter(WarehouseQueryFilter filter)
    {
        this.filters.add(filter);
        return this;
    }
}


