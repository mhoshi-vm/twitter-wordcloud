package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.utils;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TwitterClientHealthIndicator implements HealthIndicator {

	TwitterClient twitterClient;

	public TwitterClientHealthIndicator(TwitterClient twitterClient) {
		this.twitterClient = twitterClient;
	}

	@Override
	public Health getHealth(boolean includeDetails) {
		return HealthIndicator.super.getHealth(includeDetails);
	}

	@Override
	public Health health() {
		String twitterStatus = twitterClient.getStatus();
		Health.Builder status = Health.up();
		if (twitterStatus.equals(TwitterClient.DOWN)) {
			status = Health.down();
		}
		return status.build();
	}

}
