package com.guicedee.activitymaster.fsdm.injections;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
import com.hazelcast.client.config.*;
import com.hazelcast.config.ListenerConfig;
import com.hazelcast.config.MetricsJmxConfig;
import com.hazelcast.config.SocketInterceptorConfig;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;

public class HazelcastClientConfig implements IGuicedHazelcastClientConfig<HazelcastClientConfig>
{

    @Override
    public ClientConfig buildConfig(ClientConfig config)
    {
        // Network and operation retry settings
        config.getNetworkConfig()
                .setRedoOperation(true)
                .setSmartRouting(true);

        config.getConnectionStrategyConfig()
                .setAsyncStart(true)
                .setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ASYNC);

        // Increase operation timeout
        config.setProperty("hazelcast.client.operation.timeout", "0"); // Infinite timeout

        // Heartbeat settings
        config.setProperty("hazelcast.client.heartbeat.interval", "5000"); // Every 5 seconds
        config.setProperty("hazelcast.client.heartbeat.timeout", "60000"); // Timeout = 60 seconds

        // Add lifecycle listener for monitoring
        config.addListenerConfig(new ListenerConfig(new LifecycleListener() {
            @Override
            public void stateChanged(LifecycleEvent event)
            {
                if (event.getState() == LifecycleEvent.LifecycleState.CLIENT_DISCONNECTED) {
                    System.out.println("Hazelcast client disconnected.");
                }
                if (event.getState() == LifecycleEvent.LifecycleState.CLIENT_CONNECTED) {
                    System.out.println("Hazelcast client reconnected.");
                }
            }
        }));

        // JMX Metrics Configuration
        config.setMetricsConfig(new ClientMetricsConfig());
        config.getMetricsConfig()
                .setEnabled(true)
                .setJmxConfig(new MetricsJmxConfig().setEnabled(true))
                .setCollectionFrequencySeconds(5);

        // Address configuration
        String address = HazelcastProperties.getAddress();
        if (address.contains(":"))
        {
            String port = address.substring(address.indexOf(":") + 1);
            config.getNetworkConfig()
                    .addOutboundPort(Integer.parseInt(port));
        }
        return config;
    }
}
