package com.guicedee.activitymaster;

import com.guicedee.activitymaster.fsdm.plugins.AgeRating;
import com.guicedee.activitymaster.fsdm.plugins.PluginModels;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AgeRatingTest {
    private static final LocalDate TODAY = LocalDate.of(2026, 10, 1);

    @Test void allNeedsNoDateOfBirth() {
        assertTrue(AgeRating.parse(null).permits(null, TODAY));
        assertTrue(AgeRating.parse("All").permits(null, TODAY));
        assertFalse(AgeRating.parse("All").requiresDateOfBirth());
    }

    @Test void namedRatingsRequireDateOfBirthButNoMinimumAge() {
        for (String code : List.of(AgeRating.FAMILY_FRIENDLY, AgeRating.PARENTAL_GUIDANCE)) {
            AgeRating rating = AgeRating.parse(code);
            assertFalse(rating.permits(null, TODAY), code);
            assertTrue(rating.permits(TODAY.minusYears(5), TODAY), code);
        }
    }

    @Test void numericRatingsEnforceCompletedYears() {
        AgeRating adult = AgeRating.parse("18+");
        assertEquals(18, adult.minimumAge());
        assertFalse(adult.permits(null, TODAY));
        assertTrue(adult.permits(LocalDate.of(2008, 10, 1), TODAY));
        assertFalse(adult.permits(LocalDate.of(2008, 10, 2), TODAY));
        assertFalse(adult.permits(TODAY.plusDays(1), TODAY), "future date of birth is invalid");
    }

    @Test void rejectsUnknownCodes() {
        for (String code : List.of("18", "0+", "26+", "PG", "all", "100+"))
            assertThrows(IllegalArgumentException.class, () -> AgeRating.parse(code), code);
    }

    @Test void optionsIncludeNamedRatingsAndCountryOrDefaultAges() {
        assertEquals(List.of("All", "FamilyFriendly", "ParentalGuidance", "13+", "16+", "18+"), AgeRating.options(null));
        assertEquals(AgeRating.options(null), AgeRating.options("XX"));
        assertEquals(List.of("All", "FamilyFriendly", "ParentalGuidance", "12+", "15+", "18+"), AgeRating.options("gb"));
        assertEquals(List.of("All", "FamilyFriendly", "ParentalGuidance", "10+", "13+", "16+", "18+"), AgeRating.options("ZA"));
    }

    @Test void registrationDefaultsToAllAndNormalizes() {
        var registration = new PluginModels.Registration("n", "t", "d", "1", null, List.of(), Set.of());
        assertEquals("All", registration.ageRating());
        assertEquals("All", new PluginModels.Registration("n", "t", "d", "1", null, null, null, null).ageRating());
        assertThrows(IllegalArgumentException.class,
                () -> new PluginModels.Registration("n", "t", "d", "1", null, null, null, "R"));
    }
}
