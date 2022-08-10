package jp.vmware.tanzu.twitterwordclouddemo.client;

import com.twitter.clientlib.ApiException;

import javax.annotation.PreDestroy;
import java.io.InputStream;

public interface TwitterStreamClient {

	InputStream startStreamListener() throws ApiException;

	void actionOnTweetsStream(InputStream inputStream);

	@PreDestroy
	void cleanup();

}
