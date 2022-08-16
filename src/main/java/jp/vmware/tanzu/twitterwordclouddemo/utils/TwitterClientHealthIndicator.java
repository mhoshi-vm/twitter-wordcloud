package jp.vmware.tanzu.twitterwordclouddemo.utils;

import jp.vmware.tanzu.twitterwordclouddemo.utils.TwitterStreamClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TwitterClientHealthIndicator implements HealthIndicator {

	TwitterStreamClient twitterStreamClient;

	public TwitterClientHealthIndicator(TwitterStreamClient twitterStreamClient) {
		this.twitterStreamClient = twitterStreamClient;
	}

	@Override
	public Health getHealth(boolean includeDetails) {
		return HealthIndicator.super.getHealth(includeDetails);
	}

	@Override
	public Health health() {
		String twitterStatus = twitterStreamClient.getStatus();
		Health.Builder status = Health.up();
		if (twitterStatus.equals(twitterStreamClient.DOWN)) {
			status = Health.down();
		}
		return status.build();
	}

}
