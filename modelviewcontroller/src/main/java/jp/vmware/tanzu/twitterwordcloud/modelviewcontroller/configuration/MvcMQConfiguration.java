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
@ConditionalOnProperty(value = "message.queue.enabled", havingValue = "true")
public class MvcMQConfiguration {

	@Value("${message.queue.exchange}")
	public String exchangeName;

	@Value("${message.queue.queue}")
	public String queueName;

	@Value("notification-${random.uuid}")
	public String notificationQueue;

	@Bean
	FanoutExchange mvcExchange() {
		return new FanoutExchange(exchangeName);
	}

	public String getNotificationQueue() {
		return this.notificationQueue;
	}

	@Bean
	public Queue tweetQueue() {
		return new Queue(queueName);
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
