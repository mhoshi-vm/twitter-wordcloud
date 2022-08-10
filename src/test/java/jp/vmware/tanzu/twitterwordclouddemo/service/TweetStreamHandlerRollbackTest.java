package jp.vmware.tanzu.twitterwordclouddemo.service;

import com.twitter.clientlib.model.Expansions;
import com.twitter.clientlib.model.StreamingTweetResponse;
import com.twitter.clientlib.model.Tweet;
import com.twitter.clientlib.model.User;
import jp.vmware.tanzu.twitterwordclouddemo.client.MorphologicalAnalysis;
import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import jp.vmware.tanzu.twitterwordclouddemo.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class TweetStreamHandlerRollbackTest {

	@Autowired
	private MyTweetRepository myTweetRepository;

	@MockBean
	private TweetTextRepository tweetTextRepository;

	private final MorphologicalAnalysis morphologicalAnalysis = new MorphologicalAnalysis();

	TweetStreamHandler tweetStreamHandler;

	TweetStreamHandler spyTweetStreamHandler;

	@BeforeEach
	void setup() {
		this.tweetStreamHandler = new TweetStreamHandlerImpl(myTweetRepository, tweetTextRepository,
				morphologicalAnalysis);

		this.spyTweetStreamHandler = Mockito.spy(tweetStreamHandler);

	}


	/*
	@Test
	void rollbackTest() throws IOException, InterruptedException {

		Mockito.when(tweetTextRepository.save(Mockito.any())).thenThrow(new NullPointerException("Error occurred"));

		Tweet dummyTweet = new Tweet();
		dummyTweet.setId("111");
		dummyTweet.setText("#hoge_foo #foo_bar This is !$ test tweet");

		User dummyUser = new User();
		dummyUser.setUsername("Jannie");
		List<User> dummyUsers = new ArrayList<>();
		dummyUsers.add(dummyUser);

		Expansions expansions = new Expansions();
		expansions.setUsers(dummyUsers);

		StreamingTweetResponse streamingTweetResponse = new StreamingTweetResponse();
		streamingTweetResponse.setData(dummyTweet);
		streamingTweetResponse.setIncludes(expansions);

		Mockito.doReturn(streamingTweetResponse).when(spyTweetStreamHandler).setStreamTweetResponse(Mockito.any());

		try {
			spyTweetStreamHandler.handler("this is test");
		}catch (Exception e){

		}

		List<MyTweet> myTweets = myTweetRepository.findAllByOrderByTweetIdDesc();
		assertEquals(0, myTweets.size());

		List<TweetTextRepository.TextCount> textCounts = tweetTextRepository.listTextCount(Pageable.ofSize(10));
		assertEquals(0, textCounts.size());
	}*/

}