package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "jp.vmware.tanzu.twitterwordcloud")
public class ModelViewControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModelViewControllerApplication.class, args);
	}

}
