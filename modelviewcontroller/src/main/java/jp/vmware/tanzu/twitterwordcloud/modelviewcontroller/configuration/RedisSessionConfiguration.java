package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

@Configuration
@ConditionalOnProperty(value = "spring.session.store-type", havingValue = "redis")
public class RedisSessionConfiguration {

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}
