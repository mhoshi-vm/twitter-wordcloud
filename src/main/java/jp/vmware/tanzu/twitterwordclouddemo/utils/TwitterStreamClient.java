package jp.vmware.tanzu.twitterwordclouddemo.utils;

import com.twitter.clientlib.ApiException;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PreDestroy;
import java.io.InputStream;

public interface TwitterStreamClient {

	String UP = "UP";

	String DOWN = "DOWN";

	InputStream startStreamListener() throws ApiException;

	String getStatus();

	@Async
	void actionOnTweetsStream(InputStream inputStream);

	@PreDestroy
	void cleanup();

}
