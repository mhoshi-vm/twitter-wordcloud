package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.utils;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.api.TweetsApi;
import com.twitter.clientlib.model.*;
import jp.vmware.tanzu.twitterwordcloud.library.utils.TweetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(value = "twitter.search.mode", havingValue = "stream")
public class FilteredStream implements TweetSearch {

	private static final int RETRIES = 0;

	private static final Logger logger = LoggerFactory.getLogger(FilteredStream.class);

	TwitterClient twitterClient;

	TweetHandler tweetHandler;

	AddRulesRequest addRulesRequest;

	DeleteRulesRequestDelete deleteRulesRequestDelete;

	TweetsApi apiInstance;

	List<String> hashTags;

	public FilteredStream(TwitterClient twitterClient, TweetHandler tweetHandler,
			@Value("${twitter.hashtags}") List<String> hashTags) {
		this.twitterClient = twitterClient;
		this.tweetHandler = tweetHandler;
		this.hashTags = hashTags;
		this.apiInstance = twitterClient.getApiInstance();
		this.addRulesRequest = new AddRulesRequest();
		this.deleteRulesRequestDelete = new DeleteRulesRequestDelete();
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

	private void setRule(List<String> hashTags) throws ApiException {

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

			logger.info("Add Rules Response: {}", addOrDeleteRule(apiInstance, addOrDeleteRulesRequest));
		}

		if (unneededRules.size() > 0) {

			logger.info("Found unneeded twitter rules : " + unneededRules);

			unneededRules.forEach(deleteRulesRequestDelete::addValuesItem);

			AddOrDeleteRulesRequest addOrDeleteRulesRequest = new AddOrDeleteRulesRequest(
					new DeleteRulesRequest().delete(deleteRulesRequestDelete));

			logger.info("Delete Rules Response: {}", addOrDeleteRule(apiInstance, addOrDeleteRulesRequest));
		}
	}

	public AddOrDeleteRulesResponse addOrDeleteRule(TweetsApi apiInstance,
			AddOrDeleteRulesRequest addOrDeleteRulesRequest) {
		try {
			return apiInstance.addOrDeleteRules(addOrDeleteRulesRequest).execute(RETRIES);
		}
		catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream setTwitterInputStream() throws ApiException {

		setRule(hashTags);

		return setInputStream(tweetFields, expansions, userFields);
	}

	public InputStream setInputStream(Set<String> tweetFields, Set<String> expansions, Set<String> userFields)
			throws ApiException {
		return apiInstance.searchStream().backfillMinutes(0).tweetFields(tweetFields).expansions(expansions)
				.mediaFields(null).pollFields(null).userFields(userFields).placeFields(null).execute(RETRIES);
	}

	@Override
	public void actionOnTweetsAsync() throws ApiException {
		InputStream inputStream = setTwitterInputStream();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			twitterClient.setStatus(twitterClient.UP);
			String line = reader.readLine();
			while (twitterClient.getStatus().equals("UP")) {
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
			twitterClient.setStatus(twitterClient.DOWN);
			throw new RuntimeException(e);
		}
	}

}
