package com.guicedee.activitymaster.fsdm.plugins;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Content age rating for plugins and marketplace listings.
 * <p>
 * {@link #ALL} needs no date of birth. {@link #FAMILY_FRIENDLY} and {@link #PARENTAL_GUIDANCE} require a
 * date of birth but no minimum age. Numeric ratings ({@code "18+"}) require a date of birth and that minimum age.
 */
public record AgeRating(String code, int minimumAge) {
    public static final String ALL = "All";
    public static final String FAMILY_FRIENDLY = "FamilyFriendly";
    public static final String PARENTAL_GUIDANCE = "ParentalGuidance";
    public static final List<Integer> DEFAULT_AGES = List.of(13, 16, 18);
    private static final Pattern NUMERIC = Pattern.compile("([1-9][0-9]?)\\+");

    /** Country-specific numeric rating sets, keyed by two-letter Geography country code. */
    private static final Map<String, List<Integer>> COUNTRY_AGES = Map.ofEntries(
            Map.entry("ZA", List.of(10, 13, 16, 18)),
            Map.entry("GB", List.of(12, 15, 18)),
            Map.entry("IE", List.of(12, 15, 16, 18)),
            Map.entry("US", List.of(13, 17, 18)),
            Map.entry("CA", List.of(14, 18)),
            Map.entry("AU", List.of(15, 18)),
            Map.entry("NZ", List.of(13, 15, 16, 18)),
            Map.entry("DE", List.of(6, 12, 16, 18)),
            Map.entry("FR", List.of(10, 12, 16, 18)),
            Map.entry("NL", List.of(6, 9, 12, 14, 16, 18)),
            Map.entry("BE", List.of(6, 9, 12, 14, 16, 18)),
            Map.entry("BR", List.of(10, 12, 14, 16, 18)),
            Map.entry("IN", List.of(7, 13, 16, 18)),
            Map.entry("JP", List.of(12, 15, 17, 18)),
            Map.entry("KR", List.of(12, 15, 18)));

    public AgeRating {
        Objects.requireNonNull(code, "code");
    }

    public static AgeRating all() { return new AgeRating(ALL, 0); }

    /** Parses a stored or requested code; null/blank is {@link #ALL}. Rejects unknown codes. */
    public static AgeRating parse(String code) {
        if (code == null || code.isBlank() || ALL.equals(code)) return all();
        if (FAMILY_FRIENDLY.equals(code) || PARENTAL_GUIDANCE.equals(code)) return new AgeRating(code, 0);
        var matcher = NUMERIC.matcher(code);
        if (matcher.matches()) {
            int age = Integer.parseInt(matcher.group(1));
            if (age <= 25) return new AgeRating(code, age);
        }
        throw new IllegalArgumentException("Unknown age rating: " + code);
    }

    public static String numeric(int age) { return age + "+"; }

    /** Numeric ages for a two-letter country code, or the default set when absent or unknown. */
    public static List<Integer> ages(String countryCode) {
        if (countryCode == null) return DEFAULT_AGES;
        return COUNTRY_AGES.getOrDefault(countryCode.toUpperCase(Locale.ROOT), DEFAULT_AGES);
    }

    /** All, Family Friendly and Parental Guidance, followed by the country (or default) numeric ratings. */
    public static List<String> options(String countryCode) {
        List<String> options = new ArrayList<>(List.of(ALL, FAMILY_FRIENDLY, PARENTAL_GUIDANCE));
        for (int age : ages(countryCode)) options.add(numeric(age));
        return List.copyOf(options);
    }

    public boolean requiresDateOfBirth() { return !ALL.equals(code); }

    /** Pure decision: missing date of birth denies everything except {@link #ALL}. */
    public boolean permits(LocalDate dateOfBirth, LocalDate today) {
        if (!requiresDateOfBirth()) return true;
        if (dateOfBirth == null || today == null || dateOfBirth.isAfter(today)) return false;
        return Period.between(dateOfBirth, today).getYears() >= minimumAge;
    }
}
