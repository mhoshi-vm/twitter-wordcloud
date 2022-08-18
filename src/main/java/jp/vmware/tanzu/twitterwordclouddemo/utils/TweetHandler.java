package jp.vmware.tanzu.twitterwordclouddemo.utils;

import java.io.IOException;

public interface TweetHandler {

	void handle(String tweet) throws IOException, InterruptedException;

}
