package jp.vmware.tanzu.twitterwordclouddemo.service;

import java.io.InputStream;

public interface TweetStreamHandler {

	void actionOnTweetsStream(InputStream stream);

}
