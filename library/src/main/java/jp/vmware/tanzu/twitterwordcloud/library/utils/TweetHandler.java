package jp.vmware.tanzu.twitterwordcloud.library.utils;

import io.micrometer.tracing.annotation.NewSpan;

import java.io.IOException;

public interface TweetHandler {

	@NewSpan(name = "tweet-stream-handler")
	void handle(String tweet) throws IOException, InterruptedException;

}
