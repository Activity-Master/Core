package com.guicedee.activitymaster.test;

import com.guicedee.activitymaster.fsdm.db.ActivityMasterPoolConfiguration;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ActivityMasterPoolPolicyTest {
    Map<String,String> local() {return new HashMap<>(Map.of("ENVIRONMENT","test","FSDM_SSL_MODE","disable",
            "FSDM_DBSERVER","127.0.0.1","FSDM_PASSWORD","fixture-password"));}
    @Test void invalidPolicyFailsActualGuiceModuleConstruction() {
        String previous=(String)System.getProperties().get("FSDM_SSL_MODE");
        try {
            System.setProperty("FSDM_SSL_MODE","unsupported-fixture-mode");
            var failure=assertThrows(com.google.inject.CreationException.class,() ->
                    com.google.inject.Guice.createInjector(new com.guicedee.activitymaster.fsdm.db.ActivityMasterDBModule()));
            assertTrue(failure.getMessage().contains("Invalid ActivityMaster pool configuration"));
        } finally {
            if(previous==null)System.clearProperty("FSDM_SSL_MODE");else System.setProperty("FSDM_SSL_MODE",previous);
        }
    }
    @Test void tlsIsMandatoryByDefault() {
        assertThrows(IllegalArgumentException.class,() -> ActivityMasterPoolConfiguration.resolve(Map.of("FSDM_PASSWORD","fixture-password")::get));
    }
    @Test void plaintextRequiresExplicitLocalEnvironmentAndLiteralLoopback() {
        for(String environment:List.of("production","","dev")) {
            var s=local();s.put("ENVIRONMENT",environment);
            assertThrows(IllegalArgumentException.class,() -> ActivityMasterPoolConfiguration.resolve(s::get));
        }
        for(String host:List.of("localhost","10.0.0.1","example.invalid")) {
            var s=local();s.put("FSDM_DBSERVER",host);
            assertThrows(IllegalArgumentException.class,() -> ActivityMasterPoolConfiguration.resolve(s::get));
        }
        assertNotNull(ActivityMasterPoolConfiguration.resolve(local()::get));
    }
    @Test void unsupportedTlsModesNeverDowngrade() {
        for(String mode:List.of("prefer","require","verify-ca","true","VERIFY_FULL")) {
            var s=local();s.put("FSDM_SSL_MODE",mode);
            assertThrows(IllegalArgumentException.class,() -> ActivityMasterPoolConfiguration.resolve(s::get));
        }
    }
    @Test void unboundedOrMalformedCapacityAndTimeoutsAreRejected() {
        for(String name:List.of("FSDM_POOL_MAX_SIZE","FSDM_POOL_ACQUIRE_TIMEOUT_MS","FSDM_CONNECT_TIMEOUT_MS",
                "FSDM_STATEMENT_TIMEOUT_MS","FSDM_LOCK_TIMEOUT_MS")) {
            for(String value:List.of("0","-1","1000000"," 10","1.5")) {
                var s=local();s.put(name,value);
                assertThrows(IllegalArgumentException.class,() -> ActivityMasterPoolConfiguration.resolve(s::get));
            }
        }
        var s=local();s.put("FSDM_POOL_MAX_WAIT_QUEUE","-1");
        assertThrows(IllegalArgumentException.class,() -> ActivityMasterPoolConfiguration.resolve(s::get));
    }
    @Test void missingPasswordFailsWithoutEchoingConfiguration() {
        var s=local();s.remove("FSDM_PASSWORD");
        var failure=assertThrows(IllegalArgumentException.class,() -> ActivityMasterPoolConfiguration.resolve(s::get));
        assertNull(failure.getCause());assertEquals("Invalid ActivityMaster pool configuration",failure.getMessage());
        assertFalse(ActivityMasterPoolConfiguration.resolve(local()::get).toString().contains("fixture-password"));
    }
}
