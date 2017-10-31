package com.github.vbekiaris.springbootcachedemo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class LocationBeanImpl implements LocationBean {

    @Cacheable("xyzCache")
    @Override
    public String getCurrentLocation() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // ignore
        }
        return "London";
    }
}
