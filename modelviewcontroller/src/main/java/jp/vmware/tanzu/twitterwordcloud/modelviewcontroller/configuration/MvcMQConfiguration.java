package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "test", havingValue = "false")
public class MvcMQConfiguration {

	@Value("notification-${random.uuid}")
	public String notificationQueue;

	public static final String EXCHANGE_NAME = "tweet-fanout";

	public static final String QUEUE_NAME = "tweet-handler";

	@Bean
	FanoutExchange mvcExchange() {
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
	Binding tweetQueueBinding(Queue tweetQueue, FanoutExchange mvcExchange) {
		return BindingBuilder.bind(tweetQueue).to(mvcExchange);
	}

	@Bean
	Binding notificationQueueBinding(Queue notificationQueue, FanoutExchange exchange) {
		return BindingBuilder.bind(notificationQueue).to(exchange);
	}

}
