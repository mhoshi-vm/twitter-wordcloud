package jp.vmware.tanzu.twitterwordclouddemo.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@SpringRabbitTest
class TweetMQControllerTest {

	@Autowired
	private RabbitTemplate template;

}