package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.controller;

import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.configuration.MvcMQConfiguration;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.service.TweetStreamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@ConditionalOnProperty(value = "message.queue.enabled", havingValue = "true")
public class TweetMQController {

	private static final Logger logger = LoggerFactory.getLogger(TweetMQController.class);

	TweetStreamService tweetStreamService;

	public TweetMQController(TweetStreamService tweetStreamService) {
		this.tweetStreamService = tweetStreamService;
	}

	@RabbitListener(queues = MvcMQConfiguration.QUEUE_NAME)
	public void tweetHandle(String tweet) throws IOException, InterruptedException {
		logger.debug("Queue Received : " + tweet);
		if (!tweet.isEmpty()) {
			logger.debug("Queue Processing");
			tweetStreamService.handler(tweet);
		}
	}

	@RabbitListener(queues = "#{mvcMQConfiguration.getNotificationQueue()}")
	public void notificationHandle(String tweet) throws IOException {
		logger.debug("Queue Received : " + tweet);
		tweetStreamService.notifyTweetEvent(tweet);
	}

}
