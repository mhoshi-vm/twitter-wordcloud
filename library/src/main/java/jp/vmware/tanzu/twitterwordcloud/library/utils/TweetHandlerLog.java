package jp.vmware.tanzu.twitterwordcloud.library.utils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "message.queue.enabled", havingValue = "false", matchIfMissing = true)
public class TweetHandlerLog implements TweetHandler {

	@Override
	public void handle(String tweet) {
		System.out.println(tweet);
	}

}
