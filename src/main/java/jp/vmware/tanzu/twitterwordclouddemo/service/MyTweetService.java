package jp.vmware.tanzu.twitterwordclouddemo.service;

import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import jp.vmware.tanzu.twitterwordclouddemo.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
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

	public void deleteTweet(String tweetId) {
		myTweetRepository.deleteByTweetId(tweetId);
		tweetTextRepository.deleteByTweetId(tweetId);
	}

}
