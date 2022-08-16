package jp.vmware.tanzu.twitterwordclouddemo.observability.utils;

import jp.vmware.tanzu.twitterwordclouddemo.utils.TwitterStreamClient;
import jp.vmware.tanzu.twitterwordclouddemo.service.TweetStreamService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Primary
public class TestTwitterStreamClient implements TwitterStreamClient {

	TweetStreamService tweetStreamService;

	public TestTwitterStreamClient(TweetStreamService tweetStreamService) {
		this.tweetStreamService = tweetStreamService;
	}

	@Override
	public InputStream startStreamListener() {
		return null;
	}

	@Override
	public String getStatus() {
		return null;
	}

	@Override
	public void actionOnTweetsStream(InputStream inputStream) {
		try {
			tweetStreamService.handler("");
		}
		catch (Exception ignored) {
		}

	}

	@Override
	public void cleanup() {

	}

}
