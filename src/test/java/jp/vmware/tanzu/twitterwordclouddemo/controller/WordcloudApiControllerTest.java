package jp.vmware.tanzu.twitterwordclouddemo.controller;

import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import jp.vmware.tanzu.twitterwordclouddemo.system.security.WebSecurityConfigLocal;
import jp.vmware.tanzu.twitterwordclouddemo.system.spans.WfServletSpans;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("stateless")
class WordcloudApiControllerTest {

	@MockBean
	private TweetRepository tweetRepository;

	@Autowired
	private MockMvc mockMvc;

	// Silent WfServletBean
	@MockBean
	private WfServletSpans wfServletSpans;

	@MockBean
	private TweetTextRepository tweetTextRepository;

	class CustomTextCount implements TweetTextRepository.TextCount {

		String text;

		Long size;

		@Override
		public String getText() {
			return this.text;
		}

		@Override
		public Long getSize() {
			return this.size;
		}

		public void setText(String text) {
			this.text = text;
		}

		public void setSize(Long size) {
			this.size = size;
		}

	}

	// https://medium.com/@reachansari/rest-endpoint-testing-with-mockmvc-7b3da1f83fbb
	@Test
	void listTweetCount() throws Exception {
		CustomTextCount textCount = new CustomTextCount();
		textCount.setText("aaaa");
		textCount.setSize(100L);
		List<TweetTextRepository.TextCount> textCounts = new ArrayList<>();
		textCounts.add(textCount);

		for (TweetTextRepository.TextCount textCount1 : textCounts) {
			System.out.println("aaa" + textCount1.getText());
			System.out.println("bbb" + textCount1.getSize());
		}

		when(tweetTextRepository.listTextCount(any())).thenReturn(textCounts);

		RequestBuilder request = MockMvcRequestBuilders.get("/api/tweetcount").contentType(MediaType.APPLICATION_JSON);

		MockHttpServletResponse returnedemp = (MockHttpServletResponse) mockMvc.perform(request)
				.andExpect(status().isOk()).andReturn().getResponse();

		System.out.println("ccc" + returnedemp.getContentAsString());

		mockMvc.perform(get("/api/tweetcount")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].text", Matchers.equalTo("aaaa")))
				.andExpect(jsonPath("$[0].size", Matchers.equalTo(100)));
	}

}