package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.clientlib.model.Expansions;
import com.twitter.clientlib.model.StreamingTweetResponse;
import com.twitter.clientlib.model.Tweet;
import com.twitter.clientlib.model.User;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.model.MyTweet;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.model.TweetText;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.repository.TweetTextRepository;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.utils.MorphologicalAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TweetStreamService {

	private static final Logger logger = LoggerFactory.getLogger(TweetStreamService.class);

	public MyTweetRepository myTweetRepository;

	public TweetTextRepository tweetTextRepository;

	public MorphologicalAnalysis morphologicalAnalysis;

	private final List<SseEmitter> emitters;

	Pattern nonLetterPattern;

	public TweetStreamService(MyTweetRepository myTweetRepository, TweetTextRepository tweetTextRepository,
			MorphologicalAnalysis morphologicalAnalysis) {
		this.myTweetRepository = myTweetRepository;
		this.tweetTextRepository = tweetTextRepository;
		this.morphologicalAnalysis = morphologicalAnalysis;
		this.emitters = new CopyOnWriteArrayList<>();
		this.nonLetterPattern = Pattern.compile("^\\W+$", Pattern.UNICODE_CHARACTER_CLASS);
	}

	public List<SseEmitter> getEmitters() {
		return emitters;
	}

	@Transactional
	public void handler(String line) throws InterruptedException, IOException {

		StreamingTweetResponse streamingTweetResponse = setStreamTweetResponse(line);

		if (streamingTweetResponse == null) {
			Thread.sleep(100);
			return;
		}
		Tweet tweet = streamingTweetResponse.getData();
		Expansions expansions = streamingTweetResponse.getIncludes();
		List<User> users = expansions.getUsers();

		if (!tweet.getLang().equals("ja")) {
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

	public void notifyTweetEvent(String line) {
		StreamingTweetResponse streamingTweetResponse = setStreamTweetResponse(line);

		if (streamingTweetResponse == null) {
			return;
		}

		Tweet tweet = streamingTweetResponse.getData();

		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(SseEmitter.event().name("newTweet").data("New Tweet Arrived : " + tweet.getText()));
			}
			catch (IOException e) {
				logger.warn("Failed to send SSE :" + e);
			}
		}
	}

	public StreamingTweetResponse setStreamTweetResponse(String line) {

		if (line.isEmpty()) {
			return null;
		}

		StreamingTweetResponse streamingTweetResponse = new StreamingTweetResponse();

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonFullNode;
		try {
			jsonFullNode = objectMapper.readTree(line);
		}
		catch (Exception e) {
			return null;
		}

		JsonNode jsonDataNode = jsonFullNode.get("data");
		JsonNode jsonExpansionNode = jsonFullNode.get("includes");

		if (jsonDataNode != null) {
			Tweet tweet = new Tweet();
			tweet.setId(jsonDataNode.get("id").asText());
			tweet.setText(jsonDataNode.get("text").asText());
			tweet.setLang(jsonDataNode.get("lang").asText());
			streamingTweetResponse.setData(tweet);
		}

		if (jsonExpansionNode != null) {
			JsonNode jsonUserNode = jsonExpansionNode.get("users");
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

		Tweet tweet = streamingTweetResponse.getData();
		if (tweet == null) {
			return null;
		}

		Expansions expansions = streamingTweetResponse.getIncludes();
		if (expansions == null) {
			return null;
		}
		List<User> users = expansions.getUsers();
		if (users == null) {
			return null;
		}

		return streamingTweetResponse;
	}

}
