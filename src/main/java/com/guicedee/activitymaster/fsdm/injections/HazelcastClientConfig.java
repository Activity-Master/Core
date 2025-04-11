package com.guicedee.activitymaster.fsdm.injections;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedhazelcast.services.IGuicedHazelcastClientConfig;
import com.hazelcast.client.config.*;
import com.hazelcast.config.*;
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
                .setAsyncStart(false)
                .setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ON);

        // Heartbeat settings
        config.setProperty("hazelcast.client.heartbeat.interval", "5000"); // Every 5 seconds
        config.setProperty("hazelcast.client.heartbeat.timeout", "120000"); // Timeout = 60 seconds
        config.setProperty("hazelcast.client.operation.timeout", "300000");

        // Configure Near Cache
        NearCacheConfig nearCacheConfig = new NearCacheConfig("FarmLookup")
                .setInMemoryFormat(InMemoryFormat.OBJECT)  // Can also use InMemoryFormat.BINARY
                .setInvalidateOnChange(true)  // Keep in sync with the cluster
                .setTimeToLiveSeconds(3600)   // Expire entries after 1 hour
                .setMaxIdleSeconds(900)       // Max idle time of 15 minutes
                .setEvictionConfig(new EvictionConfig().setSize(1000)         // Limit items in Near Cache to 1000 entries
                        .setMaxSizePolicy(MaxSizePolicy.ENTRY_COUNT)
                        .setEvictionPolicy(EvictionPolicy.LRU));  // Evict based on LRU
        config.addNearCacheConfig(nearCacheConfig);

        // Add lifecycle listener for monitoring
        config.addListenerConfig(new ListenerConfig(new LifecycleListener()
        {
            @Override
            public void stateChanged(LifecycleEvent event)
            {
                if (event.getState() == LifecycleEvent.LifecycleState.CLIENT_DISCONNECTED)
                {
                    System.out.println("Hazelcast client disconnected. -");
                }
                if (event.getState() == LifecycleEvent.LifecycleState.CLIENT_CONNECTED)
                {
                    System.out.println("Hazelcast client reconnected.");
                }
            }
        }));

        config.setProperty("hazelcast.client.socket.receive.buffer.size", "65536");
        config.setProperty("hazelcast.client.socket.send.buffer.size", "65536");


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
