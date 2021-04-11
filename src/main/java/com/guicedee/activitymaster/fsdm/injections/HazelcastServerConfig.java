package com.guicedee.activitymaster.fsdm.injections;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastServerConfig;
import com.guicedee.guicedinjection.GuiceContext;
import com.hazelcast.client.cache.impl.HazelcastClientCacheManager;
import com.hazelcast.config.Config;
import com.hazelcast.config.MetricsConfig;
import com.hazelcast.config.MetricsJmxConfig;
import com.hazelcast.config.MetricsManagementCenterConfig;

import jakarta.cache.CacheManager;

public class HazelcastServerConfig
        implements IGuicedHazelcastServerConfig<HazelcastServerConfig> {
    @Override
    public Config buildConfig(Config config) {
        if (HazelcastProperties.isStartLocal())
        {
            config.getNetworkConfig()
                    .getJoin()
                    .getMulticastConfig()
                    .setEnabled(false);
        }
        config.getManagementCenterConfig()
                .setScriptingEnabled(false);
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
