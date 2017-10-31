# spring-boot-cache-demo

Sample spring-boot application with caching backed by [Hazelcast](https://github.com/hazelcast/hazelcast) [IMap](http://docs.hazelcast.org/docs/latest/manual/html-single/index.html#map).

## Try it out
```
$ ./mvnw package
...
[INFO] BUILD SUCCESS

$ java -jar target/spring-boot-cache-demo-0.0.1-SNAPSHOT.jar
...
2017-10-31 12:39:07.525  INFO 61126 --- [           main] c.g.v.s.SpringBootCacheDemoApplication   : Started SpringBootCacheDemoApplication in 17.671 seconds (JVM running for 18.092)

$ curl http://localhost:8080/dumpCache # nothing in cache yet
$ curl http://localhost:8080/location  # first time invocation will take 5 seconds 
London
$ curl http://localhost:8080/dumpCache
SimpleKey [] -> London
$ curl http://localhost:8080/location  # returns immediately
London
```

## Step by step
- Start a new spring-boot project with the `Cache` dependency
- Add hazelcast-spring dependency:
```
		<dependency>
			<groupId>com.hazelcast</groupId>
			<artifactId>hazelcast-spring</artifactId>
			<version>3.9</version>
		</dependency>
```
- Enable caching in a `@Configuration` class and provide a custom `com.hazelcast.config.Config` class to configure the `HazelcastInstance` embedded member that will be started on spring-boot app startup.
```
@EnableCaching
public class HazelcastConfiguration {

    @Bean
    public Config getConfig() throws FileNotFoundException {
        Config config = new Config();
        MapConfig cache = new MapConfig();
        cache.setName("xyzCache");
        cache.setTimeToLiveSeconds(86400);
        config.addMapConfig(cache);
        return config;
    }
}
```
- Annotate your resource-intensive methods with Spring's `@Cacheable` annotation:
```
    @Cacheable("xyzCache")
    public String getCurrentLocation() {
        // do something
        return "London";
    }
```

## Further reading
Spring Caching can work with Hazelcast as an embedded member or a client connecting to an existing Hazelcast cluster. Also one may back Spring's Caching with Hazelcast's `IMap` or its JCache implementation.
- [Spring Boot cache providers configuration](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#_supported_cache_providers)
- [Hazelcast Spring Bboot caching sample](https://github.com/hazelcast/hazelcast-code-samples/tree/master/hazelcast-integration/springboot-caching)
- [Hazelcast Spring Boot caching with JCache sample](https://github.com/hazelcast/hazelcast-code-samples/tree/master/hazelcast-integration/springboot-caching-jcache)
