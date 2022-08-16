package jp.vmware.tanzu.twitterwordclouddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TwitterWordcloudDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterWordcloudDemoApplication.class, args);
	}

}
