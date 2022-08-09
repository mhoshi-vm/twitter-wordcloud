package jp.vmware.tanzu.twitterwordclouddemo.repository;

import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TweetRepositoryTest {

	@Autowired
	private TweetRepository tweetRepository;

	@Test
	void findAllByOrderByTweetIdDesc() {

		MyTweet myTweet1 = new MyTweet();
		myTweet1.setTweetId("1111");
		myTweet1.setText("Hello");
		myTweet1.setUsername("James");

		tweetRepository.save(myTweet1);

		MyTweet myTweet2 = new MyTweet();
		myTweet2.setTweetId("1100");
		myTweet2.setText("Morning");
		myTweet2.setUsername("Jane");

		tweetRepository.save(myTweet2);

		MyTweet myTweet3 = new MyTweet();
		myTweet3.setTweetId("2222");
		myTweet3.setText("Night");
		myTweet3.setUsername("Ryan");

		tweetRepository.save(myTweet3);

		List<MyTweet> myTweets = tweetRepository.findAllByOrderByTweetIdDesc();
		assertEquals("2222", myTweets.get(0).getTweetId());
		assertEquals("1111", myTweets.get(1).getTweetId());
		assertEquals("1100", myTweets.get(2).getTweetId());
	}

}