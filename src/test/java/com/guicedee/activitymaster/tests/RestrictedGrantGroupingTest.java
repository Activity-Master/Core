package com.guicedee.activitymaster.tests;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedPartyXInvolvedPartyIdentificationType;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RestrictedGrantGroupingTest {
    private static final class Identification extends InvolvedPartyXInvolvedPartyIdentificationType {
        boolean canonical;
        @Override public Uni<Long> createScopeRestrictedSecurity(Mutiny.StatelessSession session, ISystems<?, ?> system,
                IEnterprise<?, ?> enterprise, IActiveFlag<?, ?> flag, Map<String, ISecurityToken<?, ?>> groups,
                ISecurityToken<?, ?> scope, UUID... identity) {
            canonical = true;
            return Uni.createFrom().item(5L);
        }
    }
    private static final class OtherRow extends Classification {
        boolean canonical;
        @Override public Uni<Long> createScopeRestrictedSecurity(Mutiny.StatelessSession session, ISystems<?, ?> system,
                IEnterprise<?, ?> enterprise, IActiveFlag<?, ?> flag, Map<String, ISecurityToken<?, ?>> groups,
                ISecurityToken<?, ?> scope, UUID... identity) {
            canonical = true;
            return Uni.createFrom().item(4L);
        }
    }
    @Test void scopedIdentificationUsesCanonicalPath() {
        var row = new Identification();
        var scope = mock(ISecurityToken.class);
        assertEquals(5L, row.createScopeRestrictedSecurityGrouped(null, null, null, null, Map.of(), scope)
                .await().atMost(Duration.ofSeconds(1)));
        assertTrue(row.canonical);
    }
    @Test void unsupportedRowUsesCanonicalPath() {
        var row = new OtherRow();
        assertEquals(4L, row.createScopeRestrictedSecurityGrouped(null, null, null, null, Map.of(), null)
                .await().atMost(Duration.ofSeconds(1)));
        assertTrue(row.canonical);
    }
    @Test void eligibleRowRejectsMissingRequiredFoldersBeforeWriting() {
        var row = new Identification();
        assertThrows(IllegalStateException.class, () -> row.createScopeRestrictedSecurityGrouped(null, null, null, null, Map.of(), null)
                .await().atMost(Duration.ofSeconds(1)));
        assertFalse(row.canonical);
    }
}
