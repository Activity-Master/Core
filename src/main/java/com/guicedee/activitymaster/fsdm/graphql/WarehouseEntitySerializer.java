package com.guicedee.activitymaster.fsdm.graphql;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;

import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reflectively serialises the <em>scalar</em> columns of a warehouse entity into a plain
 * {@link Map} suitable for the GraphQL {@code JSON} scalar.
 *
 * <p>FSDM entities annotate their id with {@code @JsonValue}, which makes Jackson collapse the
 * whole entity to its UUID. To expose real column data we therefore read the scalar fields
 * directly. Relationship fields (other warehouse entities and collections) are skipped to avoid
 * triggering lazy initialisation outside of the active session and to keep the payload flat.</p>
 */
public final class WarehouseEntitySerializer
{
    private static final Logger log = Logger.getLogger(WarehouseEntitySerializer.class.getName());

    private WarehouseEntitySerializer()
    {
    }

    /**
     * Converts a single entity into a flat {@code name -> value} map of its scalar columns.
     *
     * @param entity the entity (may be {@code null})
     * @return the serialised map, never {@code null}
     */
    public static Map<String, Object> toMap(Object entity)
    {
        Map<String, Object> result = new LinkedHashMap<>();
        if (entity == null)
        {
            return result;
        }
        Class<?> type = entity.getClass();
        while (type != null && type != Object.class)
        {
            for (Field field : type.getDeclaredFields())
            {
                if (field.isSynthetic() || java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                {
                    continue;
                }
                if (!isScalar(field.getType()))
                {
                    continue;
                }
                if (result.containsKey(field.getName()))
                {
                    continue;
                }
                try
                {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if (value != null)
                    {
                        result.put(field.getName(), normalise(value));
                    }
                }
                catch (Throwable t)
                {
                    log.log(Level.FINER, "Skipping unreadable field " + field.getName(), t);
                }
            }
            type = type.getSuperclass();
        }
        return result;
    }

    private static boolean isScalar(Class<?> fieldType)
    {
        if (fieldType.isPrimitive() || fieldType.isEnum())
        {
            return true;
        }
        if (Collection.class.isAssignableFrom(fieldType) || Map.class.isAssignableFrom(fieldType))
        {
            return false;
        }
        if (WarehouseBaseTable.class.isAssignableFrom(fieldType))
        {
            return false;
        }
        return CharSequence.class.isAssignableFrom(fieldType)
                || Number.class.isAssignableFrom(fieldType)
                || Boolean.class.isAssignableFrom(fieldType)
                || Character.class.isAssignableFrom(fieldType)
                || UUID.class.isAssignableFrom(fieldType)
                || Temporal.class.isAssignableFrom(fieldType)
                || java.util.Date.class.isAssignableFrom(fieldType);
    }

    private static Object normalise(Object value)
    {
        if (value instanceof Number || value instanceof Boolean)
        {
            return value;
        }
        return value.toString();
    }
}

