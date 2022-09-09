package jp.vmware.tanzu.twitterwordclouddemo.system;

import com.twitter.clientlib.ApiException;
import jp.vmware.tanzu.twitterwordclouddemo.utils.TwitterStreamClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Profile({ "default", "stateful" })
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
			System.err.println("Reason: " + e.getResponseBody());
			System.err.println("Response headers: " + e.getResponseHeaders());
			if (e.getCode() == 429){
				System.err.println("Detected rate limit, sleeping for 15min");
				Thread.sleep(900000);
			}
			e.printStackTrace();

		}

	}

}
