package jp.vmware.tanzu.twitterwordclouddemo.observability;

import brave.handler.MutableSpan;
import jp.vmware.tanzu.twitterwordclouddemo.client.TwitterStreamClient;
import jp.vmware.tanzu.twitterwordclouddemo.observability.utils.TestSpanHolder;
import jp.vmware.tanzu.twitterwordclouddemo.service.TweetStreamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "test=true" })
class WfSpansTest {

	@Autowired
	private TestSpanHolder testSpanHolder;

	@Autowired
	TwitterStreamClient twitterStreamClient;

	@Autowired
	TweetStreamService tweetStreamService;

	@Autowired
	TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	List<MutableSpan> spans;

	@BeforeEach
	void setup() throws InterruptedException {
		testRestTemplate.getForEntity("http://localhost:" + port + "/api/tweetcount", String.class);

	}

	@Test
	void checkTweetHandlerSpans() throws IOException, InterruptedException {
		tweetStreamService.handler("");

		spans = testSpanHolder.getSpans();

		boolean spanFound = false;

		assertTrue(spans.size() > 0);
		for (MutableSpan span : spans) {
			try {
				if (span.name().startsWith("tweet-stream-handler")) {
					spanFound = true;
					assertEquals("Twitter", span.tag("_outboundExternalService"));
					assertEquals("twitter-api", span.tag("_externalComponent"));
					assertNotNull(span.tag("_externalApplication"));
				}
			}
			catch (Exception ignored) {
			}
		}
		assertTrue(spanFound);
	}

	@Test
	void checkWebDBSpans() {

		spans = testSpanHolder.getSpans();

		assertTrue(spans.size() > 0);

		boolean webSpanFound = false;

		for (MutableSpan span : spans) {
			try {
				if (span.name().startsWith("GET /api/tweetcount")) {
					webSpanFound = true;
					assertEquals("LB", span.tag("_inboundExternalService"));
					assertEquals("local", span.tag("_externalComponent"));
					assertNotNull(span.tag("_externalApplication"));
				}
			}
			catch (Exception ignored) {
			}
		}
		assertTrue(webSpanFound);

		boolean dbSpanFound = false;

		for (MutableSpan span : spans) {
			try {
				if (span.name().startsWith("select") && span.tag("jdbc.query") != null) {
					dbSpanFound = true;
					assertEquals("RDB", span.tag("db.type"));
					assertEquals("TweetDB", span.tag("db.instance"));
					assertEquals("java-jdbc", span.tag("component"));
				}
			}
			catch (Exception ignored) {
			}
		}
		assertTrue(dbSpanFound);
	}

}