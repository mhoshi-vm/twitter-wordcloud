package jp.vmware.tanzu.twitterwordclouddemo.service;

import com.twitter.clientlib.model.Expansions;
import com.twitter.clientlib.model.StreamingTweetResponse;
import com.twitter.clientlib.model.Tweet;
import com.twitter.clientlib.model.User;
import jp.vmware.tanzu.twitterwordclouddemo.client.MorphologicalAnalysis;
import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import jp.vmware.tanzu.twitterwordclouddemo.model.TweetText;
import jp.vmware.tanzu.twitterwordclouddemo.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Profile({ "default", "stateful" })
public class TweetStreamHandlerImpl implements TweetStreamHandler {

	public MyTweetRepository myTweetRepository;

	public TweetTextRepository tweetTextRepository;

	public MorphologicalAnalysis morphologicalAnalysis;

	Pattern nonLetterPattern;

	public TweetStreamHandlerImpl(MyTweetRepository myTweetRepository, TweetTextRepository tweetTextRepository,
								  MorphologicalAnalysis morphologicalAnalysis) {
		this.myTweetRepository = myTweetRepository;
		this.tweetTextRepository = tweetTextRepository;
		this.morphologicalAnalysis = morphologicalAnalysis;
		this.nonLetterPattern = Pattern.compile("^\\W+$", Pattern.UNICODE_CHARACTER_CLASS);
	}

	@Override
	@Transactional
	public void handler(String line) throws InterruptedException, IOException {

		if (line.isEmpty()) {
			Thread.sleep(100);
			return;
		}

		StreamingTweetResponse streamingTweetResponse = setStreamTweetResponse(line);

		Tweet tweet = streamingTweetResponse.getData();
		if (tweet == null) {
			return;
		}

		Expansions expansions = streamingTweetResponse.getIncludes();
		if (expansions == null) {
			return;
		}
		List<User> users = expansions.getUsers();
		if (users == null) {
			return;
		}
		User user = users.get(0);

		MyTweet myTweet = new MyTweet();
		myTweet.setTweetId(tweet.getId());
		myTweet.setText(tweet.getText());
		myTweet.setUsername(user.getUsername());

		myTweetRepository.save(myTweet);

		boolean nextSkip = false;

		for (String text : morphologicalAnalysis.getToken(tweet.getText())) {

			TweetText tweetText = new TweetText();

			// Skip until blank character when hast tag or username tag found
			if (nextSkip) {
				if (text.isBlank()) {
					nextSkip = false;
				}
				continue;
			}
			// Skip hashtag and username and also set next skip to true
			if (text.equals("#") || text.equals("@")) {
				nextSkip = true;
				continue;
			}
			// Skip RT, blank, and non letter words
			Matcher m = nonLetterPattern.matcher(text);
			if (text.equals("RT") || text.isBlank() || m.matches()) {
				continue;
			}

			tweetText.setTweetId(tweet.getId());
			tweetText.setText(text);

			tweetTextRepository.save(tweetText);
		}
	}

	@Override
	public StreamingTweetResponse setStreamTweetResponse(String line) throws IOException {
		return StreamingTweetResponse.fromJson(line);
	}

}
