package jp.vmware.tanzu.twitterwordclouddemo.utils;

import jp.vmware.tanzu.twitterwordclouddemo.test_utils.TestRabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = { "test=true" })
@SpringBootTest
class TweetHandlerMQTest {

	@Autowired
	private TestRabbitTemplate template;

	@Autowired
	private TestRabbitMQConfig testRabbitMQConfig;

	@Test
	void simpleTest() {
		this.template.convertAndSend("tweet-handler", "hello1");
		assertEquals(testRabbitMQConfig.tweetIn, "tweet-handler:hello1");
	}

}