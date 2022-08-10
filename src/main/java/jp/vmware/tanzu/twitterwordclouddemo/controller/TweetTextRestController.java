package jp.vmware.tanzu.twitterwordclouddemo.controller;

import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import jp.vmware.tanzu.twitterwordclouddemo.service.TweetTextService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Profile({ "default", "stateless" })
public class TweetTextRestController {

	private final TweetTextService tweetTextService;

	public TweetTextRestController(TweetTextService tweetTextService) {
		this.tweetTextService = tweetTextService;
	}

	@GetMapping("/tweetcount")
	public ResponseEntity<List<TweetTextRepository.TextCount>> listTweetCount() {
		return new ResponseEntity<>(tweetTextService.listTweetsPage(), HttpStatus.OK);
	}

}
