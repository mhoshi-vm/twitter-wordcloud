package jp.vmware.tanzu.twitterwordclouddemo.configuration;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("stateful")
public class StatefulMQConfiguration {

	public static final String EXCHANGE_NAME = "tweet-fanout";

	@Bean
	FanoutExchange exchange() {
		return new FanoutExchange(EXCHANGE_NAME);
	}

}
