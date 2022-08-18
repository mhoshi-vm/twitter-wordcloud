package jp.vmware.tanzu.twitterwordclouddemo.utils;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.model.AddOrDeleteRulesResponse;
import com.twitter.clientlib.model.Rule;
import com.twitter.clientlib.model.RulesLookupResponse;
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
class TwitterStreamClientImplTest {

	@Mock
	TweetHandler tweetHandler;

	String twitterBearerToken = "DUMMY";

	List<String> hashTags = Arrays.asList("#hoge", "#foo");

	TwitterStreamClientImpl twitterStreamClient;

	@BeforeEach
	void setup() throws ApiException {
		TwitterStreamClientImpl spyTwitterStreamClient = new TwitterStreamClientImpl(tweetHandler, twitterBearerToken,
				hashTags);

		twitterStreamClient = Mockito.spy(spyTwitterStreamClient);

		AddOrDeleteRulesResponse addOrDeleteRulesResponse = new AddOrDeleteRulesResponse();

		Mockito.doReturn(addOrDeleteRulesResponse).when(twitterStreamClient).addOrDeleteRule(Mockito.any());
		Mockito.doReturn(null).when(twitterStreamClient).setInputStream(Mockito.any(), Mockito.any(), Mockito.any());
	}

	@Test
	void testMissingAndUnneededRules() throws ApiException {

		RulesLookupResponse rulesLookupResponse = new RulesLookupResponse();

		List<Rule> rules = new ArrayList<>();

		Rule rule1 = new Rule();
		rule1.setValue("#hoge");
		rules.add(rule1);

		Rule rule2 = new Rule();
		rule2.setValue("#bar");
		rules.add(rule2);

		rulesLookupResponse.setData(rules);

		Mockito.doReturn(rulesLookupResponse).when(twitterStreamClient).getUpstreamRules();

		twitterStreamClient.getTwitterInputStream();

		assertEquals(1, twitterStreamClient.getAddRulesRequest().getAdd().size());
		assertEquals(1, twitterStreamClient.getDeleteRulesRequestDelete().getValues().size());

		assertEquals("#foo", twitterStreamClient.getAddRulesRequest().getAdd().get(0).getValue());
		assertEquals("#bar", twitterStreamClient.getDeleteRulesRequestDelete().getValues().get(0));

	}

	@Test
	void nullRuleResponse() throws ApiException {
		RulesLookupResponse rulesLookupResponse = new RulesLookupResponse();

		List<Rule> rules = new ArrayList<>();
		rulesLookupResponse.setData(rules);

		Mockito.doReturn(rulesLookupResponse).when(twitterStreamClient).getUpstreamRules();

		twitterStreamClient.getTwitterInputStream();

		assertEquals(2, twitterStreamClient.getAddRulesRequest().getAdd().size());

		assertEquals("#hoge", twitterStreamClient.getAddRulesRequest().getAdd().get(0).getValue());
		assertEquals("#foo", twitterStreamClient.getAddRulesRequest().getAdd().get(1).getValue());
	}

}