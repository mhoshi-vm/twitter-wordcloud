package jp.vmware.tanzu.twitterwordclouddemo.utils;

import jp.vmware.tanzu.twitterwordclouddemo.configuration.StatefulMQConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("stateful")
public class TweetHandlerMQ implements TweetHandler {

	private static final Logger logger = LoggerFactory.getLogger(TweetHandlerMQ.class);

	RabbitTemplate rabbitTemplate;

	public TweetHandlerMQ(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void handle(String tweet) {
		logger.debug("Queue Arrived:" + tweet);
		if (!tweet.isEmpty()) {
			logger.debug("Queue Sent:" + tweet);
			this.rabbitTemplate.convertAndSend(StatefulMQConfiguration.EXCHANGE_NAME, "", tweet);
		}
	}

}
