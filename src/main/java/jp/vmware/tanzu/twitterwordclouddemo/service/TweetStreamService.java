package jp.vmware.tanzu.twitterwordclouddemo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.clientlib.model.Expansions;
import com.twitter.clientlib.model.StreamingTweetResponse;
import com.twitter.clientlib.model.Tweet;
import com.twitter.clientlib.model.User;
import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import jp.vmware.tanzu.twitterwordclouddemo.model.TweetText;
import jp.vmware.tanzu.twitterwordclouddemo.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import jp.vmware.tanzu.twitterwordclouddemo.utils.MorphologicalAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TweetStreamService {

	private static final Logger logger = LoggerFactory.getLogger(TweetStreamService.class);

	public MyTweetRepository myTweetRepository;

	public TweetTextRepository tweetTextRepository;

	public MorphologicalAnalysis morphologicalAnalysis;

	Pattern nonLetterPattern;

	public TweetStreamService(MyTweetRepository myTweetRepository, TweetTextRepository tweetTextRepository,
			MorphologicalAnalysis morphologicalAnalysis) {
		this.myTweetRepository = myTweetRepository;
		this.tweetTextRepository = tweetTextRepository;
		this.morphologicalAnalysis = morphologicalAnalysis;
		this.nonLetterPattern = Pattern.compile("^\\W+$", Pattern.UNICODE_CHARACTER_CLASS);
	}

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

		logger.debug("Handling Tweet : " + tweet.getText());

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

	public StreamingTweetResponse setStreamTweetResponse(String line) throws IOException {

		StreamingTweetResponse streamingTweetResponse = new StreamingTweetResponse();

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonFullNode = objectMapper.readTree(line);
		JsonNode jsonDataNode = jsonFullNode.get("data");
		JsonNode jsonExpantionNode = jsonFullNode.get("includes");

		if (jsonDataNode != null) {
			Tweet tweet = new Tweet();
			tweet.setId(jsonDataNode.get("id").asText());
			tweet.setText(jsonDataNode.get("text").asText());
			tweet.setLang(jsonDataNode.get("lang").asText());
			streamingTweetResponse.setData(tweet);
		}

		if (jsonExpantionNode != null) {
			JsonNode jsonUserNode = jsonExpantionNode.get("users");
			Expansions expansions = new Expansions();
			if (jsonUserNode != null && jsonUserNode.size() > 0) {
				User user = new User();
				user.setUsername(jsonUserNode.get(0).get("name").asText());
				List<User> users = new ArrayList<>();
				users.add(user);
				expansions.setUsers(users);
			}
			streamingTweetResponse.setIncludes(expansions);
		}

		return streamingTweetResponse;
	}

}
