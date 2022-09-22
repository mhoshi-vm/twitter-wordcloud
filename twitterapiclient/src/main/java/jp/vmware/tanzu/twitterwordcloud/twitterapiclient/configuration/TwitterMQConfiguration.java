package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.configuration;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "message.queue.enabled", havingValue = "true")
public class TwitterMQConfiguration {

	public static final String EXCHANGE_NAME = "tweet-fanout";

	@Bean
	FanoutExchange tweetExchange() {
		return new FanoutExchange(EXCHANGE_NAME);
	}

}
