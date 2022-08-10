package jp.vmware.tanzu.twitterwordclouddemo.controller;

import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import jp.vmware.tanzu.twitterwordclouddemo.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import jp.vmware.tanzu.twitterwordclouddemo.service.MyTweetService;
import jp.vmware.tanzu.twitterwordclouddemo.system.spans.WfServletSpans;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MyTweetController.class)
@AutoConfigureMockMvc(addFilters = false)
class MyTweetControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MyTweetService myTweetService;

	// Silent WfServletBean
	@MockBean
	private WfServletSpans wfServletSpans;

	@Test
	void getAllTweets() throws Exception {
		MyTweet myTweet1 = new MyTweet();
		myTweet1.setTweetId("1111");
		myTweet1.setText("Hello");
		myTweet1.setUsername("James");

		MyTweet myTweet2 = new MyTweet();
		myTweet2.setTweetId("2222");
		myTweet2.setText("Hello");
		myTweet2.setUsername("Jane");

		List<MyTweet> myTweetList = new ArrayList<>();

		myTweetList.add(myTweet1);
		myTweetList.add(myTweet2);

		when(myTweetService.findAllByOrderByTweetIdDesc()).thenReturn(myTweetList);

		mockMvc.perform(get("/tweets")).andExpect(status().isOk()).andExpect(view().name("tweets"))
				.andExpect(model().attribute("tweets", myTweetList));
	}

}