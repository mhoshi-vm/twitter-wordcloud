package jp.vmware.tanzu.twitterwordclouddemo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jp.vmware.tanzu.twitterwordclouddemo.service.TweetStreamService;
import jp.vmware.tanzu.twitterwordclouddemo.utils.TweetHandlerMQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@Profile("stateless")
public class TweetMQController {

	private static final Logger logger = LoggerFactory.getLogger(TweetHandlerMQ.class);

	TweetStreamService tweetStreamService;

	public TweetMQController(TweetStreamService tweetStreamService) {
		this.tweetStreamService = tweetStreamService;
	}

	private static final String QUEUE_NAME = "tweet-handler";

	@Bean
	public Queue tweetQueue() {
		return new Queue(QUEUE_NAME);
	}

	@RabbitListener(queues = QUEUE_NAME)
	public void tweetHandle(String tweet) throws IOException, InterruptedException {
		logger.debug("Queue Received : " + tweet);
		if (!tweet.isEmpty()) {
			logger.debug("Queue Processing");
			tweetStreamService.handler(tweet);
		}
	}

}
