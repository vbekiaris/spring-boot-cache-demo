package com.github.vbekiaris.springbootcachedemo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.System.nanoTime;

@SpringBootApplication
@Import(HazelcastConfiguration.class)
public class SpringBootCacheDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCacheDemoApplication.class, args);
	}

	@RestController
	static class CityController {

		private static final Logger LOGGER = LoggerFactory.getLogger(CityController.class);

		@Autowired
		LocationBean locationBean;

		@Autowired
		HazelcastInstance hazelcastInstance;

		@RequestMapping("/location")
		public String getCurrentLocation() {
			String logFormat = "%s call took %d millis with result: %s";
			long start = nanoTime();
			String city = locationBean.getCurrentLocation();
			long end = nanoTime();
			LOGGER.info(format(logFormat, "Rest", TimeUnit.NANOSECONDS.toMillis(end - start), city));
			return city;
		}

		@RequestMapping("/dumpCache")
		public String dumpCache() {
			IMap map = hazelcastInstance.getMap("xyzCache");
            Set<Map.Entry> entries = map.entrySet();
            String result = entries.stream().map(entry -> entry.getKey().toString() + " -> " + entry.getValue().toString())
                   .collect(Collectors.joining("<br/>"));
            return result;
		}

	}
}
