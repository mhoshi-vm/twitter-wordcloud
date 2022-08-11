package jp.vmware.tanzu.twitterwordclouddemo.service;

import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import jp.vmware.tanzu.twitterwordclouddemo.model.TweetText;
import jp.vmware.tanzu.twitterwordclouddemo.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class MyTweetServiceRollbackTest {

	@Autowired
	private MyTweetRepository myTweetRepository;

	@Mock
	private TweetTextRepository tweetTextRepository;

	private MyTweetService myTweetService;

	@BeforeEach
	void setup() {
		myTweetService = new MyTweetService(myTweetRepository, tweetTextRepository);
	}

	// https://stackoverflow.com/questions/58443290/why-doesnt-my-transactional-method-rollback-when-testing
	// @Test
	// @Rollback(false)
	// void deleteTweetId() {
	// MyTweet myTweet1 = new MyTweet();
	// myTweet1.setTweetId("1111");
	// myTweet1.setText("Hello");
	// myTweet1.setUsername("James");
	//
	// myTweetRepository.save(myTweet1);
	//
	// MyTweet myTweet2 = new MyTweet();
	// myTweet2.setTweetId("1100");
	// myTweet2.setText("Morning");
	// myTweet2.setUsername("Jane");
	//
	// myTweetRepository.save(myTweet2);
	//
	// MyTweet myTweet3 = new MyTweet();
	// myTweet3.setTweetId("2222");
	// myTweet3.setText("Night");
	// myTweet3.setUsername("Ryan");
	//
	// myTweetRepository.save(myTweet3);
	//
	// Mockito.when(tweetTextRepository.deleteByTweetId(Mockito.any())).thenThrow(new
	// RuntimeException());
	// try{
	// myTweetService.deleteTweet("1111");
	// }catch (RuntimeException e){
	//
	// }
	//
	// List<MyTweet> myTweets = (List<MyTweet>) myTweetRepository.findAll();
	//
	// assertEquals(3, myTweets.size());
	//
	//
	// }

}