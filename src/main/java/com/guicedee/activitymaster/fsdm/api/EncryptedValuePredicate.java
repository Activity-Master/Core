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
        for (String prefix : ColumnEncryption.searchPrefixes(value, context, enterprise))
            matches.add(builder.like(column, prefix + "%"));
        Predicate any = builder.or(matches.toArray(Predicate[]::new));
        return operand == Operand.NotEquals ? builder.not(any) : any;
    }
}

