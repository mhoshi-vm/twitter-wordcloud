package jp.vmware.tanzu.twitterwordclouddemo.controller;

import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WordcloudApiController {

	private final TweetTextRepository tweetTextRepository;

	public WordcloudApiController(TweetTextRepository tweetTextRepository) {
		this.tweetTextRepository = tweetTextRepository;
	}

	@GetMapping("/tweetcount")
	public ResponseEntity<List<TweetTextRepository.TextCount>> listTweetCount() {
		return new ResponseEntity<>(tweetTextRepository.listTextCount(PageRequest.of(0, 200)), HttpStatus.OK);
	}

}
