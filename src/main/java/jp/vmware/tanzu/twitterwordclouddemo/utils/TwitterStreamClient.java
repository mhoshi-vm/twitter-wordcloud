package jp.vmware.tanzu.twitterwordclouddemo.utils;

import com.twitter.clientlib.ApiException;
import org.springframework.scheduling.annotation.Async;

import java.io.InputStream;

public interface TwitterStreamClient {

	String UP = "UP";

	String DOWN = "DOWN";

	InputStream getTwitterInputStream() throws ApiException;

	String getStatus();

	@Async
	void actionOnTweetsStreamAsync(InputStream inputStream);

}
