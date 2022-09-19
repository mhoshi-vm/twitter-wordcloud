package jp.vmware.tanzu.twitterwordcloud.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "jp.vmware.tanzu.twitterwordcloud")
public class StandaloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(StandaloneApplication.class, args);
	}

}
