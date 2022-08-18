package jp.vmware.tanzu.twitterwordclouddemo.utils;

import jp.vmware.tanzu.twitterwordclouddemo.service.TweetStreamService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile({ "default" })
public class TweetHandlerDB implements TweetHandler {

	TweetStreamService tweetStreamService;

	public TweetHandlerDB(TweetStreamService tweetStreamService) {
		this.tweetStreamService = tweetStreamService;
	}

	@Override
	public void handle(String tweet) throws IOException, InterruptedException {
		tweetStreamService.handler(tweet);
	}

}
