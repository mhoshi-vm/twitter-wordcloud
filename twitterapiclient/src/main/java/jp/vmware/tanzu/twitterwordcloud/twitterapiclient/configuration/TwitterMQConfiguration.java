package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.configuration;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "message.queue.enabled", havingValue = "true")
public class TwitterMQConfiguration {

	@Value("${message.queue.exchange}")
	public String exchangeName;

	@Bean
	FanoutExchange tweetExchange() {
		return new FanoutExchange(exchangeName);
	}

}
