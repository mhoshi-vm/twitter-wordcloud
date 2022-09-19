package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.test_utils;

import jp.vmware.tanzu.twitterwordcloud.library.utils.TweetHandler;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class TestTweetHandler implements TweetHandler {

	@Override
	public void handle(String tweet) {

	}

}
