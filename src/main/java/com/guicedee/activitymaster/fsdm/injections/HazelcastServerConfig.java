package com.guicedee.activitymaster.fsdm.injections;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastServerConfig;
import com.hazelcast.config.*;

public class HazelcastServerConfig
        implements IGuicedHazelcastServerConfig<HazelcastServerConfig> {
    @Override
    public Config buildConfig(Config config) {
        if (HazelcastProperties.isStartLocal())
        {
            config.getNetworkConfig()
                    .getJoin()
                    .getMulticastConfig()
                    .setEnabled(true);
        }
        config.getManagementCenterConfig()
                .setScriptingEnabled(true);
        
        config.getJetConfig().setEnabled(true);
        config.setIntegrityCheckerConfig(new IntegrityCheckerConfig().setEnabled(false));
        
        config.setClusterName(HazelcastProperties.getGroupName());
        config.setInstanceName(HazelcastProperties.getInstanceName());

        config.setMetricsConfig(new MetricsConfig());
        config.getMetricsConfig()
                .setEnabled(true)
                .setJmxConfig(new MetricsJmxConfig().setEnabled(true))
                .setManagementCenterConfig(new MetricsManagementCenterConfig().setEnabled(true)
                        .setRetentionSeconds(5))
                .setCollectionFrequencySeconds(5);

        return config;
    }
}
