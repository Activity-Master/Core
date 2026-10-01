package com.guicedee.activitymaster.fsdm.api;

import com.entityassist.enumerations.Operand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** One predicate, ANDed by the caller with all existing security and business filters. */
public final class EncryptedValuePredicate
{
    /** Complete authenticated lookup header; shared verbatim with the PostgreSQL expression indexes. */
    public static final String LOOKUP_HEADER_PATTERN = "^amenc:[12]:[A-Za-z0-9_-]+:[0-9a-f]{64}:";
    private EncryptedValuePredicate() { }

    public static Predicate create(CriteriaBuilder builder, Expression<String> column,
                                   Operand operand, String value, String context)
    {
        return create(builder, column, operand, value, context, null);
    }

    public static Predicate create(CriteriaBuilder builder, Expression<String> column,
                                   Operand operand, String value, String context, UUID enterprise)
    {
        if (operand == Operand.Null) return builder.isNull(column);
        if (operand == Operand.NotNull) return builder.isNotNull(column);
        if (operand != Operand.Equals && operand != Operand.NotEquals)
            throw new IllegalArgumentException("Encrypted columns support only Equals, NotEquals, Null and NotNull");
        if (value == null)
            return operand == Operand.Equals ? builder.isNull(column) : builder.isNotNull(column);
        List<Predicate> matches = new ArrayList<>();
        matches.add(builder.equal(column, value));
        matches.add(builder.equal(column, ColumnEncryption.legacyObfuscatedValue(value)));
        Expression<String> lookupHeader = builder.coalesce(
                builder.function("regexp_substr", String.class, column, builder.literal(LOOKUP_HEADER_PATTERN)), "");
        for (String header : ColumnEncryption.searchPrefixes(value, context, enterprise))
            matches.add(builder.equal(lookupHeader, header));
        Predicate any = builder.or(matches.toArray(Predicate[]::new));
        return operand == Operand.NotEquals ? builder.not(any) : any;
    }
}

