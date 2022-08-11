package jp.vmware.tanzu.twitterwordclouddemo.service;

import com.twitter.clientlib.model.Expansions;
import com.twitter.clientlib.model.StreamingTweetResponse;
import com.twitter.clientlib.model.Tweet;
import com.twitter.clientlib.model.User;
import jp.vmware.tanzu.twitterwordclouddemo.client.MorphologicalAnalysis;
import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import jp.vmware.tanzu.twitterwordclouddemo.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class TweetStreamServiceRollbackTest {

	@Autowired
	private MyTweetRepository myTweetRepository;

	@Mock
	private TweetTextRepository tweetTextRepository;

	private TweetTextRepository spyTweetTextRepository;

	private final MorphologicalAnalysis morphologicalAnalysis = new MorphologicalAnalysis();

	TweetStreamService tweetStreamService;

	TweetStreamService spyTweetStreamService;

	// https://stackoverflow.com/questions/58443290/why-doesnt-my-transactional-method-rollback-when-testing

	// @BeforeEach
	// void setup() throws IOException {
	//
	// this.tweetStreamService = new TweetStreamService(myTweetRepository,
	// spyTweetTextRepository,
	// morphologicalAnalysis);
	//
	// this.spyTweetStreamService = Mockito.spy(tweetStreamService);
	//
	// Tweet dummyTweet = new Tweet();
	// dummyTweet.setId("111");
	// dummyTweet.setText("#hoge_foo #foo_bar This is !$ test tweet");
	//
	// User dummyUser = new User();
	// dummyUser.setUsername("Jannie");
	//
	// List<User> dummyUsers = new ArrayList<>();
	// dummyUsers.add(dummyUser);
	//
	// Expansions expansions = new Expansions();
	// expansions.setUsers(dummyUsers);
	//
	// StreamingTweetResponse streamingTweetResponse = new StreamingTweetResponse();
	// streamingTweetResponse.setData(dummyTweet);
	// streamingTweetResponse.setIncludes(expansions);
	//
	// Mockito.doReturn(streamingTweetResponse).when(spyTweetStreamService).setStreamTweetResponse(Mockito.any());
	// }
	//
	//
	// @Test
	// void rollbackTest() {
	//
	// Mockito.when(tweetTextRepository.save(Mockito.any())).thenThrow(new
	// RuntimeException());
	//
	// try {
	// spyTweetStreamService.handler("this is test");
	// } catch (Exception e) {
	// }
	//
	// List<MyTweet> myTweets = myTweetRepository.findAllByOrderByTweetIdDesc();
	// assertEquals(0, myTweets.size());
	// }

}