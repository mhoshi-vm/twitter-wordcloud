package jp.vmware.tanzu.twitterwordclouddemo.security;

import jp.vmware.tanzu.twitterwordclouddemo.controller.WebSocketEventController;
import jp.vmware.tanzu.twitterwordclouddemo.observability.WfServletSpans;
import jp.vmware.tanzu.twitterwordclouddemo.service.MyTweetService;
import jp.vmware.tanzu.twitterwordclouddemo.service.TweetTextService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(WebSecurityConfigOidc.class)
class WebSecurityConfigOidcTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MyTweetService myTweetService;

	@MockBean
	private TweetTextService tweetTextService;

	// Silent WfServletBean
	@MockBean
	private WfServletSpans wfServletSpans;

	@MockBean
	private WebSocketEventController webSocketEventController;

	@Test
	void securityFilterChainAuthenticated() throws Exception {
		this.mockMvc.perform(get("/").with(oidcLogin())).andExpect(status().isOk());
		this.mockMvc.perform(get("/login").with(oidcLogin())).andExpect(status().isOk());
		this.mockMvc.perform(get("/api/tweetcount").with(oidcLogin())).andExpect(status().isOk());
		this.mockMvc.perform(get("/tweets").with(oidcLogin())).andExpect(status().isOk());
	}

}