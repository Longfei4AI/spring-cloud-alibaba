package com.altomni.apn.authority.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Component
public class BeanInjection {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

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

}
