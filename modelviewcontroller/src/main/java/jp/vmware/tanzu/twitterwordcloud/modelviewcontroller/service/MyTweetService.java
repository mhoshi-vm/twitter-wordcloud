package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.service;

import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.model.MyTweet;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.repository.TweetTextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyTweetService {

	public final MyTweetRepository myTweetRepository;

	public final TweetTextRepository tweetTextRepository;

	public MyTweetService(MyTweetRepository myTweetRepository, TweetTextRepository tweetTextRepository) {
		this.myTweetRepository = myTweetRepository;
		this.tweetTextRepository = tweetTextRepository;
	}

	public List<MyTweet> findAllByOrderByTweetIdDesc() {
		return myTweetRepository.findAllByOrderByTweetIdDesc();
	}

	@Transactional
	public void deleteTweet(String tweetId) {
		myTweetRepository.deleteByTweetId(tweetId);
		tweetTextRepository.deleteByTweetId(tweetId);
	}

}
