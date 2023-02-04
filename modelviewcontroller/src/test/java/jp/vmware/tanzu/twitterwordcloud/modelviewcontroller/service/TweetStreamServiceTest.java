package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.service;

import com.twitter.clientlib.model.Expansions;
import com.twitter.clientlib.model.StreamingTweetResponse;
import com.twitter.clientlib.model.Tweet;
import com.twitter.clientlib.model.User;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.model.MyTweet;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.repository.TweetTextRepository;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.utils.MorphologicalAnalysis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class TweetStreamServiceTest {

	private final MorphologicalAnalysis morphologicalAnalysis = new MorphologicalAnalysis();

	TweetStreamService tweetStreamService;

	TweetStreamService spyTweetStreamService;

	@Autowired
	private MyTweetRepository myTweetRepository;

	@Autowired
	private TweetTextRepository tweetTextRepository;

	@BeforeEach
	void setup() {
		this.tweetStreamService = new TweetStreamService(myTweetRepository, tweetTextRepository, morphologicalAnalysis,
				"ja");

		this.spyTweetStreamService = Mockito.spy(tweetStreamService);

	}

	@Test
	void normalCase() throws IOException, InterruptedException {

		Tweet dummyTweet = new Tweet();
		dummyTweet.setId("111");
		dummyTweet.setText("This is test tweet");
		dummyTweet.setLang("ja");

		User dummyUser = new User();
		dummyUser.setUsername("Jannie");
		List<User> dummyUsers = new ArrayList<>();
		dummyUsers.add(dummyUser);

		Expansions expansions = new Expansions();
		expansions.setUsers(dummyUsers);

		StreamingTweetResponse streamingTweetResponse = new StreamingTweetResponse();
		streamingTweetResponse.setData(dummyTweet);
		streamingTweetResponse.setIncludes(expansions);

		Mockito.doReturn(streamingTweetResponse).when(spyTweetStreamService).setStreamTweetResponse(Mockito.any());

		spyTweetStreamService.handler("this is test");

		List<MyTweet> myTweets = myTweetRepository.findAllByOrderByTweetIdDesc();
		assertEquals(1, myTweets.size());

		List<TweetTextRepository.TextCount> textCounts = tweetTextRepository.listTextCount(Pageable.ofSize(10));
		assertEquals("This", textCounts.get(0).getText());
		assertEquals(1, textCounts.get(0).getSize());
		assertEquals("is", textCounts.get(1).getText());
		assertEquals(1, textCounts.get(1).getSize());
		assertEquals("test", textCounts.get(2).getText());
		assertEquals(1, textCounts.get(2).getSize());
		assertEquals("tweet", textCounts.get(3).getText());
		assertEquals(1, textCounts.get(3).getSize());
	}

	@Test
	void returnWhenLineIsEmpty() throws IOException, InterruptedException {
		spyTweetStreamService.handler("");

		List<MyTweet> myTweets = myTweetRepository.findAllByOrderByTweetIdDesc();
		assertEquals(0, myTweets.size());

		List<TweetTextRepository.TextCount> textCounts = tweetTextRepository.listTextCount(Pageable.ofSize(10));
		assertEquals(0, textCounts.size());
	}

	@Test
	void doNothingOnNonJson() throws IOException, InterruptedException {
		spyTweetStreamService.handler("dd");

		List<MyTweet> myTweets = myTweetRepository.findAllByOrderByTweetIdDesc();
		assertEquals(0, myTweets.size());

		List<TweetTextRepository.TextCount> textCounts = tweetTextRepository.listTextCount(Pageable.ofSize(10));
		assertEquals(0, textCounts.size());
	}

	@Test
	void skipHashTagAndUsername() throws IOException, InterruptedException {
		Tweet dummyTweet = new Tweet();
		dummyTweet.setId("111");
		dummyTweet.setText("#hoge_foo #foo_bar This is !$ test tweet");
		dummyTweet.setLang("ja");

		User dummyUser = new User();
		dummyUser.setUsername("Jannie");
		List<User> dummyUsers = new ArrayList<>();
		dummyUsers.add(dummyUser);

		Expansions expansions = new Expansions();
		expansions.setUsers(dummyUsers);

		StreamingTweetResponse streamingTweetResponse = new StreamingTweetResponse();
		streamingTweetResponse.setData(dummyTweet);
		streamingTweetResponse.setIncludes(expansions);

		Mockito.doReturn(streamingTweetResponse).when(spyTweetStreamService).setStreamTweetResponse(Mockito.any());

		spyTweetStreamService.handler("this is test");

		List<MyTweet> myTweets = myTweetRepository.findAllByOrderByTweetIdDesc();
		assertEquals(1, myTweets.size());

		List<TweetTextRepository.TextCount> textCounts = tweetTextRepository.listTextCount(Pageable.ofSize(10));
		assertEquals("This", textCounts.get(0).getText());
		assertEquals(1, textCounts.get(0).getSize());
		assertEquals("is", textCounts.get(1).getText());
		assertEquals(1, textCounts.get(1).getSize());
		assertEquals("test", textCounts.get(2).getText());
		assertEquals(1, textCounts.get(2).getSize());
		assertEquals("tweet", textCounts.get(3).getText());
		assertEquals(1, textCounts.get(3).getSize());
	}

	/*
	 * @Test void skipNonJapanese() throws InterruptedException {
	 *
	 * Tweet dummyTweet = new Tweet(); dummyTweet.setId("111");
	 * dummyTweet.setText("This is test tweet"); dummyTweet.setLang("en");
	 *
	 * User dummyUser = new User(); dummyUser.setUsername("Jannie"); List<User> dummyUsers
	 * = new ArrayList<>(); dummyUsers.add(dummyUser);
	 *
	 * Expansions expansions = new Expansions(); expansions.setUsers(dummyUsers);
	 *
	 * StreamingTweetResponse streamingTweetResponse = new StreamingTweetResponse();
	 * streamingTweetResponse.setData(dummyTweet);
	 * streamingTweetResponse.setIncludes(expansions);
	 *
	 * Mockito.doReturn(streamingTweetResponse).when(spyTweetStreamService).
	 * setStreamTweetResponse(Mockito.any());
	 *
	 * spyTweetStreamService.handler("this is test");
	 *
	 * List<MyTweet> myTweets = myTweetRepository.findAllByOrderByTweetIdDesc();
	 * assertEquals(0, myTweets.size());
	 *
	 * }
	 *
	 * @Test void englishSupport() throws InterruptedException {
	 *
	 * this.tweetStreamService = new TweetStreamService(myTweetRepository,
	 * tweetTextRepository, morphologicalAnalysis, "en");
	 *
	 * this.spyTweetStreamService = Mockito.spy(tweetStreamService);
	 *
	 * Tweet dummyTweet = new Tweet(); dummyTweet.setId("111");
	 * dummyTweet.setText("This is test tweet"); dummyTweet.setLang("en");
	 *
	 * User dummyUser = new User(); dummyUser.setUsername("Jannie"); List<User> dummyUsers
	 * = new ArrayList<>(); dummyUsers.add(dummyUser);
	 *
	 * Expansions expansions = new Expansions(); expansions.setUsers(dummyUsers);
	 *
	 * StreamingTweetResponse streamingTweetResponse = new StreamingTweetResponse();
	 * streamingTweetResponse.setData(dummyTweet);
	 * streamingTweetResponse.setIncludes(expansions);
	 *
	 * Mockito.doReturn(streamingTweetResponse).when(spyTweetStreamService).
	 * setStreamTweetResponse(Mockito.any());
	 *
	 * spyTweetStreamService.handler("this is test");
	 *
	 * List<MyTweet> myTweets = myTweetRepository.findAllByOrderByTweetIdDesc();
	 * assertEquals(1, myTweets.size());
	 *
	 * }
	 */

}