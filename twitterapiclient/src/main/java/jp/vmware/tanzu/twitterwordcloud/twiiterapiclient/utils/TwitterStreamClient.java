package jp.vmware.tanzu.twitterwordcloud.twiiterapiclient.utils;

import com.twitter.clientlib.ApiException;
import org.springframework.scheduling.annotation.Async;

import java.io.InputStream;

public interface TwitterStreamClient {

	String UP = "UP";

	String DOWN = "DOWN";

	InputStream getTwitterInputStream() throws ApiException;

	String getStatus();

	@Async
	void actionOnTweetsStreamAsync(InputStream inputStream) throws InterruptedException;

}
