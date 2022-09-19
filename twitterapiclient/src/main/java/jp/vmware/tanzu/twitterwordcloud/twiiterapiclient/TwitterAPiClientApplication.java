package jp.vmware.tanzu.twitterwordcloud.twiiterapiclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "jp.vmware.tanzu.twitterwordcloud")
public class TwitterAPiClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterAPiClientApplication.class, args);
	}

}
