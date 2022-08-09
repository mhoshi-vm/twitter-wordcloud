package jp.vmware.tanzu.twitterwordclouddemo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TweetTextTest {

	private TweetText tweetText;

	@BeforeEach
	void setUp() {
		tweetText = new TweetText();
		tweetText.setTweetId("1111");
		tweetText.setTxt("Hello");
	}

	@Test
	void getTweetId() {
		assertEquals("1111", tweetText.getTweetId());
	}

	@Test
	void setTweetId() {
		tweetText.setTweetId("2222");
		assertEquals("2222", tweetText.getTweetId());
	}

	@Test
	void getTxt() {
		assertEquals("Hello", tweetText.getTxt());
	}

	@Test
	void setTxt() {
		tweetText.setTxt("Goodbye");
		assertEquals("Goodbye", tweetText.getTxt());
	}

}