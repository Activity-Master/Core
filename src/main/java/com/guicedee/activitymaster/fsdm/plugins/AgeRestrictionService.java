package com.guicedee.activitymaster.fsdm.plugins;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.plugins.IAgeProfileProvider.AgeProfile;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Age restriction decisions for the verified actor, based on the profile date of birth and residential country.
 * Fails closed: an unavailable date of birth denies every rating other than {@link AgeRating#ALL}.
 */
public final class AgeRestrictionService {
    private static volatile List<Class<? extends IAgeProfileProvider>> providerTypes;

    /** Merges the first date of birth and first country reported by the discovered providers. */
    public Uni<AgeProfile> profile(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID partyId) {
        Objects.requireNonNull(session, "session");
        if (enterprise == null || partyId == null) return Uni.createFrom().item(AgeProfile.EMPTY);
        Uni<AgeProfile> merged = Uni.createFrom().item(AgeProfile.EMPTY);
        for (IAgeProfileProvider provider : providers())
            merged = merged.chain(current -> current.dateOfBirth() != null && current.countryCode() != null
                    ? Uni.createFrom().item(current)
                    : provider.find(session, enterprise, partyId).map(found -> found == null ? current : new AgeProfile(
                            current.dateOfBirth() != null ? current.dateOfBirth() : found.dateOfBirth(),
                            current.countryCode() != null ? current.countryCode() : normalizeCountry(found.countryCode()))));
        return merged;
    }

    /** All, Family Friendly, Parental Guidance and the numeric ratings for the actor's profile country. */
    public Uni<List<String>> options(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID partyId) {
        return profile(session, enterprise, partyId).map(profile -> AgeRating.options(profile.countryCode()));
    }

    /** Validates a rating assigned by the actor against the options for their profile country. */
    public Uni<AgeRating> assignable(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID partyId, String code) {
        AgeRating rating;
        try { rating = AgeRating.parse(code); }
        catch (IllegalArgumentException invalid) { return Uni.createFrom().failure(invalid); }
        if (rating.minimumAge() == 0) return Uni.createFrom().item(rating);
        return options(session, enterprise, partyId).map(options -> {
            if (!options.contains(rating.code()))
                throw new IllegalArgumentException("Age rating " + rating.code() + " is not available; choose one of " + options);
            return rating;
        });
    }

    /** Succeeds when the actor satisfies the rating; otherwise fails with a SecurityException. */
    public Uni<Void> require(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID partyId, AgeRating rating) {
        if (rating == null || !rating.requiresDateOfBirth()) return Uni.createFrom().voidItem();
        return profile(session, enterprise, partyId).chain(profile -> {
            if (profile.dateOfBirth() == null)
                return Uni.createFrom().failure(new SecurityException("Age restricted: a profile date of birth is required"));
            return rating.permits(profile.dateOfBirth(), today()) ? Uni.createFrom().voidItem()
                    : Uni.createFrom().failure(new SecurityException("Age restricted: " + rating.code()));
        });
    }

    public static LocalDate today() { return LocalDate.now(ZoneOffset.UTC); }

    /** Completed whole years, or null without a valid date of birth. */
    public static Integer age(AgeProfile profile) {
        if (profile == null || profile.dateOfBirth() == null || profile.dateOfBirth().isAfter(today())) return null;
        return java.time.Period.between(profile.dateOfBirth(), today()).getYears();
    }

    private static String normalizeCountry(String code) {
        return code != null && code.matches("[A-Za-z]{2}") ? code.toUpperCase(Locale.ROOT) : null;
    }

    private static List<IAgeProfileProvider> providers() {
        List<Class<? extends IAgeProfileProvider>> types = providerTypes;
        if (types == null) {
            List<Class<? extends IAgeProfileProvider>> found = new ArrayList<>();
            ServiceLoader.load(IAgeProfileProvider.class).stream().forEach(provider -> found.add(provider.type()));
            found.sort(Comparator.comparing(Class::getName));
            providerTypes = types = List.copyOf(found);
        }
        List<IAgeProfileProvider> instances = new ArrayList<>();
        for (var type : types) instances.add(IGuiceContext.get(type));
        return instances;
    }
}
