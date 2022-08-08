package jp.vmware.tanzu.twitterwordclouddemo.config;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Profile({ "default", "stateful" })
public class TwitterStreamClientImpl implements TwitterStreamClient {

	private static final Logger logger = LoggerFactory.getLogger(TwitterStreamClientImpl.class);

	TwitterApi apiInstance;

	String twitterBearerToken;

	List<String> hashTags;

	public TwitterStreamClientImpl(@Value("${twitter.bearer.token}") String twitterBearerToken,
			@Value("${twitter.hashtags}") List<String> hashTags) {
		this.twitterBearerToken = twitterBearerToken;
		this.apiInstance = new TwitterApi(new TwitterCredentialsBearer(twitterBearerToken));
		this.hashTags = hashTags;
	}

	private void addRule() {
		AddRulesRequest addRulesRequest = new AddRulesRequest();
		List<RuleNoId> ruleNoIds = hashTags.stream().map(keyword -> new RuleNoId().value(keyword))
				.collect(Collectors.toList());
		addRulesRequest.add(ruleNoIds);
		AddOrDeleteRulesRequest addOrDeleteRulesRequest = new AddOrDeleteRulesRequest(addRulesRequest);
		logger.debug("Add Rules Response: {}", AddOrDeleteRule(addOrDeleteRulesRequest));
	}

	private AddOrDeleteRulesResponse AddOrDeleteRule(AddOrDeleteRulesRequest addOrDeleteRulesRequest) {
		try {
			return apiInstance.tweets().addOrDeleteRules(addOrDeleteRulesRequest).execute();
		}
		catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream startStreamListener() throws ApiException {
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

		addRule();

		return apiInstance.tweets().searchStream().backfillMinutes(0).tweetFields(tweetFields).expansions(expansions)
				.mediaFields(null).pollFields(null).userFields(userFields).placeFields(null).execute();
	}

	@Override
	@PreDestroy
	public void cleanup() {
		DeleteRulesRequestDelete deleteRulesRequestDelete = new DeleteRulesRequestDelete();
		hashTags.forEach(deleteRulesRequestDelete::addValuesItem);

		AddOrDeleteRulesRequest addOrDeleteRulesRequest = new AddOrDeleteRulesRequest(
				new DeleteRulesRequest().delete(deleteRulesRequestDelete));

		logger.debug("Add Rules Response: {}", AddOrDeleteRule(addOrDeleteRulesRequest));
	}

}
