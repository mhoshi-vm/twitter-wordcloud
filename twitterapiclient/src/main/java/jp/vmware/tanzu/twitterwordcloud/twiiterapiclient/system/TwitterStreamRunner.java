package jp.vmware.tanzu.twitterwordcloud.twiiterapiclient.system;

import com.twitter.clientlib.ApiException;
import jp.vmware.tanzu.twitterwordcloud.twiiterapiclient.utils.TwitterStreamClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class TwitterStreamRunner implements CommandLineRunner {

	TwitterStreamClient twitterStreamClient;

	public TwitterStreamRunner(TwitterStreamClient twitterStreamClient) {
		this.twitterStreamClient = twitterStreamClient;
	}

	@Override
	public void run(String... args) throws Exception {
		InputStream inputStream;
		try {
			inputStream = this.twitterStreamClient.getTwitterInputStream();
			this.twitterStreamClient.actionOnTweetsStreamAsync(inputStream);
		}
		catch (ApiException e) {
			System.err.println("Status code: " + e.getCode());
			System.err.println("Reason: " + e.getCause());
			System.err.println("Response headers: " + e.getResponseHeaders());
			e.printStackTrace();
		}

	}

}
