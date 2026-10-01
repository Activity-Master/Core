package com.guicedee.activitymaster.fsdm.plugins;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Host-authenticated identities and catalogue data. Request IDs never establish authority. */
public final class PluginModels {
    private PluginModels() { }

    public record Identity(UUID partyId, UUID enterpriseId, UUID identityToken) {
        public Identity {
            Objects.requireNonNull(partyId, "partyId");
            Objects.requireNonNull(enterpriseId, "enterpriseId");
            Objects.requireNonNull(identityToken, "identityToken");
        }
        public UUID[] tokens() { return new UUID[]{identityToken}; }
    }

    /** Bound by the authenticated host to the initiating extension and authorized installation party. */
    public record Invocation(UUID pluginId, UUID installationPartyId) {
        public Invocation {
            Objects.requireNonNull(pluginId, "pluginId");
            Objects.requireNonNull(installationPartyId, "installationPartyId");
        }
    }

    /** {@code ageRating} is an {@link AgeRating} code; null means {@link AgeRating#ALL}. */
    public record Registration(String name, String title, String description, String version,
                               UUID icon, List<UUID> screenshots, Set<UUID> systems, String ageRating) {
        public Registration {
            name = text(name, 150, "name");
            title = text(title, 150, "title");
            description = text(description, 250, "description");
            version = text(version, 150, "version");
            screenshots = screenshots == null ? List.of() : List.copyOf(screenshots);
            systems = systems == null ? Set.of() : Set.copyOf(systems);
            if (screenshots.size() > 20 || systems.size() > 100)
                throw new IllegalArgumentException("At most 20 screenshots and 100 systems per plugin");
            ageRating = AgeRating.parse(ageRating).code();
        }
        public Registration(String name, String title, String description, String version,
                            UUID icon, List<UUID> screenshots, Set<UUID> systems) {
            this(name, title, description, version, icon, screenshots, systems, AgeRating.ALL);
        }
    }

    public record Plugin(UUID id, String name, String title, String description, String version,
                         UUID icon, List<UUID> screenshots, Set<UUID> systems, String ageRating) {
        public Plugin {
            screenshots = List.copyOf(screenshots);
            systems = Set.copyOf(systems);
            ageRating = AgeRating.parse(ageRating).code();
        }
        public Plugin(UUID id, String name, String title, String description, String version,
                      UUID icon, List<UUID> screenshots, Set<UUID> systems) {
            this(id, name, title, description, version, icon, screenshots, systems, AgeRating.ALL);
        }
    }
    public record Installation(UUID id, UUID pluginId, UUID partyId) { }

    private static String text(String value, int max, String field) {
        if (value == null || value.isBlank() || value.length() > max || value.indexOf('\0') >= 0)
            throw new IllegalArgumentException("Plugin " + field + " must be 1.." + max + " characters");
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (Character.isHighSurrogate(c)) {
                if (++i >= value.length() || !Character.isLowSurrogate(value.charAt(i)))
                    throw new IllegalArgumentException("Invalid Unicode in plugin " + field);
            } else if (Character.isLowSurrogate(c)) {
                throw new IllegalArgumentException("Invalid Unicode in plugin " + field);
            }
        }
        return value;
    }
}
