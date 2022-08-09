package jp.vmware.tanzu.twitterwordclouddemo.controller;

import jp.vmware.tanzu.twitterwordclouddemo.system.security.WebSecurityConfigLocal;
import jp.vmware.tanzu.twitterwordclouddemo.system.spans.WfServletSpans;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// https://github.com/spring-projects/spring-boot/issues/6514
@WebMvcTest(WordcloudController.class)
@Import(WebSecurityConfigLocal.class)
class WordcloudControllerTest {

	@Autowired
	private MockMvc mockMvc;

	// Silent WfServletBean
	@MockBean
	private WfServletSpans wfServletSpans;

	@Test
	void wordcloud() throws Exception {
		this.mockMvc.perform(get("/"))
				.andExpect(status().isOk());
	}

	@Test
	void login() throws Exception {
		this.mockMvc.perform(get("/login"))
				.andExpect(status().isOk());
	}

}