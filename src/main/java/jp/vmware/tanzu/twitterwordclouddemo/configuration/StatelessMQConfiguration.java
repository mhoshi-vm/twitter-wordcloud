package jp.vmware.tanzu.twitterwordclouddemo.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("stateless")
public class StatelessMQConfiguration {

	@Value("notification-${random.uuid}")
	public String notificationQueue;

	public static final String EXCHANGE_NAME = "tweet-fanout";

	public static final String QUEUE_NAME = "tweet-handler";

	@Bean
	FanoutExchange exchange() {
		return new FanoutExchange(EXCHANGE_NAME);
	}

	public String getNotificationQueue() {
		return this.notificationQueue;
	}

	@Bean
	public Queue tweetQueue() {
		return new Queue(QUEUE_NAME);
	}

	@Bean
	public Queue notificationQueue() {
		return new Queue(this.notificationQueue, true, false, true);
	}

	@Bean
	Binding tweetQueueBinding(Queue tweetQueue, FanoutExchange exchange) {
		return BindingBuilder.bind(tweetQueue).to(exchange);
	}

	@Bean
	Binding notificationQueueBinding(Queue notificationQueue, FanoutExchange exchange) {
		return BindingBuilder.bind(notificationQueue).to(exchange);
	}

}
