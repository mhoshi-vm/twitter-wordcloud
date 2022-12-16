package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.api.TweetsApi;
import com.twitter.clientlib.model.Expansions;
import com.twitter.clientlib.model.Get2TweetsSearchRecentResponse;
import com.twitter.clientlib.model.Tweet;
import jp.vmware.tanzu.twitterwordcloud.library.utils.TweetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

@Component
@ConditionalOnProperty(value = "twitter.search.mode", havingValue = "interval")
public class IntervalSearch implements TweetSearch {

	private static final Logger logger = LoggerFactory.getLogger(IntervalSearch.class);

	TwitterClient twitterClient;

	TweetHandler tweetHandler;

	TweetsApi apiInstance;

	List<String> hashTags;

	OffsetDateTime startTime;

	String sinceId;

	int maxResults = 100;

	String sortOrder = "recency";

	public IntervalSearch(TwitterClient twitterClient, TweetHandler tweetHandler,
			@Value("${twitter.hashtags}") List<String> hashTags) {
		this.twitterClient = twitterClient;
		this.tweetHandler = tweetHandler;
		this.hashTags = hashTags;
		this.apiInstance = twitterClient.getApiInstance();
		setStartTime(OffsetDateTime.now());
		setSinceId(null);
	}

	public String getSinceId() {
		return sinceId;
	}

	public void setSinceId(String sinceId) {
		this.sinceId = sinceId;
	}

	public OffsetDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(OffsetDateTime startTime) {
		this.startTime = startTime;
	}

	public Get2TweetsSearchRecentResponse recentSearch(OffsetDateTime startTime, String nextToken, String sinceId)
			throws ApiException {
		String query = String.join(",", hashTags);
		return apiInstance.tweetsRecentSearch(query).startTime(startTime).sinceId(sinceId).paginationToken(nextToken)
				.maxResults(maxResults).sortOrder(sortOrder).tweetFields(tweetFields).expansions(expansions)
				.userFields(userFields).execute();
	}

	public JsonNode createJson(Tweet tweet, Expansions expansions) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode jNode = mapper.createObjectNode();
		ObjectNode dataNode = mapper.createObjectNode();
		ObjectNode includesNode = mapper.createObjectNode();
		ArrayNode usersNode = mapper.createArrayNode();
		ObjectNode nullNode = mapper.createObjectNode();

		jNode.set("data", nullNode);
		jNode.set("includes", nullNode);

		if (tweet != null) {
			dataNode.put("id", tweet.getId());
			dataNode.put("text", tweet.getText());
			dataNode.put("lang", tweet.getLang());
		}
		if (expansions != null && expansions.getUsers() != null) {
			ObjectNode userNode = mapper.createObjectNode();
			userNode.put("name", expansions.getUsers().get(0).getName());
			usersNode.add(userNode);
			includesNode.set("users", usersNode);
		}

		jNode.set("data", dataNode);
		jNode.set("includes", includesNode);

		return jNode;
	}

	@Scheduled(initialDelayString = "${twitter.interval}", fixedRateString = "${twitter.interval}")
	public void performTwitterSearch() throws ApiException {
		twitterClient.setStatus(twitterClient.UP);
		logger.info("perform interval search");

		boolean maxResultsReturned = true;
		String nextToken = null;

		while (maxResultsReturned) {
			logger.debug("start time :" + getStartTime());
			logger.debug("nextToken :" + nextToken);
			logger.debug("since Id :" + getSinceId());
			Get2TweetsSearchRecentResponse result = recentSearch(getStartTime(), nextToken, getSinceId());

			if (result.getData() != null) {
				result.getData().forEach(tweet -> {
					try {
						logger.info("Found tweet ");
						tweetHandler.handle(createJson(tweet, result.getIncludes()).toString());
						if (result.getMeta() != null) {
							setStartTime(null);
							setSinceId(result.getMeta().getNewestId());
						}
					}
					catch (IOException | InterruptedException e) {
						throw new RuntimeException(e);
					}
				});

				if (result.getData().size() >= maxResults) {
					if (result.getMeta() != null) {
						logger.info("maxResult exceeded: researching");
						setStartTime(null);
						nextToken = result.getMeta().getNextToken();
					}
				}
				else {
					maxResultsReturned = false;
				}
			}
			else {
				maxResultsReturned = false;
			}
		}
	}

	@Override
	public void actionOnTweetsAsync() {
		// Do nothing as we perform search via schedule
	}

}
