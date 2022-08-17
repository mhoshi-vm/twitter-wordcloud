package jp.vmware.tanzu.twitterwordclouddemo.system;

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
		InputStream inputStream = this.twitterStreamClient.getTwitterInputStream();
		this.twitterStreamClient.actionOnTweetsStreamAsync(inputStream);
	}

}
