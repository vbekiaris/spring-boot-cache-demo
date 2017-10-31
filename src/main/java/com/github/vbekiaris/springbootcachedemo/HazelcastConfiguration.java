package com.github.vbekiaris.springbootcachedemo;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.io.FileNotFoundException;

@EnableCaching
public class HazelcastConfiguration {

    @Bean
    public Config getConfig() throws FileNotFoundException {
        Config config = new Config();

        MapConfig cache = new MapConfig();

        cache.setName("xyzCache");
        cache.getMaxSizeConfig().setSize(1);
        cache.setMaxIdleSeconds(0);
        cache.setTimeToLiveSeconds(86400);
        cache.setEvictionPolicy(EvictionPolicy.LRU);
        config.addMapConfig(cache);
        return config;
    }
}
