package jp.vmware.tanzu.twitterwordclouddemo.controller;

import jp.vmware.tanzu.twitterwordclouddemo.repository.MyTweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import jp.vmware.tanzu.twitterwordclouddemo.system.spans.WfServletSpans;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

// https://github.com/spring-projects/spring-boot/issues/6514
@WebMvcTest(WordcloudController.class)
@AutoConfigureMockMvc(addFilters = false)
class WordcloudControllerTest {

	@Autowired
	private MockMvc mockMvc;

	// Silent WfServletBean
	@MockBean
	private WfServletSpans wfServletSpans;

	@Test
	void wordcloud() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("wordcloud"));
	}

	@Test
	void login() throws Exception {
		this.mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));
	}

}