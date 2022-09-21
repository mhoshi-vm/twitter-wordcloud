package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.utils;

import jp.vmware.tanzu.twitterwordcloud.library.utils.TweetHandler;
import jp.vmware.tanzu.twitterwordcloud.twitterapiclient.configuration.TwitterMQConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = { "test" }, havingValue = "false")
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
			this.rabbitTemplate.convertAndSend(TwitterMQConfiguration.EXCHANGE_NAME, "", tweet);
		}
	}

}
