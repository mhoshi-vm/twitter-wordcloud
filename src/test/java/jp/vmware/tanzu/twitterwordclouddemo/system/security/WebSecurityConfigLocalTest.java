package jp.vmware.tanzu.twitterwordclouddemo.system.security;

import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetRepository;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import jp.vmware.tanzu.twitterwordclouddemo.system.spans.WfServletSpans;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
@Import(WebSecurityConfigLocal.class)
class WebSecurityConfigLocalTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TweetRepository tweetRepository;

	@MockBean
	private TweetTextRepository tweetTextRepository;

	// Silent WfServletBean
	@MockBean
	private WfServletSpans wfServletSpans;
	@Test
	void securityFilterChain() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk());
		this.mockMvc.perform(get("/login")).andExpect(status().isOk());
		this.mockMvc.perform(get("/api/tweetcount")).andExpect(status().isOk());
		this.mockMvc.perform(get("/tweets")).andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser
	void securityFilterChainAuthenticated() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk());
		this.mockMvc.perform(get("/login")).andExpect(status().isOk());
		this.mockMvc.perform(get("/api/tweetcount")).andExpect(status().isOk());
		this.mockMvc.perform(get("/tweets")).andExpect(status().isOk());
	}

}