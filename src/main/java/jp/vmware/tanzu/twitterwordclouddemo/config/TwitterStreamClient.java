package jp.vmware.tanzu.twitterwordclouddemo.config;

import com.twitter.clientlib.ApiException;

import javax.annotation.PreDestroy;
import java.io.InputStream;

public interface TwitterStreamClient {

	InputStream startStreamListener() throws ApiException;

	@PreDestroy
	void cleanup();

}
