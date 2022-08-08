package jp.vmware.tanzu.twitterwordclouddemo.controller;

import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Profile({ "default", "stateless" })
public class listTweetsController {

	public final TweetRepository tweetRepository;

	public listTweetsController(TweetRepository tweetRepository) {
		this.tweetRepository = tweetRepository;
	}

	@GetMapping({ "/tweets" })
	public ModelAndView getAllTweets() {
		ModelAndView mav = new ModelAndView("list-tweets");
		mav.addObject("tweets", tweetRepository.findAllByOrderByTweetIdDesc());
		return mav;
	}

}
