package jp.vmware.tanzu.twitterwordclouddemo.system.spans.utils;

import com.twitter.clientlib.model.StreamingTweetResponse;
import jp.vmware.tanzu.twitterwordclouddemo.service.TweetStreamHandler;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Primary
public class TestTweetStreamHandler implements TweetStreamHandler {

	@Override
	public void handler(String line) throws InterruptedException, IOException {

	}

	@Override
	public StreamingTweetResponse setStreamTweetResponse(String line) throws IOException {
		return null;
	}

}
