package jp.vmware.tanzu.twitterwordcloud.library.servicebindings;

import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RabbitMQEnabledViaBindings implements BindingsPropertiesProcessor {

	public static final String TYPE = "rabbitmq";

	@Override
	public void process(Environment environment, Bindings bindings, Map<String, Object> map) {

		List<Binding> rmqBindings = bindings.filterBindings(TYPE);
		System.out.println(environment.getActiveProfiles());
		if (rmqBindings.size() == 0) {
			map.put("message.queue.enabled", "false");
		}
		else {
			map.put("message.queue.enabled", "true");
			map.put("management.health.rabbit.enabled", "true");
		}

	}

}
