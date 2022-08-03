package jp.vmware.tanzu.twitterwordclouddemo.system;

import jp.vmware.tanzu.twitterwordclouddemo.client.TwitterStreamClient;
import jp.vmware.tanzu.twitterwordclouddemo.service.TweetStreamHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class TwitterStreamService implements CommandLineRunner {

	TwitterStreamClient twitterStreamClient;

	TweetStreamHandler tweetStreamHandler;

	public TwitterStreamService(TwitterStreamClient twitterStreamClient, TweetStreamHandler tweetStreamHandler) {
		this.twitterStreamClient = twitterStreamClient;
		this.tweetStreamHandler = tweetStreamHandler;
	}

	@Override
	public void run(String... args) throws Exception {
		InputStream inputStream = this.twitterStreamClient.startStreamListener();
		this.tweetStreamHandler.actionOnTweetsStream(inputStream);
	}

}
