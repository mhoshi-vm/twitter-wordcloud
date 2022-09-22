package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.controller;

import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.repository.TweetTextRepository;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.service.TweetTextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
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
