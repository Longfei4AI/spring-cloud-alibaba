package com.altomni.apn.talent.config;

import com.mongodb.MongoClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author longfeiwang
 */
@Configuration
public class MongoDbConfig {
    @Bean
    public MongoClientOptions mongoOptions() {
        return MongoClientOptions.builder().maxConnectionIdleTime(60000).build();
    }
}
