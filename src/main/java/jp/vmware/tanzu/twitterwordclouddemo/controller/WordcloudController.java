package jp.vmware.tanzu.twitterwordclouddemo.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile({ "default", "stateless" })
public class WordcloudController {

	@GetMapping("/")
	public String Wordcloud() {
		return "wordcloud";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
