package jp.vmware.tanzu.twitterwordclouddemo.repository;

import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import jp.vmware.tanzu.twitterwordclouddemo.model.TweetText;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TweetTextRepositoryTest {

	@Autowired
	private TweetTextRepository tweetTextRepository;

	@Test
	void listTextCount() {
		TweetText myTweet1 = new TweetText();
		myTweet1.setTweetId("1111");
		myTweet1.setTxt("Hello");

		tweetTextRepository.save(myTweet1);

		TweetText myTweet2 = new TweetText();
		myTweet2.setTweetId("1100");
		myTweet2.setTxt("Morning");

		tweetTextRepository.save(myTweet2);

		TweetText myTweet3 = new TweetText();
		myTweet3.setTweetId("2222");
		myTweet3.setTxt("Morning");

		tweetTextRepository.save(myTweet3);

		TweetText myTweet4 = new TweetText();
		myTweet4.setTweetId("1111");
		myTweet4.setTxt("Hello");

		tweetTextRepository.save(myTweet4);

		TweetText myTweet5 = new TweetText();
		myTweet5.setTweetId("1100");
		myTweet5.setTxt("Morning");

		tweetTextRepository.save(myTweet5);

		TweetText myTweet6 = new TweetText();
		myTweet6.setTweetId("2222");
		myTweet6.setTxt("Night");

		tweetTextRepository.save(myTweet6);

		tweetTextRepository.listTextCount(Pageable.ofSize(10));

		List<TweetTextRepository.TextCount> textCounts = tweetTextRepository.listTextCount(Pageable.ofSize(10));
		assertEquals("Morning", textCounts.get(0).getText());
		assertEquals(3, textCounts.get(0).getSize());
		assertEquals("Hello", textCounts.get(1).getText());
		assertEquals(2, textCounts.get(1).getSize());
		assertEquals("Night", textCounts.get(2).getText());
		assertEquals(1, textCounts.get(2).getSize());
	}

}