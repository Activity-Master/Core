package com.guicedee.activitymaster.fsdm.encryption;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import java.util.List;
import java.util.UUID;

/** PostgreSQL store. Uses the caller's session/transaction; never opens a nested unit of work. */
public class EnterpriseKeyStore
{
    public Uni<List<EnterpriseDataKey>> load(Mutiny.Session session, UUID enterprise)
    {
        return session.createNativeQuery("select keyid, keyreference, wrappedkey, active from security.enterpriseencryptionkey where enterpriseid = :enterprise", Object[].class)
                .setParameter("enterprise", enterprise).getResultList()
                .map(rows -> rows.stream().map(row -> new EnterpriseDataKey((String) row[0], enterprise,
                        (String) row[1], (String) row[2], (Boolean) row[3])).toList());
    }

    public Uni<Void> deactivate(Mutiny.Session session, UUID enterprise)
    {
        return session.createNativeQuery("update security.enterpriseencryptionkey set active = false where enterpriseid = :enterprise and active = true")
                .setParameter("enterprise", enterprise).executeUpdate().replaceWithVoid();
    }

    public Uni<Void> insert(Mutiny.Session session, EnterpriseDataKey row)
    {
        return session.createNativeQuery("insert into security.enterpriseencryptionkey (keyid, enterpriseid, keyreference, wrappedkey, active) values (:id, :enterprise, :reference, :wrapped, true)")
                .setParameter("id", row.getId()).setParameter("enterprise", row.getEnterpriseId())
                .setParameter("reference", row.getKeyReference()).setParameter("wrapped", row.getWrappedKey())
                .executeUpdate().replaceWithVoid();
    }
}
