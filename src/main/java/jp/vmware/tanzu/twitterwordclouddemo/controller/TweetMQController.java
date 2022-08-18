package jp.vmware.tanzu.twitterwordclouddemo.controller;

import jp.vmware.tanzu.twitterwordclouddemo.service.TweetStreamService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@Profile("stateless")
public class TweetMQController {

	TweetStreamService tweetStreamService;

	private static final String QUEUE_NAME = "tweet-handler";

	@RabbitListener(queues = QUEUE_NAME)
	public void tweetHandle(String tweet) throws IOException, InterruptedException {
		tweetStreamService.handler(tweet);
	}

}
