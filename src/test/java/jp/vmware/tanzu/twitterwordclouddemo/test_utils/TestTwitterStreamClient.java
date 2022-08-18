package jp.vmware.tanzu.twitterwordclouddemo.test_utils;

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
	public InputStream getTwitterInputStream() {
		return null;
	}

	@Override
	public String getStatus() {
		return null;
	}

	@Override
	public void actionOnTweetsStreamAsync(InputStream inputStream) {
		try {
			tweetStreamService.handler("");
		}
		catch (Exception ignored) {
		}

	}

}
