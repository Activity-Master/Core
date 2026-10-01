package com.guicedee.activitymaster.fsdm.plugins;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Supplies the verified actor's date of birth and residential country for age restrictions.
 * Profiles Master provides the standard implementation. Without a provider, every rating other than
 * {@link AgeRating#ALL} is denied. Implementations run on the caller's session and must not swallow SQL failures.
 */
public interface IAgeProfileProvider {
    record AgeProfile(LocalDate dateOfBirth, String countryCode) {
        public static final AgeProfile EMPTY = new AgeProfile(null, null);
    }

    /** Returns the profile, or {@link AgeProfile#EMPTY} (never null) when no data is available. */
    Uni<AgeProfile> find(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID partyId);
}
