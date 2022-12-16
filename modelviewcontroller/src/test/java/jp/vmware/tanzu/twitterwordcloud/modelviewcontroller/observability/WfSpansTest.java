package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.observability;

import brave.handler.MutableSpan;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.service.TweetStreamService;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.test_utils.TestConfig;
import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.test_utils.TestSpanHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "test=true" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class WfSpansTest {

	@Autowired
	TweetStreamService tweetStreamService;

	@Autowired
	TestRestTemplate testRestTemplate;

	List<MutableSpan> spans;

	@Autowired
	private TestSpanHolder testSpanHolder;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() throws InterruptedException {
		testRestTemplate.getForEntity("http://localhost:" + port + "/api/tweetcount", String.class);

	}

	// Disabling due to requires major rewrite from spring boot 3
	// @Test
	void checkBWebDBSpans() {

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