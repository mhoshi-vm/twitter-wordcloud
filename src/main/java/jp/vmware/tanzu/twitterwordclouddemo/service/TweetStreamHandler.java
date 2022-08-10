package jp.vmware.tanzu.twitterwordclouddemo.service;

import com.twitter.clientlib.model.StreamingTweetResponse;
import org.springframework.cloud.sleuth.annotation.NewSpan;

import java.io.IOException;

public interface TweetStreamHandler {

	@NewSpan(name = "TweetStreamHandler")
	void handler(String line) throws InterruptedException, IOException;

	StreamingTweetResponse setStreamTweetResponse(String line) throws IOException;

}
