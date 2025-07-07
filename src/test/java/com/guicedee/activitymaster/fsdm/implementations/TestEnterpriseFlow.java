package com.guicedee.activitymaster.fsdm.implementations;

import com.guicedee.activitymaster.fsdm.ActivityMasterService;
import com.guicedee.activitymaster.fsdm.DefaultEnterprise;
import com.guicedee.activitymaster.fsdm.EnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.guicedee.client.IGuiceContext.get;

/**
 * Test class for the complete enterprise flow from creation to running internal updates.
 * This test verifies that an enterprise can be created and that internal updates can be run successfully.
 */
@ExtendWith(DefaultTestConfig.class)
public class TestEnterpriseFlow {
    private static final Logger log = Logger.getLogger(TestEnterpriseFlow.class.getName());
    private static final String TEST_ENTERPRISE_NAME = DefaultEnterprise.TestEnterprise.toString();
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    /**
     * Tests the creation of an enterprise.
     * This test verifies that an enterprise can be created successfully.
     */
    @Test
    public void testEnterpriseCreation() {
        System.setErr(System.out);
        log.log(Level.INFO, "Starting enterprise creation test");

        // Create the enterprise
        IEnterprise<?, ?> enterprise = get(EnterpriseService.class)
                .startNewEnterprise(TEST_ENTERPRISE_NAME, ADMIN_USERNAME, ADMIN_PASSWORD);

        // Verify that the enterprise was created
        log.log(Level.INFO, "Enterprise created: {0}", enterprise.getName());
    }

    /**
     * Tests running internal updates for an enterprise.
     * This test verifies that internal updates can be run successfully.
     */
    @Test
    public void testInternalUpdates() {
        System.setErr(System.out);
        log.log(Level.INFO, "Starting internal updates test");

        // Get the enterprise
        EnterpriseService enterpriseService = get(EnterpriseService.class);
        IEnterprise<?, ?> enterprise = enterpriseService.getEnterprise(TEST_ENTERPRISE_NAME);
        log.log(Level.INFO, "Enterprise found: {0}", enterprise.getName());

        // Get the ActivityMasterConfiguration
        ActivityMasterConfiguration config = get(ActivityMasterConfiguration.class);
        config.setApplicationEnterpriseName(TEST_ENTERPRISE_NAME);

        // Get the systems for the enterprise
        ISystemsService<?> systemsService = get(ISystemsService.class);
        ISystems<?, ?> systems = systemsService.getActivityMaster(enterprise);
        log.log(Level.INFO, "Systems found: {0}", systems.getName());

        // Get the security identity token
        UUID identityToken = get(ISystemsService.class).getSecurityIdentityToken(systems);
        log.log(Level.INFO, "Security identity token: {0}", identityToken);

        // Load systems for the enterprise
        ActivityMasterService activityMasterService = get(ActivityMasterService.class);
        activityMasterService.loadSystems(TEST_ENTERPRISE_NAME);
        log.log(Level.INFO, "Systems loaded");

        // Load updates for the enterprise
        int updatesCount = enterpriseService.loadUpdates(enterprise);
        log.log(Level.INFO, "Updates loaded: {0}", updatesCount);
    }

    /**
     * Tests the complete flow from enterprise creation to running internal updates.
     * This test verifies that an enterprise can be created and that internal updates can be run successfully.
     */
    @Test
    public void testCompleteFlow() {
        System.setErr(System.out);
        log.log(Level.INFO, "Starting complete flow test");

        // Create the enterprise
        EnterpriseService enterpriseService = get(EnterpriseService.class);
        IEnterprise<?, ?> enterprise = enterpriseService.startNewEnterprise(TEST_ENTERPRISE_NAME, ADMIN_USERNAME, ADMIN_PASSWORD);
        log.log(Level.INFO, "Enterprise created: {0}", enterprise.getName());

        // Get the ActivityMasterConfiguration
        ActivityMasterConfiguration config = get(ActivityMasterConfiguration.class);
        config.setApplicationEnterpriseName(TEST_ENTERPRISE_NAME);

        // Get the systems for the enterprise
        ISystemsService<?> systemsService = get(ISystemsService.class);
        ISystems<?, ?> systems = systemsService.getActivityMaster(enterprise);
        log.log(Level.INFO, "Systems found: {0}", systems.getName());

        // Get the security identity token
        UUID identityToken = get(ISystemsService.class).getSecurityIdentityToken(systems);
        log.log(Level.INFO, "Security identity token: {0}", identityToken);

        // Load systems for the enterprise
        ActivityMasterService activityMasterService = get(ActivityMasterService.class);
        activityMasterService.loadSystems(TEST_ENTERPRISE_NAME);
        log.log(Level.INFO, "Systems loaded");

        // Load updates for the enterprise
        int updatesCount = enterpriseService.loadUpdates(enterprise);
        log.log(Level.INFO, "Updates loaded: {0}", updatesCount);

        // Get all systems
        Set<IActivityMasterSystem<?>> allSystems = config.getAllSystems();
        log.log(Level.INFO, "All systems: {0}", allSystems.size());

        // Log the names of all systems
        for (IActivityMasterSystem<?> system : allSystems) {
            log.log(Level.INFO, "System: {0}", system.getSystemName());
        }
    }
}