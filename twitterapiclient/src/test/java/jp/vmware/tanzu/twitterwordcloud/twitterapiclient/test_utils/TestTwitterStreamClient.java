package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.test_utils;

import jp.vmware.tanzu.twitterwordcloud.library.utils.TweetHandler;
import jp.vmware.tanzu.twitterwordcloud.twiiterapiclient.utils.TwitterStreamClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Primary
public class TestTwitterStreamClient implements TwitterStreamClient {

	TweetHandler tweetHandler;

	public TestTwitterStreamClient(TweetHandler tweetHandler) {
		this.tweetHandler = tweetHandler;
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
			tweetHandler.handle("");
		}
		catch (Exception ignored) {
		}

	}

}
