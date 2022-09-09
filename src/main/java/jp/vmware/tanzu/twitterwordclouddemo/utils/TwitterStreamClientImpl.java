package jp.vmware.tanzu.twitterwordclouddemo.utils;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TweetsApi;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Profile({ "default", "stateful" })
@ConditionalOnProperty(value = "test", havingValue = "false")
public class TwitterStreamClientImpl implements TwitterStreamClient {

	private static final int RETRIES = 5;

	private static final Logger logger = LoggerFactory.getLogger(TwitterStreamClientImpl.class);

	TweetHandler tweetHandler;

	TweetsApi apiInstance;

	String twitterBearerToken;

	List<String> hashTags;

	String status;

	AddRulesRequest addRulesRequest;

	DeleteRulesRequestDelete deleteRulesRequestDelete;

	public TwitterStreamClientImpl(TweetHandler tweetHandler,
			@Value("${twitter.bearer.token}") String twitterBearerToken,
			@Value("${twitter.hashtags}") List<String> hashTags) {
		this.tweetHandler = tweetHandler;
		this.twitterBearerToken = twitterBearerToken;
		this.apiInstance = createTwitterInstance();
		this.hashTags = hashTags;
		this.status = DOWN;
		this.addRulesRequest = new AddRulesRequest();
		this.deleteRulesRequestDelete = new DeleteRulesRequestDelete();
	}

	public TweetsApi createTwitterInstance() {
		return new TwitterApi(new TwitterCredentialsBearer(twitterBearerToken)).tweets();
	}

	@Override
	public String getStatus() {
		return status;
	}

	public AddRulesRequest getAddRulesRequest() {
		return addRulesRequest;
	}

	public DeleteRulesRequestDelete getDeleteRulesRequestDelete() {
		return deleteRulesRequestDelete;
	}

	public RulesLookupResponse getUpstreamRules() throws ApiException {
		return apiInstance.getRules().execute(RETRIES);
	}

	private void setRule() throws ApiException {

		RulesLookupResponse rulesLookupResponse = getUpstreamRules();

		List<Rule> rules = rulesLookupResponse.getData();
		List<String> configuredRules = new ArrayList<>();
		if (rules != null) {
			rules.forEach(s -> configuredRules.add(s.getValue()));
		}
		logger.debug("Configured Rules : " + configuredRules);

		List<String> missingRules = hashTags.stream().filter(element -> !configuredRules.contains(element))
				.collect(Collectors.toList());
		List<String> unneededRules = configuredRules.stream().filter(element -> !hashTags.contains(element))
				.collect(Collectors.toList());

		logger.debug("Missing Rules : " + missingRules);
		logger.debug("Unneeded Rules : " + unneededRules);

		if (missingRules.size() > 0) {

			logger.info("Found missing twitter rules : " + missingRules);

			List<RuleNoId> ruleNoIds = missingRules.stream().map(keyword -> new RuleNoId().value(keyword))
					.collect(Collectors.toList());
			addRulesRequest.add(ruleNoIds);
			AddOrDeleteRulesRequest addOrDeleteRulesRequest = new AddOrDeleteRulesRequest(addRulesRequest);

			logger.info("Add Rules Response: {}", addOrDeleteRule(addOrDeleteRulesRequest));
		}

		if (unneededRules.size() > 0) {

			logger.info("Found unneeded twitter rules : " + unneededRules);

			unneededRules.forEach(deleteRulesRequestDelete::addValuesItem);

			AddOrDeleteRulesRequest addOrDeleteRulesRequest = new AddOrDeleteRulesRequest(
					new DeleteRulesRequest().delete(deleteRulesRequestDelete));

			logger.info("Delete Rules Response: {}", addOrDeleteRule(addOrDeleteRulesRequest));
		}
	}

	public AddOrDeleteRulesResponse addOrDeleteRule(AddOrDeleteRulesRequest addOrDeleteRulesRequest) {
		try {
			return apiInstance.addOrDeleteRules(addOrDeleteRulesRequest).execute(RETRIES);
		}
		catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream getTwitterInputStream() throws ApiException {
		Set<String> tweetFields = new HashSet<>();
		tweetFields.add("author_id");
		tweetFields.add("id");
		tweetFields.add("created_at");
		tweetFields.add("lang");

		Set<String> expansions = new HashSet<>();
		expansions.add("author_id");

		Set<String> userFields = new HashSet<>();
		userFields.add("name");
		userFields.add("username");

		setRule();

		return setInputStream(tweetFields, expansions, userFields);
	}

	public InputStream setInputStream(Set<String> tweetFields, Set<String> expansions, Set<String> userFields)
			throws ApiException {
		return apiInstance.searchStream().backfillMinutes(0).tweetFields(tweetFields).expansions(expansions)
				.mediaFields(null).pollFields(null).userFields(userFields).placeFields(null).execute(RETRIES);
	}

	@Override
	public void actionOnTweetsStreamAsync(InputStream inputStream) {

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			status = UP;
			String line = reader.readLine();
			while (status.equals("UP")) {
				logger.info("New input stream : " + line);
				if (line == null || line.isEmpty()) {
					Thread.sleep(100);
					line = reader.readLine();
					continue;
				}
				try {

					tweetHandler.handle(line);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				line = reader.readLine();
			}
		}
		catch (Exception e) {
			status = DOWN;
			throw new RuntimeException(e);
		}
	}

}
