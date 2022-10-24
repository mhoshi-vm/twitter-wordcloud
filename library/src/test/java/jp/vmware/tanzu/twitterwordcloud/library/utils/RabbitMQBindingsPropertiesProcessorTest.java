package jp.vmware.tanzu.twitterwordcloud.library.utils;

import jp.vmware.tanzu.twitterwordcloud.library.servicebindings.RabbitMQEnabledViaBindings;
import jp.vmware.tanzu.twitterwordcloud.library.test_utils.FluentMap;
import org.assertj.core.api.MapAssert;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.mock.env.MockEnvironment;

import java.nio.file.Paths;
import java.util.HashMap;

import static jp.vmware.tanzu.twitterwordcloud.library.servicebindings.RabbitMQEnabledViaBindings.TYPE;
import static org.assertj.core.api.Assertions.assertThat;

class RabbitMQBindingsPropertiesProcessorTest {

	private final Bindings bindings = new Bindings(new Binding("test-name", Paths.get("test-path"),
			new FluentMap().withEntry(Binding.TYPE, TYPE).withEntry("dummy", "dummy")));

	private final MockEnvironment environment = new MockEnvironment();

	private final HashMap<String, Object> properties = new HashMap<>();

	@Test
	void test() {
		new RabbitMQEnabledViaBindings().process(environment, bindings, properties);
		MapAssert<String, Object> stringObjectMapAssert = assertThat(properties).containsEntry("message.queue.enabled",
				"true");
	}

}