package jp.vmware.tanzu.twitterwordclouddemo.service;

import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import jp.vmware.tanzu.twitterwordclouddemo.model.TweetText;
import jp.vmware.tanzu.twitterwordclouddemo.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class MyTweetServiceTest {

	@Autowired
	private MyTweetRepository myTweetRepository;

	@Autowired
	private TweetTextRepository tweetTextRepository;

	private MyTweetService myTweetService;

	@BeforeEach
	void setup() {
		myTweetService = new MyTweetService(myTweetRepository, tweetTextRepository);
	}

	@Test
	void findAllByOrderByTweetIdDesc() {

		MyTweet myTweet1 = new MyTweet();
		myTweet1.setTweetId("1111");
		myTweet1.setText("Hello");
		myTweet1.setUsername("James");

		myTweetRepository.save(myTweet1);

		MyTweet myTweet2 = new MyTweet();
		myTweet2.setTweetId("1100");
		myTweet2.setText("Morning");
		myTweet2.setUsername("Jane");

		myTweetRepository.save(myTweet2);

		MyTweet myTweet3 = new MyTweet();
		myTweet3.setTweetId("2222");
		myTweet3.setText("Night");
		myTweet3.setUsername("Ryan");

		myTweetRepository.save(myTweet3);

		List<MyTweet> myTweets = myTweetService.findAllByOrderByTweetIdDesc();
		assertEquals("2222", myTweets.get(0).getTweetId());
		assertEquals("1111", myTweets.get(1).getTweetId());
		assertEquals("1100", myTweets.get(2).getTweetId());
	}

	@Test
	void deleteTweetId() {
		MyTweet myTweet1 = new MyTweet();
		myTweet1.setTweetId("1111");
		myTweet1.setText("Hello");
		myTweet1.setUsername("James");

		myTweetRepository.save(myTweet1);

		MyTweet myTweet2 = new MyTweet();
		myTweet2.setTweetId("1100");
		myTweet2.setText("Morning");
		myTweet2.setUsername("Jane");

		myTweetRepository.save(myTweet2);

		MyTweet myTweet3 = new MyTweet();
		myTweet3.setTweetId("2222");
		myTweet3.setText("Night");
		myTweet3.setUsername("Ryan");

		myTweetRepository.save(myTweet3);

		TweetText tweetText1 = new TweetText();
		tweetText1.setTweetId("1111");
		tweetText1.setText("Hello");

		tweetTextRepository.save(tweetText1);

		TweetText tweetText2 = new TweetText();
		tweetText2.setTweetId("1100");
		tweetText2.setText("Morning");

		tweetTextRepository.save(tweetText2);

		TweetText tweetText3 = new TweetText();
		tweetText3.setTweetId("2222");
		tweetText3.setText("Morning");

		tweetTextRepository.save(tweetText3);

		TweetText tweetText4 = new TweetText();
		tweetText4.setTweetId("1111");
		tweetText4.setText("Hello");

		tweetTextRepository.save(tweetText4);

		myTweetService.deleteTweet("1111");

		List<MyTweet> myTweets = (List<MyTweet>) myTweetRepository.findAll();
		List<TweetText> tweetTexts = (List<TweetText>) tweetTextRepository.findAll();

		assertEquals(2, myTweets.size());
		assertEquals(2, tweetTexts.size());

	}

}