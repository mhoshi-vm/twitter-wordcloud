package jp.vmware.tanzu.twitterwordclouddemo.controller;

import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class listTweetsController {

    public final TweetRepository tweetRepository;

    public listTweetsController(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @GetMapping({"/tweets", "/"})
    public ModelAndView getAllEmployees() {
        ModelAndView mav = new ModelAndView("list-tweets");
        mav.addObject("tweets", tweetRepository.findAllByOrderByTweetIdDesc());
        return mav;
    }
}
