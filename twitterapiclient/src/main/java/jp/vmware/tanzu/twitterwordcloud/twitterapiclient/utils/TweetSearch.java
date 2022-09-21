package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.utils;

import com.twitter.clientlib.ApiException;
import org.springframework.scheduling.annotation.Async;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface TweetSearch {

	Set<String> tweetFields = new HashSet<>(Arrays.asList("author_id", "id", "created_at", "lang"));

	Set<String> expansions = new HashSet<>(List.of("author_id"));

	Set<String> userFields = new HashSet<>(Arrays.asList("name", "username"));

	@Async
	void actionOnTweetsAsync() throws ApiException;

}
