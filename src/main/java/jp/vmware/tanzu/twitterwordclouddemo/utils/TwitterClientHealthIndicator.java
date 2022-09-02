package jp.vmware.tanzu.twitterwordclouddemo.utils;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"default", "stateful"})
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
		if (twitterStatus.equals(TwitterStreamClient.DOWN)) {
			status = Health.down();
		}
		return status.build();
	}

}
