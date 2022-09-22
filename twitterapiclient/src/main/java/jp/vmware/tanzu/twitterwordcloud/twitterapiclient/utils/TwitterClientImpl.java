package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.utils;

import com.twitter.clientlib.ApiClient;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TweetsApi;
import com.twitter.clientlib.api.TwitterApi;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class TwitterClientImpl implements TwitterClient {

	private static final Logger logger = LoggerFactory.getLogger(TwitterClientImpl.class);

	TweetsApi apiInstance;

	String twitterBearerToken;

	String status;

	OkHttpClient httpClient;

	public TwitterClientImpl(@Value("${twitter.bearer.token}") String twitterBearerToken) {
		this.twitterBearerToken = twitterBearerToken;
		this.apiInstance = createTwitterInstance();
		this.status = DOWN;
	}

	private TweetsApi createTwitterInstance() {

		OkHttpClient.Builder builder = new OkHttpClient.Builder();

		httpClient = builder.connectTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS)
				.readTimeout(120, TimeUnit.SECONDS).connectionPool(new ConnectionPool(0, 5, TimeUnit.SECONDS)).build();

		ApiClient apiClient = new ApiClient(httpClient);
		apiClient.setTwitterCredentials(new TwitterCredentialsBearer(twitterBearerToken));
		return new TwitterApi(apiClient).tweets();
	}

	@Override
	public TweetsApi getApiInstance() {
		return apiInstance;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	@PreDestroy
	private void closeConnection() throws IOException {

		httpClient.dispatcher().executorService().shutdown();
		if (httpClient.cache() != null && !httpClient.cache().isClosed()) {
			httpClient.cache().close();
		}
		httpClient.connectionPool().evictAll();
	}

}
