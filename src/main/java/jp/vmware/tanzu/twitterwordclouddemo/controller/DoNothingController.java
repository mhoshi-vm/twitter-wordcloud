package jp.vmware.tanzu.twitterwordclouddemo.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("stateful")
public class DoNothingController {

	@GetMapping("/")
	public String doNothing() {
		return "running...";
	}

}
