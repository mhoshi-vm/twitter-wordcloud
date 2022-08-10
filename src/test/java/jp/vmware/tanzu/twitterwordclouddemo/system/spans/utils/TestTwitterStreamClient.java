package jp.vmware.tanzu.twitterwordclouddemo.system.spans.utils;

import jp.vmware.tanzu.twitterwordclouddemo.client.TwitterStreamClient;
import jp.vmware.tanzu.twitterwordclouddemo.service.TweetStreamHandler;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Primary
public class TestTwitterStreamClient implements TwitterStreamClient {

    TweetStreamHandler tweetStreamHandler;

    public TestTwitterStreamClient(TweetStreamHandler tweetStreamHandler) {
        this.tweetStreamHandler = tweetStreamHandler;
    }

    @Override
    public InputStream startStreamListener() {
        return null;
    }

    @Override
    public void actionOnTweetsStream(InputStream inputStream) {
        try {
            tweetStreamHandler.handler("");
        }catch (Exception ignored){
        }

    }

    @Override
    public void cleanup() {

    }
}
