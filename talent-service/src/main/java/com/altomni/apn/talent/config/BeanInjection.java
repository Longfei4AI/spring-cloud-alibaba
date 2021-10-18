package com.altomni.apn.talent.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

/**
 * @author longfeiwang
 */
@Configuration
public class BeanInjection {

    /**
     * to remove "stack trace" from response content
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer problemObjectMapperModules() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.modules(
                new ProblemModule(),
                new ConstraintViolationProblemModule()
        );
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
