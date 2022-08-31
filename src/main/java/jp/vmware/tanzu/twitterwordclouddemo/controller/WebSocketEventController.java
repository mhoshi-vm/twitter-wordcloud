package jp.vmware.tanzu.twitterwordclouddemo.controller;

import jp.vmware.tanzu.twitterwordclouddemo.service.TweetStreamService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Controller
@RequestMapping("/api")
public class WebSocketEventController {

	TweetStreamService tweetStreamService;

	public WebSocketEventController(TweetStreamService tweetStreamService) {
		this.tweetStreamService = tweetStreamService;
	}

	@RequestMapping("/tweetEvent")
	public SseEmitter newTweet() {
		SseEmitter sseEmitter = new SseEmitter(-1L);
		List<SseEmitter> emitters = tweetStreamService.getEmitters();
		emitters.add(sseEmitter);
		sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));

		return sseEmitter;
	}

}
