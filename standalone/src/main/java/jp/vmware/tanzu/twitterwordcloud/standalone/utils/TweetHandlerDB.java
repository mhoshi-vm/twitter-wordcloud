package jp.vmware.tanzu.twitterwordcloud.standalone.utils;

import jp.vmware.tanzu.twitterwordcloud.library.utils.TweetHandler;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.service.TweetStreamService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Primary
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
