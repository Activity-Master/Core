package com.guicedee.activitymaster.fsdm.injections;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
import com.hazelcast.client.config.*;
import com.hazelcast.config.MetricsJmxConfig;

import static com.guicedee.guicedinjection.json.StaticStrings.*;

public class HazelcastClientConfig implements IGuicedHazelcastClientConfig<HazelcastClientConfig> {

    @Override
    public ClientConfig buildConfig(ClientConfig config) {
        config.getNetworkConfig()
                .addAddress(HazelcastProperties.getAddress())
                .setRedoOperation(true)
                .setSmartRouting(true);

        config.setClusterName(HazelcastProperties.getGroupName());
        config.setInstanceName(HazelcastProperties.getInstanceName());
    
        config.getConnectionStrategyConfig()
                    .setAsyncStart(true)
                    .setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ASYNC);

        config.setMetricsConfig(new ClientMetricsConfig());
        config.getMetricsConfig()
                .setEnabled(true)
                .setJmxConfig(new MetricsJmxConfig().setEnabled(true))
                .setCollectionFrequencySeconds(5);

        String address = HazelcastProperties.getAddress();
        if (address.contains(STRING_DOUBLE_COLON))
        {
            String port = address.substring(address.indexOf(STRING_DOUBLE_COLON) + 1);
            config.getNetworkConfig()
                    .addOutboundPort(Integer.parseInt(port));
        }

        System.setProperty("client.address", HazelcastProperties.getAddress());

        return config;
    }
}
