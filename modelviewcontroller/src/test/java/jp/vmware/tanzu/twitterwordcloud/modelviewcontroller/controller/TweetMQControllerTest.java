package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringRabbitTest
class TweetMQControllerTest {

	@Autowired
	private RabbitTemplate template;

}