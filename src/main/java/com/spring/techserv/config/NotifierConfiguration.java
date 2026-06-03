package com.spring.techserv.config;

import com.spring.techserv.dataClient.CustomRetryer;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class NotifierConfiguration {

    @Bean("address-service")
    public Executor orderExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(20);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("NOTIFIER-EXECUTOR-");
        executor.initialize();
        return executor;
    }
    @Bean
    public Retryer customRetryer() {
        return new CustomRetryer();
    }

}