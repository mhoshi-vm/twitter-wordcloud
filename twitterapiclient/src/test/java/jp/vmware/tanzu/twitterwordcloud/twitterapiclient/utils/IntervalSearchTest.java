package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.utils;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.model.Get2TweetsSearchAllResponseMeta;
import com.twitter.clientlib.model.Get2TweetsSearchRecentResponse;
import com.twitter.clientlib.model.Tweet;
import jp.vmware.tanzu.twitterwordcloud.twitterapiclient.test_utils.TestTweetHandler;
import jp.vmware.tanzu.twitterwordcloud.twitterapiclient.test_utils.TestTwitterClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class IntervalSearchTest {

	@Mock
	IntervalSearch twitterStream;

	TwitterClient twitterClient;

	TestTweetHandler tweetHandler;

	List<String> hashTags = Arrays.asList("#hoge", "#foo");

	@BeforeEach
	void setup() throws ApiException {
		twitterClient = new TestTwitterClient();
		tweetHandler = new TestTweetHandler();

		IntervalSearch spyTwitterStream = new IntervalSearch(twitterClient, tweetHandler, hashTags);

		twitterStream = Mockito.spy(spyTwitterStream);

	}

	@Test
	void testSingleTweet() throws ApiException {

		Get2TweetsSearchRecentResponse recentResponse = new Get2TweetsSearchRecentResponse();
		List<Tweet> tweets = new ArrayList<>();
		Tweet tweet = new Tweet();
		tweet.setId("10");
		tweet.setText("AAAA");
		tweet.setLang("en");
		tweets.add(tweet);
		recentResponse.setData(tweets);

		Mockito.doReturn(recentResponse).when(twitterStream).recentSearch(Mockito.any(), Mockito.any(), Mockito.any());

		twitterStream.performTwitterSearch();
		System.out.println(tweetHandler.getTweets());
		assertEquals(1, tweetHandler.getTweets().size());
	}

	@Test
	void testAfterFirstHit() throws ApiException {

		Get2TweetsSearchRecentResponse recentResponse = new Get2TweetsSearchRecentResponse();
		List<Tweet> tweets = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Tweet tweet = new Tweet();
			tweet.setId("10");
			tweet.setText("AAAA");
			tweet.setLang("en");
			tweets.add(tweet);
		}
		recentResponse.setData(tweets);
		Get2TweetsSearchAllResponseMeta meta = new Get2TweetsSearchAllResponseMeta();
		meta.setNextToken("dummy");
		meta.setNewestId("dummy");
		recentResponse.setMeta(meta);

		Get2TweetsSearchRecentResponse nextRecentResponse = new Get2TweetsSearchRecentResponse();
		tweets = new ArrayList<>();
		Tweet tweet = new Tweet();
		tweet.setId("20");
		tweet.setText("BBBB");
		tweet.setLang("ja");
		tweets.add(tweet);
		nextRecentResponse.setData(tweets);

		Mockito.doReturn(nextRecentResponse).when(twitterStream).recentSearch(Mockito.eq(null), Mockito.eq(null),
				Mockito.eq("dummy"));
		Mockito.doReturn(recentResponse).when(twitterStream).recentSearch(Mockito.any(), Mockito.eq(null),
				Mockito.eq(null));

		twitterStream.performTwitterSearch();
		twitterStream.performTwitterSearch();
		assertEquals(11, tweetHandler.getTweets().size());
	}

	@Test
	void testMaxResults() throws ApiException {

		Get2TweetsSearchRecentResponse recentResponse = new Get2TweetsSearchRecentResponse();
		List<Tweet> tweets = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Tweet tweet = new Tweet();
			tweet.setId("10");
			tweet.setText("AAAA");
			tweet.setLang("en");
			tweets.add(tweet);
		}
		recentResponse.setData(tweets);
		Get2TweetsSearchAllResponseMeta meta = new Get2TweetsSearchAllResponseMeta();
		meta.setNextToken("dummy");
		recentResponse.setMeta(meta);

		Get2TweetsSearchRecentResponse nextRecentResponse = new Get2TweetsSearchRecentResponse();
		tweets = new ArrayList<>();
		Tweet tweet = new Tweet();
		tweet.setId("20");
		tweet.setText("BBBB");
		tweet.setLang("ja");
		tweets.add(tweet);
		nextRecentResponse.setData(tweets);

		Mockito.doReturn(nextRecentResponse).when(twitterStream).recentSearch(Mockito.eq(null), Mockito.eq("dummy"),
				Mockito.any());
		Mockito.doReturn(recentResponse).when(twitterStream).recentSearch(Mockito.any(), Mockito.eq(null),
				Mockito.any());

		twitterStream.performTwitterSearch();
		assertEquals(101, tweetHandler.getTweets().size());
	}

}