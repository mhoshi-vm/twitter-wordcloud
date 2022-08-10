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
		tweetText.setText("Hello");
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
		assertEquals("Hello", tweetText.getText());
	}

	@Test
	void setTxt() {
		tweetText.setText("Goodbye");
		assertEquals("Goodbye", tweetText.getText());
	}

}