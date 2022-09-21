package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConditionalOnProperty(value = "twitter.search.mode", havingValue = "interval")
@EnableScheduling
public class TwitterIntervalConfiguration {

}
