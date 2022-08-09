package jp.vmware.tanzu.twitterwordclouddemo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyTweetTest {

	private MyTweet myTweet;

	@BeforeEach
	void setUp() {
		myTweet = new MyTweet();
		myTweet.setTweetId("1111");
		myTweet.setText("Hello");
		myTweet.setUsername("James");
	}

	@Test
	void getTweetId() {
		assertEquals("1111", myTweet.getTweetId());
	}

	@Test
	void setTweetId() {
		myTweet.setTweetId("2222");
		assertEquals("2222", myTweet.getTweetId());
	}

	@Test
	void getText() {
		assertEquals("Hello", myTweet.getText());
	}

	@Test
	void setText() {
		myTweet.setText("Goodbye");
		assertEquals("Goodbye", myTweet.getText());
	}

	@Test
	void getUsername() {
		assertEquals("James", myTweet.getUsername());
	}

	@Test
	void setUsername() {
		myTweet.setUsername("Robert");
		assertEquals("Robert", myTweet.getUsername());
	}

}