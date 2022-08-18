package jp.vmware.tanzu.twitterwordclouddemo.test_utils;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

@Configuration
@EnableRabbit
public class TestRabbitMQConfig {

	public String tweetIn = "";

	@Bean
	public TestRabbitTemplate template() {
		return new TestRabbitTemplate(connectionFactory());
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		ConnectionFactory factory = mock(ConnectionFactory.class);
		Connection connection = mock(Connection.class);
		Channel channel = mock(Channel.class);
		willReturn(connection).given(factory).createConnection();
		willReturn(channel).given(connection).createChannel(anyBoolean());
		given(channel.isOpen()).willReturn(true);
		return factory;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		return factory;
	}

	@RabbitListener(queues = "tweet-handler")
	public void tweet(String in) {
		this.tweetIn += "tweet-handler:" + in;
	}

}
