package jp.vmware.tanzu.twitterwordclouddemo.service;

import com.twitter.clientlib.model.StreamingTweetResponse;
import com.twitter.clientlib.model.Tweet;
import com.twitter.clientlib.model.User;
import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import jp.vmware.tanzu.twitterwordclouddemo.model.TweetText;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class TweetStreamHandlerImpl implements TweetStreamHandler {

	public TweetRepository tweetRepository;

	public TweetTextRepository tweetTextRepository;

	public MorphologicalAnalysis morphologicalAnalysis;

	public TweetStreamHandlerImpl(TweetRepository tweetRepository, TweetTextRepository tweetTextRepository,
			MorphologicalAnalysis morphologicalAnalysis) {
		this.tweetRepository = tweetRepository;
		this.tweetTextRepository = tweetTextRepository;
		this.morphologicalAnalysis = morphologicalAnalysis;
	}

	@Override
	public void actionOnTweetsStream(InputStream inputStream) {

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = reader.readLine();
			while (line != null) {
				if (line.isEmpty()) {
					Thread.sleep(100);
					line = reader.readLine();
					continue;
				}

				StreamingTweetResponse streamingTweetResponse = StreamingTweetResponse.fromJson(line);

				Tweet tweet = streamingTweetResponse.getData();
				User user = streamingTweetResponse.getIncludes().getUsers().get(0);

				MyTweet myTweet = new MyTweet();
				myTweet.setTweetId(tweet.getId());
				myTweet.setText(tweet.getText());
				myTweet.setUsername(user.getUsername());

				tweetRepository.save(myTweet);

				for (String text : morphologicalAnalysis.getToken(tweet.getText())) {
					TweetText tweetText = new TweetText();
					System.out.println(text);
					if (text.contains("RT") || text.isBlank()) {
						continue;
					}
					tweetText.setTweetId(tweet.getId());
					tweetText.setTxt(text);
					tweetTextRepository.save(tweetText);
				}
				line = reader.readLine();
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
