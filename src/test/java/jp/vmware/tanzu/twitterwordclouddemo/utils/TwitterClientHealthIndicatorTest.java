package jp.vmware.tanzu.twitterwordclouddemo.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(
		properties = { "test=true", "management.endpoint.health.group.liveness.include=livenessState,twitterClient",
				"management.endpoint.health.group.liveness.additional-path=server:/livez",
				"management.endpoint.health.show-details=always", "management.health.probes.enabled=true" })
class TwitterClientHealthIndicatorTest {

	@MockBean
	TwitterStreamClient twitterStreamClient;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void healthy() throws Exception {

		Mockito.when(twitterStreamClient.getStatus()).thenReturn(twitterStreamClient.UP);

		mockMvc.perform(get("/actuator/health/liveness")).andExpect(jsonPath("$.status").value("UP"))
				.andExpect(jsonPath("$.components.livenessState.status").value("UP"))
				.andExpect(jsonPath("$.components.twitterClient.status").value("UP"));

		mockMvc.perform(get("/livez")).andExpect(jsonPath("$.status").value("UP"))
				.andExpect(jsonPath("$.components.livenessState.status").value("UP"))
				.andExpect(jsonPath("$.components.twitterClient.status").value("UP"));

	}

	@Test
	void unhealthy() throws Exception {

		Mockito.when(twitterStreamClient.getStatus()).thenReturn(twitterStreamClient.DOWN);

		mockMvc.perform(get("/actuator/health/liveness")).andExpect(jsonPath("$.status").value("DOWN"))
				.andExpect(jsonPath("$.components.livenessState.status").value("UP"))
				.andExpect(jsonPath("$.components.twitterClient.status").value("DOWN"));

		mockMvc.perform(get("/livez")).andExpect(jsonPath("$.status").value("DOWN"))
				.andExpect(jsonPath("$.components.livenessState.status").value("UP"))
				.andExpect(jsonPath("$.components.twitterClient.status").value("DOWN"));

	}

}