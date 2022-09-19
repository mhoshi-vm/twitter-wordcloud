package jp.vmware.tanzu.twitterwordcloud.twitterapiclient.utils;

import jp.vmware.tanzu.twitterwordcloud.twiiterapiclient.utils.TwitterStreamClient;
import jp.vmware.tanzu.twitterwordcloud.twitterapiclient.test_utils.TestConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.profiles.active=stateful", "test=true",
		"management.endpoint.health.group.liveness.include=livenessState,twitterClient",
		"management.endpoint.health.group.liveness.additional-path=server:/livez",
		"management.endpoint.health.show-details=always", "management.health.probes.enabled=true" })
class TwitterClientHealthIndicatorTest {

	@MockBean
	TwitterStreamClient twitterStreamClient;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void healthy() throws Exception {

		Mockito.when(twitterStreamClient.getStatus()).thenReturn(TwitterStreamClient.UP);

		mockMvc.perform(get("/actuator/health/liveness")).andExpect(jsonPath("$.status").value("UP"))
				.andExpect(jsonPath("$.components.livenessState.status").value("UP"))
				.andExpect(jsonPath("$.components.twitterClient.status").value("UP"));

		mockMvc.perform(get("/livez")).andExpect(jsonPath("$.status").value("UP"))
				.andExpect(jsonPath("$.components.livenessState.status").value("UP"))
				.andExpect(jsonPath("$.components.twitterClient.status").value("UP"));

	}

	@Test
	void unhealthy() throws Exception {

		Mockito.when(twitterStreamClient.getStatus()).thenReturn(TwitterStreamClient.DOWN);

		mockMvc.perform(get("/actuator/health/liveness")).andExpect(jsonPath("$.status").value("DOWN"))
				.andExpect(jsonPath("$.components.livenessState.status").value("UP"))
				.andExpect(jsonPath("$.components.twitterClient.status").value("DOWN"));

		mockMvc.perform(get("/livez")).andExpect(jsonPath("$.status").value("DOWN"))
				.andExpect(jsonPath("$.components.livenessState.status").value("UP"))
				.andExpect(jsonPath("$.components.twitterClient.status").value("DOWN"));

	}

}